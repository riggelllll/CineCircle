package com.koniukhov.cinecircle.feature.ai.recommendations

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.jetbrains.bio.npy.NpyFile
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.nio.file.Paths
import javax.inject.Inject
import javax.inject.Singleton

data class MediaVector(
    val movieId: Int,
    val tmdbId: Int,
    val vector: FloatArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MediaVector
        if (movieId != other.movieId) return false
        if (tmdbId != other.tmdbId) return false
        if (!vector.contentEquals(other.vector)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = movieId
        result = 31 * result + tmdbId
        result = 31 * result + vector.contentHashCode()
        return result
    }
}

@Singleton
class MediaRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var allMediaVectors: List<MediaVector>? = null

    private fun getAssetStream(fileName: String): InputStream {
        return context.assets.open(fileName)
    }

    private fun copyAssetToTempFile(fileName: String): File {
        val tempFile = File(context.cacheDir, fileName)
        if (tempFile.exists() && tempFile.length() > 0) {
            Timber.d("Asset file already exists in cache: $fileName")
            return tempFile
        }
        try {
            Timber.d("Copying asset file to cache: $fileName")
            getAssetStream(fileName).use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            Timber.d("Successfully copied asset file: $fileName (${tempFile.length()} bytes)")
            if (!tempFile.exists()) throw IllegalStateException("File was not created: $fileName")
            if (tempFile.length() == 0L) throw IllegalStateException("File is empty: $fileName")
            return tempFile
        } catch (e: Exception) {
            Timber.e(e, "Failed to copy asset file: $fileName")
            tempFile.delete()
            throw e
        }
    }

    @SuppressLint("NewApi")
    private fun loadNpyData(): Map<Int, FloatArray> {
        try {
            Timber.d("Starting NPY data loading...")
            val movieIdsFile = copyAssetToTempFile(MOVIE_IDS_FILE)
            Timber.d("Loading movie IDs from: ${movieIdsFile.absolutePath}")
            val movieIdsArray = NpyFile.read(Paths.get(movieIdsFile.absolutePath))
            val movieIds: List<Int> = when (val data = movieIdsArray.data) {
                is IntArray -> data.toList()
                is LongArray -> data.map { it.toInt() }
                else -> throw IllegalStateException("Expected IntArray or LongArray for movie_ids")
            }
            Timber.d("Loaded ${movieIds.size} movie IDs")

            val embeddingsFile = copyAssetToTempFile(MOVIE_EMBEDDINGS_FILE)
            Timber.d("Loading embeddings from: ${embeddingsFile.absolutePath}")
            val embeddingsArray = NpyFile.read(Paths.get(embeddingsFile.absolutePath))
            if (embeddingsArray.shape.size != 2) throw IllegalStateException("Embeddings array must be 2D.")

            val numMovies = embeddingsArray.shape[0]
            val dim = embeddingsArray.shape[1]
            Timber.d("Embeddings shape: $numMovies x $dim")

            if (numMovies != movieIds.size) {
                throw IllegalStateException("Mismatch in number of movies in IDs ($numMovies) and Embeddings (${movieIds.size}).")
            }

            val embeddingsMap = mutableMapOf<Int, FloatArray>()
            val rawData = when (val data = embeddingsArray.data) {
                is FloatArray -> data
                is DoubleArray -> data.map { it.toFloat() }.toFloatArray()
                else -> throw IllegalStateException("Expected FloatArray or DoubleArray for embeddings")
            }

            for (index in 0 until numMovies) {
                val movieId = movieIds[index]
                val vector = FloatArray(dim)
                val offset = index * dim
                for (i in 0 until dim) {
                    vector[i] = rawData[offset + i]
                }
                embeddingsMap[movieId] = vector
            }

            Timber.d("Successfully loaded embeddings for ${embeddingsMap.size} movies")
            return embeddingsMap
        } catch (e: Exception) {
            Timber.e(e, "Error loading NPY data")
            throw e
        }
    }

    private fun loadLinksAndCombine(embeddingsMap: Map<Int, FloatArray>) {
        try {
            Timber.d("Loading links from CSV...")
            val linksStream = getAssetStream(LINKS_FILE)
            val links = linksStream.bufferedReader().useLines { lines ->
                lines.drop(1).mapNotNull { line ->
                    val parts = line.split(',')
                    if (parts.size >= 3) {
                        try {
                            val movieId = parts[0].trim().toInt()
                            val tmdbId = parts[2].trim().toInt()
                            movieId to tmdbId
                        } catch (_: NumberFormatException) {
                            null
                        }
                    } else null
                }.toMap()
            }
            Timber.d("Loaded ${links.size} TMDB links")

            val combined = embeddingsMap.mapNotNull { (movieId, vector) ->
                val tmdb = links[movieId] ?: -1
                if (tmdb <= 0) {
                    null
                } else {
                    MediaVector(
                        movieId = movieId,
                        tmdbId = tmdb,
                        vector = vector
                    )
                }
            }

            allMediaVectors = combined
            Timber.d("Combined embeddings with TMDB links: ${allMediaVectors?.size} media vectors (filtered out ${embeddingsMap.size - combined.size} entries without TMDB id)")
        } catch (e: Exception) {
            Timber.e(e, "Error loading links and combining data")
            throw e
        }
    }

    fun initialize() {
        if (allMediaVectors == null) {
            try {
                Timber.d("Initializing MediaRepository...")
                val embeddingsMap = loadNpyData()
                loadLinksAndCombine(embeddingsMap)
                Timber.d("MediaRepository initialized successfully")
            } catch (e: Exception) {
                Timber.e(e, "Failed to initialize MediaRepository")
                throw e
            }
        } else {
            Timber.d("MediaRepository already initialized with ${allMediaVectors?.size} vectors")
        }
    }

    fun getMovieVector(movieId: Int): MediaVector? {
        return allMediaVectors?.find { it.movieId == movieId }
    }

    fun getAllMovieVectors(): List<MediaVector> {
        return allMediaVectors ?: emptyList()
    }

    fun hasVectorForTmdb(tmdbId: Int): Boolean {
        return allMediaVectors?.any { it.tmdbId == tmdbId } ?: false
    }

    fun getVectorByTmdb(tmdbId: Int): MediaVector? {
        return allMediaVectors?.find { it.tmdbId == tmdbId }
    }

    companion object {
        private const val MOVIE_IDS_FILE = "movie_ids.npy"
        private const val MOVIE_EMBEDDINGS_FILE = "movie_embeddings.npy"
        private const val LINKS_FILE = "links.csv"
    }
}
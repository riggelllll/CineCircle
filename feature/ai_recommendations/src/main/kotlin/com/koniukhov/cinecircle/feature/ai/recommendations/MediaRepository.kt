package com.koniukhov.cinecircle.feature.ai.recommendations

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.jetbrains.bio.npy.NpyFile
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
        getAssetStream(fileName).use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

    @SuppressLint("NewApi")
    private fun loadNpyData(): Map<Int, FloatArray> {
        val movieIdsFile = copyAssetToTempFile(MOVIE_IDS_FILE)
        val movieIdsArray = NpyFile.read(Paths.get(movieIdsFile.absolutePath))

        val movieIds: List<Int> = when (val data = movieIdsArray.data) {
            is IntArray -> data.toList()
            is LongArray -> data.map { it.toInt() }
            else -> throw IllegalStateException("Expected IntArray or LongArray for movie_ids")
        }
        movieIdsFile.delete()

        val embeddingsFile = copyAssetToTempFile(MOVIE_EMBEDDINGS_FILE)
        val embeddingsArray = NpyFile.read(Paths.get(embeddingsFile.absolutePath))

        if (embeddingsArray.shape.size != 2) {
            embeddingsFile.delete()
            throw IllegalStateException("Embeddings array must be 2D.")
        }

        val numMovies = embeddingsArray.shape[0]
        val dim = embeddingsArray.shape[1]

        if (numMovies != movieIds.size) {
            embeddingsFile.delete()
            throw IllegalStateException("Mismatch in number of movies in IDs and Embeddings.")
        }

        val embeddingsMap = mutableMapOf<Int, FloatArray>()
        val rawData = when (val data = embeddingsArray.data) {
            is FloatArray -> data
            is DoubleArray -> data.map { it.toFloat() }.toFloatArray()
            else -> {
                embeddingsFile.delete()
                throw IllegalStateException("Expected FloatArray or DoubleArray for embeddings")
            }
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

        embeddingsFile.delete()
        return embeddingsMap
    }

    private fun loadLinksAndCombine(embeddingsMap: Map<Int, FloatArray>) {
        val linksStream = getAssetStream(LINKS_FILE)
        val links = linksStream.bufferedReader().useLines { lines ->
            lines.drop(1)
                .mapNotNull { line ->
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

        allMediaVectors = embeddingsMap.map { (movieId, vector) ->
            MediaVector(
                movieId = movieId,
                tmdbId = links[movieId] ?: -1,
                vector = vector
            )
        }
    }

    fun initialize() {
        if (allMediaVectors == null) {
            val embeddingsMap = loadNpyData()
            loadLinksAndCombine(embeddingsMap)
        }
    }

    fun getMovieVector(movieId: Int): MediaVector? {
        return allMediaVectors?.find { it.movieId == movieId }
    }

    fun getAllMovieVectors(): List<MediaVector> {
        return allMediaVectors ?: emptyList()
    }

    companion object {
        private const val MOVIE_IDS_FILE = "movie_ids.npy"
        private const val MOVIE_EMBEDDINGS_FILE = "movie_embeddings.npy"
        private const val LINKS_FILE = "links.csv"
    }
}
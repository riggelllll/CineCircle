package com.koniukhov.cinecircle.core.design.util
import com.koniukhov.cinecircle.core.common.model.GenreUi
import com.koniukhov.cinecircle.core.common.util.movieGenreMap
import com.koniukhov.cinecircle.core.common.util.tvSeriesGenreMap
import com.koniukhov.cinecircle.core.design.R

val moviesGenreDrawableMap = mapOf(
    22 to R.drawable.movie_genre_action_poster,
    12 to R.drawable.movie_genre_adventure_poster,
    16 to R.drawable.movie_genre_animation_poster,
    35 to R.drawable.movie_genre_comedy_poster,
    80 to R.drawable.movie_genre_crime_poster,
    99 to R.drawable.movie_genre_documentary_poster,
    18 to R.drawable.movie_genre_drama_poster,
    10751 to R.drawable.movie_genre_family_poster,
    14 to R.drawable.movie_genre_fantasy_poster,
    36 to R.drawable.movie_genre_history_poster,
    27 to R.drawable.movie_genre_horror_poster,
    10402 to R.drawable.movie_genre_music_poster,
    9648 to R.drawable.movie_genre_mystery_poster,
    10749 to R.drawable.movie_genre_romance_poster,
    878 to R.drawable.movie_genre_science_fiction_poster,
    10770 to R.drawable.movie_genre_tv_poster,
    53 to R.drawable.movie_genre_thriller_poster,
    10752 to R.drawable.movie_genre_war_poster,
    37 to R.drawable.movie_genre_western_poster
)

val tvSeriesGenreDrawableMap = mapOf(
    10759 to R.drawable.tv_series_genre_action_adventure_poster,
    16 to R.drawable.tv_series_genre_animation_poster,
    35 to R.drawable.tv_series_genre_comedy_poster,
    80 to R.drawable.tv_series_genre_crime_poster,
    99 to R.drawable.tv_series_genre_documentary_poster,
    18 to R.drawable.tv_series_genre_drama_poster,
    10751 to R.drawable.tv_series_genre_family_poster,
    10762 to R.drawable.tv_series_genre_kids_poster,
    9648 to R.drawable.tv_series_genre_mystery_poster,
    10763 to R.drawable.tv_series_genre_news_poster,
    10764 to R.drawable.tv_series_genre_reality_poster,
    10765 to R.drawable.tv_series_genre_sci_fi_fantasy_poster,
    10766 to R.drawable.tv_series_genre_soap_poster,
    10767 to R.drawable.tv_series_genre_talk_poster,
    10768 to R.drawable.tv_series_genre_war_politics_poster,
    37 to R.drawable.tv_series_genre_western_poster
)

val moviesGenreUiList = movieGenreMap.mapNotNull { (id, name) ->
    moviesGenreDrawableMap[id]?.let { drawableId ->
        GenreUi(
            id = id,
            name = name,
            imageResId = drawableId
        )
    }
}

val tvSeriesGenreUiList = tvSeriesGenreDrawableMap.mapNotNull { (id, drawableId) ->
    tvSeriesGenreMap[id]?.let { name ->
        GenreUi(
            id = id,
            name = name,
            imageResId = drawableId
        )
    }
}
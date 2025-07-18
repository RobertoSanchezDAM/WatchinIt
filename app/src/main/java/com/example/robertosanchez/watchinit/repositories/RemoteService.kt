package com.example.robertosanchez.watchinit.repositories

import com.example.robertosanchez.watchinit.repositories.models.CreditsResponse
import com.example.robertosanchez.watchinit.repositories.models.MovieImagesResponse
import com.example.robertosanchez.watchinit.repositories.models.RemoteResult
import com.example.robertosanchez.watchinit.repositories.models.MovieDetail
import com.example.robertosanchez.watchinit.repositories.models.WatchProviders
import com.example.robertosanchez.watchinit.repositories.models.MovieVideosResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface RemoteService {
    @GET("discover/movie?language=es-ES&sort_by=popularity.desc")
    suspend fun peliculasPopulares(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("discover/movie?language=es-ES&sort_by=vote_count.desc")
    suspend fun peliculasRated(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("movie/{movie_id}/credits")
    suspend fun getCreditos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): CreditsResponse

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getPlataformas(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): WatchProviders

    @GET("movie/{movie_id}/images")
    suspend fun getImagenes(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieImagesResponse

    @GET("search/movie")
    suspend fun buscarPeliculas(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_original_language") includeOriginalLanguage: Boolean = true
    ): RemoteResult

    @GET("discover/movie")
    suspend fun peliculaFecha (
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("sort_by") sort_by: String = "popularity.desc",
        @Query("primary_release_year") year: Int,
    ): RemoteResult

    @GET("discover/movie")
    suspend fun peliculaGenero (
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("sort_by") sort_by: String = "popularity.desc",
        @Query("with_genres") genre: Int,
    ): RemoteResult

    @GET("movie/upcoming")
    suspend fun proximosEstrenos(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "es-ES"
    ): RemoteResult

    @GET("movie/now_playing")
    suspend fun peliculasEnCine(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("region") region: String = "ES",
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("movie/{movie_id}")
    suspend fun peliculaDetalle(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): MovieDetail

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): MovieVideosResponse
}
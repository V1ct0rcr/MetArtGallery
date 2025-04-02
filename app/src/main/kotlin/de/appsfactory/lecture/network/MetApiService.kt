package de.appsfactory.lecture.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class SearchResponse(
    val total: Int,
    val objectIDs: List<Int>?
)



@JsonClass(generateAdapter = true)
data class ObjectDetails(
    val objectID: Int,
    val title: String,
    val department: String,
    val objectName: String,
    val primaryImage: String,
    val additionalImages: List<String>
)

interface MetApiService {
    @GET("public/collection/v1/search")
    suspend fun searchObjects(
        @Query("q") query: String,
        @Query("hasImages") hasImages: Boolean = true
    ): SearchResponse


    @GET("public/collection/v1/objects/{id}")
    suspend fun getObjectDetails(@Path("id") id: Int): ObjectDetails
}

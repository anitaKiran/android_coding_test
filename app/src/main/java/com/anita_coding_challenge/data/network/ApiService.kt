package com.anita_coding_challenge

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Anita Kiran on 6/1/2022.
 */
interface ApiService {

    @GET("repositories")
    suspend fun getRepoItems(
        @Query("q") searchStr: String,
        @Query("per_page") perPage: Int,
        @Query("page") pageNo: Int
    ) : Response<SearchItemModel>
}
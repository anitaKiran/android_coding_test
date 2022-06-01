package com.anita_coding_challenge

import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Anita Kiran on 6/1/2022.
 */
class MainRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getItems(searchStr: String): Response<SearchItemModel> =
        apiService.getRepoItems(searchStr)
}
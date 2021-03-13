package com.directpl.gituserlist.api

import com.directpl.gituserlist.model.Results
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("users?per_page=100")
    suspend fun getUsersFromApi(): Response<List<Results>>

    @GET("users/{name}")
    suspend fun getUserDetailFromApi(
        @Path("name") username: String): Response<Results>

}
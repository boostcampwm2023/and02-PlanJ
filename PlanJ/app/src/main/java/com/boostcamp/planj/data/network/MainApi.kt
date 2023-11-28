package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.DeleteScheduleBody
import com.boostcamp.planj.data.model.PatchCategoryRequest
import com.boostcamp.planj.data.model.PatchCategoryResponse
import com.boostcamp.planj.data.model.PatchScheduleBody
import com.boostcamp.planj.data.model.PatchScheduleResponse
import com.boostcamp.planj.data.model.PostCategoryBody
import com.boostcamp.planj.data.model.PostCategoryResponse
import com.boostcamp.planj.data.model.PostScheduleBody
import com.boostcamp.planj.data.model.PostScheduleResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MainApi {

    @POST("/api/category/add")
    suspend fun postCategory(@Body createCategoryBody: PostCategoryBody): PostCategoryResponse

    @POST("/api/schedule/add")
    suspend fun postSchedule(@Body postScheduleBody: PostScheduleBody): PostScheduleResponse

    @DELETE("/api/category/delete/{categoryUuid}")
    suspend fun deleteCategory(@Path("categoryUuid") categoryUuid: String)

    @HTTP(method = "DELETE", path = "/api/schedule/delete", hasBody = true)
    suspend fun deleteSchedule(@Body deleteScheduleBody: DeleteScheduleBody)

    @PATCH("/api/schedule/update")
    suspend fun patchSchedule(@Body patchScheduleBody: PatchScheduleBody): PatchScheduleResponse

    @PATCH("/api/category/update")
    suspend fun patchCategory(@Body patchCategoryRequest: PatchCategoryRequest): PatchCategoryResponse
}
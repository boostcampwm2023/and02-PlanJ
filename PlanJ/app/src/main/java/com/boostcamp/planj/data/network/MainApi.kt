package com.boostcamp.planj.data.network

import com.boostcamp.planj.data.model.dto.DeleteScheduleBody
import com.boostcamp.planj.data.model.dto.GetCategoryResponse
import com.boostcamp.planj.data.model.dto.GetFriendResponse
import com.boostcamp.planj.data.model.dto.GetSchedulesResponse
import com.boostcamp.planj.data.model.dto.PatchCategoryRequest
import com.boostcamp.planj.data.model.dto.PatchCategoryResponse
import com.boostcamp.planj.data.model.dto.PatchScheduleBody
import com.boostcamp.planj.data.model.dto.PatchScheduleResponse
import com.boostcamp.planj.data.model.dto.PostCategoryBody
import com.boostcamp.planj.data.model.dto.PostCategoryResponse
import com.boostcamp.planj.data.model.dto.PostFriendRequest
import com.boostcamp.planj.data.model.dto.PostScheduleBody
import com.boostcamp.planj.data.model.dto.PostScheduleResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/api/category/list")
    suspend fun getCategoryList(): GetCategoryResponse

    @GET("/api/category")
    suspend fun getCategorySchedule(@Query("categoryUuid") categoryUuid: String): GetSchedulesResponse

    @GET("/api/schedule/weekly")
    suspend fun getWeeklySchedule(@Query("date") date: String): GetSchedulesResponse

    @GET("/api/schedule/daily")
    suspend fun getDailySchedule(@Query("date") date: String): GetSchedulesResponse

    @POST("/api/friend/add")
    suspend fun postFriend(@Body postFriendRequest: PostFriendRequest)

    @GET("/api/friend")
    suspend fun getFriends(): GetFriendResponse
}
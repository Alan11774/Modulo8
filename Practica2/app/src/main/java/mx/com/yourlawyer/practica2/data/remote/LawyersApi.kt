package mx.com.yourlawyer.practica2.data.remote

import mx.com.yourlawyer.practica2.data.remote.model.LawyerDetailDto
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface LawyersApi {
    //https://private-56712-yourlawyer1.apiary-mock.com/lawyer/types
    @GET
    fun getLawyers(
        @Url url: String?
    ): Call<MutableList<LawyerDto>>

    @GET("lawyer/types?")
    fun getLawyerDetail(
        @Query("id") id: String?/*,
        @Query("name") name: String?*/
    ): Call<LawyerDetailDto>

    @GET("lawyer/types")
    fun getLawyersApiary(): Call<MutableList<LawyerDto>>

    @GET("lawyer/types/{id}")
    fun getLawyerDetailApiary(
        @Path("id") id: String?
    ): Call<LawyerDetailDto>
}
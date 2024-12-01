package mx.com.yourlawyer.practica2.data.remote

import mx.com.yourlawyer.practica2.data.remote.model.LawyerDetailDto
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDetailDtoApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LawyerApi {
    //https://pokeapi.co/api/v2/pokemon/149/

    @GET("lawyer/types/{id}")
    fun getLawyerDetail(
        @Path("id") id: String?
    ): Call<LawyerDetailDtoApi>
}
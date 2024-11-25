package mx.com.yourlawyer.practica2.data

import mx.com.yourlawyer.practica2.data.remote.LawyersApi
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDetailDto
import mx.com.yourlawyer.practica2.data.remote.model.LawyerDto
import retrofit2.Call
import retrofit2.Retrofit

class LawyerRepository(
    private val retrofit: Retrofit
) {

    private val lawyersApi: LawyersApi = retrofit.create(LawyersApi::class.java)


    fun getLawyersApiary(): Call<MutableList<LawyerDto>> = lawyersApi.getLawyersApiary()

    fun getLawyerDetailApiary(id: String?): Call<LawyerDetailDto> =
        lawyersApi.getLawyerDetailApiary(id)
}
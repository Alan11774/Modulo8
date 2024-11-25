package mx.com.yourlawyer.practica2.application

import android.app.Application
import mx.com.yourlawyer.practica2.data.LawyerRepository
import mx.com.yourlawyer.practica2.data.remote.RetrofitHelper

class LawyersRfApp: Application() {

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        LawyerRepository(retrofit)
    }

}
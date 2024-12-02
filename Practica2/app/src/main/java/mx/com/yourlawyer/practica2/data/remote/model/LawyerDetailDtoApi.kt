package mx.com.yourlawyer.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class LawyerDetailDtoApi (
    var id: String? = null,
    var category: String? = null,
    var subcategory: MutableList<String>? = null,
    var description: String? = null,
    var examples: MutableList<String>? = null,
    var image: String? = null,
    var activeLawyers: Int? = null,
    var video: String? = null,

)
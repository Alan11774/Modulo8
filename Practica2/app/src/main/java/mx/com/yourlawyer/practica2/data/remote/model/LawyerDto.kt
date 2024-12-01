package mx.com.yourlawyer.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

class LawyerDto (
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("subcategory")
    var subcategory: MutableList<String>? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("active_lawyers")
    var activeLawyers: String? = null,
    @SerializedName("description")
    var description: String? = null,

    )
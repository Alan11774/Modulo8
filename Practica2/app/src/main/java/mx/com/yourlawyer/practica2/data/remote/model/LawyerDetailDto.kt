package mx.com.yourlawyer.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class LawyerDetailDto (
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

    @SerializedName("examples")
    var examples: MutableList<String>? = null,

    @SerializedName("video")
    var video: String? = null,

    @SerializedName("coordinates")
    var coordinates: CoordinatesDto? = null,


)

data class CoordinatesDto(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("latitude")
    var latitude: String? = null,
    @SerializedName("longitude")
    var longitude: String? = null
)
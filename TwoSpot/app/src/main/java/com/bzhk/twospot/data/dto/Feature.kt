package com.bzhk.twospot.data.dto


import com.google.gson.annotations.SerializedName

data class Feature(
//    @SerializedName("geometry")
//    val geometry: Geometry,
    @SerializedName("properties")
    val properties: Properties,
//    @SerializedName("type")
//    val type: String
)
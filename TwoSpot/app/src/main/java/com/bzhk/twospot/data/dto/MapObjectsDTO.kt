package com.bzhk.twospot.data.dto


import com.bzhk.twospot.entity.MapObjects
import com.google.gson.annotations.SerializedName

data class MapObjectsDTO(
    @SerializedName("features")
    override val features: List<Feature>,
    @SerializedName("type")
    override val type: String
) : MapObjects
package com.bzhk.twospot.data.dto


import com.google.gson.annotations.SerializedName

data class Datasource(
    @SerializedName("attribution")
    val attribution: String,
    @SerializedName("license")
    val license: String,
    @SerializedName("raw")
    val raw: Raw,
    @SerializedName("sourcename")
    val sourcename: String,
    @SerializedName("url")
    val url: String
)
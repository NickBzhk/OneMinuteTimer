package com.bzhk.twospot.data.dto


import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("address_line1")
    val addressLine1: String,
    @SerializedName("address_line2")
    val addressLine2: String,
    @SerializedName("categories")
    val categories: List<String>,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("datasource")
    val datasource: Datasource,
    @SerializedName("details")
    val details: List<String>,
    @SerializedName("distance")
    val distance: Int,
    @SerializedName("district")
    val district: String,
    @SerializedName("formatted")
    val formatted: String,
    @SerializedName("housenumber")
    val housenumber: String?,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("name")
    val name: String?,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("postcode")
    val postcode: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("street")
    val street: String
)
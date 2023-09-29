package com.bzhk.twospot.data.dto


import com.google.gson.annotations.SerializedName

data class Raw(
    @SerializedName("alt_name:ru")
    val altNameRu: String?,
    @SerializedName("amenity")
    val amenity: String?,
    @SerializedName("bic")
    val bic: String?,
    @SerializedName("branch")
    val branch: String?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("brand:en")
    val brandEn: String?,
    @SerializedName("brand:fa")
    val brandFa: String?,
    @SerializedName("brand:he")
    val brandHe: String?,
    @SerializedName("brand:ru")
    val brandRu: String?,
    @SerializedName("brand:wikidata")
    val brandWikidata: String?,
    @SerializedName("brand:wikipedia")
    val brandWikipedia: String?,
    @SerializedName("cash_in")
    val cashIn: String?,
    @SerializedName("check_date")
    val checkDate: String?,
    @SerializedName("contact:email")
    val contactEmail: String?,
    @SerializedName("contact:facebook")
    val contactFacebook: String?,
    @SerializedName("contact:instagram")
    val contactInstagram: String?,
    @SerializedName("contact:ok")
    val contactOk: String?,
    @SerializedName("contact:phone")
    val contactPhone: String?,
    @SerializedName("contact:twitter")
    val contactTwitter: String?,
    @SerializedName("contact:vk")
    val contactVk: String?,
    @SerializedName("contact:website")
    val contactWebsite: String?,
    @SerializedName("contact:youtube")
    val contactYoutube: String?,
    @SerializedName("currency:EUR")
    val currencyEUR: String?,
    @SerializedName("currency:RUB")
    val currencyRUB: String?,
    @SerializedName("currency:USD")
    val currencyUSD: String?,
    @SerializedName("drive_through")
    val driveThrough: String?,
    @SerializedName("level")
    val level: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("name:en")
    val nameEn: String?,
    @SerializedName("name:he")
    val nameHe: String?,
    @SerializedName("name:ru")
    val nameRu: String?,
    @SerializedName("opening_hours")
    val openingHours: String?,
    @SerializedName("operator")
    val `operator`: String?,
    @SerializedName("operator:wikidata")
    val operatorWikidata: String?,
    @SerializedName("operator:wikipedia")
    val operatorWikipedia: String?,
    @SerializedName("osm_id")
    val osmId: Long,
    @SerializedName("osm_type")
    val osmType: String,
    @SerializedName("ref")
    val ref: String?,
    @SerializedName("shop")
    val shop: String?,
    @SerializedName("telegram")
    val telegram: String?,
    @SerializedName("website")
    val website: String?,
    @SerializedName("wheelchair")
    val wheelchair: String?
)
package com.bzhk.unbore.data

import com.bzhk.unbore.entity.IIdea
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

data class IdeasDto @Inject constructor(
    @SerializedName("activity")
    override var activity: String,
    @SerializedName("participants")
    override var participants: Int,
    @SerializedName("price")
    override var price: Double,
    @SerializedName("type")
    override var type: String
) : IIdea
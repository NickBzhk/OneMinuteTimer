package com.bzhk.twospot.entity

import com.bzhk.twospot.data.dto.Feature

interface MapObjects {
    val features: List<Feature>
    val type: String
}
package com.bzhk.martian.data
import com.bzhk.martian.entity.RoverData
import com.google.gson.annotations.SerializedName

data class RoverDataDTO(
    @SerializedName("photos")
    override var photos: List<Photo>
) : RoverData

data class Photo(
    @SerializedName("camera")
    val camera: Camera,
    @SerializedName("earth_date")
    val earthDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("img_src")
    val imgSrc: String,
    @SerializedName("rover")
    val rover: Rover,
    @SerializedName("sol")
    val sol: Int
)

data class Camera(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("rover_id")
    val roverId: Int
)

data class Rover(
    @SerializedName("cameras")
    val cameras: List<CameraX>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("landing_date")
    val landingDate: String,
    @SerializedName("launch_date")
    val launchDate: String,
    @SerializedName("max_date")
    val maxDate: String,
    @SerializedName("max_sol")
    val maxSol: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_photos")
    val totalPhotos: Int
)

data class CameraX(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("name")
    val name: String
)
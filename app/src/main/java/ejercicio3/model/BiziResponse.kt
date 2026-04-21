package com.mariajosegamez.psp_to03.ejercicio3.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BiziResponse(
    @SerializedName("result") val estaciones: List<EstacionBizi>
)

data class EstacionBizi(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("bicisDisponibles") val bicisDisponibles: Int,
    @SerializedName("anclajesDisponibles") val anclajesDisponibles: Int,
    @SerializedName("lastUpdated") val lastUpdated: String
) : Serializable
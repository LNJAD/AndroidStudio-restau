package fr.isen.AdrienMARTIN.androiderestaurant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class DisheClass (

  @SerializedName("data" ) var data : List<Data> = arrayListOf()

): Serializable
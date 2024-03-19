package fr.isen.AdrienMARTIN.androiderestaurant.model

import com.google.gson.annotations.SerializedName


data class DisheClass (

  @SerializedName("data" ) var data : ArrayList<Data> = arrayListOf()

)
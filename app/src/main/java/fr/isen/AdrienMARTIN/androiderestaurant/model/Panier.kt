package fr.isen.AdrienMARTIN.androiderestaurant.model

import java.io.Serializable

data class PanierItem(
    var items: Items,
    var count: Int,
    var totalPrice: Float,
): Serializable

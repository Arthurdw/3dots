package com.arthurdw.threedots.data.objects

import kotlinx.serialization.Serializable

@Serializable
data class ShareUser(
    val user: User,
    val pickedStocks: List<PickedStock>,
    val followedStocks: List<BasicStock>,
    val worth: List<Float>
)

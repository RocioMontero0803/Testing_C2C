package com.example.testing_

import com.example.testing_.User
import com.google.firebase.database.PropertyName

data class Posts(
    var description: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url") var image_url: String = "",
    @get:PropertyName("time_stamp") @set:PropertyName("time_stamp")var time_stamp: Long = 0,
    var user: User? = null
    )

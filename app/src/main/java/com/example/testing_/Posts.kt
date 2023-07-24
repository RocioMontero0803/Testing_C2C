package com.example.testing_

import com.example.testing_.User
import com.google.firebase.database.PropertyName

class Posts {
    var description: String? = null
    var image_url: String? = null
    var user: User? = null
    var title: String? = null
    constructor(description: String?,
                image_url: String?,
                user: User?,
                title: String?){
        this.description = description
        this.image_url = image_url
        this.user = user
        this.title = title
    }

}

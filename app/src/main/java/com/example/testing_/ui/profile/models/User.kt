package com.example.testing_.ui.profile.models

import android.net.Uri

data class User(var firstName: String ?= null, var lastName: String ?= null,var status: String ?= null, var image: Uri ?= null)
package com.funnyproger.chatapp.fragments.fragments_userAccount.models

import android.net.Uri

data class UserModel(
    var userID: String,
    var userName: String,
    var userEmail: String,
    var userPassword: String,
    var userAVA: Uri?
)

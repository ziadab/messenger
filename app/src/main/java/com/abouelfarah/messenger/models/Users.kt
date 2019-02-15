package com.abouelfarah.messenger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val fullname:String, val uid:String, val email:String, val password:String, val profileImgUrl:String) : Parcelable{
    constructor() : this("", "", "", "", "")
}
package com.abouelfarah.messenger.models


class User(val fullname:String, val uid:String, val email:String, val password:String, val profileImgUrl:String){
    constructor() : this("", "", "", "", "")
}

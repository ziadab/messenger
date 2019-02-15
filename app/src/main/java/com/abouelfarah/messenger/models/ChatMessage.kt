package com.abouelfarah.messenger.models

class ChatMessage(val uid : String, val message : String, val from_id : String, val to_id : String, val timestamp : Long){
    constructor() : this("", "", "", "", -1)
}
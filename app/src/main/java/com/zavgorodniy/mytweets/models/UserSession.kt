package com.zavgorodniy.mytweets.models

object UserSession {
    var userId: Long? = null
    var userName: String? = null
    var secret: String? = null
    var token: String? = null

    fun newSession(userId: Long, userName: String, secret: String?, token: String?) {
        this.userId = userId
        this.userName = userName
        this.secret = secret
        this.token = token
    }

    fun clear() {
        userId = null
        userName = null
        secret = null
        token = null
    }
}
package com.zavgorodniy.mytweets.network.converters

import com.zavgorodniy.mytweets.EMPTY_STRING
import com.zavgorodniy.mytweets.NO_ID
import com.zavgorodniy.mytweets.models.UserSession
import com.zavgorodniy.mytweets.models.converters.BaseConverter
import com.zavgorodniy.mytweets.network.responses.TokenResponse

interface SessionConverter

class SessionConverterImpl : BaseConverter<TokenResponse, UserSession>(), SessionConverter {

    override fun processConvertInToOut(inObject: TokenResponse) = inObject.run {
        UserSession.newSession(NO_ID, EMPTY_STRING, EMPTY_STRING, token = "Bearer $token", type = type)
        UserSession
    }
}
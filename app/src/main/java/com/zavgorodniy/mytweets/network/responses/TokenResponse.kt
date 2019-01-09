package com.zavgorodniy.mytweets.network.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenResponse(
    @JsonProperty("token_type") val type: String?,
    @JsonProperty("access_token") val token: String?
)
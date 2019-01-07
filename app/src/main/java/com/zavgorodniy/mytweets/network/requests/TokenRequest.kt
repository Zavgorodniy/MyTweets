package com.zavgorodniy.mytweets.network.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenRequest(@JsonProperty("grant_type") var grantType: String,
                        @JsonProperty("code") var code: String)
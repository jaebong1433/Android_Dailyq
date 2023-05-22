package online.dailyq.api.response

//API 문서의 토큰 발급/갱신 API를 보고 응답을 받을 때 사용함
data class AuthToken(val accessToken: String, val refreshToken: String)
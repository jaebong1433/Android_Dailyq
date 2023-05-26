package online.dailyq.api

import okhttp3.Interceptor
import okhttp3.Response
import online.dailyq.AuthManager

//인터셉터를 이용해서 AuthManager에서 보관하는 액세스 토큰을 요청의 헤더에 추가
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        //AuthType.ACCESS_TOKEN을 기본값으로 정하고
        //authType이 AuthType.ACCESS_TOKEN인 경우에만 Authorization 헤더를 추가함
        val authType = request.tag(AuthType::class.java) ?: AuthType.ACCESS_TOKEN
        when (authType) {
            AuthType.NO_AUTH -> {
            }
            AuthType.ACCESS_TOKEN -> {
                AuthManager.accessToken?.let { token ->
                    builder.header("Authorization", "Bearer $token")
                }
            }
        }
        return chain.proceed(builder.build())
    }
}
package online.dailyq.api

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import online.dailyq.AuthManager

//API에 따라 요청이 Authorization 헤더로 액세스 토큰을 보냈는지 확인 없다면 null을 반환
//리프레시 토큰이 있어야 토큰 갱신을 할 수 있기 때문에
//AuthManager에 리프레시 토큰이 있는지 확인하고 없다면 null을 반환
class TokenRefreshAuthenticator() : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val accessToken = response.request.header("Authorization")
            ?.split("")
            ?.getOrNull(1)
        accessToken ?: return null
        AuthManager.refreshToken ?: return null

        val api = ApiService.getInstance()

        //이미 갱신된 토큰을 다시 갱신하지 않기 위해 synchronized 키워드로 토큰 갱신 블럭을 동기화하고
        //블럭에 진입한 후엔 다시 액세스 토큰이 이미 갱신돼 바뀌지 않았는지 확인 후 갱신 요청을 보냄
        synchronized(this){
            if(accessToken == AuthManager.accessToken) {
                val authTokenResponse =
                    api.refreshToken(AuthManager.refreshToken!!).
                execute().body()!!

                AuthManager.accessToken = authTokenResponse.accessToken
                AuthManager.refreshToken = authTokenResponse.refreshToken
            }
        }

        //토큰이 갱신되면 새로운 Request를 만들고 Authorization 헤더가 교체된 요청을 만들어 반환
        return response.request.newBuilder()
            .header("Authorization", "Bearer ${AuthManager.accessToken}")
            .build()
    }
}
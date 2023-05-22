package online.dailyq.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import online.dailyq.AuthManager
import online.dailyq.R
import online.dailyq.databinding.ActivityLoginBinding
import online.dailyq.ui.base.BaseActivity
import online.dailyq.ui.main.MainActivity

//사용자 아이디와 패스워드가 유효한지 검사하고 '토큰 발급/갱신' API를 호출해 토큰을 받아옴
//받아온 토큰을 AuthManager에 저장하고 MainActivity를 시작함
class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.password.setOnEditorActionListener { _, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    login()
                    return@setOnEditorActionListener true
                }
                EditorInfo.IME_ACTION_UNSPECIFIED -> {
                    if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                        login()
                        return@setOnEditorActionListener true
                    }
                }
            }
            false
        }

        binding.login.setOnClickListener {
            login()
        }
    }

    //먼저 입력한 아이디와 패스워드의 길이를 검사하고
    //TextInputLayout인 userIdLayout과 password-Layout의 error 속성으로 에러 메시지를 표시
    //정규 표현식 : 숫자의 포함 여부 확인
    fun validateUidAndPassword(uid: String, password: String): Boolean {
        binding.userIdLayout.error = null
        binding.passwordLayout.error = null

        if (uid.length < 5) {
            binding.userIdLayout.error = getString(R.string.error_uid_too_short)
            return false
        }
        if (password.length < 8) {
            binding.passwordLayout.error = getString(R.string.error_password_too_short)
            return false
        }
        val numberRegex = "[0-9]".toRegex()
        if (!numberRegex.containsMatchIn(password)) {
            binding.passwordLayout.error = getString(R.string.error_password_must_contain_number)
            return false
        }

        // val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()]).{8,20}$".toRegex()

        return true
    }

    //ApiService.login() 메서드를 호출하기 전에 프로그래스바를 표시하고
    //실패하면 재시도할 수 있게 프로그래스바를 다시 숨김
    //중복으로 호출하는 것을 방지하기 위해
    //LoginActivity.login()메서드에 진입했을 때 프로그래스바가 표시된 상태면 더 이상 진행하지 않고 반환
    //토큰을 AuthManager에 저장한 후에는 MainActivity를 시작하고 LoginActivity를 종료함
    fun login() {
        if (binding.progress.isVisible) {
            return
        }

        val uid = binding.userId.text?.trim().toString()
        val password = binding.password.text?.trim().toString()

        if (validateUidAndPassword(uid, password)) {
            binding.progress.isVisible = true

            lifecycleScope.launch {
                val authTokenResponse = api.login(uid, password)
                if (authTokenResponse.isSuccessful) {
                    val authToken = authTokenResponse.body()

                    AuthManager.uid = uid
                    AuthManager.accessToken = authToken?.accessToken
                    AuthManager.refreshToken = authToken?.refreshToken

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    binding.progress.isVisible = false
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.error_login_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
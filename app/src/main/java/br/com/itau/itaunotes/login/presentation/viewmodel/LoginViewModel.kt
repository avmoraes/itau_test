package br.com.itau.itaunotes.login.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.itau.itaunotes.login.data.repository.LoginRepositoryContract
import br.com.itau.itaunotes.login.domain.model.User

interface LoginViewModelContract{

    fun bindLoading():LiveData<Boolean>
    fun bindValidEmail():LiveData<Boolean>
    fun bindValidPassword():LiveData<Boolean>
    fun bindLoginPassed():LiveData<Boolean>
    fun bindUserInfo():LiveData<User>
    fun login(email: String, password: String)
    fun saveLoggedUser()
    fun loadLoggedUser()
}

class LoginViewModel(private val repository: LoginRepositoryContract): ViewModel(), LoginViewModelContract{

    private val loading = MutableLiveData<Boolean>()
    private val validEmail = MutableLiveData<Boolean>()
    private val validPassword = MutableLiveData<Boolean>()
    private val loginPassed = MutableLiveData<Boolean>()
    private val userInfo = MutableLiveData<User>()

    private lateinit var loggedUser: User

    override fun bindLoading(): LiveData<Boolean> = loading
    override fun bindValidEmail(): LiveData<Boolean> = validEmail
    override fun bindValidPassword(): LiveData<Boolean> = validPassword
    override fun bindLoginPassed(): LiveData<Boolean> = loginPassed
    override fun bindUserInfo(): LiveData<User> = userInfo

    override fun login(email: String, password: String){
       when {
           email.isEmpty() -> validEmail.value = false
           password.isEmpty() -> validPassword.value = false
           else -> {
               loggedUser = User(email, password)

               loading.value = true

               repository.login(loggedUser, {
                   loading.value = false
                   loginPassed.value = true
               }, {
                   loading.value = false
                   loginPassed.value = false
               })
           }
       }
    }

    override fun saveLoggedUser() = repository.saveLoggedUser(loggedUser)

    override fun loadLoggedUser() {
        if (repository.containsLoggedUser()) {
            val user = repository.getLogged()
            userInfo.value = user
        }
    }
}
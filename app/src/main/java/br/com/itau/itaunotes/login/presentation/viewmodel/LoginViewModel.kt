package br.com.itau.itaunotes.login.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.itau.itaunotes.login.data.repository.LoginRepositoryContract
import br.com.itau.itaunotes.login.data.model.User

class LoginViewModel(private val repository: LoginRepositoryContract): ViewModel(){

    private val _loading by lazy { MutableLiveData<Boolean>() }
    private val _validEmail by lazy { MutableLiveData<Boolean>() }
    private val _validPassword by lazy { MutableLiveData<Boolean>() }
    private val _loginSuccess by lazy { MutableLiveData<Boolean>() }
    private val _saveUser by lazy { MutableLiveData<Boolean>() }
    private val _userInfo by lazy { MutableLiveData<User>() }

    val isLoading: LiveData<Boolean>
        get() = _loading

    val validEmail: LiveData<Boolean>
        get() = _validEmail

    val validPassword: LiveData<Boolean>
        get() = _validPassword

    val logged: LiveData<Boolean>
        get() = _loginSuccess

    val saveLogin: LiveData<Boolean>
        get() = _saveUser

    val user: LiveData<User>
        get() = _userInfo

    private lateinit var loggedUser: User

    fun login(email: String, password: String){
       when {
           email.isEmpty() -> _validEmail.value = false
           password.isEmpty() -> _validPassword.value = false
           else -> {

               //Check the previous logged user.
               if (loggedUser.email != email ||
                   loggedUser.password != password) {

                   _saveUser.postValue(true)
               }

               loggedUser = User(email, password)

               _loading.value = true

               repository.login(loggedUser, {
                   _loading.value = false
                   _loginSuccess.value = true
               }, {
                   _loading.value = false
                   _loginSuccess.value = false
               })
           }
       }
    }

    fun saveLoggedUser() = repository.saveLoggedUser(loggedUser)

    fun load() {
        if (repository.containsLoggedUser()) {
            loggedUser = repository.getLogged()
            _userInfo.value = loggedUser
        }
    }
}
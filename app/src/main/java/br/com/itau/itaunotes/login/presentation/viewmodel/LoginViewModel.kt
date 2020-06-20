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
    private val _shouldSaveUser by lazy { MutableLiveData<Boolean>() }
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
        get() = _shouldSaveUser

    val user: LiveData<User?>
        get() = _userInfo

    fun login(email: String, password: String){
       when {
           email.isEmpty() -> _validEmail.value = false
           password.isEmpty() -> _validPassword.value = false
           else -> {
               _loading.value = true

               val user = User(email, password)

               repository.login(user, {
                   _loading.value = false

                   //Check the previous logged user.
                   when {
                       (_userInfo.value?.email != email || _userInfo.value?.password != password) -> {
                           _userInfo.value = user
                           _shouldSaveUser.postValue(true)
                       }
                       else -> _loginSuccess.postValue(true)
                   }
               }, {
                   _loading.value = false
                   _loginSuccess.value = false
               })
           }
       }
    }

    fun saveLoggedUser() {
        _userInfo.value?.let {user ->
            repository.saveLoggedUser(user)
        }
    }

    fun load() {
        if (repository.containsLoggedUser()) {
            _userInfo.value = repository.getLogged()
        }
    }
}
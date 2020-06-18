package br.com.itau.itaunotes.login.data.datasource

import android.util.Log
import br.com.itau.itaunotes.login.domain.model.User
import com.google.firebase.auth.FirebaseAuth

typealias baseBaseCallBack = () -> Unit

interface LoginDataSourceContract{
    fun login(
        loginRequest: User,
        successCallBack: baseBaseCallBack,
        errorCallBack: baseBaseCallBack
    )
}

class FirebaseDataSource: LoginDataSourceContract {
    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        firebaseAuth.addAuthStateListener {
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d("AUTH", "onAuthStateChanged:signed_in:" + user.uid)
            } else {
                Log.d("AUTH", "onAuthStateChanged:signed_out")
            }
        }
    }

    override fun login(
        loginRequest: User,
        successCallBack: baseBaseCallBack,
        errorCallBack: baseBaseCallBack
    ) {
        firebaseAuth.signInWithEmailAndPassword(
            loginRequest.email,
            loginRequest.password
        ).addOnCompleteListener {task ->
            when {
                task.isSuccessful -> successCallBack.invoke()
                else -> errorCallBack.invoke()
            }
        }
    }
}
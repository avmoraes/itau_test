package br.com.itau.itaunotes.login.data.datasource

import android.util.Log
import br.com.itau.itaunotes.login.data.model.User
import com.google.firebase.auth.FirebaseAuth

typealias baseCallBack = () -> Unit

interface LoginDataSourceContract{
    fun login(
        loginRequest: User,
        successCallBack: baseCallBack?,
        errorCallBack: baseCallBack?
    )
}

class FirebaseDataSource(
    private val firebaseAuth: FirebaseAuth
): LoginDataSourceContract {

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
        successCallBack: baseCallBack?,
        errorCallBack: baseCallBack?
    ) {
        firebaseAuth.signInWithEmailAndPassword(
            loginRequest.email,
            loginRequest.password
        ).addOnCompleteListener {task ->
            when {
                task.isSuccessful -> successCallBack?.invoke()
                else -> errorCallBack?.invoke()
            }
        }
    }
}
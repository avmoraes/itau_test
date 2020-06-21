package br.com.itau.itaunotes.login.data.datasource.auth

import android.util.Log
import br.com.itau.itaunotes.login.data.datasource.baseCallBack
import com.google.firebase.auth.FirebaseAuth

interface AuthContract{
    fun loginByEmail(email: String,
                     password: String,
                     success: baseCallBack?,
                        error: baseCallBack?
    )
}

class FirebaseEmailAuth(
    private val firebaseAuth: FirebaseAuth
): AuthContract{
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

    override fun loginByEmail(
        email: String,
        password: String,
        success: baseCallBack?,
        error: baseCallBack?
    ) {
        firebaseAuth.apply {
            signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task->
                    when {
                        task.isSuccessful -> success?.invoke()
                        else -> error?.invoke()
                    }
                }
        }
    }
}
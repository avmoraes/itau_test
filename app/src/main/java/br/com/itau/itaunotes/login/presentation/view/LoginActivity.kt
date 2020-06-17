package br.com.itau.itaunotes.login.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.commons.hideKeyBoard
import br.com.itau.itaunotes.commons.viewmodel.ViewModelFactory
import br.com.itau.itaunotes.login.domain.User
import br.com.itau.itaunotes.login.presentation.viewmodel.LoginViewModel
import br.com.itau.itaunotes.login.presentation.viewmodel.LoginViewModelContract
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var viewModel: LoginViewModelContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(LoginViewModel::class.java)

        viewModel.bindValidEmail().observe(this, Observer<Boolean> { _ ->
            showMsg(R.string.login_empty_email_text)
        })

        viewModel.bindValidPassword().observe(this, Observer<Boolean> { _ ->
            showMsg(R.string.login_empty_password_text)
        })

        viewModel.bindLoading().observe(this, Observer<Boolean> { loading ->
            loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        })

        viewModel.bindLoginPassed().observe(this, Observer<Boolean> { loginPassed ->
            when {
                loginPassed -> createSaveUserInfoDialog()?.show()
                else -> showMsg(R.string.login_error_text)
            }
        })

        viewModel.bindUserInfo().observe(this, Observer<User> { user ->
            loginEditText.setText(user.email)
            passwordEditText.setText(user.password)
        })

        loginButton.setOnClickListener {
            hideKeyBoard()
            viewModel.login(loginEditText.text.toString(), passwordEditText.text.toString())
        }

        viewModel.loadLoggedUser()
   }

    private fun showMsg(msgRes: Int){
        Toast.makeText( this, msgRes, Toast.LENGTH_LONG).show()
    }

    private fun createSaveUserInfoDialog(): AlertDialog? {
        return let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage(R.string.login_save_info_alert_content)
                setPositiveButton(R.string.login_save_info_alert_yes_bt
                ) { dialog, _ ->
                    viewModel.saveLoggedUser()
                    dialog.dismiss()
                }
                setNegativeButton(R.string.login_save_info_alert_no_bt
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            }
            builder.create()
        }
    }
}
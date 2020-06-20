package br.com.itau.itaunotes.login.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.commons.hideKeyBoard
import br.com.itau.itaunotes.login.data.model.User
import br.com.itau.itaunotes.login.di.firebaseModule
import br.com.itau.itaunotes.login.di.loginModule
import br.com.itau.itaunotes.login.di.prefsModule
import br.com.itau.itaunotes.login.presentation.viewmodel.LoginViewModel
import br.com.itau.itaunotes.notes.presentation.list.view.NotesListActivity
import br.com.itau.itaunotes.notes.presentation.list.viewmodel.NotesListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class LoginActivity : AppCompatActivity(R.layout.activity_main) {

    @VisibleForTesting
    private val loginDependencies by lazy { loadKoinModules(listOf(prefsModule, firebaseModule, loginModule)) }
    private fun inject() = loginDependencies

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        viewModel.validEmail.observe(this, Observer<Boolean> { _ ->
            showMsg(R.string.login_empty_email_text)
        })

        viewModel.validPassword.observe(this, Observer<Boolean> { _ ->
            showMsg(R.string.login_empty_password_text)
        })

        viewModel.isLoading.observe(this, Observer<Boolean> { loading ->
            loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        })

        viewModel.logged.observe(this, Observer<Boolean> { loginPassed ->
            when {
                loginPassed -> goToNotesList()
                else -> showMsg(R.string.login_error_text)
            }
        })

        viewModel.user.observe(this, Observer<User?> { user ->
            user?.let { logged ->
                loginEditText.setText(logged.email)
                passwordEditText.setText(logged.password)
            }
        })

        viewModel.saveLogin.observe(this, Observer<Boolean> { _ ->
            createSaveUserInfoDialog()?.show()
        })

        loginButton.setOnClickListener {
            hideKeyBoard()
            viewModel.login(loginEditText.text.toString(), passwordEditText.text.toString())
        }

        viewModel.load()
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
                    goToNotesList()
                }
                setNegativeButton(R.string.login_save_info_alert_no_bt
                ) { dialog, _ ->
                    dialog.dismiss()
                    goToNotesList()
                }
            }

            builder.create()
        }
    }

    private fun goToNotesList(){
        val intent = Intent(this, NotesListActivity::class.java)
        startActivity(intent)
        finish()
    }
}
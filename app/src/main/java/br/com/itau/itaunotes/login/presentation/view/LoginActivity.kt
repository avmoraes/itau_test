package br.com.itau.itaunotes.login.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.commons.hideKeyBoard
import br.com.itau.itaunotes.login.domain.model.User
import br.com.itau.itaunotes.login.presentation.viewmodel.LoginViewModel
import br.com.itau.itaunotes.notes.presentation.list.view.NotesListActivity
import br.com.itau.itaunotes.notes.presentation.list.viewmodel.NotesListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
package br.com.itau.itaunotes.tools.mocks

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class KoinRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(
            cl, KoinTestApp::class.java.name, context
        )
    }
}
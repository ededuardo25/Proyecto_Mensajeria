package com.example.proyecto_mensajeria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val timer = Timer()
        timer.schedule(
            timerTask {
                gotoLoginActivity() //Nombre del private
            }, 1000
        )

    }

    //MI FUNCION CREADA PARA QUE SE DIRIJA A TAL ACTIVITY
    private fun gotoLoginActivity() {
        val intent = Intent(this, MainLogin::class.java)
        startActivity(intent)
        finish()
    }

}




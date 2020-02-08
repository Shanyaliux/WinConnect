package com.shanya.winconnect

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.log

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnClickListener {
            val intent = Intent(this,MyIntentService::class.java)
            startService(intent)
        }

        button2.setOnClickListener {

        }

        button3.setOnClickListener {

        }


    }



}

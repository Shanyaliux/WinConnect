package com.shanya.winconnect

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.Socket


class MyIntentService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        Thread(Runnable {
            socket = Socket("192.168.1.118",8765)
            ReceivedDataThread().start()
        }).start()
        var previousTime:Long = 0
        cmb?.addPrimaryClipChangedListener {
            val now = System.currentTimeMillis()
            if (now - previousTime < 200){
                previousTime = now
                return@addPrimaryClipChangedListener
            }
            previousTime = now
            cLabel = cmb?.primaryClipDescription?.label
            if (cLabel != "Socket") {
                clipData = cmb?.primaryClip
                val item11: ClipData.Item? = clipData?.getItemAt(0)
                SendDataThread(item11?.text.toString()).start()
            }
        }
        return START_STICKY
    }

    var socket:Socket? = null
    private var data:ByteArray? = null
    private var cmb: ClipboardManager? = null
    var clipData: ClipData? = cmb?.primaryClip
    private var cLabel = cmb?.primaryClipDescription?.label
    private var tempData = ""

    inner class ReceivedDataThread:Thread(){
        override fun run() {
            super.run()
            try {
                val inputStream = socket?.getInputStream()
                var len: Int
                var flag = false
                while (true) {
                    sleep(100)
                    len = inputStream?.available()!!
                    while (len != 0) {
                        flag = true
                        data = ByteArray(len)
                        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                        inputStream.read(data)
                        sleep(10)
                        len = inputStream.available()
                    }
                    tempData = data?.let { String(it,Charsets.UTF_8) }.toString()
                    if (flag){
                        clipData = (ClipData.newPlainText("Socket",tempData))
                        cmb?.setPrimaryClip(clipData!!)
                        flag = false
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    inner class SendDataThread(private var sendData:String): Thread(){
        override fun run() {
            super.run()
            try {
                val outputStream = socket?.getOutputStream()
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
                bufferedWriter.write(sendData)
                bufferedWriter.flush()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

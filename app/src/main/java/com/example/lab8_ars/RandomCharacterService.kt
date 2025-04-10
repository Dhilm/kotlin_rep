package com.example.serviceexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.util.*

class RandomCharacterService : Service() {

    private var isRandomGeneratorOn = false
    private val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    private val TAG = "RandomCharacterService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Service Started", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Service started...")
        Log.i(TAG, "In onStartCommand Thread ID is ${Thread.currentThread().id}")
        isRandomGeneratorOn = true

        Thread {
            startRandomGenerator()
        }.start()

        return START_STICKY
    }

    private fun startRandomGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000)
                if (isRandomGeneratorOn) {
                    val randomIdx = Random().nextInt(26)  // Число от 0 до 25
                    val myRandomCharacter = alphabet[randomIdx]
                    Log.i(TAG, "Thread ID is ${Thread.currentThread().id}, Random Character is $myRandomCharacter")

                    val broadcastIntent = Intent("my.custom.action.tag.lab6").apply {
                        putExtra("randomCharacter", myRandomCharacter)
                    }
                    sendBroadcast(broadcastIntent)
                }
            } catch (e: InterruptedException) {
                Log.i(TAG, "Thread Interrupted.")
            }
        }
    }

    private fun stopRandomGenerator() {
        isRandomGeneratorOn = false
    }

    override fun onDestroy() {
        stopRandomGenerator()
        Toast.makeText(applicationContext, "Service Stopped", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Service Destroyed...")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
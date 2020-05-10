package com.org.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            CoroutineScope(IO).launch {
                fakeAPICall()
            }
        }
    }

    public suspend fun fakeAPICall() {
        // 2 seconds delay to simulate an API Call
        delay(2000)
        setTextUI("Coroutine started")
        setTextOnButton("Restart Coroutine")
        setTextUI("Coroutine restarted")
    }

    public suspend fun setTextUI(text : String) {
        withContext(Main){
            textView.text = text
        }
    }

    public fun setTextOnButton(text: String) {
        CoroutineScope(Main).launch {
            var job = withTimeoutOrNull(2000) {
                //delay(3000)
                button.text = text
            }

            if (job == null) {
                button.text = "Timeout"
            }
        }
    }
}

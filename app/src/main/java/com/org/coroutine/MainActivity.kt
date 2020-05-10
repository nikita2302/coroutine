package com.org.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.test.withTestContext

class MainActivity : AppCompatActivity() {


    private lateinit var job: CompletableJob
    private var clicked = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initJob()

        button.setOnClickListener {
            if (clicked) {
                clicked = false
                button.text = "Cancel Coroutine"
                initJob()
                CoroutineScope(IO + job).launch {
                    fakeAPICall()
                }
            } else {
                button.text = "Start Coroutine"
                clicked = true
                job.cancel(CancellationException("Job Cancelled Manually"))
                textView.text = "Coroutine Canceled"
            }
        }
    }

/*
    fun Button.startOrCancelJob(job : Job) {
        if(clicked == false) {
            //Job has started
            job.cancel()
        } else {
            //Start Job
        }
    }*/

    fun initJob() {
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var message = it
                if (message.isNullOrBlank()) {
                    message = "Job Cancelled"
                }
                showToast(message)

            }
        }
    }

    public fun showToast(text: String) {
        GlobalScope.launch(Main) {
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT ).show()
        }
    }

    public suspend fun fakeAPICall() {
        // 2 seconds delay to simulate an API Call
        delay(2000)
        setTextUI("Coroutine started")
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

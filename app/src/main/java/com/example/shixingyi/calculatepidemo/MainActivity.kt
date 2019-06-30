package com.example.shixingyi.calculatepidemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.TextView
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    private lateinit var result: TextView
    var n = 1
    private val STOP = -1
    private val SUSPEND = 0
    private val RUNNING = 1
    private var status = -1
    val mRunnable = Runnable {
        kotlin.run {
            while (status == RUNNING) {
                if (Thread.currentThread().isInterrupted()) {
                    break
                }
                val msg = Message()
                msg.obj = CalculatePi.calculatePi(n)
                handler.sendMessage(msg)
                n++
                if (n > 10000) {
                    status = STOP
                }
            }
        }
    }
    var handler: Handler = WithoutLeakhandler(this)

    private class WithoutLeakhandler(activity: MainActivity) : Handler() {
        private var activity: WeakReference<MainActivity> = WeakReference(activity)
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val activity = activity.get()
            if (activity != null) {
                activity.result.setText(msg!!.obj.toString())
                if (activity.status == -1) {
                    activity.result.setText("0")
                }
            } else {
                return
            }

        }
    }

    var myThread = Thread(mRunnable)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById(R.id.tv_result)
    }

    fun startCalculated(view: View) {
        if (status == STOP) {
            n = 1
            status = RUNNING
            myThread = Thread(mRunnable)
            myThread.start()
        }
        if (status == SUSPEND) {
            status = RUNNING
            myThread = Thread(mRunnable)
            myThread.start()
        }
    }

    fun pauseCalculated(view: View) {
        if (status == RUNNING) {
            status = SUSPEND
            myThread.interrupt()
        }
    }

    fun stopCalculated(view: View) {
        if (status == RUNNING) {
            myThread.interrupt()
            status = STOP
        }
        if (status == SUSPEND) {
            n = 1
            status = STOP
        }
        result.setText("0")

    }

    override fun onDestroy() {
        WithoutLeakhandler(this).removeCallbacksAndMessages(null)
        super.onDestroy()
    }


}

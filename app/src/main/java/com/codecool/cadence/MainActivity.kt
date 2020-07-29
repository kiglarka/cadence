package com.codecool.cadence

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val DOWNLOAD_JOB_KEY = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            initJobScheduler()
        }
    }

    fun initJobScheduler() {
        Log.d(TAG, "initJobScheduler: job initiated")
        val componentName: ComponentName = ComponentName(this, TimerService::class.java)
        val bundle: PersistableBundle = PersistableBundle()
        bundle.putInt("number", 10)
        val builder: JobInfo.Builder = JobInfo.Builder(DOWNLOAD_JOB_KEY, componentName)
            .setExtras(bundle)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)

        //reschedule very 15 mins
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPeriodic(15 * 60 * 1000, 30 * 60 * 1000)
        } else {
            builder.setPeriodic(15 * 60 * 1000)
        }

        val scheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(builder.build())

    }
}
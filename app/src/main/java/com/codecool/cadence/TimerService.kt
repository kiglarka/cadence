package com.codecool.cadence

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.AsyncTask
import android.os.PersistableBundle
import android.os.SystemClock
import android.util.Log

class TimerService : JobService() {

    lateinit private var asyncTask: MyAsyncTask
    private lateinit var parameters: JobParameters

    companion object {
        private const val TAG = "TimerService"

    }

    override fun onStartJob(params: JobParameters): Boolean {
        this.parameters = params
        Log.d(TAG, "onStartJob: job started")
        val bundle: PersistableBundle? = params?.extras
        var number: Int? = bundle?.getInt("number")
        asyncTask = MyAsyncTask()
        asyncTask.execute(number)
        // if job finishes successfully, return, it means that there is no need to reschedule the job
        // but in case we have another thread to run yet, or job to reschedule, return true
        return true
    }


     override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob: job canceled")
         if (null != asyncTask) {
             if (!asyncTask.isCancelled) {
                 asyncTask.cancel(true)
             }
         }
        return false
    }

    inner class MyAsyncTask : AsyncTask<Int, Int, String>() {

        override fun doInBackground(vararg params: Int?): String {
            for (i in 0 ..params[0]!!) {
                SystemClock.sleep(1000)
                publishProgress(i)
            }
            return "Job has been finished"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            Log.d(TAG, "onProgressUpdate: i was ${values[0]}")

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d(TAG, "onPostExecute: $result")
            jobFinished(parameters, true)
        }
    }
}
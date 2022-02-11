package com.breaktime.lab3.service

import android.app.*
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.breaktime.lab3.MainActivity
import java.util.concurrent.TimeUnit
import kotlin.math.abs


const val CHANNEL_ID = "5"
const val ACCELEROMETER_NOTIFICATION_ID = 123

class AccelerometerService : Service(), SensorEventListener {
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null

    var x = 0f
    var y = 0f
    var z = 0f
    var time = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        createNotificationChannel()
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        mAccelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager?.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        Toast.makeText(this, "on create", Toast.LENGTH_SHORT).show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "My service channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        Toast.makeText(this, "on start", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = Notification
            .Builder(this, CHANNEL_ID)
            .setContentText("Music player")
            .setContentIntent(pendingIntent)
            .build()

        startForeground(ACCELEROMETER_NOTIFICATION_ID, notification)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (abs(x - event.values[0]) > 3 || abs(y - event.values[1]) > 3 || abs(z - event.values[2]) > 3) {
            x = event.values[0]
            y = event.values[1]
            z = event.values[2]
            time = System.nanoTime()
        }
        if (TimeUnit.SECONDS.convert(System.nanoTime() - time, TimeUnit.NANOSECONDS) > 30) {
            time = System.nanoTime()
            Toast.makeText(this, "You need to rest", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}
package com.diaztisyah.sisteminformasi.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.marufaldi.sisteminformasi.R
import java.io.IOException
import java.lang.ref.WeakReference

class MediaService : Service(), MediaPlayerCallback {

    private var isReady: Boolean = false
    private var mMediaPlayer: MediaPlayer? = null

    private val TAG = MediaService::class.java.simpleName

    companion object {
        val ACTION_CREATE = " mediaservice.create"
        val ACTION_DESTROY = " mediaservice.destroy"
        const val PLAY = 0
        const val STOP = 1
        const val MUSIC_INDEX = "music_index"
    }

    fun makeBroadcast(data:String){
        val intent = Intent()
        intent.action = "mediaservice"
        intent.putExtra("name", data)
        sendBroadcast(intent)
    }

    override fun onStop() {
        if (mMediaPlayer?.isPlaying as Boolean || isReady) {
            mMediaPlayer?.stop()
            isReady = false
            stopNotif()
        }
    }

    override fun onPlay() {
        if (!isReady) {
            mMediaPlayer?.prepareAsync()
            val intent1 = Intent()
            makeBroadcast("Pause")
        } else {
            if (mMediaPlayer?.isPlaying as Boolean) {
                mMediaPlayer?.pause()
                makeBroadcast("Play")
            } else {
                mMediaPlayer?.start()
                makeBroadcast("Pause")
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        makeBroadcast("Play")
        return mMessenger.binder
    }

    override fun onUnbind(intent: Intent): Boolean{
        makeBroadcast("onUndbind")
        return super.onUnbind(intent)
    }

    private fun stopNotif() {
        stopForeground(false)
    }

    private fun showNotif(musicIndex:String) {
        val CHANNEL_DEFAULT_IMPORTANCE = "Channel_Test"
        val ONGOING_NOTIFICATION_ID = 1
        val notificationIntent = Intent(this, SetelMusikActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle("Music Player " + getApplicationInfo().loadLabel(getPackageManager()).toString())
            .setContentText("Playing music "+musicIndex)
            .setSmallIcon(R.drawable.ic_play_circle_outline_black_24dp)
            .setContentIntent(pendingIntent)
            .setTicker("Music Player")
            .build()
        val mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_DEFAULT_IMPORTANCE, "Music Player",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.setShowBadge(false)
            channel.setSound(null, null)
            mNotificationManager.createNotificationChannel(channel)
        }
        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }
    private fun init(musicIndex:String) {
        mMediaPlayer = MediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attribute = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            mMediaPlayer?.setAudioAttributes(attribute)
        } else {
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        val afd = applicationContext.resources.openRawResourceFd(getResources().getIdentifier(musicIndex,
            "raw" , getPackageName()))
        try {
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mMediaPlayer?.setOnPreparedListener(){
            isReady = true
            mMediaPlayer?.start()
            showNotif(musicIndex)
        }
        mMediaPlayer?.setOnErrorListener { mp, what, extra ->
            false
        }
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        val musicIndex = intent.getStringExtra(MUSIC_INDEX)
        init(musicIndex)
        if (action != null) {
            when (action) {
                ACTION_CREATE -> if (mMediaPlayer == null) {
                    Log.d(TAG,"action create")
                    init(musicIndex)
                }
                ACTION_DESTROY -> if (mMediaPlayer?.isPlaying as Boolean) {
                    Log.d(TAG,"action destroy")
                    stopSelf()
                }
                else -> {
                    Log.d(TAG,"action else")
                    init(musicIndex)
                }
            }
        }
        Log.d(TAG, "onStartCommand: ")
        return flags
    }
    private val mMessenger = Messenger(IncomingHandler(this))
    internal class IncomingHandler(playerCallback: MediaPlayerCallback) : Handler() {
        private val mediaPlayerCallbackWeakReference: WeakReference<MediaPlayerCallback> =
            WeakReference(playerCallback)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PLAY -> mediaPlayerCallbackWeakReference.get()?.onPlay()
                STOP -> mediaPlayerCallbackWeakReference.get()?.onStop()
                else -> super.handleMessage(msg)
            }
        }
    }
}

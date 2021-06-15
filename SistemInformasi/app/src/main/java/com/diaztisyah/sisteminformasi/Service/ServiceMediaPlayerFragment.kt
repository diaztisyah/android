package com.diaztisyah.sisteminformasi.Service


import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.marufaldi.sisteminformasi.App.DashboardActivity
import com.marufaldi.sisteminformasi.R
import kotlinx.android.synthetic.main.fragment_service_media_player.view.*

/**
 * A simple [Fragment] subclass.
 */
class ServiceMediaPlayerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_service_media_player, container, false)
        val spinnerItems = resources.getStringArray(R.array.music_index)
        val spinnerAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        root.spinner.adapter = spinnerAdapter as SpinnerAdapter?
        root.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?,
                                        position: Int, id: Long) {
                mService?.send(Message.obtain(null, MediaService.STOP, 0, 0))
                mBoundServiceIntent = Intent(activity, MediaService::class.java)
                mBoundServiceIntent.putExtra(MediaService.MUSIC_INDEX,
                    spinnerItems[position]);
                mBoundServiceIntent.action = MediaService.ACTION_CREATE
                activity!!.startService(mBoundServiceIntent)
                activity!!.bindService(mBoundServiceIntent, mServiceConnection,
                    Context.BIND_AUTO_CREATE)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        root.btn_play.setOnClickListener{
            mService?.send(Message.obtain(null, MediaService.PLAY, 0, 0))
        }
        root.btn_stop.setOnClickListener{
            mService?.send(Message.obtain(null, MediaService.STOP, 0, 0))
        }
        root.btn_kembali.setOnClickListener{
            activity?.let{
                val intent = Intent (it, DashboardActivity::class.java)
                it.startActivity(intent)
            }
        }
        activity!!.registerReceiver(broadcastReceiver, intentActionName)
        return root
    }
    private val intentActionName = IntentFilter("mediaservice")
    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
        }
    }

    private var mService: Messenger? = null
    private lateinit var mBoundServiceIntent: Intent
    private var mServiceBound = false

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mServiceBound = false
        }
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = Messenger(service)
            mServiceBound = true
        }
    }

}

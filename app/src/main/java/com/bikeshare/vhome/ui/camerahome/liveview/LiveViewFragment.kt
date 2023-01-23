package com.bikeshare.vhome.ui.camerahome.liveview

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bikeshare.vhome.databinding.FragmentLiveviewBinding
import dagger.hilt.android.AndroidEntryPoint
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.io.IOException

@AndroidEntryPoint
class LiveViewFragment : Fragment() {
    private var _binding: FragmentLiveviewBinding? = null
    private val binding get() = _binding!!

    private var mLibVLC: LibVLC? = null
    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLiveviewBinding.inflate(inflater, container, false)
        val view = binding.root
        initPlayerView()

        return view
    }

    private fun initPlayerView() {
        //val filePath = intent.getStringExtra(KEY_FILE_PATH)
        mLibVLC = LibVLC(context, ArrayList<String>().apply {
            add("--no-drop-late-frames")
            add("--no-skip-frames")
            add("--rtsp-tcp")
            add("-vvv")
        })
        mMediaPlayer = MediaPlayer(mLibVLC)
        mMediaPlayer?.attachViews(binding.videoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW)
        try {
            val rtspUrl = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4"
            val httpUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            val uri = Uri.parse(httpUrl) // ..whatever you want url...or even file fromm asset

            Media(mLibVLC, uri).apply {
                setHWDecoderEnabled(true, false);
                addOption(":network-caching=150");
                addOption(":clock-jitter=0");
                addOption(":clock-synchro=0");
                mMediaPlayer?.media = this

            }.release()

            mMediaPlayer?.play();

            binding.videoLayout.setOnClickListener {
                if(mMediaPlayer!!.isPlaying()){
                    mMediaPlayer?.pause()
                } else {
                    mMediaPlayer?.play();
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    companion object {
        private const val USE_TEXTURE_VIEW = false
        private const val ENABLE_SUBTITLES = true
    }
}
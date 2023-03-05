package com.example.audiorecorderfinalchoice

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.audiorecorderfinalchoice.databinding.FragmentRecordingListBinding
import java.io.File
import java.io.IOException


class RecordingListFragment : Fragment(), OnItemClick {
    private lateinit var binding: FragmentRecordingListBinding
    private lateinit var audioListAdapter: AudioListAdapter
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var listOfFiles: List<File> = listOf()
    private lateinit var fileToPlay: File
    private val playerSeekBar: SeekBar by lazy {
        requireView().findViewById(R.id.sb_player)
    }
    private lateinit var playerHandler: Handler
    private val btnPlay: ImageView by lazy {
        requireView().findViewById(R.id.iv_play_list)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = requireActivity().getExternalFilesDir("/")?.absolutePath
        val directory = File(path.toString())
        listOfFiles = directory.listFiles()!!.toList()
        audioListAdapter = AudioListAdapter(listOfFiles, this)
        with(binding) {
            rvRecordList.setHasFixedSize(true)
            rvRecordList.layoutManager = LinearLayoutManager(requireContext())
            rvRecordList.adapter = audioListAdapter

        }
    }

    override fun onItemClickListener(file: File, position: Int) {
        fileToPlay = file
        if (isPlaying) {
            stopAudio()
            playAudio(fileToPlay)
        } else {
            playAudio(fileToPlay)
        }
    }

    private fun playAudio(fileToPlay: File) {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer?.setDataSource(fileToPlay.absolutePath)
            mediaPlayer?.prepare()
            mediaPlayer?.start()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        initializeSeekBar()
        isPlaying = true
        mediaPlayer?.setOnCompletionListener {
            stopAudio()
        }
        btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
        playerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }
    private fun initializeSeekBar(){
        playerSeekBar.max = mediaPlayer!!.duration
        playerHandler = Handler()
        playerHandler.postDelayed(object : Runnable{
            override fun run() {
                try{
                    playerSeekBar.progress = mediaPlayer!!.currentPosition
                    playerHandler.postDelayed(this,1000)
                }catch (e:Exception){
                    playerSeekBar.progress = 0
                }
            }

        },0)
    }


    private fun stopAudio() {
        btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
        isPlaying = false
        mediaPlayer?.stop()
        mediaPlayer?.reset()

    }
}
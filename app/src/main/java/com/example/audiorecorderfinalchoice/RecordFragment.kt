package com.example.audiorecorderfinalchoice

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.audiorecorderfinalchoice.databinding.FragmentRecordBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RecordFragment : Fragment() {
    private lateinit var binding: FragmentRecordBinding
    private var isRecording: Boolean = false
    private var recordPermission = Manifest.permission.RECORD_AUDIO
    private var permissionCode = 21
    private var mediaRecorder: MediaRecorder? = null
    private var recordFile: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listRecordBtn.setOnClickListener {
            findNavController().navigate(R.id.action_recordFragment_to_recordinListFragment)
        }
        binding.recordBtn.setOnClickListener {
            if (isRecording) {
                stopRecording()
                binding.recordBtn.setImageResource(R.drawable.ic_baseline_mic_none_24)
                isRecording = false

            } else {
                if (checkPermission()) {
                    startRecording()
                    binding.recordBtn.setImageResource(R.drawable.ic_baseline_stop_24)
                    isRecording = true
                }

            }

        }
    }

    private fun startRecording() = with(binding) {
        binding.chronometer.start()
        val path = requireActivity().getExternalFilesDir("/")?.absolutePath
        val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault())
        val currentTime = Date()
        recordFile = "Recording ${formatter.format(currentTime)}.3gp"
        tvHintForUser.text = getString(R.string.recording_started, recordFile)
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setOutputFile("$path/$recordFile")
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        try {
            mediaRecorder?.prepare()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        mediaRecorder?.start()

    }

    private fun stopRecording() {
        try {
            mediaRecorder?.stop()
        } catch (e: RuntimeException) {
            e.printStackTrace()
            mediaRecorder?.reset()
            mediaRecorder?.release()
            mediaRecorder = null
        }
        binding.tvHintForUser.text = getString(R.string.recording_stopped, recordFile)
        binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.chronometer.stop()
    }

    private fun checkPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                recordPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(recordPermission),
                permissionCode
            )
            false
        }

    }

    override fun onStop() {
        super.onStop()
        if(isRecording) stopRecording()
    }


}
package com.learning.mobile.miwok

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import com.learning.mobile.miwok.databinding.WordListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [NumbersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NumbersFragment : Fragment() {
    lateinit var binding: WordListBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioManager: AudioManager
    private val audioMgrChangeListener = AudioManager.OnAudioFocusChangeListener {
        when (it) {
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mediaPlayer!!.pause()
                mediaPlayer!!.seekTo(0)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                mediaPlayer!!.stop()
                releaseMediaPlayer()
            }
            AudioManager.AUDIOFOCUS_GAIN -> mediaPlayer!!.start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding =   WordListBinding.inflate(layoutInflater)

        audioManager = activity!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val words: MutableList<Word> = mutableListOf(
                Word("one", "lutti", R.drawable.number_one, R.raw.number_one),
                Word("two", "otiiko", R.drawable.number_two, R.raw.number_two),
                Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three),
                Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four),
                Word("five", "massoka", R.drawable.number_five, R.raw.number_five),
                Word("six", "temmoka", R.drawable.number_six, R.raw.number_six),
                Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven),
                Word("eight", "kawinda", R.drawable.number_eight, R.raw.number_eight),
                Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine),
                Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten),
        )
        //This method should never be used as this will take lot of memory in the system.
        /* for(word in words){
             var  wordsView : TextView = TextView(this)
             wordsView.text = word
             binding.rootView.addView(wordsView)
         }*/

        val itemsAdapter: WordAdapter = WordAdapter(activity!!, words, resources.getColor(R.color.category_numbers))
        binding.list.adapter = itemsAdapter

        binding.list.setOnItemClickListener { _, _, position, _ ->
            //val clickWord: Word = parent.adapter.getItem(position) as Word
            val clickWord: Word = words[position]
            releaseMediaPlayer()
            if(requestAudioFocus()) {
                mediaPlayer = MediaPlayer.create(activity, clickWord.audioResourceId)
                mediaPlayer!!.start()
                mediaPlayer!!.setOnCompletionListener { releaseMediaPlayer() }
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NumbersFragment.
         */
        @JvmStatic
        fun newInstance() = NumbersFragment()
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }
    private fun releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        // Regardless of the current state of the media player, release its resources
        // because we no longer need it.
        if (mediaPlayer != null) {
            //mediaPlayer!!.reset()
            mediaPlayer!!.release()
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null
            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            audioManager.abandonAudioFocus(audioMgrChangeListener)
        }

    }

    private fun requestAudioFocus(): Boolean {
        return audioManager.requestAudioFocus(audioMgrChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED

        //alternative method in the latest version
        /*  var audioattr: AudioAttributes = AudioAttributes.Builder()
                  .setUsage(AudioAttributes.USAGE_MEDIA)
                  .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                  .build()
          var audioFocusRequest: AudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                  .setAudioAttributes(audioattr)
                  .setAcceptsDelayedFocusGain(true)
                  .setWillPauseWhenDucked(true)
                  .setOnAudioFocusChangeLister(audioMgrChangeListener)
                  .build()*/
    }
}
package com.learning.mobile.miwok

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.learning.mobile.miwok.databinding.WordListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [FamilyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FamilyFragment : Fragment() {
    lateinit var binding: WordListBinding
    private  var mediaPlayer: MediaPlayer? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =   WordListBinding.inflate(layoutInflater)
        audioManager = activity!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        var words: MutableList<Word> = mutableListOf(
            Word("father", "әpә",R.drawable.family_father, R.raw.family_father),
            Word("mother", "әṭa",R.drawable.family_mother, R.raw.family_mother),
            Word("son", "angsi",R.drawable.family_son, R.raw.family_son),
            Word("daughter", "tune",R.drawable.family_daughter, R.raw.family_daughter),
            Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother),
            Word("young brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother),
            Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister),
            Word("young sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister),
            Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother),
            Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather),
        )

        /* for(word in words){
             var  wordsView : TextView = TextView(this)
             wordsView.text = word
             binding.rootView.addView(wordsView)
         }*/

        var itemsAdapter: WordAdapter = WordAdapter(activity!!, words, resources.getColor(R.color.category_family))
        binding.list.adapter = itemsAdapter
        binding.list.setOnItemClickListener { parent, view, position, id ->
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
         * @return A new instance of fragment FamilyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = FamilyFragment()
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
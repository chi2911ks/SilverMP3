package com.cbtool.silvermp3.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.FragmentPlayerBinding
import com.cbtool.silvermp3.interfaces.FragmentUIConfig
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.utils.TimeHelper.formatDuration
import com.cbtool.silvermp3.utils.glideCustom
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class PlayerFragment : Fragment(), FragmentUIConfig {


    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val _controller by lazy { (activity as MainActivity).getController() }
    private val controller get() = _controller

    private val playerViewModel: PlayerViewModel by activityViewModel()
    private val libraryViewModel: LibraryViewModel by activityViewModel()

    private val handler = Handler(Looper.getMainLooper())

    private var playlists: MutableMap<String, Song> = mutableMapOf()
    private var currentIndex: Int = 0

    override fun shouldShowBottomBar() = false
    override fun getNavigationItemId() = R.id.player
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            currentIndex = getInt(ARG_INDEX)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.favouriteBtn.setOnClickListener(onClickListener)
        binding.shuffleBtn.setOnClickListener(onClickListener)
        binding.repeatBtn.setOnClickListener(onClickListener)
        binding.playBtn.setOnClickListener(onClickListener)
        binding.previousBtn.setOnClickListener(onClickListener)
        binding.nextBtn.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it) {
            binding.favouriteBtn -> {
                playerViewModel.toggleFavourite(playerViewModel.currentSong.value!!)
                libraryViewModel.refreshFavouriteCount()
            }

            binding.shuffleBtn -> shuffleClick()
            binding.repeatBtn -> repeatClick()
            binding.playBtn -> playClick()
            binding.previousBtn -> {
                controller?.apply {
                    if (hasPreviousMediaItem()) {
                        seekToPreviousMediaItem()
                    }
                }

            }
            binding.nextBtn -> {
                controller?.apply {
                    if (hasNextMediaItem()) {
                        seekToNextMediaItem()
                    }
                }

            }
        }

    }


    private val playerListener = object : Player.Listener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            binding.playBtn.isSelected = isPlaying
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaItem?.apply {
                loadDetailSong(
                    mediaId,
                    mediaMetadata.title.toString(),
                    mediaMetadata.artist.toString(),
                    mediaMetadata.artworkUri.toString()
                )
                if (playlists.containsKey(mediaId)) {
                    playerViewModel.setCurrentSong(playlists[mediaId] ?: Song())
                }
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                ready()
            }
        }
    }

    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            controller?.let {
                val currentPositionInSeconds = (it.currentPosition / 1000)
                binding.startTime.text = formatDuration(currentPositionInSeconds)
                binding.seekBar.progress = currentPositionInSeconds.toInt()
                handler.postDelayed(this, 500)
                playerViewModel.setCurrentDuration(currentPositionInSeconds.toInt())
            }
        }
    }

    private fun updateSeekBar() {
        handler.removeCallbacks(updateSeekBarRunnable)

        handler.post(updateSeekBarRunnable)
    }

    private fun stopSeekBarUpdate() {
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    private fun initSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    binding.startTime.text = formatDuration(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                stopSeekBarUpdate()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    val newPosition = it.progress.toLong()
                    controller?.seekTo(newPosition * 1000L)
                }
                updateSeekBar()
            }
        })
    }

    fun setMediaItem(song: Song): MediaItem {
        return MediaItem.Builder()
            .setMediaId(song.id)
            .setUri(song.audioUrl)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(song.title)
                    .setArtist(song.artistName)
                    .setArtworkUri(song.coverUrl.toUri())
                    .build()
            ).build()
    }

    fun loadDetailSong(id: String, title: String, artistName: String, coverUrl: String) {
        if (!isAdded || context == null) return
        binding.titleTv.text = title
        binding.artistTv.text = artistName
        glideCustom(requireContext(), binding.coverImage, coverUrl, 50)
        playerViewModel.checkFavourite(id)
    }

    fun ready() {
        Log.d("Media", "ready")
        controller!!.apply {
            val duration = duration / 1000L
            binding.seekBar.max = duration.toInt()
            binding.endTime.text = formatDuration(duration)
        }
        updateSeekBar()
    }

    fun init() {
        controller?.addListener(playerListener)
        initSeekBar()
        playerViewModel.isFavourite.observe(viewLifecycleOwner) {
            binding.favouriteBtn.isSelected = it
        }
        playerViewModel.currentSong.observe(viewLifecycleOwner){
            it.apply {
                binding.playBtn.isSelected = controller?.isPlaying == true
                loadDetailSong(id, title, artistName, coverUrl)
                ready()
            }
        }
        playerViewModel.songs.observe(viewLifecycleOwner) {
            playerViewModel.setCurrentSong(it[0])
            it.forEach { song ->
                playlists[song.id] = song
            }
            Log.d("Media", it[0].toString())
            controller?.apply {
                if (playerViewModel.currentSong.value != null && controller?.currentMediaItem?.mediaId == playerViewModel.currentSong.value!!.id) return@observe

                setMediaItems(it.map { song -> setMediaItem(song) })
                seekTo(currentIndex, 0)
                prepare()
                play()
            } ?: run {
                Log.d("Media", "MediaController chưa sẵn sàng")
            }
        }

    }
    fun shuffleClick() {
        binding.shuffleBtn.isSelected = !binding.shuffleBtn.isSelected
        controller?.shuffleModeEnabled = binding.shuffleBtn.isSelected
    }

    fun repeatClick() {
        binding.repeatBtn.isSelected = !binding.repeatBtn.isSelected
        controller?.repeatMode =
            if (binding.repeatBtn.isSelected) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
    }

    fun playClick() {
        controller?.apply {
            if (isPlaying) {
                pause()
            } else {
                play()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        controller?.removeListener(playerListener)
        stopSeekBarUpdate()
    }

    companion object {
        const val TAG = "PlayerFragment"
        private const val ARG_INDEX = "INDEX"
        @JvmStatic
        fun newInstance(index: Int=0) = PlayerFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_INDEX, index)
            }
        }

    }
}
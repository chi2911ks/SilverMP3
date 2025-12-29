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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.SilverApplication
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.FragmentPlayerBinding
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.utils.TimeHelper.formatDuration
import com.cbtool.silvermp3.utils.slideUpAndShow
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val _controller by lazy { (activity as MainActivity).getController() }
    private val controller get() = _controller

    private val viewModel: PlayerViewModel by activityViewModel()
    private val libraryViewModel: LibraryViewModel by activityViewModel()

    private val handler = Handler(Looper.getMainLooper())

    private var songs: HashMap<String, Song> = hashMapOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        this.slideUpAndShow()

        init()
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.favouriteBtn.setOnClickListener(onClickListener)
        binding.shuffleBtn.setOnClickListener(onClickListener)
        binding.repeatBtn.setOnClickListener(onClickListener)
        binding.playBtn.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it) {
            binding.favouriteBtn -> {
                viewModel.toggleFavourite(viewModel.currentSong.value!!)
                libraryViewModel.refreshFavouriteCount()
            }
            binding.shuffleBtn -> shuffleClick()
            binding.repeatBtn -> repeatClick()
            binding.playBtn -> playClick()
            binding.previousBtn -> controller?.seekToPrevious()
            binding.nextBtn -> controller?.seekToNext()
        }

    }

    fun addListener() {
        controller?.apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    binding.playBtn.isSelected = isPlaying
//                    (activity as MainActivity).showMiniUi(isPlaying)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val id = mediaItem?.mediaId
                    val song = songs[id]
                    song?.apply {
                        viewModel.setCurrentSong(this)
                        loadDetailSong(this)
                    }


                }
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_ENDED -> {}

                        Player.STATE_IDLE -> {}
                        Player.STATE_BUFFERING -> {}
                        Player.STATE_READY -> ready()

                    }

                }
            })

        }

    }

    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            controller?.let {
                val currentPositionInSeconds = (it.currentPosition / 1000)
                binding.startTime.text = formatDuration(currentPositionInSeconds)
                binding.seekBar.progress = currentPositionInSeconds.toInt()
                handler.postDelayed(this, 500)
                viewModel.setCurrentDuration(currentPositionInSeconds.toInt())
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
    fun setMediaItem(song: Song): MediaItem{
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
    fun loadDetailSong(song: Song){
        binding.titleTv.text = song.title
        binding.artistTv.text = song.artistName
        Glide
            .with(requireContext())
            .load(song.coverUrl)
            .transform(CenterCrop(), RoundedCorners(50))
            .into(binding.coverImage)
        viewModel.checkFavourite(song.id)
    }
    fun ready(){
        controller!!.apply {
            val duration = duration / 1000L
            binding.seekBar.max = duration.toInt()
            binding.endTime.text = formatDuration(duration)
        }
        updateSeekBar()
    }
    fun init() {
        addListener()
        initSeekBar()
        viewModel.isFavourite.observe(viewLifecycleOwner){
            binding.favouriteBtn.isSelected = it
        }

        (activity as MainActivity).setSelectedItemId()
        viewModel.currentSong.observe(viewLifecycleOwner){ song ->
            binding.playBtn.isSelected = controller?.isPlaying == true
            loadDetailSong(song)
            ready()
        }
        viewModel.songs.observe(viewLifecycleOwner) {
            val items = mutableListOf<MediaItem>()
            it.forEach { song ->
                songs.put(song.id, song)
                items.add(setMediaItem(song))
            }
            controller?.let { p ->
                if (controller?.currentMediaItem?.mediaId == items[0].mediaId) return@observe
                p.setMediaItems(items)
                p.prepare()
                p.play()
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
    }

    companion object {
        @JvmStatic
        val instance: PlayerFragment by lazy { PlayerFragment() }

        @JvmStatic
        fun newInstance() = PlayerFragment()
    }
}
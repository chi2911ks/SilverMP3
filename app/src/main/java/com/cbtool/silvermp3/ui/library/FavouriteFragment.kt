package com.cbtool.silvermp3.ui.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.Player
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.FragmentFavouriteBinding
import com.cbtool.silvermp3.ui.custom.SongOptionsSheet
import com.cbtool.silvermp3.ui.player.PlaybackPersistence
import com.cbtool.silvermp3.ui.player.PlaybackState
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val favouriteViewModel: FavouriteViewModel by activityViewModel()
    private val playerViewModel: PlayerViewModel by activityViewModel()
    private val songs: MutableList<Song> = mutableListOf()
    private val _controller by lazy { (activity as MainActivity).getController() }
    private val controller get() = _controller
    private val playbackPersistence by lazy { PlaybackPersistence(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        controller?.removeListener(playerListener)
        controller?.addListener(playerListener)
        Log.d("FavouriteFragment", "init: getSongs")
        favouriteViewModel.getSongs()
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()

        }
        binding.playBtn.isSelected = controller?.isPlaying == true && PlaybackState.currentSourcePlaying["favourite"] == true
        binding.rvSongs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = SongAdapter(
            onItemClick = { song ->
                playerViewModel.setSongs(songs)
                PlaybackState.currentIndex = songs.indexOf(song)
                PlaybackState.currentSourcePlaying = hashMapOf("favourite" to true)
//                playbackPersistence.saveState("favourite", songs.indexOf(song))
                (activity as MainActivity).navigateTo(PlayerFragment.newInstance())
            },
            moreClick = { song ->
                val bottomSheet = SongOptionsSheet.newInstance(song, "favourites")
                bottomSheet.show(requireActivity().supportFragmentManager, "BottomSheetSong")
            })
        binding.rvSongs.adapter = adapter
        favouriteViewModel.song.observe(viewLifecycleOwner) {
            binding.tvCount.text = "${it.size} bài hát"
            adapter.submitList(it)
            songs.clear()
            songs.addAll(it)
        }
        binding.playBtn.setOnClickListener {

            if (binding.playBtn.isSelected) {
                controller?.pause()
                binding.playBtn.isSelected = false
            } else {
                binding.playBtn.isSelected = true
                val lastIndex = PlaybackState.currentIndex
                if (lastIndex != 0) {
                    controller?.play()
                }else{
                    PlaybackState.currentIndex = 0
                    playerViewModel.setSongs(songs)
                    (activity as MainActivity).navigateTo(PlayerFragment.newInstance())
                }

            }
            PlaybackState.currentSourcePlaying = hashMapOf("favourite" to binding.playBtn.isSelected)
        }
    }
    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (PlaybackState.currentSourcePlaying.containsKey("favourite")) {
                if (_binding != null)
                {
                    binding.playBtn.isSelected = isPlaying
                }

                PlaybackState.currentSourcePlaying = hashMapOf("favourite" to isPlaying)
            }
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        controller?.removeListener(playerListener)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }
}
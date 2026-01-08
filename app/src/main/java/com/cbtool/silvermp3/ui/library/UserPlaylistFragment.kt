package com.cbtool.silvermp3.ui.library

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.media3.common.Player
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.FragmentPlayListBinding
import com.cbtool.silvermp3.interfaces.FragmentUIConfig
import com.cbtool.silvermp3.ui.custom.PlaylistOptionsSheet
import com.cbtool.silvermp3.ui.custom.SongOptionsSheet
import com.cbtool.silvermp3.ui.player.PlaybackPersistence
import com.cbtool.silvermp3.ui.player.PlaybackState
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.collections.hashMapOf

class UserPlaylistFragment : Fragment() {
    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlist: Playlist
    private val playlistViewModel: UserPlaylistViewModel by activityViewModel()
    private val playerViewModel: PlayerViewModel by activityViewModel()
    private val songs: MutableList<Song> = mutableListOf()
    private val playbackPersistence by lazy { PlaybackPersistence(requireContext()) }
    private val controller by lazy { (activity as MainActivity).getController() }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlist = arguments?.getParcelable(ARG_PLAYLIST, Playlist::class.java) ?: Playlist()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val lastSource = playbackPersistence.getLastSource()
        controller?.removeListener(playerListener)
        controller?.addListener(playerListener)
        playlist.apply {
            binding.tvTitle.text = title
            binding.tvDescription.text = description
            playlistViewModel.getSongs(id)
            binding.playBtn.isSelected = controller?.isPlaying == true && PlaybackState.currentSourcePlaying[id] == true
        }
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.moreBtn.setOnClickListener {
            PlaylistOptionsSheet.newInstance(playlist)
                .show(requireActivity().supportFragmentManager, "PlaylistOptionsSheet")
        }
        val songAdapter = SongAdapter(onItemClick = { song ->
            playerViewModel.setSongs(songs)
            PlaybackState.currentIndex = songs.indexOf(song)
            PlaybackState.currentSourcePlaying = hashMapOf(playlist.id to true)

//            playbackPersistence.saveState(playlist.id, songs.indexOf(song))
            (activity as MainActivity).navigateTo(PlayerFragment.newInstance())

        }, moreClick = { song ->
            val bottomSheet = SongOptionsSheet.newInstance(song, playlist.id)
            bottomSheet.show(requireActivity().supportFragmentManager, "SongOptionsSheet")
        })
        binding.rvSongs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSongs.adapter = songAdapter
        playlistViewModel.song.observe(viewLifecycleOwner) {
            it.apply {
                binding.tvCount.text = "$size bài hát"
                songAdapter.submitList(it)
                songs.clear()
                songs.addAll(it)
            }
        }
        playlistViewModel.playlist.observe(viewLifecycleOwner) {
            it.apply {
                binding.tvTitle.text = title
                binding.tvDescription.text = description
            }
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
            PlaybackState.currentSourcePlaying = hashMapOf(playlist.id to binding.playBtn.isSelected)
        }

    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (PlaybackState.currentSourcePlaying.containsKey(playlist.id)) {
                if (_binding != null)
                {
                    binding.playBtn.isSelected = isPlaying
                }
                PlaybackState.currentSourcePlaying = hashMapOf(playlist.id to isPlaying)
            }
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        controller?.removeListener(playerListener)
    }

    companion object {
        const val ARG_PLAYLIST = "PLAYLIST"

        @JvmStatic
        fun newInstance(playlist: Playlist) =
            UserPlaylistFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLAYLIST, playlist)
                }
            }
    }
}
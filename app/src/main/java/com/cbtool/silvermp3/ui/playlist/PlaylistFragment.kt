package com.cbtool.silvermp3.ui.playlist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.FragmentPlayListBinding
import com.cbtool.silvermp3.ui.custom.SongOptionsSheet
import com.cbtool.silvermp3.ui.player.PlaybackPersistence
import com.cbtool.silvermp3.ui.player.PlaybackState
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import com.cbtool.silvermp3.utils.glideCustom
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlist: Playlist
    private val playlistViewModel: PlaylistViewModel by activityViewModel()
    private val playerViewModel: PlayerViewModel by activityViewModel()
    private val songs: MutableList<Song> = mutableListOf()
    private val playbackPersistence by lazy { PlaybackPersistence(requireContext()) }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlist = it.getParcelable(ARG_PLAYLIST, Playlist::class.java) ?: Playlist()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun init() {
        playlist.apply {
            playlistViewModel.refresh(id)
        }

        playlistViewModel.playlist.observe(viewLifecycleOwner) {
            it.apply {
                binding.tvTitle.text = title
                binding.tvDescription.text = description
                glideCustom(requireContext(), binding.imageView, coverUrl)

            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val songAdapter = SongAdapter(onItemClick = { song ->
            playerViewModel.setSongs(songs)
            PlaybackState.currentIndex = songs.indexOf(song)
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


    }


    companion object {
        const val ARG_PLAYLIST = "PLAYLIST"

        @JvmStatic
        fun newInstance(playlist: Playlist) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PLAYLIST, playlist)
            }
        }
    }
}
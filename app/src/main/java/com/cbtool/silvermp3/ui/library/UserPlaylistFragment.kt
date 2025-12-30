package com.cbtool.silvermp3.ui.library

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.FragmentPlayListBinding
import com.cbtool.silvermp3.ui.custom.PlaylistOptionsSheet
import com.cbtool.silvermp3.ui.custom.SongOptionsSheet
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class UserPlaylistFragment : Fragment() {
    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlist: Playlist
    private val playlistViewModel: UserPlaylistViewModel by activityViewModel()
    private val playerViewModel: PlayerViewModel by activityViewModel()

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
        playlist.apply {
            binding.tvTitle.text = title
            binding.tvDescription.text = description
            playlistViewModel.getSongs(id)

        }
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.moreBtn.setOnClickListener {
            PlaylistOptionsSheet.newInstance(playlist)
                .show(requireActivity().supportFragmentManager, "PlaylistOptionsSheet")
        }
        val songAdapter = SongAdapter(onItemClick = { song ->
            playerViewModel.setSongs(song)
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
            }
        }
        playlistViewModel.playlist.observe(viewLifecycleOwner) {
            it.apply {
                binding.tvTitle.text = title
                binding.tvDescription.text = description
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
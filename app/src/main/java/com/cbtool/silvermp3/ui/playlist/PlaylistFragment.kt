package com.cbtool.silvermp3.ui.playlist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.FragmentPlayListBinding
import com.cbtool.silvermp3.interfaces.FragmentUIConfig
import com.cbtool.silvermp3.ui.custom.SongOptionsSheet
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlist: Playlist
    private val playlistViewModel: PlaylistViewModel by activityViewModel()
    private val playerViewModel: PlayerViewModel by activityViewModel()

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
                Glide
                    .with(requireContext())
                    .load(coverUrl)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(binding.imageView)
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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
package com.cbtool.silvermp3.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.FragmentGenreBinding
import com.cbtool.silvermp3.ui.custom.SongOptionsSheet
import com.cbtool.silvermp3.ui.player.PlaybackState
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class GenreFragment : Fragment() {
    private var _binding: FragmentGenreBinding? = null
    private val binding get() = _binding!!
    private var name: String? = null

    private val genreViewModel: GenreViewModel by viewModel()
    private val playerViewModel: PlayerViewModel by activityViewModel()
    private val songs: MutableList<Song> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_GENRE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    fun init() {
        binding.processSong.visibility = View.VISIBLE
        name?.let {
            binding.nameTv.text = it
            genreViewModel.getSongByGenre(it.lowercase())
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val songAdapter = SongAdapter(
            moreClick = {
                val bottomSheet = SongOptionsSheet.newInstance(it)
                bottomSheet.show(requireActivity().supportFragmentManager, "BottomSheetSong")
            },
            onItemClick = {
                playerViewModel.setSongs(songs)
                PlaybackState.currentIndex = songs.indexOf(it)
                (activity as MainActivity).navigateTo(PlayerFragment.newInstance())
            }
        )
        binding.songsRc.adapter = songAdapter
        binding.songsRc.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        genreViewModel.songs.observe(viewLifecycleOwner){
            songAdapter.submitList(it)
            binding.processSong.visibility = View.GONE
            songs.clear()
            songs.addAll(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val ARG_GENRE = "GENRE"

        @JvmStatic
        fun newInstance(name: String) =
            GenreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GENRE, name)
                }
            }
    }
}
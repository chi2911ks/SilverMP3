package com.cbtool.silvermp3.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.adapter.ArtistAdapter
import com.cbtool.silvermp3.adapter.PlaylistAdapter
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.databinding.FragmentHomeBinding
import com.cbtool.silvermp3.interfaces.FragmentUIConfig
import com.cbtool.silvermp3.ui.custom.SongOptionsSheet
import com.cbtool.silvermp3.ui.player.PlaybackState
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import com.cbtool.silvermp3.ui.playlist.PlaylistFragment
import com.cbtool.silvermp3.ui.profile.ProfileFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : Fragment(), FragmentUIConfig {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModel()
    private val playerViewModel: PlayerViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("HomeFragment", "HomeViewModel: $viewModel")
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        binding.profileBtn.setOnClickListener {
            (activity as MainActivity).navigateTo(ProfileFragment.newInstance())
        }
        val songAdapter = SongAdapter(onItemClick = { song ->
            playerViewModel.setSongs(listOf(song))
            PlaybackState.currentIndex = 0
            (activity as MainActivity).navigateTo(PlayerFragment.newInstance())
        }, moreClick = { song ->
            val bottomSheet = SongOptionsSheet.newInstance(song)
            bottomSheet.show(requireActivity().supportFragmentManager, "BottomSheetSong")
        })
        val playlistAdapter = PlaylistAdapter {
            (activity as MainActivity).navigateTo(PlaylistFragment.newInstance(it))
        }
        val artistAdapter = ArtistAdapter {

        }
        binding.rvSongs.isNestedScrollingEnabled = false
        binding.rvPlaylists.isNestedScrollingEnabled = false
        binding.rvArtists.isNestedScrollingEnabled = false
        binding.rvSongs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPlaylists.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArtists.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvSongs.adapter = songAdapter
        binding.rvPlaylists.adapter = playlistAdapter
        binding.rvArtists.adapter = artistAdapter
        viewModel.songs.observe(viewLifecycleOwner) { songAdapter.submitList(it) }
        viewModel.playlists.observe(viewLifecycleOwner) {
            playlistAdapter.submitList(it)
        }
        viewModel.artists.observe(viewLifecycleOwner) {
            artistAdapter.submitList(it)
        }
        if (viewModel.songs.value.isNullOrEmpty()) {
            viewModel.loadSongs()
        }
        if (viewModel.artists.value.isNullOrEmpty()){
            viewModel.loadArtists()
        }
        if (viewModel.playlists.value.isNullOrEmpty()) {
            viewModel.loadPlaylists()
        }
        binding.swipeRefreshLayout.apply {
            // Ẩn background trắng mờ (chỉ còn vòng xoay)
            setProgressBackgroundColorSchemeColor(android.graphics.Color.TRANSPARENT)

            // Tuỳ chỉnh vị trí vòng xoay (ví dụ dịch xuống 80dp)
            val density = resources.displayMetrics.density
            setProgressViewOffset(true, 0, (80 * density).toInt())
            setSize(SwipeRefreshLayout.LARGE)

            // Đổi màu vòng xoay (nếu muốn)
            setColorSchemeResources(
                R.color.white,
                R.color.colorSecondary
            )

            // Lắng nghe khi user kéo xuống
            setOnRefreshListener {
                refresh()
                isRefreshing = false
            }
        }
    }

    private fun refresh() {
        viewModel.loadSongs()
        viewModel.loadPlaylists()
    }

    override fun getNavigationItemId(): Int = R.id.home

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
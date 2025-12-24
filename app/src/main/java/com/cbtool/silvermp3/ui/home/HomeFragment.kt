package com.cbtool.silvermp3.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.databinding.FragmentHomeBinding
import com.cbtool.silvermp3.ui.custom.BottomSheetSong
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

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

    private fun init() {
        (activity as MainActivity).setSelectedItemId()
        val songAdapter = SongAdapter(onItemClick = { song ->
            playerViewModel.setSongs(song)
            (activity as MainActivity).navigateTo(PlayerFragment.newInstance())

        }, moreClick = { song ->
            val bottomSheet = BottomSheetSong.newInstance(song)
            bottomSheet.show(requireActivity().supportFragmentManager, "BottomSheetSong")
        })
        binding.rvSongs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSongs.adapter = songAdapter
        viewModel.songs.observe(viewLifecycleOwner) { songAdapter.submitList(it) }
        if (viewModel.songs.value.isNullOrEmpty()) {
            viewModel.loadSongs()
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
                viewModel.loadSongs()
                isRefreshing = false
            }
        }
    }
    companion object {
//        @JvmStatic
//        val instance: HomeFragment by lazy { HomeFragment() }

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
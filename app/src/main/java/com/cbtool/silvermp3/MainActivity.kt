package com.cbtool.silvermp3

import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cbtool.silvermp3.databinding.ActivityMainBinding
import com.cbtool.silvermp3.interfaces.FragmentUIConfig
import com.cbtool.silvermp3.service.PlayBackService
import com.cbtool.silvermp3.ui.OnBoardingActivity
import com.cbtool.silvermp3.ui.home.HomeFragment
import com.cbtool.silvermp3.ui.library.LibraryFragment
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.ui.player.PlaybackState
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import com.cbtool.silvermp3.ui.search.SearchFragment
import com.cbtool.silvermp3.utils.createNicePaletteBackground
import com.cbtool.silvermp3.utils.navigateTo
import com.cbtool.silvermp3.utils.startNewActivity
import com.google.common.util.concurrent.MoreExecutors
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var mediaController: MediaController
    private val playerViewModel: PlayerViewModel by viewModel()
    private val libraryViewModel: LibraryViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()
    private var progressJob: Job? = null
    private val onClick: View.OnClickListener = View.OnClickListener {
        when (it) {
            binding.miniUIPlayer -> {
                navigateTo(PlayerFragment.newInstance())
            }

            binding.favouriteBtn -> {
                playerViewModel.toggleFavourite(playerViewModel.currentSong.value!!)
                libraryViewModel.refreshFavouriteCount()
            }

            binding.miniPlayBtn -> {
                mediaController.apply {
                    if (isPlaying) {
                        pause()
                    } else {
                        play()
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        if (auth.currentUser == null) {
            startNewActivity(OnBoardingActivity::class.java, true)
            return
        }
        navigateTo(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navigateTo(HomeFragment())
                R.id.search -> navigateTo(SearchFragment())
                R.id.library -> navigateTo(LibraryFragment())
                R.id.player -> {
                    if (mediaController.isPlaying){
                    navigateTo(PlayerFragment())}
                }

            }
            true
        }
        init()
    }


    fun setSelectedItemId() {
        val isPlaying = mediaController.isPlaying
        val fragment = getCurrentFragment()
        if (fragment == null) {
            finish()
        }
        if (fragment is FragmentUIConfig) {
            binding.bottomNavigationView.selectedItemId = fragment.getNavigationItemId()
            binding.bottomAppBar.isVisible = fragment.shouldShowBottomBar()
            // MiniPlayer chỉ hiện khi không phải PlayerFragment và đang có nhạc
            binding.miniUIPlayer.isVisible = fragment.shouldShowBottomBar() && isPlaying
        }else{
            binding.bottomAppBar.isVisible = true
            binding.miniUIPlayer.isVisible = isPlaying
        }
    }

    fun init() {
        mainViewModel.createController()
        lifecycleScope.launch {
            mainViewModel.controller.collectLatest { controller ->
                if (controller != null) {
                    controller.removeListener(playerListener)
                    controller.addListener(playerListener)
                    mediaController = controller

                    // set up button
                    supportFragmentManager.addOnBackStackChangedListener {
                        setSelectedItemId()
                    }
                    binding.miniUIPlayer.setOnClickListener(onClick)
                    binding.favouriteBtn.setOnClickListener(onClick)
                    binding.miniPlayBtn.setOnClickListener(onClick)
                    playerViewModel.isFavourite.observe(this@MainActivity) {
                        binding.favouriteBtn.isSelected = it
                    }
                }
            }
        }


    }
    private val playerListener = object: Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            showMiniUi(isPlaying)

            if (isPlaying) {
                startProgress()
            } else {
                progressJob?.cancel()
            }
        }


        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onMediaMetadataChanged(mediaMetadata)
            PlaybackState.currentIndex = mediaController.currentMediaItemIndex
            binding.tvMiniArtist.text = mediaMetadata.artist.toString()
            binding.tvMiniTitle.text = mediaMetadata.title.toString()
            Glide.with(this@MainActivity)
                .asBitmap()
                .load(mediaMetadata.artworkUri.toString())
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        binding.imageMiniCover.setImageBitmap(resource)
                        val gradient = applicationContext.createNicePaletteBackground(resource)
                        binding.miniUIPlayer.background = gradient

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
            binding.miniPlayBtn.isSelected = mediaController.isPlaying == true
        }
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                binding.progressBar.max = (mediaController.duration / 1000).toInt()
            }
        }
    }
    private fun startProgress() {
        if (progressJob?.isActive == true) return
        progressJob = lifecycleScope.launch {
            while (isActive) {
                binding.progressBar.progress =
                    (mediaController.currentPosition / 1000).toInt()
                delay(1000)
            }
        }
    }


    fun showMiniUi(isPlaying: Boolean) {
        binding.miniPlayBtn.isSelected = isPlaying
        if (isPlaying && getCurrentFragment() !is PlayerFragment) {
            binding.miniUIPlayer.isVisible = true
        }
    }

    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(binding.frameLayout.id)
    }

    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.navigateTo(binding.frameLayout.id, fragment, addToBackStack)
    }

    override fun onDestroy() {
        if (::mediaController.isInitialized){
            mediaController.release()
            mediaController.removeListener(playerListener)
        }

        progressJob?.cancel()
        progressJob = null

        super.onDestroy()
    }
}
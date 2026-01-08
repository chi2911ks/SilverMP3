package com.cbtool.silvermp3

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.ActivityMainBinding
import com.cbtool.silvermp3.interfaces.FragmentUIConfig
import com.cbtool.silvermp3.ui.OnBoardingActivity
import com.cbtool.silvermp3.ui.home.HomeFragment
import com.cbtool.silvermp3.ui.library.LibraryFragment
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.ui.player.PlaybackPersistence
import com.cbtool.silvermp3.ui.player.PlaybackState
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import com.cbtool.silvermp3.ui.search.SearchFragment
import com.cbtool.silvermp3.utils.createNicePaletteBackground
import com.cbtool.silvermp3.utils.navigateTo
import com.cbtool.silvermp3.utils.startNewActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mediaController get() = (application as SilverApplication).mediaController
    private val playerViewModel: PlayerViewModel by viewModel()
    private val libraryViewModel: LibraryViewModel by viewModel()
    private var progressJob: Job? = null
    private val playbackPersistence by lazy { PlaybackPersistence(this) }
    fun getController(): MediaController? = mediaController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Áp dụng padding top cho Status Bar và padding bottom cho Navigation Bar
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        if (auth.currentUser == null) {
            startNewActivity(OnBoardingActivity::class.java, true)
            return
        }
        if (savedInstanceState == null) {
            navigateTo(HomeFragment())
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navigateTo(HomeFragment())
                R.id.search -> navigateTo(SearchFragment())
                R.id.library -> navigateTo(LibraryFragment())
                R.id.player -> {
                    if (mediaController?.isPlaying == true){
                    navigateTo(PlayerFragment())}

                }

            }
            true
        }
        init()
    }

    override fun onDestroy() {
        mediaController?.release()
        progressJob?.cancel()
        playbackPersistence.clearState()
        super.onDestroy()
    }

    fun setSelectedItemId() {
        val isPlaying = (application as SilverApplication).mediaController?.isPlaying == true
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
        observeMediaController()
        supportFragmentManager.addOnBackStackChangedListener {
            setSelectedItemId()
        }
        binding.miniUIPlayer.setOnClickListener(onClick)
        binding.favouriteBtn.setOnClickListener(onClick)
        binding.miniPlayBtn.setOnClickListener(onClick)
        playerViewModel.isFavourite.observe(this) {
            binding.favouriteBtn.isSelected = it
        }

    }

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
                mediaController?.apply {
                    if (isPlaying) {
                        pause()
                    } else {
                        play()
                    }
                }
            }
        }
    }

    private fun observeMediaController() {
        val app = (application as SilverApplication)

        lifecycleScope.launch {
            // Quan sát Flow, khi nào có controller (khác null) thì thực hiện logic
            app.controllerFlow.collectLatest { controller ->
                if (controller != null) {
                    Log.d("MainActivity", "Controller nhận được, đang đăng ký listener")
                    setupMediaListener(controller)
                }
            }
        }
    }

    // Tách riêng logic đăng ký listener
    private fun setupMediaListener(controller: MediaController) {
        controller.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                showMiniUi(isPlaying)

                if (isPlaying) {
                    startProgress(controller)
                } else {
                    progressJob?.cancel()
                }
            }


            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
//                playbackPersistence.setLastIndex(controller.currentMediaItemIndex)
                PlaybackState.currentIndex = controller.currentMediaItemIndex
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
                binding.miniPlayBtn.isSelected = mediaController?.isPlaying == true
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    binding.progressBar.max = (controller.duration / 1000).toInt()
                }
            }
        })

        // Cập nhật UI ngay lập tức nếu đang có nhạc chạy sẵn
        showMiniUi(controller.isPlaying)
    }
    private fun startProgress(controller: MediaController) {
        progressJob?.cancel()

        progressJob = lifecycleScope.launch {
            while (isActive) {
                binding.progressBar.progress =
                    (controller.currentPosition / 1000).toInt()
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
}
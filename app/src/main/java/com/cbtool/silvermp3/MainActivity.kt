package com.cbtool.silvermp3

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.ActivityMainBinding
import com.cbtool.silvermp3.ui.OnBoardingActivity
import com.cbtool.silvermp3.ui.home.HomeFragment
import com.cbtool.silvermp3.ui.library.LibraryFragment
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import com.cbtool.silvermp3.ui.search.SearchFragment
import com.cbtool.silvermp3.utils.createNicePaletteBackground
import com.cbtool.silvermp3.utils.navigateTo
import com.cbtool.silvermp3.utils.startNewActivity
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mediaController get() = (application as SilverApplication).mediaController
    private val playerViewModel: PlayerViewModel by viewModel()
    private val libraryViewModel: LibraryViewModel by viewModel()
    fun getController(): MediaController? = mediaController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0) // chỉ chừa khoảng top
            insets
        }
        if (auth.currentUser == null) {
            startNewActivity(OnBoardingActivity::class.java, true)
        }
        if (savedInstanceState == null) {
            navigateTo(HomeFragment())
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> navigateTo(HomeFragment())
                R.id.search -> navigateTo(SearchFragment())
                R.id.library -> navigateTo(LibraryFragment())
                R.id.now_playing -> navigateTo(PlayerFragment())

            }
            true
        }
        init()
    }
    fun setSelectedItemId() {
        val currentFragment = getCurrentFragment()
        when (currentFragment) {
            is HomeFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.home
                binding.bottomAppBar.isVisible = true
                binding.miniUIPlayer.isVisible = mediaController?.isPlaying == true
            }

            is SearchFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.search
                binding.bottomAppBar.isVisible = true
                binding.miniUIPlayer.isVisible = mediaController?.isPlaying == true
            }

            is LibraryFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.library
                binding.bottomAppBar.isVisible = true
                binding.miniUIPlayer.isVisible = mediaController?.isPlaying == true
            }

            is PlayerFragment -> {
                binding.bottomAppBar.isVisible = false
                binding.miniUIPlayer.isVisible = false
            }

            else -> {
                // Optional: Handle any other fragments or null case
                binding.bottomAppBar.isVisible = true
                binding.miniUIPlayer.isVisible = mediaController?.isPlaying == true
            }
        }
    }
    fun init(){
        addListener()
        onBackPressedDispatcher.addCallback(this){
            if (supportFragmentManager.backStackEntryCount > 0){
                supportFragmentManager.popBackStack()
                setSelectedItemId()
            }else{
                finish()
            }
        }
        binding.miniUIPlayer.setOnClickListener(onClick)
        binding.favouriteBtn.setOnClickListener(onClick)
        binding.miniPlayBtn.setOnClickListener(onClick)
        playerViewModel.currentSong.observe(this){
            loadUISong(song = it)
        }
        playerViewModel.currentDuration.observe(this){
            binding.progressBar.progress = it
        }
        playerViewModel.isFavourite.observe(this){
            binding.favouriteBtn.isSelected = it
        }

    }
    private val onClick: View.OnClickListener = View.OnClickListener{
        when(it){
            binding.miniUIPlayer -> {
                navigateTo(PlayerFragment.newInstance())
            }
            binding.favouriteBtn -> {
                playerViewModel.toggleFavourite(playerViewModel.currentSong.value!!)
                libraryViewModel.refreshFavouriteCount()
            }
            binding.miniPlayBtn -> {
                mediaController?.apply {
                    if (isPlaying){
                        pause()
                    }else{
                        play()
                    }
                }
            }
        }
    }
    private fun loadUISong(song: Song){
        song.apply {
            binding.tvMiniArtist.text = artistName
            binding.tvMiniTitle.text = title
            Glide.with(this@MainActivity)
                .asBitmap()
                .load(song.coverUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.imageMiniCover.setImageBitmap(resource)
                        val gradient = applicationContext.createNicePaletteBackground(resource)
                        binding.miniUIPlayer.background = gradient
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
            binding.miniPlayBtn.isSelected = mediaController?.isPlaying == true
        }

    }
    fun addListener() {
        val controller = mediaController
        if (controller == null) {
            Log.w("MainActivityChi", "MediaController chưa sẵn sàng → chờ init lại sau 300ms")
            Handler(Looper.getMainLooper()).postDelayed({
                addListener()
            }, 300)
            return
        }

        mediaController?.apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    showMiniUi(isPlaying)
                }
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_ENDED -> {}
                        Player.STATE_IDLE -> {}
                        Player.STATE_BUFFERING -> {}
                        Player.STATE_READY -> binding.progressBar.max = (duration / 1000).toInt()
                    }

                }
            })

        }

    }
    fun showMiniUi(isPlaying: Boolean){
        binding.miniPlayBtn.isSelected = isPlaying
        if (isPlaying && getCurrentFragment() is HomeFragment){
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
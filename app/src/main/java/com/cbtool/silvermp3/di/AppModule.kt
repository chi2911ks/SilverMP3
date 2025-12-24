package com.cbtool.silvermp3.di


import com.cbtool.silvermp3.data.repository.auth.EmailAuthRepository
import com.cbtool.silvermp3.data.repository.auth.GoogleAuthRepository
import com.cbtool.silvermp3.data.repository.auth.PhoneAuthRepository
import com.cbtool.silvermp3.data.repository.firestore.GenresRepository
import com.cbtool.silvermp3.data.repository.firestore.SongsRepository
import com.cbtool.silvermp3.data.repository.firestore.UserFavouriteRepository
import com.cbtool.silvermp3.data.repository.firestore.UserPlaylistRepository
import com.cbtool.silvermp3.data.repository.firestore.UsersRepository
import com.cbtool.silvermp3.ui.auth.login.viewmodel.EmailLoginViewModel
import com.cbtool.silvermp3.ui.auth.login.viewmodel.GoogleLoginViewModel
import com.cbtool.silvermp3.ui.auth.login.viewmodel.PhoneAuthViewModel

import com.cbtool.silvermp3.ui.auth.register.viewmodel.EmailRegisterViewModel
import com.cbtool.silvermp3.ui.custom.SongOptionsViewModel
import com.cbtool.silvermp3.ui.home.HomeViewModel
import com.cbtool.silvermp3.ui.library.FavouriteViewModel
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.ui.library.PlaylistViewModel
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import com.cbtool.silvermp3.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { EmailAuthRepository() }
    single { PhoneAuthRepository() }
    single { GoogleAuthRepository(androidContext()) }

    single { UsersRepository() }
    single { SongsRepository() }
    single { GenresRepository() }
    single { UserPlaylistRepository() }
    single { UserFavouriteRepository() }
    viewModel { EmailLoginViewModel(get())  }
    viewModel { EmailRegisterViewModel(get(), get()) }
    viewModel { GoogleLoginViewModel(get(), get()) }
    viewModel { PhoneAuthViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { PlayerViewModel(get()) }
    viewModel { LibraryViewModel(get(), get()) }
    viewModel { FavouriteViewModel(get()) }
    viewModel { SongOptionsViewModel(get()) }
    viewModel { PlaylistViewModel(get()) }
}
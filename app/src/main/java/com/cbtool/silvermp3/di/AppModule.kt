package com.cbtool.silvermp3.di


import com.cbtool.silvermp3.data.repository.auth.EmailAuthRepository
import com.cbtool.silvermp3.data.repository.auth.GoogleAuthRepository
import com.cbtool.silvermp3.data.repository.auth.PhoneAuthRepository
import com.cbtool.silvermp3.data.repository.firestore.ArtistsRepositoryImpl
import com.cbtool.silvermp3.data.repository.firestore.GenresRepositoryImpl
import com.cbtool.silvermp3.data.repository.firestore.PlaylistRepositoryImpl
import com.cbtool.silvermp3.data.repository.firestore.SongsRepositoryImpl
import com.cbtool.silvermp3.data.repository.firestore.UserFavouriteRepositoryImpl
import com.cbtool.silvermp3.data.repository.firestore.UserPlaylistRepositoryImpl
import com.cbtool.silvermp3.data.repository.firestore.UsersRepositoryImpl
import com.cbtool.silvermp3.interfaces.ArtistsRepository
import com.cbtool.silvermp3.interfaces.GenresRepository
import com.cbtool.silvermp3.interfaces.PlaylistRepository
import com.cbtool.silvermp3.interfaces.SongRepository
import com.cbtool.silvermp3.interfaces.UserFavouriteRepository
import com.cbtool.silvermp3.interfaces.UserPlaylistRepository
import com.cbtool.silvermp3.ui.auth.login.viewmodel.EmailLoginViewModel
import com.cbtool.silvermp3.ui.auth.login.viewmodel.GoogleLoginViewModel
import com.cbtool.silvermp3.ui.auth.login.viewmodel.PhoneAuthViewModel

import com.cbtool.silvermp3.ui.auth.register.viewmodel.EmailRegisterViewModel
import com.cbtool.silvermp3.ui.custom.SongOptionsViewModel
import com.cbtool.silvermp3.ui.home.HomeViewModel
import com.cbtool.silvermp3.ui.library.FavouriteViewModel
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.ui.library.UserPlaylistViewModel
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import com.cbtool.silvermp3.ui.playlist.PlaylistViewModel
import com.cbtool.silvermp3.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { EmailAuthRepository() }
    single { PhoneAuthRepository() }
    single { GoogleAuthRepository(androidContext()) }

    single { UsersRepositoryImpl() }

    singleOf(::SongsRepositoryImpl) {bind<SongRepository>()}
    singleOf(::GenresRepositoryImpl) {bind<GenresRepository>()}
    singleOf(::UserPlaylistRepositoryImpl) {bind<UserPlaylistRepository>()}
    singleOf(::UserFavouriteRepositoryImpl) {bind<UserFavouriteRepository>()}
    singleOf(::PlaylistRepositoryImpl) {bind<PlaylistRepository>()}
    singleOf(::ArtistsRepositoryImpl) {bind<ArtistsRepository>()}


    viewModel { EmailLoginViewModel(get())  }
    viewModel { EmailRegisterViewModel(get(), get()) }
    viewModel { GoogleLoginViewModel(get(), get()) }
    viewModel { PhoneAuthViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { PlayerViewModel(get()) }
    viewModel { LibraryViewModel(get(), get()) }
    viewModel { FavouriteViewModel(get()) }
    viewModel { SongOptionsViewModel(get()) }
    viewModel { UserPlaylistViewModel(get()) }
    viewModel { PlaylistViewModel(get()) }
}
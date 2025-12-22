package com.cbtool.silvermp3.utils

import com.cbtool.silvermp3.data.model.Artist
import com.cbtool.silvermp3.data.model.Genre
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.data.repository.firestore.ArtistsRepository
import com.cbtool.silvermp3.data.repository.firestore.GenresRepository
import com.cbtool.silvermp3.data.repository.firestore.SongsRepository

object Test {
    private const val URL =
        "https://raw.githubusercontent.com/chi2911ks/music-data/refs/heads/main/"

    fun addArtist() {
        val repo = ArtistsRepository()
        repo.add(
            Artist(
                name = "Đen Vâu",
                bio = "Đen Vâu",
                photoUrl = URL + "artists/den-vau/photo.jpg"
            )
        )
        repo.add(
            Artist(
                name = "Sơn Tùng - MTP",
                bio = "Sơn Tùng",
                photoUrl = URL + "artists/son-tung-mtp/photo.jpg"
            )
        )
        repo.add(
            Artist(
                name = "Jack-J97",
                bio = "Jack",
                photoUrl = URL + "artists/jack-97/photo.jpg"
            )
        )
        repo.add(
            Artist(
                name = "MCK",
                bio = "MCK",
                photoUrl = URL + "artists/mck/photo.jpg"
            )
        )
    }

    fun addSongs() {
        val repo = SongsRepository()
        repo.add(
            Song(
                title = "Chúng Ta Của Hiện Tại",
                artistId = "UWjsLXCcOFf3HGo0eG6i",
                artistName = "Sơn Tùng - MTP",
                coverUrl = URL + "audios/son-tung-m-tp-chung-ta-cua-hien-tai-official-music-video/son-tung-m-tp-chung-ta-cua-hien-tai-official-music-video.jpg",
                audioUrl = URL + "audios/son-tung-m-tp-chung-ta-cua-hien-tai-official-music-video/son-tung-m-tp-chung-ta-cua-hien-tai-official-music-video.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Chúng Ta Của Tương Lai",
                artistId = "UWjsLXCcOFf3HGo0eG6i",
                artistName = "Sơn Tùng - MTP",
                coverUrl = URL + "audios/son-tung-m-tp-chung-ta-cua-tuong-lai-official-music-video/son-tung-m-tp-chung-ta-cua-tuong-lai-official-music-video.jpg",
                audioUrl = URL + "audios/son-tung-m-tp-chung-ta-cua-tuong-lai-official-music-video/son-tung-m-tp-chung-ta-cua-tuong-lai-official-music-video.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Có Chắc Yêu Là Đây",
                artistId = "UWjsLXCcOFf3HGo0eG6i",
                artistName = "Sơn Tùng - MTP",
                coverUrl = URL + "audios/son-tung-m-tp-co-chac-yeu-la-ay-official-music-video/son-tung-m-tp-co-chac-yeu-la-ay-official-music-video.jpg",
                audioUrl = URL + "audios/son-tung-m-tp-co-chac-yeu-la-ay-official-music-video/son-tung-m-tp-co-chac-yeu-la-ay-official-music-video.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Muộn Rồi Mà Sao Còn",
                artistId = "UWjsLXCcOFf3HGo0eG6i",
                artistName = "Sơn Tùng - MTP",
                coverUrl = URL + "audios/son-tung-m-tp-muon-roi-ma-sao-con-official-music-video/son-tung-m-tp-muon-roi-ma-sao-con-official-music-video.jpg",
                audioUrl = URL + "audios/son-tung-m-tp-muon-roi-ma-sao-con-official-music-video/son-tung-m-tp-muon-roi-ma-sao-con-official-music-video.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Đừng Làm Trái Tim Anh Đau",
                artistId = "UWjsLXCcOFf3HGo0eG6i",
                artistName = "Sơn Tùng - MTP",
                coverUrl = URL + "audios/son-tung-m-tp-ung-lam-trai-tim-anh-au-official-music-video/son-tung-m-tp-ung-lam-trai-tim-anh-au-official-music-video.jpg",
                audioUrl = URL + "audios/son-tung-m-tp-ung-lam-trai-tim-anh-au-official-music-video/son-tung-m-tp-ung-lam-trai-tim-anh-au-official-music-video.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Ngôi Sao Cô Đơn",
                artistId = "F6AuExLO8bvhe7u7WI8L",
                artistName = "Jack - J97",
                coverUrl = URL + "audios/jack-j97-ngoi-sao-co-on-official-music-video/jack-j97-ngoi-sao-co-on-official-music-video.jpg",
                audioUrl = URL + "audios/jack-j97-ngoi-sao-co-on-official-music-video/jack-j97-ngoi-sao-co-on-official-music-video.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Thiên Lý Ơi",
                artistId = "F6AuExLO8bvhe7u7WI8L",
                artistName = "Jack - J97",
                coverUrl = URL + "audios/jack-j97-thien-ly-oi-official-music-video/jack-j97-thien-ly-oi-official-music-video.jpg",
                audioUrl = URL + "audios/jack-j97-thien-ly-oi-official-music-video/jack-j97-thien-ly-oi-official-music-video.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Từ Nơi Tôi Sinh Ra",
                artistId = "F6AuExLO8bvhe7u7WI8L",
                artistName = "Jack - J97",
                coverUrl = URL + "audios/jack-j97-tu-noi-toi-sinh-ra-official-video-huge-respect-from-vietnam/jack-j97-tu-noi-toi-sinh-ra-official-video-huge-respect-from-vietnam.jpg",
                audioUrl = URL + "audios/jack-j97-tu-noi-toi-sinh-ra-official-video-huge-respect-from-vietnam/jack-j97-tu-noi-toi-sinh-ra-official-video-huge-respect-from-vietnam.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Xoá Tên Anh Đi",
                artistId = "F6AuExLO8bvhe7u7WI8L",
                artistName = "Jack - J97",
                coverUrl = URL + "audios/jack-j97-xoa-ten-anh-i-official-music-video-album26/jack-j97-xoa-ten-anh-i-official-music-video-album26.jpg",
                audioUrl = URL + "audios/jack-j97-xoa-ten-anh-i-official-music-video-album26/jack-j97-xoa-ten-anh-i-official-music-video-album26.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "Là Một Thằng Con Trai",
                artistId = "F6AuExLO8bvhe7u7WI8L",
                artistName = "Jack - J97",
                coverUrl = URL + "audios/jack-la-1-thang-con-trai-official-mv-j97/jack-la-1-thang-con-trai-official-mv-j97.jpg",
                audioUrl = URL + "audios/jack-la-1-thang-con-trai-official-mv-j97/jack-la-1-thang-con-trai-official-mv-j97.mp3",
                duration = 0,
                genres = listOf("pop", "v-pop")
            )
        )
        repo.add(
            Song(
                title = "2H",
                artistId = "HFfhO68stypRxvNm1Iqk",
                artistName = "MCK",
                coverUrl = URL + "audios/2h-mck/2h-mck.jpg",
                audioUrl = URL + "audios/2h-mck/2h-mck.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "N0L4B3L",
                artistId = "HFfhO68stypRxvNm1Iqk",
                artistName = "MCK",
                coverUrl = URL + "audios/N0L4B3L/N0L4B3L.jpg",
                audioUrl = URL + "audios/N0L4B3L/N0L4B3L.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "RPT MCK - Best of MCK | One Click Version",
                artistId = "HFfhO68stypRxvNm1Iqk",
                artistName = "MCK",
                coverUrl = URL + "audios/rpt-mck-best-of-mck-one-click-version/rpt-mck-best-of-mck-one-click-version.jpg",
                audioUrl = URL + "audios/rpt-mck-best-of-mck-one-click-version/rpt-mck-best-of-mck-one-click-version.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )

        repo.add(
            Song(
                title = "Anh Đếch Cần Gì Nhiều Ngoài Em",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-anh-ech-can-gi-nhieu-ngoai-em-ft-vu-thanh-ong-mv/en-anh-ech-can-gi-nhieu-ngoai-em-ft-vu-thanh-ong-mv.jpg",
                audioUrl = URL + "audios/en-anh-ech-can-gi-nhieu-ngoai-em-ft-vu-thanh-ong-mv/en-anh-ech-can-gi-nhieu-ngoai-em-ft-vu-thanh-ong-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Lang Thang",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-lang-ang-mv/en-lang-ang-mv.jpg",
                audioUrl = URL + "audios/en-lang-ang-mv/en-lang-ang-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Luôn Yêu Đời",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-luon-yeu-oi-ft-cheng-mv/en-luon-yeu-oi-ft-cheng-mv.jpg",
                audioUrl = URL + "audios/en-luon-yeu-oi-ft-cheng-mv/en-luon-yeu-oi-ft-cheng-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Mang Tiền Về Cho Mẹ",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-mang-tien-ve-cho-me-ft-nguyen-thao-mv/en-mang-tien-ve-cho-me-ft-nguyen-thao-mv.jpg",
                audioUrl = URL + "audios/en-mang-tien-ve-cho-me-ft-nguyen-thao-mv/en-mang-tien-ve-cho-me-ft-nguyen-thao-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Một Triệu Like",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-mot-trieu-like-ft-thanh-ong-mv/en-mot-trieu-like-ft-thanh-ong-mv.jpg",
                audioUrl = URL + "audios/en-mot-trieu-like-ft-thanh-ong-mv/en-mot-trieu-like-ft-thanh-ong-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Lộn Xộn 3",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-muoi-nam-ft-ngoc-linh-mv-lon-xon-3/en-muoi-nam-ft-ngoc-linh-mv-lon-xon-3.jpg",
                audioUrl = URL + "audios/en-muoi-nam-ft-ngoc-linh-mv-lon-xon-3/en-muoi-nam-ft-ngoc-linh-mv-lon-xon-3.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Nấu Ăn Cho Em",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-nau-an-cho-em-ft-pialinh-mv/en-nau-an-cho-em-ft-pialinh-mv.jpg",
                audioUrl = URL + "audios/en-nau-an-cho-em-ft-pialinh-mv/en-nau-an-cho-em-ft-pialinh-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Ta Cứ Đi Cùng Nhau",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-ta-cu-i-cung-nhau-ft-linh-cao-prod-by-i-teu-mv/en-ta-cu-i-cung-nhau-ft-linh-cao-prod-by-i-teu-mv.jpg",
                audioUrl = URL + "audios/en-ta-cu-i-cung-nhau-ft-linh-cao-prod-by-i-teu-mv/en-ta-cu-i-cung-nhau-ft-linh-cao-prod-by-i-teu-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Đưa Nhau Đi Trốn",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-ua-nhau-i-tron-ft-linh-cao-new-version-mv/en-ua-nhau-i-tron-ft-linh-cao-new-version-mv.jpg",
                audioUrl = URL + "audios/en-ua-nhau-i-tron-ft-linh-cao-new-version-mv/en-ua-nhau-i-tron-ft-linh-cao-new-version-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
        repo.add(
            Song(
                title = "Vị Nhà",
                artistId = "rgofDUwpJb65lujKfKFx",
                artistName = "Đen Vâu",
                coverUrl = URL + "audios/en-vi-nha-mv/en-vi-nha-mv.jpg",
                audioUrl = URL + "audios/en-vi-nha-mv/en-vi-nha-mv.mp3",
                duration = 0,
                genres = listOf("rap")
            )
        )
    }

    fun addGenres() {
        val repo = GenresRepository()
        repo.add(
            Genre(
                name = "Pop",
                imageURL = URL + "genres/pop.jpg",
                description = "pop"
            )
        )
        repo.add(
            Genre(
                name = "V-Pop",
                imageURL = URL + "genres/vpop.jpg",
                description = "v-pop"
            )
        )
        repo.add(
            Genre(
                name = "EDM",
                imageURL = URL + "genres/edm.jpg",
                description = "edm"
            )
        )
        repo.add(
            Genre(
                name = "Rap",
                imageURL = URL + "genres/rap.jpg",
                description = "rap"
            )
        )
        repo.add(
            Genre(
                name = "Rock",
                imageURL = URL + "genres/rock.jpg",
                description = "rock"
            )
        )
        repo.add(
            Genre(
                name = "Bolero",
                imageURL = URL + "genres/bolero.jpg",
                description = "bolero"
            )
        )
        repo.add(
            Genre(
                name = "Ballad",
                imageURL = URL + "genres/ballad.jpg",
                description = "ballad"
            )
        )
    }

}
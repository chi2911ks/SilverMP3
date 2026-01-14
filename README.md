# 🎵 SilverMP3 - Android Music Streaming Application

**SilverMP3** là một ứng dụng nghe nhạc trực tuyến hiện đại dành cho nền tảng Android. Ứng dụng được xây dựng với kiến trúc MVVM chuẩn, giao diện bắt mắt và tích hợp các công nghệ mới nhất từ Google và cộng đồng mã nguồn mở.

---

## 📺 Video Hướng Dẫn / Demo
> [Xem video hướng dẫn tại đây](link_video_cua_ban)

---

## ✨ Tính Năng Nổi Bật

- **Phát nhạc chất lượng cao:** Sử dụng bộ thư viện **Media3 ExoPlayer** mới nhất để tối ưu hóa việc phát nhạc và xử lý luồng dữ liệu.
- **Chạy nền & Thông báo:** Tích hợp **MediaSessionService** cho phép điều khiển nhạc qua thanh thông báo và tiếp tục phát khi ứng dụng ở chế độ nền.
- **Giao diện thích ứng (Dynamic UI):** Tự động trích xuất màu sắc từ ảnh bìa (Artwork) bài hát bằng **Palette API** để tạo hiệu ứng nền Gradient đẹp mắt.
- **Xác thực đa phương thức:** Hỗ trợ đăng nhập qua **Email**, **Số điện thoại (OTP)** và **Google Auth** thông qua Firebase.
- **Quản lý Playlist:** Người dùng có thể tạo, chỉnh sửa và thêm bài hát vào danh sách phát cá nhân.
- **Yêu thích & Thư viện:** Lưu trữ các bài hát yêu thích đồng bộ theo thời gian thực với Cloud Firestore.
- **Kiến trúc hiện đại:** Sử dụng **Koin** để quản lý Dependency Injection, giúp mã nguồn sạch và dễ bảo trì.

---

## 🛠 Công Nghệ Sử Dụng

- **Ngôn ngữ:** Kotlin.
- **UI Framework:** XML (ViewBinding)
- **Media:** AndroidX Media3 (ExoPlayer, Session, UI).
- **Backend:** Firebase Authentication, Cloud Firestore.
- **Dependency Injection:** Koin (Android, Coroutines).
- **Image Loading:** Glide (phiên bản 5.0.5).
- **Khác:** Coroutines, Flow, Palette, Material Design 3.

---

## 🚀 Hướng Dẫn Cài Đặt

### 1. Cấu hình Firebase
- Tạo một dự án mới trên [Firebase Console](https://console.firebase.google.com/).
- Thêm ứng dụng Android với Package Name: `com.cbtool.silvermp3`.
- Tải tệp `google-services.json` và đặt vào thư mục `app/` của dự án.
- Kích hoạt **Authentication** (Email, Google, Phone) và **Cloud Firestore**.

### 2. Yêu cầu hệ thống
- Android Studio Ladybug hoặc phiên bản mới hơn.
- Android SDK tối thiểu: **API 24 (Android 7.0)**.
- Android SDK biên dịch: **API 36**.

### 3. Build & Run
- Mở project trong Android Studio.
- Đợi quá trình Gradle Sync hoàn tất.
- Nhấn nút **Run** để chạy trên máy ảo hoặc thiết bị thật.

---

## 📂 Cấu Trúc Thư Mục Chính

- `com.cbtool.silvermp3.service`: Chứa `PlayBackService` xử lý logic phát nhạc chính.
- `com.cbtool.silvermp3.ui`: Chứa các màn hình (Home, Player, Library, Search) và các ViewModel tương ứng.
- `com.cbtool.silvermp3.data`: Quản lý các Model (Song, Artist, Playlist) và Repository tương tác với Firestore.
- `com.cbtool.silvermp3.di`: Cấu hình các module Koin cho dự án.
- `com.cbtool.silvermp3.utils`: Các hàm tiện ích mở rộng cho ứng dụng.

---

## 📄 Giấy phép

Dự án này được phát triển phục vụ mục đích học tập.

---

**SilverMP3** - *Trải nghiệm âm nhạc không giới hạn!* 🎧
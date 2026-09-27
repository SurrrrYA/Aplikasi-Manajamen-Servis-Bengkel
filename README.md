# Sempoelur Garage

Aplikasi manajemen bengkel motor berbasis Android yang dikembangkan dengan backend PHP/MySQL untuk mendukung operasional bengkel secara digital — mulai dari pendaftaran servis, monitoring progres, hingga laporan keuangan.

## Fitur Utama

- **Manajemen Role** — 4 peran pengguna: Owner, Kasir, Mekanik, dan Customer, masing-masing dengan akses dan tampilan berbeda
- **Autentikasi** — Login/registrasi/reset password menggunakan Firebase Authentication, tersinkron dengan database MySQL
- **Servis Motor** — Dua tipe servis: Servis Biasa dan Servis Monitoring
- **Monitoring Progres** — Update progres servis dengan foto & video, riwayat lengkap, dan detail item yang digunakan
- **Manajemen Barang** — Pencatatan harga beli & harga jual untuk perhitungan laba
- **Absensi Mekanik** — Sistem absensi untuk perhitungan gaji harian
- **Laporan & Statistik** — Dashboard owner dengan statistik servis harian dan status servis yang belum selesai
- **Cetak Struk** — Cetak struk pembayaran via printer thermal Bluetooth
- **Mode Offline** — Cache data lokal agar aplikasi tetap bisa diakses saat koneksi terbatas
- **Notifikasi Email** — Email otomatis saat pelanggan mendaftar

## Teknologi

**Frontend (Android)**
- Kotlin
- Material Design (tema ungu-putih)
- Volley (networking)
- Firebase Authentication
- RecyclerView, BottomSheetDialogFragment

**Backend**
- PHP
- MySQL
- Bootstrap 5 (panel admin web)


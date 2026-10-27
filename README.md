# LinkDownloader - Internet File Downloader (Java Swing)

Aplikasi pengunduh file berbasis Java Swing dengan arsitektur modular (MVC) yang dirancang untuk kolaborasi tim 4 orang pada mata kuliah Pemrograman Jaringan (Prognet).

---

## 📁 Struktur Proyek & Pembagian Tugas Tim

Proyek ini telah dipisahkan menjadi beberapa modul independen agar terhindar dari *merge conflict* dan memiliki pemisahan tanggung jawab (*Separation of Concerns*) yang jelas:

```text
DOWNLOADGUI/
├── Main.java                         # Titik masuk utama aplikasi (Entry Point)
│
├── view/                             # 👤 ANGGOTA 1 (Frontend / UI Designer)
│   ├── DownloadFrame.java            # Jendela utama GUI, layout form, progress bar & tombol
│   └── UIStyle.java                  # Palet warna, tipografi font, dan styling komponen
│
├── service/                          # 👤 ANGGOTA 2 (Network & Core Downloader)
│   └── DownloadWorker.java           # Background SwingWorker, koneksi HTTP/HTTPS & streaming buffer
│
├── controller/                       # 👤 ANGGOTA 3 (Controller & Application Logic)
│   └── DownloadController.java       # Penghubung GUI & Worker, event listener, dan validasi form
│
├── model/                            # 👤 ANGGOTA 4 (Data Model & Utilities)
│   └── DownloadResult.java           # Model objek hasil unduhan (file & ukuran byte)
│
├── util/                             # 👤 ANGGOTA 4 (Data Model & Utilities)
│   ├── FormatUtils.java              # Format byte (KB/MB/GB), kecepatan, dan estimasi waktu (ETA)
│   └── FileUtils.java                # Ekstraksi nama file URL & manajemen folder penyimpanan
│
└── backup_lama/                      # Salinan kode asli sebelum pemisahan modul
```

---

## 👥 Rincian Tanggung Jawab Anggota Kelompok

| No | Peran / Anggota | File yang Dikerjakan | Fokus & Kontribusi |
|:--:|:---|:---|:---|
| **1** | **UI / Frontend Designer** | [DownloadFrame.java](file:///d:/SEMESTER%207/Prognet/DOWNLOADGUI/view/DownloadFrame.java), [UIStyle.java](file:///d:/SEMESTER%207/Prognet/DOWNLOADGUI/view/UIStyle.java) | Mendesain tampilan antarmuka modern (Swing), menyusun tata letak *Header*, *Input URL*, tombol aksi, progress bar, serta mengaktifkan tombol Batal. |
| **2** | **Network & Core Engineer** | [DownloadWorker.java](file:///d:/SEMESTER%207/Prognet/DOWNLOADGUI/service/DownloadWorker.java) | Menangani koneksi HTTP/HTTPS, header `User-Agent`, penanganan status code (403, 404, 500), pembuatan file `.part`, dan fungsi *cancel* download. |
| **3** | **Controller & Logic Coordinator** | [DownloadController.java](file:///d:/SEMESTER%207/Prognet/DOWNLOADGUI/controller/DownloadController.java), [Main.java](file:///d:/SEMESTER%207/Prognet/DOWNLOADGUI/Main.java) | Menangani event klik tombol, validasi protokol URL, dialog konfirmasi *overwrite* file, serta memperbarui progres UI dari event worker. |
| **4** | **Utilities & Data Specialist** | [FormatUtils.java](file:///d:/SEMESTER%207/Prognet/DOWNLOADGUI/util/FormatUtils.java), [FileUtils.java](file:///d:/SEMESTER%207/Prognet/DOWNLOADGUI/util/FileUtils.java), [DownloadResult.java](file:///d:/SEMESTER%207/Prognet/DOWNLOADGUI/model/DownloadResult.java) | Membuat utilitas konversi satuan byte (B/KB/MB/GB), kalkulator sisa waktu (ETA), ekstraksi nama file dari URL, dan model representasi unduhan. |

---

## 🚀 Cara Menjalankan Aplikasi

### Menggunakan Command Line:
```bash
# 1. Kompilasi semua file:
javac Main.java

# 2. Jalankan aplikasi:
java Main
```

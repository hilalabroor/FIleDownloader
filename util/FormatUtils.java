package util;

/**
 * Utilitas untuk memformat byte, kecepatan download, dan sisa waktu (ETA).
 * Dikerjakan oleh Anggota 4 (Model & Utilities).
 */
public class FormatUtils {

    /**
     * Mengubah satuan byte menjadi representasi B, KB, MB, atau GB.
     */
    public static String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        }
        if (bytes < 1024L * 1024L * 1024L) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        }
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * Memformat kecepatan unduh per detik.
     */
    public static String formatSpeed(double bytesPerSecond) {
        return formatBytes((long) bytesPerSecond) + "/detik";
    }

    /**
     * Mengubah durasi detik menjadi format waktu jam/menit/detik yang mudah dibaca.
     */
    public static String formatTime(long seconds) {
        if (seconds < 60) {
            return seconds + " detik";
        }
        if (seconds < 3600) {
            return (seconds / 60) + " menit " + (seconds % 60) + " detik";
        }
        return (seconds / 3600) + " jam " + ((seconds % 3600) / 60) + " menit";
    }
}

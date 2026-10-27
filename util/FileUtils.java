package util;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Utilitas untuk manipulasi file, path, dan ekstraksi nama file dari URL.
 * Dikerjakan oleh Anggota 4 (Model & Utilities).
 */
public class FileUtils {

    /**
     * Mengekstrak nama file dari URL yang diberikan.
     */
    public static String getFileNameFromURL(String urlText) {
        try {
            URL url = new URL(urlText);
            String path = url.getPath();
            if (path == null || path.isEmpty()) {
                return "downloaded_file";
            }

            int lastSlash = path.lastIndexOf('/');
            String name = (lastSlash >= 0) ? path.substring(lastSlash + 1) : path;

            if (name.isEmpty()) {
                return "downloaded_file";
            }

            return URLDecoder.decode(name, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "downloaded_file";
        }
    }

    /**
     * Mendapatkan lokasi folder Downloads default dari user.
     */
    public static File getDefaultDownloadFolder() {
        File downloads = new File(System.getProperty("user.home"), "Downloads");
        if (downloads.exists()) {
            return downloads;
        }
        return new File(System.getProperty("user.home"));
    }

    /**
     * Memastikan folder tujuan ada, jika belum ada maka dibuat.
     */
    public static boolean ensureFolderExists(File folder) {
        if (folder == null) return false;
        if (folder.exists()) return true;
        return folder.mkdirs();
    }
}

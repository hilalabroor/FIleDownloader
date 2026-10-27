package util;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FileUtils {

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

    public static File getDefaultDownloadFolder() {
        File downloads = new File(System.getProperty("user.home"), "Downloads");
        if (downloads.exists()) {
            return downloads;
        }
        return new File(System.getProperty("user.home"));
    }

    public static boolean ensureFolderExists(File folder) {
        if (folder == null)
            return false;
        if (folder.exists())
            return true;
        return folder.mkdirs();
    }
}

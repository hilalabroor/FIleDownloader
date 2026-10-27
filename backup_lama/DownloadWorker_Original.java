import javax.swing.SwingWorker;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CancellationException;

public class DownloadWorker extends SwingWorker<DownloadWorker.Result, Void> {
    public static class Result {
        public final FileHolder holder;
        public final java.io.File file;
        public final long bytes;

        Result(Path path, long bytes) {
            this.file = path.toFile();
            this.holder = new FileHolder(path);
            this.bytes = bytes;
        }
    }

    public static class FileHolder {
        public final Path path;

        FileHolder(Path path) {
            this.path = path;
        }
    }

    private final String urlText;
    private final Path destination;
    private volatile boolean cancelRequested = false;
    private volatile HttpURLConnection connection;
    private volatile InputStream activeInput;

    public DownloadWorker(String urlText, Path destination) {
        this.urlText = urlText;
        this.destination = destination;
    }

    public void cancelDownload() {
        cancelRequested = true;
        try {
            if (activeInput != null)
                activeInput.close();
        } catch (IOException ignored) {
        }
        if (connection != null)
            connection.disconnect();
        cancel(true);
    }

    @Override
    protected Result doInBackground() throws Exception {
        Path parent = destination.toAbsolutePath().getParent();
        Path tempFile = Files.createTempFile(parent, "linkdown-", ".part");
        boolean success = false;
        long totalBytes = 0;
        long lastReportTime = System.currentTimeMillis();
        long lastReportBytes = 0;
        long startTime = System.currentTimeMillis();

        try {
            URL url = new URL(urlText);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "LinkDown/1.0 (Java Swing Downloader)");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                String explanation;
                if (responseCode == 403)
                    explanation = "Server menolak akses (HTTP 403). Gunakan tautan file langsung atau periksa izin akses.";
                else if (responseCode == 404)
                    explanation = "File tidak ditemukan (HTTP 404). Periksa kembali URL.";
                else if (responseCode >= 500)
                    explanation = "Server mengalami masalah (HTTP " + responseCode + "). Coba lagi nanti.";
                else
                    explanation = "Server mengembalikan HTTP " + responseCode + ".";
                throw new IOException(explanation);
            }

            long contentLength = connection.getContentLengthLong();
            try (InputStream input = new BufferedInputStream(connection.getInputStream());
                    OutputStream output = new BufferedOutputStream(Files.newOutputStream(tempFile))) {
                activeInput = input;
                byte[] buffer = new byte[8192];
                int count;

                while ((count = input.read(buffer)) != -1) {
                    if (cancelRequested || isCancelled())
                        throw new CancellationException("Unduhan dibatalkan.");
                    output.write(buffer, 0, count);
                    totalBytes += count;

                    if (contentLength > 0) {
                        int percent = (int) Math.min(99, totalBytes * 100 / contentLength);
                        setProgress(percent);
                    }

                    long now = System.currentTimeMillis();
                    if (now - lastReportTime >= 700) {
                        double seconds = Math.max(0.001, (now - lastReportTime) / 1000.0);
                        double speed = (totalBytes - lastReportBytes) / seconds;
                        String speedText = formatSpeed(speed);
                        String etaText = "";
                        if (contentLength > 0 && speed > 0) {
                            long remainingSeconds = (long) ((contentLength - totalBytes) / speed);
                            etaText = " | Sisa waktu: " + formatTime(remainingSeconds);
                        }
                        firePropertyChange("speedText", null, speedText + etaText);
                        String status = contentLength > 0
                                ? "Mengunduh " + formatBytes(totalBytes) + " dari " + formatBytes(contentLength)
                                : "Mengunduh " + formatBytes(totalBytes);
                        firePropertyChange("statusText", null, status);
                        lastReportTime = now;
                        lastReportBytes = totalBytes;
                    }
                }
            } finally {
                activeInput = null;
            }

            if (cancelRequested || isCancelled())
                throw new CancellationException("Unduhan dibatalkan.");
            Files.move(tempFile, destination, StandardCopyOption.REPLACE_EXISTING);
            success = true;
            return new Result(destination, totalBytes);
        } finally {
            activeInput = null;
            if (connection != null)
                connection.disconnect();
            if (!success)
                Files.deleteIfExists(tempFile);
        }
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        if (bytes < 1024 * 1024)
            return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024L * 1024L * 1024L)
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private String formatSpeed(double bytesPerSecond) {
        return formatBytes((long) bytesPerSecond) + "/detik";
    }

    private String formatTime(long seconds) {
        if (seconds < 60)
            return seconds + " detik";
        if (seconds < 3600)
            return (seconds / 60) + " menit " + (seconds % 60) + " detik";
        return (seconds / 3600) + " jam " + ((seconds % 3600) / 60) + " menit";
    }
}

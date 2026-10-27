package model;

import java.io.File;
import java.nio.file.Path;

public class DownloadResult {
    private final File file;
    private final long bytes;

    public DownloadResult(Path path, long bytes) {
        this.file = path.toFile();
        this.bytes = bytes;
    }

    public DownloadResult(File file, long bytes) {
        this.file = file;
        this.bytes = bytes;
    }

    public File getFile() {
        return file;
    }

    public long getBytes() {
        return bytes;
    }
}

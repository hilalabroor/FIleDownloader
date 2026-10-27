package controller;

import model.DownloadResult;
import service.DownloadWorker;
import util.FileUtils;
import util.FormatUtils;
import view.DownloadFrame;

import javax.swing.*;
import java.io.File;
import java.net.URL;

public class DownloadController {
    private final DownloadFrame frame;
    private DownloadWorker worker;
    private File downloadedFile;

    public DownloadController(DownloadFrame frame) {
        this.frame = frame;
        initController();
    }

    private void initController() {
        // Set folder default
        File defaultFolder = FileUtils.getDefaultDownloadFolder();
        frame.getTxtLocation().setText(new File(defaultFolder, "downloaded_file").getAbsolutePath());

        // Pasang Event Listeners
        frame.getBtnBrowse().addActionListener(e -> chooseSaveLocation());
        frame.getBtnDownload().addActionListener(e -> startDownload());
        frame.getBtnCancel().addActionListener(e -> cancelDownload());
    }

    private void chooseSaveLocation() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pilih lokasi dan nama file penyimpanan");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String current = frame.getTxtLocation().getText().trim();
        if (!current.isEmpty()) {
            chooser.setSelectedFile(new File(current));
        }

        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            frame.getTxtLocation().setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void startDownload() {
        String urlText = frame.getTxtURL().getText().trim();
        String destinationText = frame.getTxtLocation().getText().trim();

        if (urlText.isEmpty()) {
            frame.showError("Masukkan URL file terlebih dahulu.");
            frame.getTxtURL().requestFocus();
            return;
        }
        if (destinationText.isEmpty()) {
            frame.showError("Pilih lokasi penyimpanan file.");
            return;
        }

        try {
            URL url = new URL(urlText);
            String protocol = url.getProtocol();
            if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https")) {
                frame.showError("URL harus menggunakan protokol HTTP atau HTTPS.");
                return;
            }
        } catch (Exception ex) {
            frame.showError("Format URL tidak valid. Periksa kembali URL yang dimasukkan.");
            return;
        }

        File selectedFile = new File(destinationText);

        // Jika nama file masih default "downloaded_file", ambil nama dari URL
        if (selectedFile.getName().equals("downloaded_file")) {
            String fileName = FileUtils.getFileNameFromURL(urlText);
            if (fileName.equals("downloaded_file")) {
                frame.showError(
                        "Nama file tidak dapat dideteksi dari URL. Silakan tentukan nama file manual pada kolom lokasi.");
                return;
            }

            File parentFolder = selectedFile.getAbsoluteFile().getParentFile();
            selectedFile = new File(parentFolder, fileName);
            frame.getTxtLocation().setText(selectedFile.getAbsolutePath());
        }

        File destination = selectedFile;
        File parent = destination.getAbsoluteFile().getParentFile();
        if (parent == null || !FileUtils.ensureFolderExists(parent)) {
            frame.showError("Folder tujuan tidak dapat dibuat. Silakan pilih lokasi lain.");
            return;
        }

        if (destination.exists()) {
            boolean overwrite = frame.showConfirm(
                    "File sudah ada di komputer Anda. Apakah ingin menggantinya?",
                    "Konfirmasi Penggantian File");
            if (!overwrite) {
                return;
            }
        }

        // UI untuk unduh
        frame.getProgressBar().setValue(0);
        frame.getProgressBar().setString("0%");
        frame.getLblStatus().setText("Menghubungkan ke server...");
        frame.getLblSpeed().setText("");
        downloadedFile = null;
        frame.setBusyState(true);

        // Jalankan background worker
        worker = new DownloadWorker(urlText, destination.toPath());
        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int progress = (Integer) evt.getNewValue();
                frame.getProgressBar().setValue(progress);
                frame.getProgressBar().setString(progress + "%");
            } else if ("statusText".equals(evt.getPropertyName())) {
                frame.getLblStatus().setText(String.valueOf(evt.getNewValue()));
            } else if ("speedText".equals(evt.getPropertyName())) {
                frame.getLblSpeed().setText(String.valueOf(evt.getNewValue()));
            } else if ("state".equals(evt.getPropertyName())
                    && evt.getNewValue() == SwingWorker.StateValue.DONE) {
                frame.setBusyState(false);
                try {
                    if (worker != null && worker.isCancelled()) {
                        frame.getLblStatus().setText("Unduhan dibatalkan.");
                        frame.getLblSpeed().setText("");
                    } else if (worker != null) {
                        DownloadResult result = worker.get();
                        downloadedFile = result.getFile();
                        frame.getProgressBar().setValue(100);
                        frame.getProgressBar().setString("100%");
                        frame.getLblStatus().setText("Download berhasil: " + result.getFile().getName());
                        frame.getLblSpeed().setText("Total ukuran: " + FormatUtils.formatBytes(result.getBytes()));
                    }
                } catch (Exception ex) {
                    frame.getLblStatus().setText("Download gagal.");
                    String message = ex.getCause() != null && ex.getCause().getMessage() != null
                            ? ex.getCause().getMessage()
                            : ex.getMessage();
                    frame.showError(message == null ? "Terjadi kesalahan saat mengunduh file." : message);
                } finally {
                    worker = null;
                }
            }
        });

        worker.execute();
    }

    private void cancelDownload() {
        if (worker != null && !worker.isDone()) {
            worker.cancelDownload();
            frame.getLblStatus().setText("Membatalkan unduhan...");
            frame.getBtnCancel().setEnabled(false);
        }
    }

    public File getDownloadedFile() {
        return downloadedFile;
    }
}

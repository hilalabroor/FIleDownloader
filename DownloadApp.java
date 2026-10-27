import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DownloadApp extends JFrame {
    private final JTextField txtURL = new JTextField();
    private final JTextField txtLocation = new JTextField();
    private final JButton btnBrowse = new JButton("Pilih Lokasi");
    private final JButton btnDownload = new JButton("Mulai Download");
    // private final JButton btnCancel = new JButton("Batal");
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel lblStatus = new JLabel("Siap mengunduh file.");
    private final JLabel lblSpeed = new JLabel(" ");
    private DownloadWorker worker;
    private File downloadedFile;

    private final Color navy = new Color(25, 35, 60);
    private final Color blue = new Color(45, 105, 220);
    private final Color red = new Color(190, 55, 65);
    private final Color background = new Color(241, 245, 251);

    public DownloadApp() {
        setTitle("LinkDown - Internet File Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setMinimumSize(new Dimension(700, 600));
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 22));
        root.setBackground(background);
        root.setBorder(new EmptyBorder(30, 34, 28, 34));
        setContentPane(root);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(navy);
        header.setBorder(new EmptyBorder(25, 27, 25, 27));

        JLabel title = new JLabel("LinkDownloader");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Download files from the internet");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(215, 225, 245));

        header.add(title);
        header.add(Box.createVerticalStrut(10));
        header.add(subtitle);
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 25, 20, 25));

        addFieldLabel(content, "URL File");
        styleTextField(txtURL);
        txtURL.setToolTipText("Masukkan URL file yang ingin diunduh");
        content.add(txtURL);
        content.add(Box.createVerticalStrut(16));

        addFieldLabel(content, "Lokasi dan Nama File");
        styleTextField(txtLocation);
        txtLocation.setEditable(false);
        content.add(txtLocation);
        content.add(Box.createVerticalStrut(10));

        JPanel locationRow = new JPanel(new GridLayout(1, 2, 8, 0));
        locationRow.setBackground(Color.WHITE);
        styleButton(btnBrowse, new Color(230, 235, 245), navy);
        locationRow.add(btnBrowse);
        locationRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        locationRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(locationRow);
        content.add(Box.createVerticalStrut(12));

        JPanel actionRow = new JPanel(new GridLayout(1, 2, 8, 0));
        actionRow.setBackground(Color.WHITE);
        styleButton(btnDownload, blue, Color.WHITE);
        // styleButton(btnCancel, red, Color.WHITE);
        actionRow.add(btnDownload);
        // actionRow.add(btnCancel);
        actionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(actionRow);
        content.add(Box.createVerticalStrut(20));

        addFieldLabel(content, "Progres Unduhan");
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        progressBar.setValue(0);
        progressBar.setForeground(blue);
        progressBar.setBackground(new Color(225, 229, 235));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(100, 24));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(progressBar);
        content.add(Box.createVerticalStrut(10));

        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblStatus.setForeground(navy);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblStatus);
        content.add(Box.createVerticalStrut(5));

        lblSpeed.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSpeed.setForeground(new Color(90, 100, 120));
        lblSpeed.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblSpeed);
        content.add(Box.createVerticalStrut(12));

        root.add(content, BorderLayout.CENTER);

        // Default folder: Downloads
        File downloads = new File(System.getProperty("user.home"), "Downloads");
        if (!downloads.exists())
            downloads = new File(System.getProperty("user.home"));
        txtLocation.setText(new File(downloads, "downloaded_file").getAbsolutePath());

        btnBrowse.addActionListener(e -> chooseSaveLocation());
        btnDownload.addActionListener(e -> startDownload());
        // btnCancel.addActionListener(e -> cancelDownload());

        setIdleControls();
    }

    private void addFieldLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(navy);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(195, 202, 215)),
                new EmptyBorder(7, 9, 7, 9)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setText(button.getText()); // Pastikan label tombol tetap ada
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(fg);
        button.setBackground(bg);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 14, 10, 14));
    }

    private void chooseSaveLocation() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pilih lokasi dan nama file");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String current = txtLocation.getText().trim();
        if (!current.isEmpty())
            chooser.setSelectedFile(new File(current));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtLocation.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void startDownload() {
        String urlText = txtURL.getText().trim();
        String destinationText = txtLocation.getText().trim();

        if (urlText.isEmpty()) {
            showError("Masukkan URL file terlebih dahulu.");
            txtURL.requestFocus();
            return;
        }
        if (destinationText.isEmpty()) {
            showError("Pilih lokasi penyimpanan file.");
            return;
        }

        try {
            URL url = new URL(urlText);
            String protocol = url.getProtocol();
            if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https")) {
                showError("URL harus menggunakan HTTP atau HTTPS.");
                return;
            }
        } catch (Exception ex) {
            showError("Format URL tidak valid. Periksa kembali URL yang dimasukkan.");
            return;
        }

        File selectedFile = new File(destinationText);

        // Jika nama file masih nama default, gunakan nama dari URL.
        if (selectedFile.getName().equals("downloaded_file")) {
            String fileName = getFileNameFromURL(urlText);

            if (fileName.equals("downloaded_file")) {
                showError(
                        "Nama file tidak dapat diketahui dari URL. " +
                                "Silakan tentukan nama file beserta ekstensinya secara manual.");
                return;
            }

            File parentFolder = selectedFile.getAbsoluteFile().getParentFile();
            selectedFile = new File(parentFolder, fileName);
            txtLocation.setText(selectedFile.getAbsolutePath());
        }

        File destination = selectedFile;
        File parent = destination.getAbsoluteFile().getParentFile();
        if (parent == null || (!parent.exists() && !parent.mkdirs())) {
            showError("Folder tujuan tidak dapat dibuat. Pilih lokasi lain.");
            return;
        }

        if (destination.exists()) {
            int answer = JOptionPane.showConfirmDialog(this,
                    "File sudah ada. Apakah ingin menggantinya?",
                    "Konfirmasi Penggantian File", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION)
                return;
        }

        progressBar.setValue(0);
        progressBar.setString("0%");
        lblStatus.setText("Menghubungkan ke server...");
        lblSpeed.setText("");
        downloadedFile = null;
        setBusyControls();

        worker = new DownloadWorker(urlText, destination.toPath());
        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
                progressBar.setString(progress + "%");
            } else if ("statusText".equals(evt.getPropertyName())) {
                lblStatus.setText(String.valueOf(evt.getNewValue()));
            } else if ("speedText".equals(evt.getPropertyName())) {
                lblSpeed.setText(String.valueOf(evt.getNewValue()));
            } else if ("state".equals(evt.getPropertyName())
                    && evt.getNewValue() == SwingWorker.StateValue.DONE) {
                setIdleControls();
                try {
                    if (worker != null && worker.isCancelled()) {
                        lblStatus.setText("Unduhan dibatalkan.");
                    } else if (worker != null) {
                        DownloadWorker.Result result = worker.get();
                        downloadedFile = result.file;
                        progressBar.setValue(100);
                        progressBar.setString("100%");
                        lblStatus.setText("Download berhasil: " + result.file.getName());
                        lblSpeed.setText("Ukuran file: " + formatBytes(result.bytes));
                    }
                } catch (Exception ex) {
                    lblStatus.setText("Download gagal.");
                    String message = ex.getCause() != null && ex.getCause().getMessage() != null
                            ? ex.getCause().getMessage()
                            : ex.getMessage();
                    showError(message == null ? "Terjadi kesalahan saat mengunduh file." : message);
                } finally {
                    worker = null;
                }
            }
        });
        worker.execute();
    }

    // private void cancelDownload() {
    // if (worker != null && !worker.isDone()) {
    // worker.cancelDownload();
    // lblStatus.setText("Membatalkan unduhan...");
    // // btnCancel.setEnabled(false);
    // }
    // }

    private void setBusyControls() {
        txtURL.setEnabled(false);
        btnBrowse.setEnabled(false);
        btnDownload.setEnabled(false);
        // btnCancel.setEnabled(true);
    }

    private void setIdleControls() {
        txtURL.setEnabled(true);
        btnBrowse.setEnabled(true);
        btnDownload.setEnabled(true);
        // btnCancel.setEnabled(false);
    }

    // private void openDownloadedFile() {
    // if (downloadedFile == null || !downloadedFile.exists()) {
    // showError("Belum ada file hasil download yang tersedia untuk dibuka.");
    // return;
    // }
    // try {
    // if (!Desktop.isDesktopSupported())
    // throw new Exception("Fitur membuka file tidak didukung di komputer ini.");
    // Desktop.getDesktop().open(downloadedFile);
    // } catch (Exception ex) {
    // showError("File tidak dapat dibuka: " + ex.getMessage());
    // }
    // }

    // private void openSaveFolder() {
    // File file = new File(txtLocation.getText().trim()).getAbsoluteFile();
    // File folder = file.isDirectory() ? file : file.getParentFile();
    // if (folder == null || !folder.exists()) {
    // showError("Folder penyimpanan belum tersedia.");
    // return;
    // }
    // try {
    // if (!Desktop.isDesktopSupported())
    // throw new Exception("Fitur membuka folder tidak didukung.");
    // Desktop.getDesktop().open(folder);
    // } catch (Exception ex) {
    // showError("Folder tidak dapat dibuka: " + ex.getMessage());
    // }
    // }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "LinkDown - Pesan", JOptionPane.ERROR_MESSAGE);
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

    private String getFileNameFromURL(String urlText) {
        try {
            URL url = new URL(urlText);
            String path = url.getPath();

            // Ambil nama file dari bagian akhir URL
            String name = path.substring(path.lastIndexOf('/') + 1);

            if (name.isEmpty()) {
                return "downloaded_file";
            }

            return java.net.URLDecoder.decode(
                    name,
                    java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "downloaded_file";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
                // Gunakan tampilan default jika Look and Feel tidak tersedia.
            }
            new DownloadApp().setVisible(true);
        });
    }
}

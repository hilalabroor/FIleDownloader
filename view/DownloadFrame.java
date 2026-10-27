package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Tampilan utama (GUI) aplikasi downloader.
 * Dikerjakan oleh Anggota 1 (Frontend / UI Designer).
 */
public class DownloadFrame extends JFrame {
    private final JTextField txtURL = new JTextField();
    private final JTextField txtLocation = new JTextField();
    private final JButton btnBrowse = new JButton("Pilih Lokasi");
    private final JButton btnDownload = new JButton("Mulai Download");
    private final JButton btnCancel = new JButton("Batal");
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel lblStatus = new JLabel("Siap mengunduh file.");
    private final JLabel lblSpeed = new JLabel(" ");

    public DownloadFrame() {
        setTitle("LinkDown - Internet File Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setMinimumSize(new Dimension(700, 600));
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 22));
        root.setBackground(UIStyle.BACKGROUND);
        root.setBorder(new EmptyBorder(30, 34, 28, 34));
        setContentPane(root);

        // Header Panel
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIStyle.NAVY);
        header.setBorder(new EmptyBorder(25, 27, 25, 27));

        JLabel title = new JLabel("LinkDownloader");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Download files from the internet reliably and fast");
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(new Color(215, 225, 245));

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);
        root.add(header, BorderLayout.NORTH);

        // Content Card Panel
        JPanel content = new JPanel();
        content.setBackground(UIStyle.CARD_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(22, 26, 22, 26));

        // URL Input
        content.add(UIStyle.createFieldLabel("URL File:"));
        content.add(Box.createVerticalStrut(6));
        UIStyle.styleTextField(txtURL);
        txtURL.setToolTipText("Masukkan tautan URL file yang ingin diunduh");
        content.add(txtURL);
        content.add(Box.createVerticalStrut(16));

        // Location Input
        content.add(UIStyle.createFieldLabel("Lokasi dan Nama File Tujuan:"));
        content.add(Box.createVerticalStrut(6));
        UIStyle.styleTextField(txtLocation);
        txtLocation.setEditable(false);
        content.add(txtLocation);
        content.add(Box.createVerticalStrut(10));

        // Browse Button Row
        JPanel locationRow = new JPanel(new GridLayout(1, 1, 8, 0));
        locationRow.setBackground(UIStyle.CARD_BG);
        UIStyle.styleButton(btnBrowse, UIStyle.BTN_SECONDARY_BG, UIStyle.NAVY);
        locationRow.add(btnBrowse);
        locationRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        locationRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(locationRow);
        content.add(Box.createVerticalStrut(16));

        // Download & Cancel Buttons Row
        JPanel actionRow = new JPanel(new GridLayout(1, 2, 10, 0));
        actionRow.setBackground(UIStyle.CARD_BG);
        UIStyle.styleButton(btnDownload, UIStyle.BLUE, Color.WHITE);
        UIStyle.styleButton(btnCancel, UIStyle.RED, Color.WHITE);
        btnCancel.setEnabled(false); // Default non-aktif sampai download berjalan
        actionRow.add(btnDownload);
        actionRow.add(btnCancel);
        actionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(actionRow);
        content.add(Box.createVerticalStrut(22));

        // Progress Section
        content.add(UIStyle.createFieldLabel("Progres Unduhan:"));
        content.add(Box.createVerticalStrut(8));
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        progressBar.setValue(0);
        progressBar.setForeground(UIStyle.BLUE);
        progressBar.setBackground(UIStyle.PROGRESS_TRACK);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(100, 24));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(progressBar);
        content.add(Box.createVerticalStrut(10));

        // Status Label
        lblStatus.setFont(UIStyle.FONT_STATUS);
        lblStatus.setForeground(UIStyle.NAVY);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblStatus);
        content.add(Box.createVerticalStrut(6));

        // Speed Label
        lblSpeed.setFont(UIStyle.FONT_SPEED);
        lblSpeed.setForeground(UIStyle.TEXT_MUTED);
        lblSpeed.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblSpeed);

        root.add(content, BorderLayout.CENTER);
    }

    public void setBusyState(boolean busy) {
        txtURL.setEnabled(!busy);
        btnBrowse.setEnabled(!busy);
        btnDownload.setEnabled(!busy);
        btnCancel.setEnabled(busy);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "LinkDown - Pesan", JOptionPane.ERROR_MESSAGE);
    }

    public boolean showConfirm(String message, String title) {
        int choice = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    // Getters untuk diakses oleh Controller
    public JTextField getTxtURL() { return txtURL; }
    public JTextField getTxtLocation() { return txtLocation; }
    public JButton getBtnBrowse() { return btnBrowse; }
    public JButton getBtnDownload() { return btnDownload; }
    public JButton getBtnCancel() { return btnCancel; }
    public JProgressBar getProgressBar() { return progressBar; }
    public JLabel getLblStatus() { return lblStatus; }
    public JLabel getLblSpeed() { return lblSpeed; }
}

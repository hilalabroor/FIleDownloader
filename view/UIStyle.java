package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Pengaturan tema, warna, font, dan helper styling untuk antarmuka.
 * Dikerjakan oleh Anggota 1 (Frontend / UI Designer).
 */
public class UIStyle {
    // Palet Warna
    public static final Color NAVY = new Color(25, 35, 60);
    public static final Color BLUE = new Color(45, 105, 220);
    public static final Color RED = new Color(190, 55, 65);
    public static final Color BACKGROUND = new Color(241, 245, 251);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT_MUTED = new Color(90, 100, 120);
    public static final Color BORDER_COLOR = new Color(195, 202, 215);
    public static final Color PROGRESS_TRACK = new Color(225, 229, 235);
    public static final Color BTN_SECONDARY_BG = new Color(230, 235, 245);

    // Font
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 30);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_STATUS = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SPEED = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    /**
     * Menerapkan gaya visual pada JButton.
     */
    public static void styleButton(JButton button, Color bg, Color fg) {
        button.setFont(FONT_BUTTON);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 14, 10, 14));
    }

    /**
     * Menerapkan gaya visual pada JTextField.
     */
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_INPUT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(7, 9, 7, 9)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Membuat label judul field form.
     */
    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(NAVY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}

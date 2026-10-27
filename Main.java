import controller.DownloadController;
import view.DownloadFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            DownloadFrame frame = new DownloadFrame();
            new DownloadController(frame);
            frame.setVisible(true);
        });
    }
}

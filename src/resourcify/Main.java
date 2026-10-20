package resourcify;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        UITheme.applySystemTweaks();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default Swing Look & Feel if system LAF is unavailable
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

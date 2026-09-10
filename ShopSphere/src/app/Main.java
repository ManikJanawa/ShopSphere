package app;

import javax.swing.SwingUtilities;
import view.Login;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Login();
        });

    }
}
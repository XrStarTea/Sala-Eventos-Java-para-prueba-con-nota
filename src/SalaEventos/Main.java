package SalaEventos;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Instanciar y hacer visible la PrimeraVentana, que será nuestra ventana principal de login/registro.
                PrimeraVentana ventanaPrincipal = new PrimeraVentana();
                ventanaPrincipal.setVisible(true);
            }
        });
    }
}
package SalaEventos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelIniciarSesion extends JPanel {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciar;
    private JButton btnCrearNuevaCuenta;
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private PrimeraVentana ventanaPrincipalPadre;
    private GestorUsuarios gestorUsuarios;

    public PanelIniciarSesion(CardLayout layout, JPanel contenedor, PrimeraVentana padre) {
        this.cardLayout = layout;
        this.panelContenedor = contenedor;
        this.ventanaPrincipalPadre = padre;
        this.gestorUsuarios = new GestorUsuarios();

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 24);
        Font fontLabels = new Font("Segoe UI", Font.PLAIN, 16);
        Font fontCampos = new Font("Segoe UI", Font.PLAIN, 16);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 15);

        Color colorPrimario = new Color(52, 152, 219);
        Color colorTextoClaro = Color.WHITE;
        Color colorBordeCampo = new Color(180, 180, 180);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(200, 2));
        separator.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1; gbc.insets = new Insets(0, 5, 20, 5);
        add(separator, gbc);
        gbc.insets = new Insets(10, 5, 10, 5);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(fontLabels);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        add(lblUsuario, gbc);

        txtUsuario = new JTextField(20);
        txtUsuario.setFont(fontCampos);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeCampo, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtUsuario, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(fontLabels);
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(20);
        txtContrasena.setFont(fontCampos);
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeCampo, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtContrasena, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setOpaque(false);

        btnIniciar = new JButton("Acceder"); 
        btnIniciar.setFont(fontBotones);
        btnIniciar.setBackground(colorPrimario);
        btnIniciar.setForeground(colorTextoClaro);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorderPainted(false);
        btnIniciar.setPreferredSize(new Dimension(140, 38));
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnIniciar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(30, 5, 10, 5);
        add(panelBotones, gbc);

        btnCrearNuevaCuenta = new JButton("Crear Nueva Cuenta");
        btnCrearNuevaCuenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCrearNuevaCuenta.setBackground(new Color(92, 184, 92));
        btnCrearNuevaCuenta.setForeground(colorTextoClaro);
        btnCrearNuevaCuenta.setFocusPainted(false);
        btnCrearNuevaCuenta.setBorderPainted(false);
        btnCrearNuevaCuenta.setPreferredSize(new Dimension(180, 38));
        btnCrearNuevaCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 5; gbc.insets = new Insets(5, 5, 0, 5);
        add(btnCrearNuevaCuenta, gbc);

        // Lógica del botón Iniciar Sesión
        btnIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String usuarioNombre = txtUsuario.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());
                if (usuarioNombre.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(PanelIniciarSesion.this,
                            "Por favor, ingrese usuario y contraseña.",
                            "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Usamos GestorUsuarios para autenticar
                Usuario usuarioAutenticado = gestorUsuarios.autenticarUsuario(usuarioNombre, contrasena);

                if (usuarioAutenticado != null) {
                    JOptionPane.showMessageDialog(PanelIniciarSesion.this,
                            "¡Bienvenido, " + usuarioAutenticado.getNombreUsuario() + "!", "Inicio de Sesión Exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    if ("Cliente".equalsIgnoreCase(usuarioAutenticado.getTipoUsuario())) {
                        ventanaPrincipalPadre.mostrarVentanaCliente(usuarioAutenticado.getId());
                    } else if ("Administrador".equalsIgnoreCase(usuarioAutenticado.getTipoUsuario())) {
                        ventanaPrincipalPadre.mostrarVentanaAdmin(usuarioAutenticado.getId());
                    } else {
                        // En caso de un tipo de usuario inesperado
                        JOptionPane.showMessageDialog(PanelIniciarSesion.this,
                                "Tipo de usuario desconocido: " + usuarioAutenticado.getTipoUsuario(),
                                "Error Interno", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(PanelIniciarSesion.this,
                            "Usuario o contraseña incorrectos. Inténtelo de nuevo.",
                            "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                    txtContrasena.setText(""); // Limpia solo la contraseña
                }
            }
        });
        // Acción para ir al panel de Crear Cuenta
        btnCrearNuevaCuenta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                limpiarCampos();
                cardLayout.show(panelContenedor, "CrearCuenta");
            }
        });
    }
    private void limpiarCampos() {
        txtUsuario.setText("");
        txtContrasena.setText("");
    }
}
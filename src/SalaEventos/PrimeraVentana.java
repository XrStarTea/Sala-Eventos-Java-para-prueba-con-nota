package SalaEventos;

import javax.swing.*;
import java.awt.*;

public class PrimeraVentana extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelContenedor;

    public PrimeraVentana() {
        setTitle("Sistema de Gestión de Eventos - Acceso");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        // icono de la ventana
        try {
            java.net.URL url = getClass().getResource("/img/icono.png");
            if (url != null) {
                setIconImage(new ImageIcon(url).getImage());
            } else {
                System.err.println("Icono de ventana no encontrado en /img/icono.png");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono de la aplicación: " + e.getMessage());
        }
        // Diseño de cabezera
        JPanel panelCabecera = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color colorInicio = new Color(30, 136, 229);
                Color colorFin = new Color(66, 165, 245);
                GradientPaint gp = new GradientPaint(0, 0, colorInicio, getWidth(), getHeight(), colorFin);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelCabecera.setPreferredSize(new Dimension(550, 150));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // Sub-panel para el título y logo
        JPanel panelTituloLogo = new JPanel();
        panelTituloLogo.setLayout(new BoxLayout(panelTituloLogo, BoxLayout.Y_AXIS));
        panelTituloLogo.setOpaque(false);
        // Título de Bienvenida
        JLabel lblTituloBienvenida = new JLabel("Sistema de Gestión de Eventos");
        lblTituloBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloBienvenida.setForeground(Color.WHITE);
        lblTituloBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTituloLogo.add(lblTituloBienvenida);
        // Espacio entre el título y el logo
        panelTituloLogo.add(Box.createVerticalStrut(5)); 
        // Logo
        JLabel lblLogo = new JLabel();
        try {
            java.net.URL logoUrl = getClass().getResource("/img/logo.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH); 
                lblLogo.setIcon(new ImageIcon(scaledImage));
                lblLogo.setPreferredSize(new Dimension(80, 80)); 
                lblLogo.setMinimumSize(new Dimension(80, 80));
                lblLogo.setMaximumSize(new Dimension(80, 80));
            } else {
                System.err.println("Logo no encontrado en /img/logo.png. Usando texto alternativo.");
                lblLogo.setText("LOGO");
                lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
                lblLogo.setForeground(Color.WHITE);
                lblLogo.setPreferredSize(new Dimension(80, 80));
                lblLogo.setMinimumSize(new Dimension(80, 80));
                lblLogo.setMaximumSize(new Dimension(80, 80));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
            lblLogo.setText("LOGO");
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblLogo.setForeground(Color.WHITE);
            lblLogo.setPreferredSize(new Dimension(80, 80));
            lblLogo.setMinimumSize(new Dimension(80, 80));
            lblLogo.setMaximumSize(new Dimension(80, 80));
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTituloLogo.add(lblLogo);
        panelTituloLogo.add(Box.createVerticalGlue());
        panelCabecera.add(panelTituloLogo, BorderLayout.CENTER); 
        add(panelCabecera, BorderLayout.NORTH); // La cabecera en la parte superior

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        PanelIniciarSesion panelIniciarSesion = new PanelIniciarSesion(cardLayout, panelContenedor, this);
        PanelCrearCuenta panelCrearCuenta = new PanelCrearCuenta(cardLayout, panelContenedor);
        
        panelContenedor.add(panelIniciarSesion, "IniciarSesion");
        panelContenedor.add(panelCrearCuenta, "CrearCuenta"); 
        cardLayout.show(panelContenedor, "IniciarSesion");
        
        setLayout(new BorderLayout());
        add(panelCabecera, BorderLayout.NORTH); 
        add(panelContenedor, BorderLayout.CENTER); 
    }
    public void mostrarVentanaCliente(int idCliente) {
        VentanaClienteGUI ClienteGUI = new VentanaClienteGUI(idCliente);
        VentanaClienteFUN ClienteController = new VentanaClienteFUN(ClienteGUI, idCliente, this); 
        ClienteGUI.mostrar();
        this.dispose(); 
    }
    public void mostrarVentanaAdmin(int idAdmin) {
        VentanaAdminGUI adminGUI = new VentanaAdminGUI();
        VentanaAdminFUN adminController = new VentanaAdminFUN(adminGUI, idAdmin, this); 
        adminGUI.mostrar(); 
        this.dispose(); 
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PrimeraVentana ventana = new PrimeraVentana();
            ventana.setVisible(true);
        });
    }
}
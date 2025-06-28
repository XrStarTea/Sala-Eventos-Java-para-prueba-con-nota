package SalaEventos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaAdminGUI extends JFrame {

    private JTabbedPane tabbedPane;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;
    private JTable tablaEventos;
    private DefaultTableModel modeloTablaEventos;
    private JLabel lblNombreAdmin;
    private JButton btnCerrarSesion;
    private JButton btnEliminarUsuario;
    private JButton btnAgregarEvento;
    private JButton btnEditarEvento;
    private JButton btnEliminarEvento;
    private JButton btnExportarEventos;

    public VentanaAdminGUI() {
        setTitle("Administración del Sistema de Eventos");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        try {
            java.net.URL url = getClass().getResource("/img/icono.png");
            if (url != null) {
                setIconImage(new ImageIcon(url).getImage());
            } else {
                System.err.println("Icono de ventana no encontrado en /img/icono.png. Usando icono por defecto.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono de la aplicación: " + e.getMessage());
        }
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
        panelCabecera.setPreferredSize(new Dimension(getWidth(), 150));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JPanel panelTituloLogo = new JPanel();
        panelTituloLogo.setLayout(new BoxLayout(panelTituloLogo, BoxLayout.Y_AXIS)); 
        panelTituloLogo.setOpaque(false);
        JLabel lblTituloSistema = new JLabel("Sistema de Gestión de Eventos");
        lblTituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloSistema.setForeground(Color.WHITE);
        lblTituloSistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTituloLogo.add(lblTituloSistema);
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
                System.err.println("Logo de sistema no encontrado en /img/logo.png. Usando texto alternativo.");
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
        JPanel panelAdminInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); 
        panelAdminInfo.setOpaque(false); 

        lblNombreAdmin = new JLabel("Administrador: "); 
        lblNombreAdmin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombreAdmin.setForeground(Color.WHITE);
        panelAdminInfo.add(lblNombreAdmin);
        
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFocusPainted(false); 
        btnCerrarSesion.setBackground(new Color(255, 87, 34)); 
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setPreferredSize(new Dimension(120, 30)); 
        panelAdminInfo.add(btnCerrarSesion);

        panelCabecera.add(panelAdminInfo, BorderLayout.EAST); 
        add(panelCabecera, BorderLayout.NORTH);
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // --- Pestaña de Gestión de Usuarios 
        JPanel panelUsuarios = new JPanel(new BorderLayout());
        String[] columnasUsuarios = {"ID", "Nombre Usuario", "Tipo", "RUT", "Teléfono", "Email"};
        modeloTablaUsuarios = new DefaultTableModel(columnasUsuarios, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTablaUsuarios);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelUsuarios.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        JPanel panelBotonesUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnEliminarUsuario = new JButton("Eliminar Usuario");
        panelBotonesUsuario.add(btnEliminarUsuario);
        panelUsuarios.add(panelBotonesUsuario, BorderLayout.SOUTH);
        tabbedPane.addTab("Usuarios", panelUsuarios);
        // --- Pestaña de Gestión de Eventos
        JPanel panelEventos = new JPanel(new BorderLayout());
        String[] columnasEventos = {"ID", "Nombre", "Fecha", "Hora", "Lugar",
            "Descripción", "Capacidad Máxima", "Precio Entrada", "Creador"};
        modeloTablaEventos = new DefaultTableModel(columnasEventos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEventos = new JTable(modeloTablaEventos);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelEventos.add(new JScrollPane(tablaEventos), BorderLayout.CENTER);
        JPanel panelBotonesEventos = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregarEvento = new JButton("Agregar Evento");
        btnEditarEvento = new JButton("Editar Evento");
        btnEliminarEvento = new JButton("Eliminar Evento");
        btnExportarEventos = new JButton("Exportar Eventos (CSV)");
        panelBotonesEventos.add(btnAgregarEvento);
        panelBotonesEventos.add(btnEditarEvento);
        panelBotonesEventos.add(btnEliminarEvento);
        panelBotonesEventos.add(btnExportarEventos);
        panelEventos.add(panelBotonesEventos, BorderLayout.SOUTH);
        tabbedPane.addTab("Eventos", panelEventos);
    }
    // --- Getters
    public JTable getTablaUsuarios() { return tablaUsuarios; }
    public DefaultTableModel getModeloTablaUsuarios() { return modeloTablaUsuarios; }
    public JTable getTablaEventos() { return tablaEventos; }
    public DefaultTableModel getModeloTablaEventos() { return modeloTablaEventos; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnEliminarUsuario() { return btnEliminarUsuario; }
    public JButton getBtnAgregarEvento() { return btnAgregarEvento; }
    public JButton getBtnEditarEvento() { return btnEditarEvento; }
    public JButton getBtnEliminarEvento() { return btnEliminarEvento; }
    public JButton getBtnExportarEventos() { return btnExportarEventos; }
    public JLabel getLblNombreAdmin() { return lblNombreAdmin; }
    public void setNombreAdmin(String nombre) {
        lblNombreAdmin.setText("Administrador: " + nombre);
    }
    public void mostrar() { setVisible(true); }
    public void ocultar() { setVisible(false); dispose(); }
    public JFrame getFrame() {
        return this;
    }
}
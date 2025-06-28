package SalaEventos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;

public class VentanaClienteGUI extends JFrame {

    private JPanel jPanel1;
    private JPanel jPanel2;
    private JLabel jLabel1; 
    private JLabel jLabel3;
    private JLabel jLabel4; 
    private JButton btnSalir;
    private JButton btnEditarEvento;
    private JButton btnCrearEvento;
    private JLabel lblBienvenidaUsuario;
    private JLabel jLabel5;
    private int idClienteActual;
    private JTable tablaEventos;
    private DefaultTableModel modeloTablaEventos;
    private JScrollPane scrollPaneTabla;

    public VentanaClienteGUI(int idCliente) {
        this.idClienteActual = idCliente;
        setTitle("Sistema de Gestión de Eventos - Cliente");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setSize(780, 550);
        setLocationRelativeTo(null);
        setResizable(true);
        try {
            URL iconUrl = getClass().getResource("/img/icono.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            } else {
                System.err.println("Icono de ventana no encontrado en img/icono.png Usando icono por defecto.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono de la aplicación: " + e.getMessage());
        }

        initComponentsManual();
    }
    private void initComponentsManual() {
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jLabel1 = new JLabel("Sala ");
        jLabel3 = new JLabel("Eventos");
        jLabel4 = new JLabel();
        btnSalir = new JButton("Salir");
        btnEditarEvento = new JButton("Editar Evento");
        btnCrearEvento = new JButton("Crear Evento");
        lblBienvenidaUsuario = new JLabel("BIENVENIDO <USUARIO>!");
        jLabel5 = new JLabel("EVENTOS RECIENTES");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new BorderLayout());

        jPanel2.setBackground(new java.awt.Color(41, 31, 24));
        jPanel2.setPreferredSize(new Dimension(getWidth(), 150));
        jPanel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        try {
            URL logoUrl = getClass().getResource("/img/logo.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                jLabel4.setIcon(new ImageIcon(scaledImage));
            } else {
                jLabel4.setText("LOGO");
                jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 24));
                jLabel4.setForeground(new Color(241, 223, 187));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el logo en VentanaClienteGUI: " + e.getMessage());
            jLabel4.setText("LOGO");
            jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 24));
            jLabel4.setForeground(new Color(241, 223, 187));
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel2.add(jLabel4, gbc);

        // Panel para Sala Eventos y Bienvenida
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        // Sala Eventos
        jLabel1.setFont(new Font("Segoe UI", 0, 36));
        jLabel1.setForeground(new Color(241, 223, 187));
        jLabel3.setFont(new Font("Segoe UI", 0, 36));
        jLabel3.setForeground(new Color(241, 223, 187));
        
        JPanel titleContainer = new JPanel();
        titleContainer.setOpaque(false);
        titleContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
        titleContainer.add(jLabel1);
        titleContainer.add(jLabel3);
        titleContainer.setAlignmentX(Component.LEFT_ALIGNMENT); 

        textPanel.add(titleContainer);
        textPanel.add(Box.createVerticalStrut(5)); 

        // Mensaje de Bienvenida
        lblBienvenidaUsuario.setFont(new Font("Segoe UI", 0, 18));
        lblBienvenidaUsuario.setForeground(new Color(241, 223, 187));
        lblBienvenidaUsuario.setAlignmentX(Component.LEFT_ALIGNMENT); 
        textPanel.add(lblBienvenidaUsuario);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2; 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        jPanel2.add(textPanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        styleButton(btnCrearEvento);
        styleButton(btnEditarEvento);
        styleButton(btnSalir);
        buttonPanel.add(btnCrearEvento);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnEditarEvento);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnSalir);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        jPanel2.add(buttonPanel, gbc);

        jPanel1.add(jPanel2, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBackground(new Color(255, 255, 255));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jLabel5.setFont(new Font("Segoe UI", 0, 18));
        jLabel5.setForeground(new Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        mainContentPanel.add(jLabel5, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nombre", "Fecha", "Hora", "Lugar", "Capacidad", "Precio", "Descripción"};
        modeloTablaEventos = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEventos = new JTable(modeloTablaEventos);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEventos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEventos.setRowHeight(25);
        tablaEventos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaEventos.getTableHeader().setBackground(new Color(30, 136, 229));
        tablaEventos.getTableHeader().setForeground(Color.WHITE);
        scrollPaneTabla = new JScrollPane(tablaEventos);
        scrollPaneTabla.setBackground(new Color(204, 204, 204));
        scrollPaneTabla.getViewport().setBackground(new Color(204, 204, 204));
        scrollPaneTabla.setBorder(BorderFactory.createEmptyBorder());
        mainContentPanel.add(scrollPaneTabla, BorderLayout.CENTER);

        jPanel1.add(mainContentPanel, BorderLayout.CENTER);
        getContentPane().add(jPanel1);
    }
    
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 87, 34));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 30));
    }
    
    public JLabel getLblBienvenidaUsuario() {
        return lblBienvenidaUsuario;
    }
    
    public void setMensajeBienvenida(String usuario) {
        lblBienvenidaUsuario.setText("BIENVENIDO " + usuario.toUpperCase() + "!");
    }
    
    public JButton getBtnSalir() {
        return btnSalir;
    }
    
    public JButton getBtnEditarEvento() {
        return btnEditarEvento;
    }
    
    public JButton getBtnCrearEvento() {
        return btnCrearEvento;
    }
    
    public DefaultTableModel getModeloTablaEventos() {
        return modeloTablaEventos;
    }
    
    public JTable getTablaEventos() {
        return tablaEventos;
    }
    
    public int getEventoSeleccionadoId() {
        int selectedRow = tablaEventos.getSelectedRow();
        if (selectedRow != -1) {
            Object idObj = modeloTablaEventos.getValueAt(selectedRow, 0);
            if (idObj instanceof Integer) {
                return (int) idObj;
            } else if (idObj instanceof String) {
                try {
                    return Integer.parseInt((String)idObj);
                } catch (NumberFormatException e) {
                    System.err.println("ID del evento no es un número válido: " + idObj);
                    return -1;
                }
            }
        }
        return -1;
    }
    
    public void mostrar() {
        setVisible(true);
    }
    
    public void ocultar() {
        setVisible(false);
        dispose();
    }
}
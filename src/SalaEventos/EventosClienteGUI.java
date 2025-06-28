package SalaEventos;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class EventosClienteGUI extends JDialog {
    
    private JLabel lblTitulo;
    private JTextField txtNombre;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtLugar;
    private JTextArea txtDescripcion;
    private JTextField txtCapacidad;
    private JTextField txtPrecio;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public EventosClienteGUI(JFrame parent) {
        super(parent, true);
        setTitle("Gestión de Eventos");
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Carga de icono de la ventana (opcional)
        try {
            URL iconUrl = getClass().getResource("/img/icono.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono de la ventana de evento: "
                    + e.getMessage());
        }

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Título del formulario
        lblTitulo = new JLabel("Crear Nuevo Evento", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 40, 35));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(labelFont);
        formPanel.add(lblNombre, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        formPanel.add(txtNombre, gbc);

        // Fecha
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setFont(labelFont);
        formPanel.add(lblFecha, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtFecha = new JTextField(20);
        formPanel.add(txtFecha, gbc);

        // Hora
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblHora = new JLabel("Hora (HH:MM):");
        lblHora.setFont(labelFont);
        formPanel.add(lblHora, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtHora = new JTextField(20);
        formPanel.add(txtHora, gbc);

        // Lugar
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblLugar = new JLabel("Lugar:");
        lblLugar.setFont(labelFont);
        formPanel.add(lblLugar, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        txtLugar = new JTextField(20);
        formPanel.add(txtLugar, gbc);

        // Descripción
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(labelFont);
        formPanel.add(lblDescripcion, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weighty = 0.5;
        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        formPanel.add(scrollDescripcion, gbc);

        // Capacidad Máxima
        gbc.gridx = 0; gbc.gridy = 5; gbc.weighty = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblCapacidad = new JLabel("Capacidad Máxima:");
        lblCapacidad.setFont(labelFont);
        formPanel.add(lblCapacidad, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0;
        txtCapacidad = new JTextField(20);
        formPanel.add(txtCapacidad, gbc);

        // Precio Entrada
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblPrecio = new JLabel("Precio Entrada:");
        lblPrecio.setFont(labelFont);
        formPanel.add(lblPrecio, gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        txtPrecio = new JTextField(20);
        formPanel.add(txtPrecio, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        
        styleButton(btnGuardar);
        styleButton(btnCancelar);

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Método para aplicar estilo a los botones
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 87, 34));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }

    // --- MÉTODOS NUEVOS PARA EDICIÓN ---
    public void setTituloFormulario(String titulo) {
        lblTitulo.setText(titulo);
    }
    public void cargarDatosEvento(Evento evento) {
        if (evento != null) {
            txtNombre.setText(evento.getNombre());
            txtFecha.setText(evento.getFecha().toString());
            txtHora.setText(evento.getHora().toString());
            txtLugar.setText(evento.getLugar());
            txtDescripcion.setText(evento.getDescripcion());
            txtCapacidad.setText(String.valueOf(evento.getCapacidadMaxima()));
            txtPrecio.setText(String.valueOf(evento.getPrecioEntrada()));
        }
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        txtLugar.setText("");
        txtDescripcion.setText("");
        txtCapacidad.setText("");
        txtPrecio.setText("");
    }
    // ------------------------------------

    // Getters para los campos de texto y botones
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtFecha() { return txtFecha; }
    public JTextField getTxtHora() { return txtHora; }
    public JTextField getTxtLugar() { return txtLugar; }
    public JTextArea getTxtDescripcion() { return txtDescripcion; }
    public JTextField getTxtCapacidad() { return txtCapacidad; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}

package SalaEventos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PanelCrearCuenta extends JPanel {

    private JTextField txtNuevoUsuario;
    private JPasswordField txtNuevaContrasena;
    private JPasswordField txtConfirmarContrasena;
    private JRadioButton rbCliente;
    private JRadioButton rbAdmin;
    private ButtonGroup grupoTipoUsuario;
    private JButton btnCrearCuenta;
    private JButton btnVolverInicioSesion;
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private GestorUsuarios gestorUsuarios;
    private JLabel lblRut;
    private JTextField txtRut;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblTelefono;
    private JTextField txtTelefono;
    
    public PanelCrearCuenta(CardLayout layout, JPanel contenedor) {
        this.cardLayout = layout;
        this.panelContenedor = contenedor;
        this.gestorUsuarios = new GestorUsuarios();
        
        initComponents();
        setupListeners();
        actualizarVisibilidadCamposCliente(); 
    }

    private void initComponents() {
        setBackground(new Color(240, 240, 240));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Título
        JLabel lblTitulo = new JLabel("Crear Nueva Cuenta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        add(lblTitulo, gbc);
        gbc.weighty = 0; 
        gbc.gridwidth = 1;
        // Usuario
        JLabel lblNuevoUsuario = new JLabel("Nombre de Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblNuevoUsuario, gbc);
        txtNuevoUsuario = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtNuevoUsuario, gbc);
        // Contraseña
        JLabel lblNuevaContrasena = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblNuevaContrasena, gbc);
        txtNuevaContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtNuevaContrasena, gbc);
        // Confirmar Contraseña
        JLabel lblConfirmarContrasena = new JLabel("Confirmar Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblConfirmarContrasena, gbc);
        txtConfirmarContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(txtConfirmarContrasena, gbc);
        // Tipo de Usuario
        JLabel lblTipoUsuario = new JLabel("Tipo de Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(lblTipoUsuario, gbc);
        JPanel panelTipoUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipoUsuario.setOpaque(false);
        rbCliente = new JRadioButton("Cliente", true);
        rbAdmin = new JRadioButton("Administrador");
        grupoTipoUsuario = new ButtonGroup();
        grupoTipoUsuario.add(rbCliente);
        grupoTipoUsuario.add(rbAdmin);
        panelTipoUsuario.add(rbCliente);
        panelTipoUsuario.add(rbAdmin);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(panelTipoUsuario, gbc);
        // Rut
        lblRut = new JLabel("RUT:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(lblRut, gbc);
        txtRut = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(txtRut, gbc);
        // Email
        lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(lblEmail, gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(txtEmail, gbc);
        //Telefono
        lblTelefono = new JLabel("Teléfono:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(lblTelefono, gbc);
        txtTelefono = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(txtTelefono, gbc);
        // Botón Crear Cuenta
        btnCrearCuenta = new JButton("Crear Cuenta");
        btnCrearCuenta.setBackground(new Color(0, 153, 51));
        btnCrearCuenta.setForeground(Color.WHITE);
        btnCrearCuenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(btnCrearCuenta, gbc);
        // Botón Volver
        btnVolverInicioSesion = new JButton("Volver a Iniciar Sesión");
        btnVolverInicioSesion.setBackground(new Color(255, 153, 0));
        btnVolverInicioSesion.setForeground(Color.WHITE);
        btnVolverInicioSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        add(btnVolverInicioSesion, gbc);
    }

    private void setupListeners() {
        btnCrearCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String nuevoUsuario = txtNuevoUsuario.getText().trim();
                String contrasena = new String(txtNuevaContrasena.getPassword());
                String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());
                String tipoUsuario = rbCliente.isSelected() ? "Cliente" : "Administrador";
                if (nuevoUsuario.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this,
                            "Por favor, complete todos los campos de usuario y contraseña.",
                            "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!contrasena.equals(confirmarContrasena)) {
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Las contraseñas no coinciden.",
                            "Error de Contraseña", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String rut = null;
                String email = null;
                String telefono = null;
                // --- Validar campos de cliente
                if (rbCliente.isSelected()) {
                    rut = txtRut.getText().trim();
                    email = txtEmail.getText().trim();
                    telefono = txtTelefono.getText().trim();
                    if (rut.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this,
                                "Para crear una cuenta de Cliente, los campos RUT, Email y Teléfono son obligatorios.",
                                "Datos de Cliente Faltantes", JOptionPane.WARNING_MESSAGE);
                        return; // Detiene la creación de la cuenta
                    }
                    
                    // --- Validaciones de formato adicionales
                    if (!isValidRut(rut)) {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this,
                                "El RUT ingresado no tiene un formato válido (Ej: 12345678-K).",
                                "RUT Inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (!isValidEmail(email)) {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this,
                                "El Email ingresado no tiene un formato válido.",
                                "Email Inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                // Crear el objeto Usuario con los datos adicionales para clientes
                Usuario usuarioObj = new Usuario(nuevoUsuario, contrasena, tipoUsuario, rut, telefono, email);
                if (gestorUsuarios.registrarUsuario(usuarioObj)) {
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Cuenta de " + tipoUsuario +
                            " creada exitosamente para " + nuevoUsuario +
                            "!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    cardLayout.show(panelContenedor, "IniciarSesion");
                } else {
                    // Si el registro falla (ej. usuario ya existe o error en BD)
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this,
                            "Error al crear la cuenta. El nombre de usuario podría ya existir o hubo un problema en la base de datos.",
                            "Error de Registro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Listener para el botón "Volver"
        btnVolverInicioSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                limpiarCampos();
                cardLayout.show(panelContenedor, "IniciarSesion");
            }
        });
        // Listener para los RadioButtons para controlar la visibilidad de los campos de cliente
        rbCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarVisibilidadCamposCliente();
            }
        });
        rbAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarVisibilidadCamposCliente();
            }
        });
    }
    private void actualizarVisibilidadCamposCliente() {
        boolean esCliente = rbCliente.isSelected();
        lblRut.setVisible(esCliente);
        txtRut.setVisible(esCliente);
        lblEmail.setVisible(esCliente);
        txtEmail.setVisible(esCliente);
        lblTelefono.setVisible(esCliente);
        txtTelefono.setVisible(esCliente);
        revalidate();
        repaint();
    }
    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        txtNuevoUsuario.setText("");
        txtNuevaContrasena.setText("");
        txtConfirmarContrasena.setText("");
        rbCliente.setSelected(true);
        txtRut.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        actualizarVisibilidadCamposCliente();
    }
    // --- Métodos de validación de formato
    private boolean isValidRut(String rut) {
        // Formato básico de RUT chileno: XX.XXX.XXX-Y o X.XXX.XXX-Y
        // Simplificado para este ejemplo, solo verifica que termine en dígito o K
        if (rut == null || rut.length() < 2) return false;

        // Elimina puntos y guión para la validación interna
        rut = rut.replace(".", "").replace("-", "");

        if (rut.length() < 2) return false; // Al menos 1 dígito y un dígito verificador

        // Obtener el dígito verificador (dv) proporcionado
        char dv = Character.toUpperCase(rut.charAt(rut.length() - 1));
        // Obtener la parte numérica del RUT
        String numero = rut.substring(0, rut.length() - 1);

        try {
            int suma = 0;
            int multiplo = 2;
            // Calcular la suma para el dígito verificador
            for (int i = numero.length() - 1; i >= 0; i--) {
                suma += Character.getNumericValue(numero.charAt(i)) * multiplo;
                multiplo++;
                if (multiplo > 7) multiplo = 2;
            }

            int resto = suma % 11;
            char dvCalculado;

            // Determinar el dígito verificador calculado según el resto
            if (resto == 0) {
                dvCalculado = '0'; // Si el resto es 0, el dígito verificador es 0
            } else if (resto == 1) {
                dvCalculado = 'K'; // Si el resto es 1, el dígito verificador es K
            } else {
                // Para otros restos, convertir el resultado (11 - resto) a su carácter de dígito
                dvCalculado = (char) ((11 - resto) + '0');
            }

            // Comparar el dígito verificador proporcionado con el calculado
            return dv == dvCalculado;

        } catch (NumberFormatException e) {
            // Si hay un problema al convertir números, es un RUT inválido
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        // Expresión regular básica para validar email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
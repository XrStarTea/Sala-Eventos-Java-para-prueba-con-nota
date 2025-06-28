package SalaEventos;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

public class VentanaAdminFUN {

    private VentanaAdminGUI view;
    private GestorUsuarios gestorUsuarios;
    private GestorEventos gestorEventos;
    private PrimeraVentana primeraVentanaPadre;
    private int idAdminActual;
    
    // Mapa para almacenar nombres de usuario por ID, para mostrar en la tabla
    private Map<Integer, String> nombresUsuariosCache;

    public VentanaAdminFUN(VentanaAdminGUI view, int idAdmin, PrimeraVentana padre) {
        this.view = view;
        this.idAdminActual = idAdmin;
        this.primeraVentanaPadre = padre;
        this.gestorUsuarios = new GestorUsuarios();
        this.gestorEventos = new GestorEventos();
        this.nombresUsuariosCache = new HashMap<>();

        // Configurar el nombre del administrador en la GUI
        Usuario adminLogueado = gestorUsuarios.obtenerUsuarioPorId(idAdminActual);
        String nombreAdmin = (adminLogueado != null) ? adminLogueado.getNombreUsuario() : "Desconocido";
        view.setNombreAdmin(nombreAdmin);
        // Precargar todos los nombres de usuarios en la caché
        precargarNombresUsuarios();
        // Inicializar listeners
        initListeners();
        // Cargar datos iniciales
        cargarUsuariosEnTabla();
        cargarTodosLosEventosEnTabla();
    }
    private void precargarNombresUsuarios() {
        nombresUsuariosCache.clear();
        List<Usuario> usuarios = gestorUsuarios.obtenerTodosLosUsuarios();
            for (Usuario usuario : usuarios) {
                nombresUsuariosCache.put(usuario.getId(), usuario.getNombreUsuario());
            }
    }
    private void initListeners() {
        view.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        view.getBtnEliminarUsuario().addActionListener(e -> eliminarUsuario());
        view.getBtnAgregarEvento().addActionListener(e -> agregarEvento()); 
        view.getBtnEditarEvento().addActionListener(e -> editarEvento());
        view.getBtnEliminarEvento().addActionListener(e -> eliminarEvento());
        view.getBtnExportarEventos().addActionListener(e -> exportarEventosACsv());
        view.getTablaUsuarios().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = view.getTablaUsuarios().getSelectedRow();
                    if (selectedRow != -1) {
                        view.getBtnEliminarUsuario().setEnabled(true);
                    } else {
                        view.getBtnEliminarUsuario().setEnabled(false);
                    }
                }
            }
        });
        view.getTablaEventos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = view.getTablaEventos().getSelectedRow();
                    if (selectedRow != -1) {
                        view.getBtnEditarEvento().setEnabled(true);
                        view.getBtnEliminarEvento().setEnabled(true);
                    } else {
                        view.getBtnEditarEvento().setEnabled(false);
                        view.getBtnEliminarEvento().setEnabled(false);
                    }
                }
            }
        });
    }
    private void cargarUsuariosEnTabla() {
        view.getModeloTablaUsuarios().setRowCount(0);
        List<Usuario> usuarios = gestorUsuarios.obtenerTodosLosUsuarios();
        for (Usuario usuario : usuarios) {
            view.getModeloTablaUsuarios().addRow(new Object[]{
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getTipoUsuario(),
                usuario.getRut(),
                usuario.getTelefono(),
                usuario.getEmail()
            });
        }
    }
    private void cargarTodosLosEventosEnTabla() {
        view.getModeloTablaEventos().setRowCount(0);
        List<Evento> eventos = gestorEventos.obtenerTodosLosEventos();
        for (Evento evento : eventos) {
            String nombreCreador = nombresUsuariosCache.getOrDefault(evento.getIdUsuarioCreador(),
                    "ID " + evento.getIdUsuarioCreador() + " (Desconocido)");
            view.getModeloTablaEventos().addRow(new Object[]{
                evento.getId(),
                evento.getNombre(),
                evento.getFecha(),
                evento.getHora(),
                evento.getLugar(),
                evento.getDescripcion(),
                evento.getCapacidadMaxima(),
                evento.getPrecioEntrada(),
                nombreCreador
            });
        }
    }

    private void eliminarUsuario() {
        int selectedRow = view.getTablaUsuarios().getSelectedRow();
        if (selectedRow != -1) {
            int idUsuario = (int) view.getModeloTablaUsuarios().getValueAt(selectedRow, 0);
            // No permitir eliminar al administrador logueado
            if (idUsuario == idAdminActual) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "No puedes eliminar tu propia cuenta de administrador.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(view.getFrame(),
                    "¿Está seguro que desea eliminar al usuario con ID: " + idUsuario + "?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (gestorUsuarios.eliminarUsuario(idUsuario)) {
                    JOptionPane.showMessageDialog(view.getFrame(),
                            "Usuario eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE); //
                    cargarUsuariosEnTabla();
                    view.getBtnEliminarUsuario().setEnabled(false);
                    precargarNombresUsuarios();
                    cargarTodosLosEventosEnTabla();
                } else {
                    JOptionPane.showMessageDialog(view.getFrame(),
                            "Error al eliminar usuario. Puede que tenga eventos o reservas asociadas.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Por favor, seleccione un usuario de la tabla para eliminar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void agregarEvento() {
        List<Usuario> usuariosDisponibles = gestorUsuarios.obtenerTodosLosUsuarios();
        usuariosDisponibles.sort(Comparator.comparing(u -> {
            if (u.getId() == idAdminActual) return 0;
            if ("Administrador".equalsIgnoreCase(u.getTipoUsuario())) return 1;
            return 2;
        }));
        JComboBox<Usuario> creadorComboBox = new JComboBox<>();
        creadorComboBox.addItem(null);
        for (Usuario user : usuariosDisponibles) {
            creadorComboBox.addItem(user);
        }
        creadorComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Usuario) {
                    Usuario user = (Usuario) value;
                    setText(user.getNombreUsuario() + " (ID: " + user.getId() + " - " + user.getTipoUsuario() + ")");
                } else if (value == null) {
                    setText("Seleccionar Creador (por defecto: el Administrador actual)");
                }
                return this;
            }
        });
        // Establecer el administrador actual como la opción por defecto en el JComboBox
        Usuario adminActualObj = gestorUsuarios.obtenerUsuarioPorId(idAdminActual);
        if (adminActualObj != null) {
            creadorComboBox.setSelectedItem(adminActualObj);
        }
        // Campos para el formulario de nuevo evento
        JTextField nombreField = new JTextField();
        JTextField fechaField = new JTextField("YYYY-MM-DD");
        JTextField horaField = new JTextField("HH:MM");
        JTextField lugarField = new JTextField();
        JTextArea descripcionArea = new JTextArea(3, 20);
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        JScrollPane descripcionScrollPane = new JScrollPane(descripcionArea);
        JSpinner capacidadSpinner = new JSpinner(new SpinnerNumberModel(50, 1, 1000, 1));
        JTextField precioField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Creador del Evento:"));
        panel.add(creadorComboBox);
        panel.add(new JLabel("Nombre del Evento:"));
        panel.add(nombreField);
        panel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panel.add(fechaField);
        panel.add(new JLabel("Hora (HH:MM):"));
        panel.add(horaField);
        panel.add(new JLabel("Lugar:"));
        panel.add(lugarField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descripcionScrollPane);
        panel.add(new JLabel("Capacidad Máxima:"));
        panel.add(capacidadSpinner);
        panel.add(new JLabel("Precio Entrada:"));
        panel.add(precioField);
        int result = JOptionPane.showConfirmDialog(view.getFrame(), panel, //
                "Agregar Nuevo Evento", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText().trim();
            String fechaStr = fechaField.getText().trim();
            String horaStr = horaField.getText().trim();
            String lugar = lugarField.getText().trim();
            String descripcion = descripcionArea.getText().trim();
            int capacidadMaxima = (int) capacidadSpinner.getValue();
            String precioStr = precioField.getText().trim();
            // Obtener el ID del usuario creador (cliente o administrador)
            Usuario selectedUser = (Usuario) creadorComboBox.getSelectedItem();
            int idUsuarioCreador;
            if (selectedUser != null) {
                idUsuarioCreador = selectedUser.getId();
            } else {
                idUsuarioCreador = idAdminActual; // Si no se selecciona un usuario, el creador es el admin actual
            }
            // Validaciones
            if (nombre.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty()
                    || lugar.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Todos los campos son obligatorios.",
                        "Error de Validación", JOptionPane.ERROR_MESSAGE); //
                return;
            }

            LocalDate fecha;
            try {
                fecha = LocalDate.parse(fechaStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Formato de fecha inválido. Use YYYY-MM-DD.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalTime hora;
            try {
                hora = LocalTime.parse(horaStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Formato de hora inválido. Use HH:MM.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precioEntrada;
            try {
                precioEntrada = Double.parseDouble(precioStr);
                if (precioEntrada < 0) {
                    JOptionPane.showMessageDialog(view.getFrame(),
                            "El precio de entrada no puede ser negativo.",
                            "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Formato de precio inválido. Use un número.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Evento nuevoEvento = new Evento(nombre, fecha, hora, lugar,
                    descripcion, capacidadMaxima, precioEntrada, idUsuarioCreador);

            if (gestorEventos.crearEvento(nuevoEvento)) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Evento agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                precargarNombresUsuarios();
                cargarTodosLosEventosEnTabla();
            } else {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Error al agregar el evento. Intente de nuevo.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void editarEvento() {
        int selectedRow = view.getTablaEventos().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Por favor, seleccione un evento de la tabla para editar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Obtener datos del evento seleccionado
        int idEvento = (int) view.getModeloTablaEventos().getValueAt(selectedRow, 0);
        Evento eventoAEditar = gestorEventos.obtenerEventoPorId(idEvento);
        if (eventoAEditar == null) {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "No se pudo cargar la información del evento seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Usuario> todosLosUsuarios = gestorUsuarios.obtenerTodosLosUsuarios();
        todosLosUsuarios.sort(Comparator.comparing(u -> {
            if (u.getId() == eventoAEditar.getIdUsuarioCreador()) return 0; 
            if (u.getId() == idAdminActual) return 1; 
            if ("Administrador".equalsIgnoreCase(u.getTipoUsuario())) return 2; 
            return 3;
        }));
        JComboBox<Usuario> creadorComboBox = new JComboBox<>();
        creadorComboBox.addItem(null);
        for (Usuario user : todosLosUsuarios) {
            creadorComboBox.addItem(user);
        }
        Usuario creadorActual = gestorUsuarios.obtenerUsuarioPorId(eventoAEditar.getIdUsuarioCreador());
        if (creadorActual != null) {
            creadorComboBox.setSelectedItem(creadorActual);
        } else {
            creadorComboBox.setSelectedItem(null);
        }
        creadorComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list,
                    Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                if (value instanceof Usuario) {
                    Usuario user = (Usuario) value;
                    setText(user.getNombreUsuario() + " (ID: " + user.getId() + " - " + user.getTipoUsuario() + ")");
                } else if (value == null) {
                    setText("Seleccionar Creador (por defecto: el Administrador actual)");
                }
                return this;
            }
        });


        // Crear campos de entrada pre-llenados con los datos actuales del evento
        JTextField nombreField = new JTextField(eventoAEditar.getNombre());
        JTextField fechaField = new JTextField(eventoAEditar.getFecha().toString());
        JTextField horaField = new JTextField(eventoAEditar.getHora().toString());
        JTextField lugarField = new JTextField(eventoAEditar.getLugar());
        JTextArea descripcionArea = new JTextArea(eventoAEditar.getDescripcion(), 3, 20);
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        JScrollPane descripcionScrollPane = new JScrollPane(descripcionArea);
        JSpinner capacidadSpinner = new JSpinner(new SpinnerNumberModel(eventoAEditar.getCapacidadMaxima(), 1, 1000, 1));
        JTextField precioField = new JTextField(String.valueOf(eventoAEditar.getPrecioEntrada()));

        // Panel para organizar los campos de entrada
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("ID Evento:"));
        panel.add(new JLabel(String.valueOf(idEvento)));
        panel.add(new JLabel("Creador del Evento:"));
        panel.add(creadorComboBox);
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panel.add(fechaField);
        panel.add(new JLabel("Hora (HH:MM):"));
        panel.add(horaField);
        panel.add(new JLabel("Lugar:"));
        panel.add(lugarField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descripcionScrollPane);
        panel.add(new JLabel("Capacidad Máxima:"));
        panel.add(capacidadSpinner);
        panel.add(new JLabel("Precio Entrada:"));
        panel.add(precioField);

        int result = JOptionPane.showConfirmDialog(view.getFrame(), panel, //
                "Editar Evento", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText().trim();
            String fechaStr = fechaField.getText().trim();
            String horaStr = horaField.getText().trim();
            String lugar = lugarField.getText().trim();
            String descripcion = descripcionArea.getText().trim();
            int capacidadMaxima = (int) capacidadSpinner.getValue();
            String precioStr = precioField.getText().trim();
            Usuario selectedUser = (Usuario) creadorComboBox.getSelectedItem();
            int idNuevoCreador;
            if (selectedUser != null) {
                idNuevoCreador = selectedUser.getId();
            } else {
                idNuevoCreador = idAdminActual;
            }
            if (nombre.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() || lugar.isEmpty()
                    || descripcion.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Todos los campos son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate fecha;
            try {
                fecha = LocalDate.parse(fechaStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalTime hora;
            try {
                hora = LocalTime.parse(horaStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Formato de hora inválido. Use HH:MM.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precioEntrada;
            try {
                precioEntrada = Double.parseDouble(precioStr);
                if (precioEntrada < 0) {
                    JOptionPane.showMessageDialog(view.getFrame(),
                            "El precio de entrada no puede ser negativo.",
                            "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Formato de precio inválido. Use un número.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Crear un objeto Evento con los datos actualizados y el ID original
            Evento eventoActualizado = new Evento(idEvento, nombre, fecha,
                    hora, lugar, descripcion, capacidadMaxima, precioEntrada, idNuevoCreador);
            if (gestorEventos.actualizarEventoComoAdmin(eventoActualizado)) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Evento actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                precargarNombresUsuarios();
                cargarTodosLosEventosEnTabla();
                view.getBtnEditarEvento().setEnabled(false);
                view.getBtnEliminarEvento().setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(view.getFrame(), "Error al actualizar el evento. Intente de nuevo.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void eliminarEvento() {
        int selectedRow = view.getTablaEventos().getSelectedRow();
        if (selectedRow != -1) {
            int idEvento = (int) view.getModeloTablaEventos().getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(view.getFrame(),
                    "¿Está seguro que desea eliminar el evento con ID: " + idEvento + "?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (gestorEventos.eliminarEvento(idEvento)) {
                    JOptionPane.showMessageDialog(view.getFrame(),
                            "Evento eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    precargarNombresUsuarios();
                    cargarTodosLosEventosEnTabla();
                    view.getBtnEditarEvento().setEnabled(false);
                    view.getBtnEliminarEvento().setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(view.getFrame(),
                            "Error al eliminar el evento. Puede que tenga reservas asociadas.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Por favor, seleccione un evento de la tabla para eliminar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(),
                "¿Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.ocultar();
            if (primeraVentanaPadre != null) {
                primeraVentanaPadre.setVisible(true);
            }
        }
    }
    // Exportar a csv
    private void exportarEventosACsv() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Eventos como CSV");
        fileChooser.setSelectedFile(new File("eventos.csv"));
        // Filtro para solo mostrar archivos CSV
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos CSV (.csv)", "csv");
        fileChooser.addChoosableFileFilter(filter);
        int userSelection = fileChooser.showSaveDialog(view.getFrame());
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Asegurarse de que tenga la extensión .csv
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                fileToSave = new File(filePath + ".csv");
            }
            // Obtener los datos de la tabla de eventos
            DefaultTableModel model = view.getModeloTablaEventos();
            int numColumns = model.getColumnCount();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                // Escribir cabeceras
                for (int i = 0; i < numColumns; i++) {
                    writer.write(model.getColumnName(i));
                    if (i < numColumns - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
                // Escribir datos
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < numColumns; j++) {
                        Object value = model.getValueAt(i, j);
                        String formattedValue = (value == null) ? "" : value.toString();
                        if (formattedValue.contains(",") || formattedValue.contains("\"") || formattedValue.contains("\n")) {
                            formattedValue = "\"" + formattedValue.replace("\"", "\"\"") + "\"";
                        }
                        writer.write(formattedValue);
                        if (j < numColumns - 1) {
                            writer.write(",");
                        }
                    }
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Eventos exportados exitosamente a:\n" + fileToSave.getAbsolutePath(),
                        "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view.getFrame(),
                        "Error al exportar eventos: " + ex.getMessage(), "Error de Exportación",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
package SalaEventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

public class EventosClienteFUN {

    private EventosClienteGUI view;
    private int idUsuarioCreador;
    private Evento eventoAEditar;
    private GestorEventos gestorEventos;

    // Constructor para MODO CREAR EVENTO
    public EventosClienteFUN(EventosClienteGUI view, int idUsuarioCreador) {
        this.view = view;
        this.idUsuarioCreador = idUsuarioCreador;
        this.gestorEventos = new GestorEventos();
        this.eventoAEditar = null;
        initListeners();
        view.limpiarCampos();
        view.setTituloFormulario("Crear Nuevo Evento");
    }

    // Constructor para MODO EDITAR EVENTO
    public EventosClienteFUN(EventosClienteGUI view, int idUsuarioCreador,
            int idEventoAEditar) {
        this.view = view;
        this.idUsuarioCreador = idUsuarioCreador;
        this.gestorEventos = new GestorEventos();
        this.eventoAEditar = gestorEventos.obtenerEventoPorId(idEventoAEditar);
        initListeners();

        if (this.eventoAEditar != null) {
            view.cargarDatosEvento(this.eventoAEditar);
            view.setTituloFormulario("Editar Evento: " + eventoAEditar.getNombre());
        } else {
            JOptionPane.showMessageDialog(view, "No se encontró el evento para editar.", "Error de Edición", JOptionPane.ERROR_MESSAGE);
            view.dispose();
        }
    }

    private void initListeners() {
        view.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (eventoAEditar == null) {
                    crearEvento();
                } else {
                    actualizarEvento();
                }
            }
        });

        view.getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }

    // Este es el método privado 'crearEvento' de EventosClienteFUN
    private void crearEvento() {
        String nombre = view.getTxtNombre().getText().trim();
        String fechaStr = view.getTxtFecha().getText().trim();
        String horaStr = view.getTxtHora().getText().trim();
        String lugar = view.getTxtLugar().getText().trim();
        String descripcion = view.getTxtDescripcion().getText().trim();
        String capacidadStr = view.getTxtCapacidad().getText().trim();
        String precioStr = view.getTxtPrecio().getText().trim();

        if (nombre.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty()
                || lugar.isEmpty() || capacidadStr.isEmpty()
                || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                    "Por favor, complete todos los campos obligatorios.",
                    "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            int capacidad = Integer.parseInt(capacidadStr);
            double precio = Double.parseDouble(precioStr);

            if (capacidad <= 0 || precio < 0) {
                JOptionPane.showMessageDialog(view,
                        "Capacidad debe ser mayor que 0 y Precio no puede ser negativo.",
                        "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Evento nuevoEvento = new Evento(0, nombre, fecha, hora, lugar,
                    descripcion, capacidad, precio, idUsuarioCreador); 

            if (gestorEventos.crearEvento(nuevoEvento)) { 
                JOptionPane.showMessageDialog(view, "Evento creado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view,
                        "Error al crear el evento. Ya existe un evento en la misma fecha y lugar.",
                        "Error de Creación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(view,
                    "Formato de fecha u hora incorrecto. Use YYYY-MM-DD y HH:MM.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Capacidad o Precio deben ser números válidos.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Ocurrió un error inesperado al crear el evento: " 
                    + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Este es el método privado 'actualizarEvento' de EventosClienteFUN
    private void actualizarEvento() {
        if (eventoAEditar == null) {
            JOptionPane.showMessageDialog(view, "No hay evento seleccionado para actualizar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = view.getTxtNombre().getText().trim();
        String fechaStr = view.getTxtFecha().getText().trim();
        String horaStr = view.getTxtHora().getText().trim();
        String lugar = view.getTxtLugar().getText().trim();
        String descripcion = view.getTxtDescripcion().getText().trim();
        String capacidadStr = view.getTxtCapacidad().getText().trim();
        String precioStr = view.getTxtPrecio().getText().trim();

        if (nombre.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty()
                || lugar.isEmpty() || capacidadStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, complete todos los campos obligatorios.",
                    "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            int capacidad = Integer.parseInt(capacidadStr);
            double precio = Double.parseDouble(precioStr);

            if (capacidad <= 0 || precio < 0) {
                JOptionPane.showMessageDialog(view,
                        "Capacidad debe ser mayor que 0 y Precio no puede ser negativo.",
                        "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            eventoAEditar.setNombre(nombre);
            eventoAEditar.setFecha(fecha);
            eventoAEditar.setHora(hora);
            eventoAEditar.setLugar(lugar);
            eventoAEditar.setDescripcion(descripcion);
            eventoAEditar.setCapacidadMaxima(capacidad);
            eventoAEditar.setPrecioEntrada(precio);

            if (gestorEventos.actualizarEvento(eventoAEditar, this.idUsuarioCreador)) {
                JOptionPane.showMessageDialog(view, "Evento actualizado exitosamente.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, 
                        "Error al actualizar el evento. La fecha y el lugar coinciden con otro evento existente.",
                        "Error de Actualización", JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(view, "Formato de fecha u hora incorrecto. Use YYYY-MM-DD y HH:MM.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Capacidad o Precio deben ser números válidos.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Ocurrió un error inesperado al actualizar el evento: " 
                    + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
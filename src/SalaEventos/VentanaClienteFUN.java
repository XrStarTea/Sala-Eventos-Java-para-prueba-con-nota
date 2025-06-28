package SalaEventos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VentanaClienteFUN {

    private VentanaClienteGUI view;
    private int idClienteActual;
    private PrimeraVentana primeraVentanaPadre;
    private GestorUsuarios gestorUsuarios;
    private GestorEventos gestorEventos;

    public VentanaClienteFUN(VentanaClienteGUI view, int idCliente, PrimeraVentana padre) {
        this.view = view;
        this.idClienteActual = idCliente;
        this.primeraVentanaPadre = padre;
        this.gestorUsuarios = new GestorUsuarios();
        this.gestorEventos = new GestorEventos();
        cargarNombreUsuario();
        cargarEventos();
        asignarListeners();
    }

    private void cargarNombreUsuario() {
        Usuario usuarioLogueado = gestorUsuarios.obtenerUsuarioPorId(idClienteActual);
        if (usuarioLogueado != null) {
            view.setMensajeBienvenida(usuarioLogueado.getNombreUsuario());
        } else {
            view.setMensajeBienvenida("Usuario Desconocido");
        }
    }
    // Método para cargar los eventos en la tabla
    private void cargarEventos() {
        view.getModeloTablaEventos().setRowCount(0);
        List<Evento> eventos = gestorEventos.obtenerTodosLosEventos();
        for (Evento evento : eventos) {
            view.getModeloTablaEventos().addRow(new Object[]{
                evento.getId(),
                evento.getNombre(),
                evento.getFecha(),
                evento.getHora(),
                evento.getLugar(),
                evento.getCapacidadMaxima(),
                evento.getPrecioEntrada(),
                evento.getDescripcion()
            });
        }
        view.getBtnEditarEvento().setEnabled(false);
    }
    private void asignarListeners() {
        // Listener para el botón "Crear Evento"
        view.getBtnCrearEvento().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaCrearEvento();
            }
        });
        // Listener para el botón "Salir"
        view.getBtnSalir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
        // Listener para el boton "editar evento"
        view.getBtnEditarEvento().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaEditarEvento();
            }
        });
        // --- Listener para la seleccion de fila de algun evento
        // Este Listener habilita/deshabilita el botón "Editar Evento"
        view.getTablaEventos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean filaSeleccionada = view.getTablaEventos().getSelectedRow() != -1;
                    view.getBtnEditarEvento().setEnabled(filaSeleccionada);
                }
            }
        });
    }
    // Método para abrir la ventana de creación de eventos
    private void abrirVentanaCrearEvento() {
        EventosClienteGUI crearEventoView = new EventosClienteGUI(view);
        EventosClienteFUN crearEventoController = new EventosClienteFUN(crearEventoView, idClienteActual);
        crearEventoView.setVisible(true);
        cargarEventos();
    }
    // --- abrir ventana de edicion
    private void abrirVentanaEditarEvento() {
        int idEventoSeleccionado = view.getEventoSeleccionadoId(); //

        if (idEventoSeleccionado != -1) {
            // 1. Obtener el objeto completo del evento para verificar su propietario
            Evento eventoAEditar = this.gestorEventos.obtenerEventoPorId(idEventoSeleccionado);
            // Verificar si el evento fue encontrado (puede haber sido eliminado por otro usuario)
            if (eventoAEditar == null) {
                JOptionPane.showMessageDialog(view, "El evento seleccionado ya no existe.", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                cargarEventos();
                return;
            }
            // 2. VERIFICACIÓN DE PROPIEDAD: Comprobar si el ID del creador del evento es el del cliente actual
            if (eventoAEditar.getIdUsuarioCreador() == this.idClienteActual) {
                // 3. Si es el propietario, abrir la ventana de edición.
                EventosClienteGUI editarEventoView = new EventosClienteGUI(view);
                // Se pasa el id del cliente actual y el id del evento a editar
                new EventosClienteFUN(editarEventoView, idClienteActual, idEventoSeleccionado); //
                editarEventoView.setVisible(true);
                 editarEventoView.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        cargarEventos();
                    }
                });
            } else {
                // 4. Si no es el propietario, mostrar un mensaje de acceso denegado.
                JOptionPane.showMessageDialog(view, "No tiene permiso para editar este evento, ya que no fue creado por usted.",
                                              "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un evento para editar.",
                    "Ningún Evento Seleccionado", JOptionPane.WARNING_MESSAGE); //
        }
    }
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(view,
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
}
package SalaEventos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {
    private int id;
    private String nombre;
    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private String descripcion;
    private int capacidadMaxima;
    private double precioEntrada;
    private int idUsuarioCreador;

    public Evento(int id, String nombre, LocalDate fecha, LocalTime hora,
            String lugar, String descripcion, int capacidadMaxima,
            double precioEntrada, int idUsuarioCreador) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.capacidadMaxima = capacidadMaxima;
        this.precioEntrada = precioEntrada;
        this.idUsuarioCreador = idUsuarioCreador;
    }

    public Evento(String nombre, LocalDate fecha, LocalTime hora, String lugar,
            String descripcion, int capacidadMaxima, double precioEntrada,
            int idUsuarioCreador) {
        this(0, nombre, fecha, hora, lugar, descripcion, capacidadMaxima,
                precioEntrada, idUsuarioCreador);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public String getLugar() { return lugar; }
    public String getDescripcion() { return descripcion; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public double getPrecioEntrada() { return precioEntrada; }
    public int getIdUsuarioCreador() { return idUsuarioCreador; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public void setPrecioEntrada(double precioEntrada) { this.precioEntrada = precioEntrada; }
    public void setIdUsuarioCreador(int idUsuarioCreador) { this.idUsuarioCreador = idUsuarioCreador; }
}
import java.util.Date;

public class Reserva {
    private int idReserva;
    private Date fecha;
    private Cliente cliente;
    private Libro libro;

    public Reserva(int idReserva, Date fecha, Cliente cliente, Libro libro) {
        this.idReserva = idReserva;
        this.fecha = fecha;
        this.cliente = cliente;
        this.libro = libro;
    }

    public int getIdReserva() { return idReserva; }
    public Date getFecha() { return fecha; }
    public Cliente getCliente() { return cliente; }
    public Libro getLibro() { return libro; }
}

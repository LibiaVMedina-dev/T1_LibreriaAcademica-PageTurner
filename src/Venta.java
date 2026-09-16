import java.util.Date;

public class Venta {
    private int idVenta;
    private Date fecha;
    private int cantidad;
    private double total;
    private Cliente cliente;
    private Libro libro;

    public Venta(int idVenta, Date fecha, int cantidad, Cliente cliente, Libro libro) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.cliente = cliente;
        this.libro = libro;
        this.total = calcularTotal();
    }

    public double calcularTotal() {
        if (libro != null) {
            return libro.getPrecio() * cantidad;
        }
        return 0.0;
    }

    public int getIdVenta() { return idVenta; }
    public Date getFecha() { return fecha; }
    public int getCantidad() { return cantidad; }
    public double getTotal() { return total; }
    public Cliente getCliente() { return cliente; }
    public Libro getLibro() { return libro; }
}

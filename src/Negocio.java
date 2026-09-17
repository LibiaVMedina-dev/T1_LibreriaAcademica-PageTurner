import java.util.ArrayList;
import java.util.Date;

public class Negocio {
    private ArrayList<Libro> listaLibros;
    private ArrayList<Cliente> listaClientes;
    private ArrayList<Venta> listaVentas;
    private ArrayList<Reserva> listaReservas;

    private int contadorVentas;
    private int contadorReservas;

    public Negocio() {
        this.listaLibros = new ArrayList<>();
        this.listaClientes = new ArrayList<>();
        this.listaVentas = new ArrayList<>();
        this.listaReservas = new ArrayList<>();
        this.contadorVentas = 1;
        this.contadorReservas = 1;
    }

    // ==========================================
    // GESTIÓN DE LIBROS (RF-01)
    // ==========================================

    public boolean registrarLibro(String isbn, String titulo, String autor, double precio, int stock) {
        if (buscarLibro(isbn) != null || precio < 0 || stock < 0) {
            return false;
        }
        listaLibros.add(new Libro(isbn, titulo, autor, precio, stock));
        return true;
    }

    public Libro buscarLibro(String isbn) {
        for (Libro l : listaLibros) {
            if (l.getIsbn().equalsIgnoreCase(isbn)) {
                return l;
            }
        }
        return null;
    }

    // ==========================================
    // GESTIÓN DE CLIENTES (RF-02)
    // ==========================================

    public boolean registrarCliente(String dni, String nombre, String correo, String telefono) {
        if (buscarCliente(dni) != null) {
            return false;
        }
        listaClientes.add(new Cliente(dni, nombre, correo, telefono));
        return true;
    }

    public Cliente buscarCliente(String dni) {
        for (Cliente c : listaClientes) {
            if (c.getDni().equalsIgnoreCase(dni)) {
                return c;
            }
        }
        return null;
    }

    // ==========================================
    // VENTAS (HU-01, RF-03)
    // ==========================================

    public String registrarVenta(String dniCliente, String isbnLibro, int cantidad) {
        // 1. Validación de cliente existente
        Cliente cliente = buscarCliente(dniCliente);
        if (cliente == null) {
            return "Error: El cliente con DNI " + dniCliente + " no se encuentra registrado en el sistema.";
        }

        // 2. Validación de libro existente
        Libro libro = buscarLibro(isbnLibro);
        if (libro == null) {
            return "Error: El libro con ISBN " + isbnLibro + " no existe en el catálogo.";
        }

        // 3. Validación de cantidad válida
        if (cantidad <= 0) {
            return "Error: La cantidad a comprar debe ser mayor a 0.";
        }

        // 4. Validación de stock (insuficiente o inexistente y recomendación de reserva):
        if (libro.getStock() < 1) {
            return "No hay stock disponible. Se recomienda realizar una reserva del libro.";
        }
        if (libro.getStock() < cantidad) {
            return "Stock insuficiente (solo hay " + libro.getStock() + " disponibles)";
        }

        // 5. Procesamiento de la venta y descuento de stock
        Venta venta = new Venta(contadorVentas++, new Date(), cantidad, cliente, libro);
        libro.actualizarStock(-cantidad);
        listaVentas.add(venta);

        return "Venta realizada con éxito (ID: " + venta.getIdVenta() + ") - Total a pagar: S/ " + venta.getTotal();
    }

    // ==========================================
    // RESERVAS (HU-02, RF-04)
    // ==========================================

    public String registrarReserva(String dniCliente, String isbnLibro) {
        // 1. Validación de cliente existente
        Cliente cliente = buscarCliente(dniCliente);
        if (cliente == null) {
            return "Error: El cliente con DNI " + dniCliente + " no se encuentra registrado en el sistema.";
        }

        // 2. Validación de libro existente
        Libro libro = buscarLibro(isbnLibro);
        if (libro == null) {
            return "Error: El libro con ISBN " + isbnLibro + " no existe en el catálogo.";
        }

        // 3. Validación de regla de negocio: solo se reserva si NO hay stock disponible
        if (libro.getStock() > 0) {
            return "El libro posee stock disponible (" + libro.getStock() + " unidades). No se puede realizar una reserva.";
        }

        // 4. Validación de duplicidad: evitar que el cliente tenga más de una reserva activa del mismo libro
        if (tieneReservaActiva(dniCliente, isbnLibro)) {
            return "Error: El cliente ya posee una reserva activa para este libro.";
        }

        // 5. Registro de la reserva
        Reserva reserva = new Reserva(contadorReservas++, new Date(), cliente, libro);
        listaReservas.add(reserva);

        return "Reserva registrada con éxito (ID: " + reserva.getIdReserva() + ") para " + cliente.getNombre() + " con fecha " + reserva.getFecha();
    }

    public boolean tieneReservaActiva(String dniCliente, String isbnLibro) {
        for (Reserva r : listaReservas) {
            if (r.getCliente().getDni().equalsIgnoreCase(dniCliente) &&
                r.getLibro().getIsbn().equalsIgnoreCase(isbnLibro)) {
                return true;
            }
        }
        return false;
    }

    // ==========================================
    // REPORTES Y FINANZAS (HU-03, RF-05)
    // ==========================================

    public int contarUnidadesVendidasPorLibro(String isbn) {
        int totalUnidades = 0;
        for (Venta v : listaVentas) {
            if (v.getLibro().getIsbn().equalsIgnoreCase(isbn)) {
                totalUnidades += v.getCantidad();
            }
        }
        return totalUnidades;
    }

    public double calcularRecaudacionPorLibro(String isbn) {
        double totalRecaudado = 0.0;
        for (Venta v : listaVentas) {
            if (v.getLibro().getIsbn().equalsIgnoreCase(isbn)) {
                totalRecaudado += v.getTotal();
            }
        }
        return totalRecaudado;
    }

    public double calcularRecaudacionTotal() {
        double totalGlobal = 0.0;
        for (Venta v : listaVentas) {
            totalGlobal += v.getTotal();
        }
        return totalGlobal;
    }

    // ==========================================
    // GETTERS DE ACCESO A LISTAS
    // ==========================================

    public ArrayList<Libro> getListaLibros() {
        return listaLibros;
    }

    public ArrayList<Cliente> getListaClientes() {
        return listaClientes;
    }

    public ArrayList<Venta> getListaVentas() {
        return listaVentas;
    }

    public ArrayList<Reserva> getListaReservas() {
        return listaReservas;
    }
}
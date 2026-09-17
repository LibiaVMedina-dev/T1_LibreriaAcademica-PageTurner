import java.util.Scanner;

/**
 * Clase principal encargada exclusivamente de la interacción con el usuario (Vista de Consola).
 * Toda la lógica del dominio, persistencia en memoria y validaciones se delegan a Negocio.
 */
public class App {
    // Instancia única del gestor del negocio
    private static Negocio negocio = new Negocio();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = 0;

        do {
            System.out.println("\n========== SISTEMA PAGE TURNER ==========");
            System.out.println("1. Registrar Libro");
            System.out.println("2. Registrar Cliente");
            System.out.println("3. Registrar Venta");
            System.out.println("4. Registrar Reservación");
            System.out.println("5. Lista Libros");
            System.out.println("6. Lista Clientes");
            System.out.println("7. Ver Historial de Ventas (con Fecha)");
            System.out.println("8. Ver Historial de Reservas (con Fecha)");
            System.out.println("9. Ver Reporte de Ventas y Recaudación");
            System.out.println("10. Salir");
            System.out.println("=========================================");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1: registrarLibro(); break;
                    case 2: registrarCliente(); break;
                    case 3: registrarVenta(); break;
                    case 4: registrarReserva(); break;
                    case 5: listarLibros(); break;
                    case 6: listarClientes(); break;
                    case 7: listarVentas(); break;
                    case 8: listarReservas(); break;
                    case 9: generarReporte(); break;
                    case 10: System.out.println("¡Gracias por usar el sistema!"); break;
                    default: System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            }
        } while (opcion != 10);
    }

    // 1. REGISTRAR LIBRO
    private static void registrarLibro() {
        System.out.println("\n--- REGISTRAR LIBRO ---");
        System.out.print("ISBN: "); String isbn = scanner.nextLine();
        System.out.print("Título: "); String titulo = scanner.nextLine();
        System.out.print("Autor: "); String autor = scanner.nextLine();
        System.out.print("Precio: S/ "); double precio = Double.parseDouble(scanner.nextLine());
        System.out.print("Stock inicial: "); int stock = Integer.parseInt(scanner.nextLine());

        boolean registrado = negocio.registrarLibro(isbn, titulo, autor, precio, stock);
        if (registrado) {
            System.out.println("¡Libro guardado con éxito!");
        } else {
            System.out.println("Error: Ya existe un libro con ese ISBN o los valores ingresados son inválidos.");
        }
    }

    // 2. REGISTRAR CLIENTE
    private static void registrarCliente() {
        System.out.println("\n--- REGISTRAR CLIENTE ---");
        System.out.print("DNI: "); String dni = scanner.nextLine();
        System.out.print("Nombre: "); String nombre = scanner.nextLine();
        System.out.print("Correo: "); String correo = scanner.nextLine();
        System.out.print("Teléfono: "); String telefono = scanner.nextLine();

        boolean registrado = negocio.registrarCliente(dni, nombre, correo, telefono);
        if (registrado) {
            System.out.println("¡Cliente guardado con éxito!");
        } else {
            System.out.println("Error: Ya existe un cliente registrado con el DNI " + dni + ".");
        }
    }

    // 3. REGISTRAR VENTA
    private static void registrarVenta() {
        System.out.println("\n--- REGISTRAR VENTA ---");
        System.out.print("Ingrese DNI del cliente: ");
        String dni = scanner.nextLine();

        System.out.print("Ingrese ISBN del libro: ");
        String isbn = scanner.nextLine();

        System.out.print("Cantidad a comprar: ");
        int cantidad = Integer.parseInt(scanner.nextLine());

        // Toda la lógica y validaciones se delegan a Negocio
        String resultado = negocio.registrarVenta(dni, isbn, cantidad);
        System.out.println(resultado);
    }

    // 4. REGISTRAR RESERVA
    private static void registrarReserva() {
        System.out.println("\n--- REGISTRAR RESERVA ---");
        System.out.print("Ingrese DNI del cliente: ");
        String dni = scanner.nextLine();

        System.out.print("Ingrese ISBN del libro: ");
        String isbn = scanner.nextLine();

        // Toda la lógica y validaciones se delegan a Negocio
        String resultado = negocio.registrarReserva(dni, isbn);
        System.out.println(resultado);
    }

    // 5. LISTAR LIBROS
    private static void listarLibros() {
        System.out.println("\n--- LISTA DE LIBROS ---");
        if (negocio.getListaLibros().isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        for (Libro l : negocio.getListaLibros()) {
            System.out.println("ISBN: " + l.getIsbn() + " | Título: " + l.getTitulo() + 
                                " | Stock: " + l.getStock() + " | Precio: S/ " + l.getPrecio());
        }
    }

    // 6. LISTAR CLIENTES
    private static void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        if (negocio.getListaClientes().isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente c : negocio.getListaClientes()) {
            System.out.println("DNI: " + c.getDni() + " | Nombre: " + c.getNombre() + 
                                " | Correo: " + c.getCorreo() + " | Teléfono: " + c.getTelefono());
        }
    }

    // 7. LISTAR VENTAS
    private static void listarVentas() {
        System.out.println("\n--- HISTORIAL DE VENTAS ---");
        if (negocio.getListaVentas().isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        for (Venta v : negocio.getListaVentas()) {
            System.out.println("ID Venta: " + v.getIdVenta() + " | Fecha: " + v.getFecha() + 
                                " | Cliente: " + v.getCliente().getNombre() + 
                                " | Libro: " + v.getLibro().getTitulo() + 
                                " | Cantidad: " + v.getCantidad() + 
                                " | Total: S/ " + v.getTotal());
        }
    }

    // 8. LISTAR RESERVAS
    private static void listarReservas() {
        System.out.println("\n--- HISTORIAL DE RESERVAS ---");
        if (negocio.getListaReservas().isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }
        for (Reserva r : negocio.getListaReservas()) {
            System.out.println("ID Reserva: " + r.getIdReserva() + " | Fecha: " + r.getFecha() + 
                                " | Cliente: " + r.getCliente().getNombre() + 
                                " | Teléfono: " + r.getCliente().getTelefono() + 
                                " | Libro: " + r.getLibro().getTitulo());
        }
    }

    // 9. REPORTE FINANCIERO Y VENTAS POR LIBRO
    private static void generarReporte() {
        System.out.println("\n========== REPORTE DE VENTAS POR LIBRO ==========");
        if (negocio.getListaLibros().isEmpty()) {
            System.out.println("No hay libros registrados en el sistema.");
            return;
        }

        for (Libro l : negocio.getListaLibros()) {
            int unidades = negocio.contarUnidadesVendidasPorLibro(l.getIsbn());
            double dinero = negocio.calcularRecaudacionPorLibro(l.getIsbn());

            System.out.println("Libro: " + l.getTitulo() + " (ISBN: " + l.getIsbn() + ")");
            System.out.println("   - Unidades vendidas: " + unidades);
            System.out.println("   - Dinero generado: S/ " + dinero);
            System.out.println("--------------------------------------------------");
        }

        System.out.println("RECAUDACIÓN TOTAL DE LA LIBRERÍA: S/ " + negocio.calcularRecaudacionTotal());
    }
}
import java.util.ArrayList; // Importamos ArrayList para crear listas dinámicas en memoria
import java.util.Date;      // Importamos Date para obtener la fecha actual del sistema
import java.util.Scanner;   // Importamos Scanner para leer la entrada de texto por consola

public class Iniciar {
    // Se declaran 'static' para poder acceder a ellas desde el método main sin instanciar
    private static ArrayList<Libro> listaLibros = new ArrayList<>();
    private static ArrayList<Cliente> listaClientes = new ArrayList<>();
    private static ArrayList<Venta> listaVentas = new ArrayList<>();
    private static ArrayList<Reserva> listaReservas = new ArrayList<>();
    
    // Objeto Scanner global para capturar los datos que el usuario ingresa por teclado
    private static Scanner scanner = new Scanner(System.in);
    
    // Contadores autocorrelativos para asignar IDs únicos a Ventas y Reservas
    private static int contadorVentas = 1;
    private static int contadorReservas = 1;

    // MÉTODO PRINCIPAL: Punto de entrada a la ejecución del programa
    public static void main(String[] args) {
        int opcion = 0; // Variable para almacenar la opción seleccionada por el usuario
        
        // Bucle do-while: Mantiene activo el menú hasta que el usuario elija la opción 7 (Salir)
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
            System.out.println( "=====================");
            System.out.print("Seleccione una opción: ");
            
            // Estructura try-catch para capturar errores si el usuario ingresa letras en lugar de números
            try {
                opcion = Integer.parseInt(scanner.nextLine()); // Convertimos el texto ingresado a número entero
                
                // Evaluamos la opción elegida para llamar a su método correspondiente
                switch (opcion) {
                    case 1: registrarLibro(); break;
                    case 2: registrarCliente(); break;
                    case 3: registrarVenta(); break;
                    case 4: registrarReserva(); break;
                    case 5: listarLibros(); break;
                    case 6: listarClientes(); break;
                    case 7: listarVentas(); break;
                    case 8: listarReservas(); break;
                    case 9: generarReporteTotal(); break;
                    case 10: System.out.println("¡Gracias por usar el sistema!"); break;
                    default: System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (Exception e) {
                // Mensaje defensivo por si ingresan texto inválido en el menú
                System.out.println("Error: Ingrese un número válido.");
            }
        } while (opcion != 10);
    }

    // MÉTODO: REGISTRAR UN NUEVO LIBRO
    private static void registrarLibro() {
        System.out.println("\n--- REGISTRAR LIBRO ---");
        System.out.print("ISBN: "); String isbn = scanner.nextLine();
        System.out.print("Título: "); String titulo = scanner.nextLine();
        System.out.print("Autor: "); String autor = scanner.nextLine();
        System.out.print("Precio: S/ "); double precio = Double.parseDouble(scanner.nextLine());
        System.out.print("Stock inicial: "); int stock = Integer.parseInt(scanner.nextLine());

        // Instanciamos el objeto Libro y lo guardamos en la lista dinámica
        listaLibros.add(new Libro(isbn, titulo, autor, precio, stock));
        System.out.println("¡Libro guardado con éxito!");
    }

    // MÉTODO: REGISTRAR UN NUEVO CLIENTE
    private static void registrarCliente() {
        System.out.println("\n--- REGISTRAR CLIENTE ---");
        System.out.print("DNI: "); String dni = scanner.nextLine();
        System.out.print("Nombre: "); String nombre = scanner.nextLine();
        System.out.print("Correo: "); String correo = scanner.nextLine();
        System.out.print("Teléfono: "); String telefono = scanner.nextLine();

        // Instanciamos el objeto Cliente y lo guardamos en la lista dinámica
        listaClientes.add(new Cliente(dni, nombre, correo, telefono));
        System.out.println("¡Cliente guardado con éxito!");
    }

    // MÉTODO: REGISTRAR UNA VENTA (VERIFICA Y DESCUENTA STOCK)
    private static void registrarVenta() {
        System.out.println("\n--- REGISTRAR VENTA ---");
        // Buscamos que existan en el sistema el cliente y el libro ingresados por teclado
        Cliente cliente = buscarCliente();
        Libro libro = buscarLibro();

        // Si alguno no existe, cancelamos la operación regresando al menú principal
        if (cliente == null || libro == null) return;

        System.out.print("Cantidad a comprar: ");
        int cantidad = Integer.parseInt(scanner.nextLine());

        // Regla de Negocio: Validamos si la cantidad solicitada no supera el stock disponible
        if (libro.getStock() >= cantidad) {
            // Se crea la venta pasando como parámetros las entidades asociadas
            Venta venta = new Venta(contadorVentas++, new Date(), cantidad, cliente, libro);
            
            // Descontamos las unidades vendidas del stock del libro
            libro.actualizarStock(-cantidad);
            
            // Guardamos la venta registrada en la lista de ventas
            listaVentas.add(venta);
            System.out.println("¡Venta realizada con éxito! Total a pagar: S/ " + venta.getTotal());
        } else {
            // Si el stock no alcanza, se sugiere realizar una reserva
            System.out.println("Stock insuficiente (" + libro.getStock() + " disponibles). Debe hacer una reserva.");
        }
    }

    // MÉTODO: REGISTRAR UNA RESERVA (CUANDO NO HAY STOCK)
    private static void registrarReserva() {
    System.out.println("\n--- REGISTRAR RESERVA (SIN STOCK) ---");
    Libro libro = buscarLibro();

    if (libro == null) return;

    //Si hay stock disponible, bloquea la reserva
    if (libro.getStock() > 0) {
        System.out.println("El libro posee stock disponible (" + libro.getStock() + " unidades). Debe procesarse como Venta.");
        return;
    }

    System.out.print("Ingrese DNI del estudiante: ");
    String dniEstudiante = scanner.nextLine();

    //Validar si el estudiante ya tiene una reserva activa para ESTE libro
    for (Reserva r : listaReservas) {
        if (r.getCliente().getDni().equalsIgnoreCase(dniEstudiante) && 
            r.getLibro().getIsbn().equals(libro.getIsbn())) {
            System.out.println("El cliente ya posee una reserva activa para este libro.");
            return;
        }
    }

    System.out.print("Ingrese el nombre del estudiante: ");
    String nombreEstudiante = scanner.nextLine();
    
    System.out.print("Ingrese el teléfono del estudiante: ");
    String telefonoEstudiante = scanner.nextLine();

    //Crear la reserva con fecha actual
    Cliente estudiante = new Cliente(dniEstudiante, nombreEstudiante, telefonoEstudiante);
    listaClientes.add(estudiante);

    Reserva reserva = new Reserva(contadorReservas++, new Date(), estudiante, libro);
    listaReservas.add(reserva);

    System.out.println("\n¡RESERVA REGISTRADA CON ÉXITO!");
    System.out.println("Se registró la reserva para " + estudiante.getNombre() + " (Tel: " + estudiante.getTelefono() + ")");
    }

    // MÉTODO: MOSTRAR TODOS LOS LIBROS REGISTRADOS
    private static void listarLibros() {
        System.out.println("\n--- LISTA DE LIBROS ---");
        if (listaLibros.isEmpty()) System.out.println("No hay libros registrados.");
        
        // Recorremos la lista elemento por elemento imprimiendo sus datos mediante getters
        for (Libro l : listaLibros) {
            System.out.println("ISBN: " + l.getIsbn() + " | Título: " + l.getTitulo() + " | Stock: " + l.getStock() + " | Precio: S/ " + l.getPrecio());
        }
    }

    // MÉTODO: MOSTRAR TODOS LOS CLIENTES REGISTRADOS
    private static void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        if (listaClientes.isEmpty()) System.out.println("No hay clientes registrados.");
        
        for (Cliente c : listaClientes) {   
            if ("Sin correo (Reserva)".equals(c.getCorreo())) {
            System.out.println("DNI: " + c.getDni() + " | Nombre: " + c.getNombre() + " | Teléfono: " + c.getTelefono() + " | Estado: EN ESPERA (RESERVA)");
            } else {
            System.out.println("DNI: " + c.getDni() + " | Nombre: " + c.getNombre() + " | Correo: " + c.getCorreo() + " | Teléfono: " + c.getTelefono());
            }
        }
    }

    // MÉTODO DE BÚSQUEDA DE CLIENTE POR DNI (RETORNA EL OBJETO CLIENTE O NULL)
    private static Cliente buscarCliente() {
        System.out.print("Ingrese DNI del cliente: ");
        String dni = scanner.nextLine();
        
        // Recorremos la lista buscando coincidencia exacta con el DNI ingresado
        for (Cliente c : listaClientes) {
            if (c.getDni().equals(dni)) return c; // Devuelve la instancia encontrada
        }
        System.out.println("Cliente no encontrado.");
        return null; // Si no existe, retorna nulo
    }

    // MÉTODO DE BÚSQUEDA DE LIBRO POR ISBN (RETORNA EL OBJETO LIBRO O NULL)
    private static Libro buscarLibro() {
        System.out.print("Ingrese ISBN del libro: ");
        String isbn = scanner.nextLine();
        
        for (Libro l : listaLibros) {
            if (l.getIsbn().equals(isbn)) return l; // Devuelve la instancia encontrada
        }
        System.out.println("Libro no encontrado.");
        return null;
    }

    // MÉTODO: REPORTE DE VENTAS Y RECAUDACIÓN POR LIBRO (REQUISITO 6)
    private static void generarReporteTotal() {
        System.out.println("\n========== REPORTE DE VENTAS POR LIBRO ==========");
        if (listaLibros.isEmpty()) {
            System.out.println("No hay libros registrados en el sistema.");
            return;
        }

        double recaudacionTotalSistema = 0.0;

        for (Libro l : listaLibros) {
            int totalUnidadesVendidas = 0;
            double dineroGeneradoLibro = 0.0;

            // Recorremos las ventas para calcular totales por libro
            for (Venta v : listaVentas) {
                if (v.getLibro().getIsbn().equals(l.getIsbn())) {
                    totalUnidadesVendidas += v.getCantidad();
                    dineroGeneradoLibro += v.getTotal();
                }
            }
            recaudacionTotalSistema += dineroGeneradoLibro;
            System.out.println("Libro: " + l.getTitulo() + " (ISBN: " + l.getIsbn() + ")");
            System.out.println("   - Unidades vendidas: " + totalUnidadesVendidas);
            System.out.println("   - Dinero generado: S/ " + dineroGeneradoLibro);
            System.out.println("--------------------------------------------------");
        }

        System.out.println("RECAUDACIÓN TOTAL DE LA LIBRERÍA: S/ " + recaudacionTotalSistema);
    }
  
    // MÉTODO: MOSTRAR HISTORIAL DE VENTAS CON FECHA 
    private static void listarVentas() {
        System.out.println("\n--- HISTORIAL DE VENTAS ---");
        if (listaVentas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        for (Venta v : listaVentas) {
            System.out.println("ID Venta: " + v.getIdVenta() + " | Fecha: " + v.getFecha() + " | Cliente: " + v.getCliente().getNombre() + 
                               " | Libro: " + v.getLibro().getTitulo() + " | Cantidad: " + v.getCantidad() + " | Total: S/ " + v.getTotal());
        }
    }

    // MÉTODO: MOSTRAR HISTORIAL DE RESERVAS CON FECHA Y CONTACTO 
    private static void listarReservas() {
        System.out.println("\n--- HISTORIAL DE RESERVAS ---");
        if (listaReservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }
        for (Reserva r : listaReservas) {
            System.out.println("ID Reserva: " + r.getIdReserva() + " | Fecha: " + r.getFecha() + " | Estudiante: " + r.getCliente().getNombre() + 
                               " | Teléfono: " + r.getCliente().getTelefono() + " | Libro Reservado: " + r.getLibro().getTitulo());
        }
    }
}
   
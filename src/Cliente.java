public class Cliente {
    private String dni;
    private String nombre;
    private String correo;
    private String telefono;

    // CONSTRUCTOR 1: Para clientes (4 parámetros - CON correo)
    public Cliente(String dni, String nombre, String correo, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    // CONSTRUCTOR 2: Para reservas (3 parámetros - SIN correo)
    public Cliente(String dni, String nombre, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.correo = "Sin correo (Reserva)";
        this.telefono = telefono;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
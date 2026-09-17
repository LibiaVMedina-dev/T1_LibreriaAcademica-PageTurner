# Sistema de Gestión de Librería Académica "Page Turner"
> **Evaluación T1 — Programación Orientada a Objetos (POO)**  
> **Universidad Privada del Norte (UPN)** — Semestre 2026-2

---

## 📌 Información Académica y Equipo de Desarrollo

* **Institución:** Universidad Privada del Norte (UPN)
* **Curso:** Programación Orientada a Objetos
* **Docente:** Victor Alfredo Muguerza Capristan
* **Grupo:** Grupo 7

### Integrantes
| N° | Apellidos y Nombres | Código de Estudiante |
|:--:|:--------------------|:--------------------:|
| 1 | Salas Chuco, David Samuel | `n00452118` |
| 2 | Tacuri Rosales, Cristhian Arturo | `n00569399` |
| 3 | Tomas Espinoza, Diogo Enilson | `n00476546` |
| 4 | Vasquez Medina, Libia Elena | `n00156923` |

---

## 📖 Descripción del Caso de Negocio

La librería académica **"Page Turner"**, dirigida por la señora Carmen Vidal y ubicada dentro del campus universitario en Lima, atiende a estudiantes, docentes e investigadores que buscan adquirir o reservar material bibliográfico para sus cursos. 

Históricamente, la gestión se llevaba a cabo de forma manual mediante cuadernos de apuntes y hojas de Excel desconectadas, lo que generaba:
* Inconsistencias entre el stock físico y las ventas registradas.
* Extravío recurrente de los datos de contacto de estudiantes en lista de espera.
* Imposibilidad de medir el rendimiento financiero semanal o identificar los libros de mayor demanda.

### Solución Implementada
El sistema desarrollado en **Java** automatiza la operación de la librería en consola bajo el paradigma de la **Programación Orientada a Objetos (POO)**. Facilita la administración del catálogo de libros, el registro de clientes, el procesamiento de ventas con descuento en tiempo real, el control estricto de reservas para títulos agotados y la emisión de reportes financieros consolidados.

---

## 🏛️ Fundamentos de Programación Orientada a Objetos (POO)

El proyecto refleja la aplicación directa de los pilares fundamentales de la POO:

1. **Encapsulamiento y Ocultamiento de Información:**
   * Todos los atributos de las entidades del dominio (`Libro`, `Cliente`, `Venta`, `Reserva`) han sido declarados con modificador de acceso `private`.
   * El acceso a los datos y su mutación se realiza de manera controlada a través de métodos de acceso (*getters*) y de negocio (ej. `actualizarStock(int cant)`).
2. **Abstracción y Cohesión:**
   * Cada clase modela una entidad concreta del negocio con una responsabilidad única y delimitada:
     * `Libro`: Gestiona la información bibliográfica y el control de inventario disponible.
     * `Cliente`: Almacena la información de contacto e identificación del comprador/solicitante.
     * `Venta`: Registra el hecho comercial, asociando cliente, libro, fecha y cálculo dinámico del total.
     * `Reserva`: Gestiona las solicitudes en cola para libros con stock en cero.
3. **Sobrecarga de Constructores (Overloading):**
   * En la clase `Cliente` se implementan constructores sobrecargados: uno con datos completos (incluyendo correo) y otro simplificado para reservas rápidas en lista de espera.
4. **Relaciones Estructurales:**
   * **Asociación y Navegabilidad:** Las clases `Venta` y `Reserva` contienen referencias directas hacia objetos `Cliente` y `Libro`, permitiendo una comunicación fluida entre entidades sin acoplamiento rígido.

---

## 📊 Diagrama de Clases UML y Modelo de Datos

```mermaid
classDiagram
    class Libro {
        -String isbn
        -String titulo
        -String autor
        -double precio
        -int stock
        +getIsbn() String
        +getStock() int
        +actualizarStock(cant: int) void
    }

    class Cliente {
        -String dni
        -String nombre
        -String correo
        -String telefono
        +getDni() String
        +getNombre() String
        +getTelefono() String
    }

    class Venta {
        -int idVenta
        -Date fecha
        -int cantidad
        -double total
        -Cliente cliente
        -Libro libro
        +calcularTotal() double
        +getIdVenta() int
    }

    class Reserva {
        -int idReserva
        -Date fecha
        -Cliente cliente
        -Libro libro
        +getIdReserva() int
    }

    Libro "1" <-- "0..*" Venta : referencia a
    Cliente "1" <-- "0..*" Venta : realizada por
    Libro "1" <-- "0..*" Reserva : reservado en
    Cliente "1" <-- "0..*" Reserva : solicitado por
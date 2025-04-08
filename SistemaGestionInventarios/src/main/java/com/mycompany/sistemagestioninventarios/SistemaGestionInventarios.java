/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemagestioninventarios;

/**
 *
 * @author DELL
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaGestionInventarios {

    // Clase Producto (Abstracta)
    abstract static class Producto {
        protected String nombre;
        protected String codigo;
        protected double precio;
        protected int stock;
        protected Categoria categoria;

        public Producto(String nombre, String codigo, double precio, int stock, Categoria categoria) {
            this.nombre = nombre;
            this.codigo = codigo;
            this.precio = precio;
            this.stock = stock;
            this.categoria = categoria;
        }

        public void mostrarDetalles() {
            System.out.println("Producto: " + nombre);
            System.out.println("Código: " + codigo);
            System.out.println("Precio: $" + precio);
            System.out.println("Stock: " + stock);
            System.out.println("Categoría: " + categoria.getNombre());
        }

        public void actualizarStock(int cantidad) {
            this.stock += cantidad;
        }

        public void reducirStock(int cantidad) throws Exception {
            if (cantidad > this.stock) {
                throw new Exception("No hay suficiente stock.");
            }
            this.stock -= cantidad;
        }

        public int getStock() {
            return stock;
        }

        public String getCodigo() {
            return codigo;
        }
    }

    // Clase ProductoRegular (Hereda de Producto)
    static class ProductoRegular extends Producto {

        public ProductoRegular(String nombre, String codigo, double precio, int stock, Categoria categoria) {
            super(nombre, codigo, precio, stock, categoria);
        }

        @Override
        public void mostrarDetalles() {
            super.mostrarDetalles();
            System.out.println("Tipo: Regular");
        }
    }

    // Clase ProductoRefrigerado (Hereda de Producto)
    static class ProductoRefrigerado extends Producto {
        private final double temperaturaRecomendada;

        public ProductoRefrigerado(String nombre, String codigo, double precio, int stock, Categoria categoria, double temperaturaRecomendada) {
            super(nombre, codigo, precio, stock, categoria);
            this.temperaturaRecomendada = temperaturaRecomendada;
        }

        @Override
        public void mostrarDetalles() {
            super.mostrarDetalles();
            System.out.println("Tipo: Refrigerado");
            System.out.println("Temperatura Recomendada: " + temperaturaRecomendada + "°C");
        }
    }

    // Clase Categoria
    static class Categoria {
        private final String nombre;

        public Categoria(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }

    // Clase Inventario
    static class Inventario {
        private final List<Producto> productos;

        public Inventario() {
            productos = new ArrayList<>();
        }

        public void agregarProducto(Producto producto) throws Exception {
            if (buscarProducto(producto.getCodigo()) != null) {
                throw new Exception("Producto ya existe en el inventario.");
            }
            productos.add(producto);
        }

        public Producto buscarProducto(String codigo) {
            for (Producto producto : productos) {
                if (producto.getCodigo().equals(codigo)) {
                    return producto;
                }
            }
            return null;
        }

        public void eliminarProducto(String codigo) throws Exception {
            Producto producto = buscarProducto(codigo);
            if (producto == null) {
                throw new Exception("Producto no encontrado.");
            }
            productos.remove(producto);
        }

        public void listarProductos() {
            for (Producto producto : productos) {
                producto.mostrarDetalles();
                System.out.println("--------------------------");
            }
        }

        public List<Producto> getProductos() {
            return productos;
        }
    }

    // Clase SistemaDeAlertas
    static class SistemaDeAlertas {
        private final Inventario inventario;
        private final int umbralStockBajo;

        public SistemaDeAlertas(Inventario inventario, int umbralStockBajo) {
            this.inventario = inventario;
            this.umbralStockBajo = umbralStockBajo;
        }

        public void verificarAlertas() {
            boolean alerta = false;
            for (Producto producto : inventario.getProductos()) {
                if (producto.getStock() <= umbralStockBajo) {
                    System.out.println("¡Alerta! El producto " + producto.getCodigo() + " está bajo de stock.");
                    alerta = true;
                }
            }
            if (!alerta) {
                System.out.println("No hay productos con stock bajo.");
            }
        }
    }

    // Clase Principal - Menú interactivo
    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventario inventario = new Inventario();
    private static final SistemaDeAlertas sistemaDeAlertas = new SistemaDeAlertas(inventario, 5); // Umbral de stock bajo

    public static void main(String[] args) {
        while (true) {
            mostrarMenu();
            int opcion = obtenerOpcion();

            switch (opcion) {
                case 1 -> agregarProducto();
                case 2 -> eliminarProducto();
                case 3 -> buscarProducto();
                case 4 -> listarProductos();
                case 5 -> sistemaDeAlertas.verificarAlertas();
                case 6 -> {
                    System.out.println("Saliendo del sistema...");
                    return;
                }
                default -> System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n*** Sistema de Gestión de Inventarios ***");
        System.out.println("1. Agregar Producto");
        System.out.println("2. Eliminar Producto");
        System.out.println("3. Buscar Producto");
        System.out.println("4. Listar Productos");
        System.out.println("5. Verificar Alertas de Stock Bajo");
        System.out.println("6. Salir");
    }

    private static int obtenerOpcion() {
        System.out.print("Selecciona una opción: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private static void agregarProducto() {
        try {
            System.out.print("Nombre del Producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Código del Producto: ");
            String codigo = scanner.nextLine();
            System.out.print("Precio del Producto: ");
            double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock del Producto: ");
            int stock = Integer.parseInt(scanner.nextLine());
            System.out.print("Categoría del Producto: ");
            String categoriaNombre = scanner.nextLine();
            Categoria categoria = new Categoria(categoriaNombre);

            System.out.print("¿Es un producto refrigerado? (s/n): ");
            char tipo = scanner.nextLine().charAt(0);

            Producto producto;
            if (tipo == 's' || tipo == 'S') {
                System.out.print("Temperatura Recomendada: ");
                double temperaturaRecomendada = Double.parseDouble(scanner.nextLine());
                producto = new ProductoRefrigerado(nombre, codigo, precio, stock, categoria, temperaturaRecomendada);
            } else {
                producto = new ProductoRegular(nombre, codigo, precio, stock, categoria);
            }

            inventario.agregarProducto(producto);
            System.out.println("Producto agregado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        try {
            System.out.print("Código del Producto a Eliminar: ");
            String codigo = scanner.nextLine();
            inventario.eliminarProducto(codigo);
            System.out.println("Producto eliminado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void buscarProducto() {
        System.out.print("Código del Producto a Buscar: ");
        String codigo = scanner.nextLine();
        Producto producto = inventario.buscarProducto(codigo);
        if (producto != null) {
            producto.mostrarDetalles();
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void listarProductos() {
        inventario.listarProductos();
    }
}
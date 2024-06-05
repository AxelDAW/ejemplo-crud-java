package appcrud;

import java.sql.*;
import java.util.Scanner;

public class AppCrud {

    static Connection conexion;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        //Establecer la conexion
        String url = "jdbc:postgresql://localhost:5432/Lavadero";
        String user = "postgres";
        String pass = "p123";

        try {
            //ESTABLECER LA CONEXION
            conexion = DriverManager.getConnection(url, user, pass);
            
            System.out.println("*******CRUD******");
            System.out.println("¿Qué operacion desea realizar?");
            System.out.println("-Insertar un registro(1)\n"
                    + "-Consultar un registro particular(2)\n"
                    + "-Mostrar todos los registros de la tabla Cubiculo(3)\n"
                    + "-Actualizar un registro(4)\n"
                    + "-Eliminar un registro(5)\n"
                    + "-Consultar la tabla vehiculo de manera ordenada(6)\n"
                    + "-Consultar tablas combinadas(7)");
            System.out.println("Ingrese la opcion: ");
            int opcion = sc.nextInt();
            switch(opcion){
                case(1) -> {
                    System.out.println("El resgitro será insertado en la tabla 'cubiculo'");
                    System.out.print("Ingrese el id: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("");
                    System.out.print("Ingrese el nombre: ");
                    String nombre = sc.nextLine();
                    System.out.println("");
                    System.out.print("Ingrese el estado: ");
                    String estado = sc.next();
                    System.out.println("");
                    System.out.print("Ingrese el area: ");
                    String area = sc.next();
                    System.out.println("");
                    insertarRegistro(id, nombre, estado, area);
                }
                case(2) -> {
                    System.out.println("La consulta se hará en la tabla 'cubiculo'");
                    System.out.println("Ingrese el id del registro: ");
                    int id_cubiculo = sc.nextInt();
                    buscarPorId(id_cubiculo);
                }
                case(3) -> {
                    System.out.println("*Solo se muestran los primeros 7*");
                    traerTodo();
                }
                case(4) -> {
                    System.out.print("Ingrese el ID del registro que desea modificar: ");
                    int cubiculo_id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Ingrese el nombre nuevo: ");
                    String newNombre = sc.nextLine();
                    System.out.println("Ingrese el estado nuevo: ");
                    String newEstado = sc.nextLine();
                    System.out.println("Ingrese el nuevo area: ");
                    String newArea = sc.nextLine();
                    actualizarRegistro(cubiculo_id, newNombre, newEstado, newArea);
                }
                case(5) -> {
                    System.out.println("Ingrese el id del registro que desea eliminar: ");
                    int codigo = sc.nextInt();
                    eliminarRegistro(codigo);
                }
                case(6) -> consultarEnOrdenAscendente();
                case(7) -> consultaInnerJoin();
                default -> System.out.println("Opción incorrecta");
            }
            
            conexion.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }

    private static void insertarRegistro(int id, String nombre, String estado, String area) throws SQLException{
        PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO cubiculo VALUES (?,?,?,?)");

        sentencia.setInt(1, id);
        sentencia.setString(2, nombre);
        sentencia.setString(3, estado.toUpperCase());
        sentencia.setString(4, area);
        sentencia.executeUpdate();
        sentencia.close();
    }
    private static void traerTodo() throws SQLException{
        Statement sentencia = conexion.createStatement();
        ResultSet rs = sentencia.executeQuery("SELECT * FROM cubiculo LIMIT 7");
        
        while(rs.next()){
            int id  = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String estado = rs.getString("estado");
            String area = rs.getString("area");
            System.out.println("ID: " + id + ", NOMBRE:" + nombre + ", ESTADO: " + estado + ", AREA: " + area);
        }
        sentencia.close();
    }
    private static void buscarPorId(int codigo) throws SQLException{
        PreparedStatement sentencia = conexion.prepareStatement("SELECT * FROM cubiculo WHERE id = ?");
        sentencia.setInt(1, codigo);
        ResultSet rs = sentencia.executeQuery();
        
        while (rs.next()) {
            int id  = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String estado = rs.getString("estado");
            String area = rs.getString("area");
            System.out.println("ID: " + id + ", NOMBRE:" + nombre + ", ESTADO: " + estado + ", AREA: " + area);
        }
        sentencia.close();
    }
    private static void actualizarRegistro(int id, String nuevoNombre, String nuevoEstado, String nuevaArea) throws SQLException7
    {
        PreparedStatement sentencia = conexion.prepareStatement("UPDATE cubiculo SET nombre = ?, estado = ?, area = ? WHERE id = ?");
        sentencia.setInt(4, id);
        sentencia.setString(1, nuevoNombre);
        sentencia.setString(2, nuevoEstado.toUpperCase());
        sentencia.setString(3, nuevaArea);
        
        sentencia.executeUpdate();
        sentencia.close();
        
    }
    private static void eliminarRegistro(int id) throws SQLException{
        PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM cubiculo WHERE id = ?");
        sentencia.setInt(1, id);
        sentencia.executeUpdate();
        sentencia.close();
    }
    private static void consultarEnOrdenAscendente() throws SQLException{
        Statement sentencia = conexion.createStatement();
        ResultSet rs = sentencia.executeQuery("SELECT * FROM vehiculo ORDER BY color ASC");
        
        while(rs.next()){
            String matricula  = rs.getString("matricula");
            String planta = rs.getString("planta");
            String marca = rs.getString("marca");
            String tipo = rs.getString("tipo");
            String color = rs.getString("color");
            int id_cliente = rs.getInt("cliente");
            System.out.println("MATRICULA: " + matricula + ", PLANTA:" + planta + ", MARCA: " + marca + ", TIPO: " + tipo
            + ", COLOR: " + color + ", ID_CLIENTE:" + id_cliente);
        }
        sentencia.close();
    }
    private static void consultaInnerJoin() throws SQLException{
        Statement sentencia = conexion.createStatement();
        ResultSet rs = sentencia.executeQuery("SELECT * FROM vehiculo INNER JOIN cliente ON vehiculo.cliente = cliente.num_identificacion");
        
        while(rs.next()){
            String matricula  = rs.getString("matricula");
            String planta = rs.getString("planta");
            String marca = rs.getString("marca");
            String tipo = rs.getString("tipo");
            String color = rs.getString("color");
            int id_cliente = rs.getInt("cliente");
            int cliente = rs.getInt("num_identificacion");
            String nombre_cliente = rs.getString("nombres");
            System.out.println("MATRICULA: " + matricula + ", PLANTA:" + planta + ", MARCA: " + marca + ", TIPO: " + tipo
            + ", COLOR: " + color + ", ID_CLIENTE:" + id_cliente + ", NUM_IDENTIFICACION: " + cliente + ", NOMBRES: " + nombre_cliente);
        }
        sentencia.close();
    }

}

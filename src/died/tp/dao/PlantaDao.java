package died.tp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import died.tp.dominio.InsumoGeneral;
import died.tp.dominio.InsumoLiquido;
import died.tp.dominio.Planta;
import died.tp.dominio.Stock;

public class PlantaDao {

	//Atributos
	private static String insert = "insert into planta (nombrePlanta) values (?)";
	private static String delete = "delete FROM camion WHERE id_camion = ?";
	
	
	//Métodos
	/* Guarda la planta en la base de datos
	 */
	public void altaPlanta(String c) {
		Connection con = null;
		PreparedStatement pr = null;
		try {
			con = Conexion.conectar();
			pr = con.prepareStatement(insert);
			pr.setString(1, c);
			pr.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			pr.close();
			con.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* Elimina la planta de la base de datos
	 */
	public void eliminarPlanta(Integer id) {
		Connection con = null;
		con = Conexion.conectar();
		PreparedStatement pr = null;
		try {
			pr = con.prepareStatement(delete);
			pr.setInt(1, id);
			pr.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error al eliminar");
			e.printStackTrace();
		}
	}
}

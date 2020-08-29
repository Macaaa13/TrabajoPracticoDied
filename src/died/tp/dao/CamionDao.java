package died.tp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import died.tp.dominio.Camion;

public class CamionDao {

	//Atributos
 	private static final String selectAll = "SELECT * FROM CAMION";
	private static final String update = "UPDATE CAMION SET patente = ?, modelo = ?, marca = ?, kmRecorridos = ?, costoKM = ?,"
			+ " costoHora = ?, fechaCompra = ? where id_camion = ?";
	private static final String delete = "delete FROM camion WHERE id_camion = ?";
	private static final String insert = " INSERT INTO camion (patente,modelo,marca,kmRecorridos,costoHora,costoKM,fechaCompra)"
			+ "VALUES (?,?,?,?,?,?,?)";
	private static final String search = "SELECT * FROM CAMION WHERE ";
	
	
	//M�todos
	/* Si el cami�n no tiene un id asignado, significa que debe crearse.
	 * De lo contrario, debe buscarse el cami�n por el id y actualizar sus datos
	 */
	public void altaActualizacionCamion(Camion c) {
		Connection con = null;
		PreparedStatement pr = null;
		try {
			con = Conexion.conectar();
			if(c.getId() != null && c.getId() > 0) {
			pr = con.prepareStatement(update);
			pr.setString(1, c.getPatente());
			pr.setString(2, c.getModelo());
			pr.setString(3, c.getMarca());
			pr.setDouble(4, c.getKmRecorridos());
			pr.setDouble(5, c.getCostoHora());
			pr.setDouble(6, c.getCostoKM());
			java.sql.Date fechasql = new java.sql.Date(c.getFechaCasteada().getTime());
			pr.setDate(7, fechasql);
			pr.setInt(8, c.getId());
			}
			else {
				pr = con.prepareStatement(insert);
				pr.setString(1, c.getPatente());
				pr.setString(2, c.getModelo());
				pr.setString(3, c.getMarca());
				pr.setDouble(4, c.getKmRecorridos());
				pr.setDouble(5, c.getCostoHora());
				pr.setDouble(6, c.getCostoKM());
				pr.setDate(7, new java.sql.Date(c.getFechaCasteada().getTime()));
			}
			pr.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			con.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* Elimina el cami�n cuyo id coincide con el pasado por par�metro
	 */
	public void eliminarCamion(Integer id) {
		Connection con = null;
		con = Conexion.conectar();
		PreparedStatement pr = null;
		try {
			pr = con.prepareStatement(delete);
			pr.setInt(1, id);
			pr.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* Dependiendo de si el string pasado por par�metro es nulo o no, se buscan todos los camiones de la base de datos o
	 * se traen aquellos que cumpran los criterios indicados por el string
	 */
	public List<Camion> buscarCamiones(String s) {
		List<Camion> lista = new ArrayList<Camion>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = Conexion.getConexion();
			if(!s.isEmpty()) {
				pstmt= conn.prepareStatement(search+s);
				rs = pstmt.executeQuery();
			}
			else {
				pstmt= conn.prepareStatement(selectAll);
				rs = pstmt.executeQuery();
			}
			while(rs.next()) {
				Camion c = new Camion();
				c.setId(rs.getInt("id_camion"));
				c.setMarca(rs.getString("marca"));
				c.setModelo(rs.getString("modelo"));
				c.setPatente(rs.getString("patente"));
				c.setKmRecorridos(rs.getDouble("kmRecorridos"));
				c.setCostoHora(rs.getDouble("costoHora"));
				c.setCostoKM(rs.getDouble("costoKM"));
				c.setFechaCompra(rs.getDate("fechaCompra").toLocalDate());
				lista.add(c);
			}
						
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}	
		return lista;	
	}
	
	
}

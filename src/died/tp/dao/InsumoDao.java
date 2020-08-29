package died.tp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import died.tp.dominio.Insumo;
import died.tp.dominio.InsumoGeneral;
import died.tp.dominio.InsumoLiquido;

public class InsumoDao {

	//Atributos
	private static final String selectAll = "SELECT * FROM Insumo";
	private static final String update = "UPDATE Insumo SET unidadMedida = ?, costoUnidad = ?, descripcion = ? where id_insumo = ?";
	private static final String delete = "delete FROM Insumo WHERE id_insumo = ?";
	private static final String insert = " INSERT INTO Insumo (unidadMedida,costoUnidad,descripcion) VALUES (?,?,?)";
	private static final String search = "SELECT * FROM INSUMO WHERE ";
	

	//M�todos
	/* Si el insumo no tiene un id asignado, significa que debe crearse.
	 * De lo contrario, debe buscarse el insumo por el id y actualizar sus datos
	 */
	public void altaActualizacionInsumo(Insumo i) {
		Connection con = null;
		PreparedStatement pr = null;
		try {
			con = Conexion.conectar();
			if(i.getId() != null && i.getId() > 0) {
			pr = con.prepareStatement(update);
			pr.setString(1, i.getuMedida());
			pr.setDouble(2, i.getCosto());
			pr.setString(3, i.getNombre());
			pr.setInt(4, i.getId());
			pr.executeUpdate();
			}
			else {
				pr = con.prepareStatement(insert);
				pr.setString(1, i.getuMedida());
				pr.setDouble(2, i.getCosto());
				pr.setString(3, i.getNombre());
				pr.executeUpdate();
				this.setId(i);
			}
			if(i.esGeneral()) {
				PreparedStatement s = con.prepareStatement("UPDATE Insumo SET peso = "+i.getPesoDensidad().toString()+" WHERE id_insumo = "+i.getId().toString());
				s.executeUpdate();
				s.close();
			}
			else {
				PreparedStatement s = con.prepareStatement("UPDATE Insumo SET densidad = "+i.getPesoDensidad().toString()+" WHERE id_insumo = "+i.getId().toString());
				s.executeUpdate();
				s.close();
			}
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
	
	/* Al crear un insumo en la base de datos, se le asigna un id.
	 * Una vez creado se buscar el �ltimo m�ximo id y se lo asigna al insumo.
	 */
	public void setId(Insumo i) {
		Connection con = null;
		PreparedStatement pr = null;
		ResultSet rs = null;
		try {
			con = Conexion.getConexion();
			pr = con.prepareStatement("SELECT MAX(id_insumo) AS id FROM insumo");
			rs = pr.executeQuery();
			if(rs.next()) {
			i.setId(rs.getInt("id"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			rs.close();
			pr.close();
			con.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* Elimina el insumo cuyo id coincide con el pasado por par�metro
	 */
	public void eliminarI(Integer id) {
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
	
	/* Si el usuario hizo una b�squeda espec�fica, retorna un Map con los insumos que cumplen esos criterios
	 * De lo contrario, retorna un Map con todos los insumos existentes
	 */
	public Map<Insumo,Integer> buscarTodos(String campos) {
		Map<Insumo,Integer> lista = new HashMap<Insumo,Integer>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = Conexion.getConexion();
			if(campos.isEmpty()) {
				pstmt= conn.prepareStatement(selectAll);
				rs = pstmt.executeQuery();
			}
			else {
				pstmt = conn.prepareStatement(search+campos);
				rs = pstmt.executeQuery();
			}
			while(rs.next()) {
				Double den = rs.getDouble("densidad");
				if(!(den == 0)) {
					InsumoLiquido i = new InsumoLiquido();
					i.setId(rs.getInt("id_insumo"));
					i.setDescripcion(rs.getString("descripcion"));
					i.setDensidad(den);
					i.setuMedida(rs.getString("unidadMedida"));
					i.setCosto(rs.getInt("costoUnidad"));
					lista.put(i, this.traerStock(i.getId()));
				}
				else {
					InsumoGeneral i = new InsumoGeneral();
					i.setId(rs.getInt("id_insumo"));
					i.setDescripcion(rs.getString("descripcion"));
					i.setuMedida(rs.getString("unidadMedida"));
					i.setCosto(rs.getInt("costoUnidad"));
					i.setPeso(rs.getDouble("peso"));
					lista.put(i,this.traerStock(i.getId()));
				}
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
	
	/* Calula el stock del insumo cuyo id coincide con el pasado por par�metro
	 */
	public Integer traerStock(Integer id) {
		Integer stock = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = Conexion.getConexion();
			pstmt = conn.prepareStatement("select sum(ps.cantidad) as cant from insumo i, plantastock ps where i.id_insumo = ? and i.id_insumo = ps.id_insumo group by i.id_insumo");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				stock = rs.getInt("cant");
			}
		}catch (SQLException e) {
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
		return stock;
	}
	
}

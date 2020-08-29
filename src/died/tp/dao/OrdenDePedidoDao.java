package died.tp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import died.tp.dominio.Camion;
import died.tp.dominio.Insumo;
import died.tp.dominio.InsumoGeneral;
import died.tp.dominio.InsumoLiquido;
import died.tp.dominio.OrdenDePedido;
import died.tp.dominio.Planta;

public class OrdenDePedidoDao {

	//Atributos
	private static String insertOrdenPedido ="insert into OrdenPedido (plantaDestino,fechaEntrega,estado) values (?,?,1) ";
	private static String insertOrdenInsumos = "insert into OrdenInsumos (id_ordenPedido,id_insumo,cantidadInsumo) values (?,?,?)";
	private static String traerTodas = "select * from OrdenPedido where estado = ?";
	private static String obtenerPlanta = "select * from Planta where id_planta = ?";
	private static String cambiarEstadoOrden = "update OrdenPedido set estado = ? where id_ordenPedido = ?";
	private static String camionesNoAsignados = "select c.* from Camion c, OrdenPedido op where op.camionAsignado is null";
	
	
	//Métodos
	public void agregarOrdenPedido(OrdenDePedido op) {
		Connection con = null;
		PreparedStatement pr = null;
		PreparedStatement pr1 = null;
		PreparedStatement pr2 = null;
		ResultSet rs = null;
		try {
			con = Conexion.conectar();
			pr = con.prepareStatement(insertOrdenPedido);
			pr.setInt(1,op.getDestino().getId());
			pr.setDate(2, Date.valueOf(op.getFechaEntrega()));
			pr.execute();
			pr1 = con.prepareStatement("select max(id_ordenPedido) as id from OrdenPedido");
			rs = pr1.executeQuery();
			if(rs.next()) {
				for(int i = 0; i < op.getInsumos().size();i++) {
					pr2 = con.prepareStatement(insertOrdenInsumos);
					pr2.setInt(1, rs.getInt("id"));
					List<Insumo> insumos = op.getInsumos().keySet().stream().collect(Collectors.toList());
					pr2.setInt(2, insumos.get(i).getId());
					pr2.setInt(3, op.getInsumos().get(insumos.get(i)));
					pr2.execute();
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pr!=null)pr.close();
				if(pr1!=null)pr1.close();
				if(pr2!=null)pr2.close();
				if(con!=null)con.close();
				if(rs!=null)rs.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public List<Insumo> traerInsumosPorPlanta(Integer idPlanta) {
		Connection con = null;
		PreparedStatement pr = null;
		ResultSet rs = null;
		List<Insumo> resultado = new ArrayList<Insumo>();
		try {
			con = Conexion.getConexion();
			pr = con.prepareStatement("select i.* from PlantaStock ps, Insumo i where i.id_insumo = ps.id_insumo and ps.id_planta = ?");
			pr.setInt(1, idPlanta);
			rs = pr.executeQuery();
			while(rs.next()) {
				Double den = rs.getDouble("densidad");
				if(!(den == 0)) {
					InsumoLiquido i = new InsumoLiquido();
					i.setId(rs.getInt("id_insumo"));
					i.setDescripcion(rs.getString("descripcion"));
					i.setDensidad(den);
					i.setuMedida(rs.getString("unidadMedida"));
					i.setCosto(rs.getInt("costoUnidad"));
					resultado.add(i);
				}
				else {
					InsumoGeneral i = new InsumoGeneral();
					i.setId(rs.getInt("id_insumo"));
					i.setDescripcion(rs.getString("descripcion"));
					i.setuMedida(rs.getString("unidadMedida"));
					i.setCosto(rs.getInt("costoUnidad"));
					i.setPeso(rs.getDouble("peso"));
					resultado.add(i);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			if(pr!=null)pr.close();
			if(con!=null)con.close();
			if(rs!=null)rs.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return resultado;
	}
	

	
	public List<OrdenDePedido> traerTodas(Integer estado) {
		List<OrdenDePedido> ordenes = new ArrayList<OrdenDePedido>();
		Connection con = null;
		PreparedStatement pr = null;
		ResultSet rs = null;
		try {
			con = Conexion.getConexion();
			pr = con.prepareStatement(traerTodas);
			pr.setInt(1, estado);
			rs = pr.executeQuery();
			while(rs.next()) {
				OrdenDePedido odp = new OrdenDePedido();
				odp.setNroOrden(rs.getInt("id_ordenPedido"));
				odp.setFechaEntrega(rs.getDate("fechaEntrega").toLocalDate());
				odp.setCamionAsignado(obtenerCamionPorId(rs.getInt("camionAsignado")));
				if(estado == 2) {
					odp.setCostoEnvio(rs.getDouble("costoEnvio"));
					odp.setOrigen(obtenerPlantaPorId(rs.getInt("plantaOrigen")));
				}
				odp.setDestino(obtenerPlantaPorId(rs.getInt("plantaDestino")));
				odp.setEst(OrdenDePedido.estado.CREADA);
				odp.setInsumos(this.traerOrdenesInsumos(odp.getNroOrden()));
				ordenes.add(odp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pr!=null) pr.close();
				if(con!=null) con.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return ordenes;
		
	}
	
	public Camion obtenerCamionPorId(Integer id) {
		Connection conn = null;
		PreparedStatement consulta = null;
		ResultSet rs = null;
		Camion c = new Camion();
		try {
			conn = Conexion.getConexion();
			consulta = conn.prepareStatement("select * from camion where id_camion = ?");
			consulta.setInt(1, id);
			rs = consulta.executeQuery();
			if(rs.next()) {
				c.setId(rs.getInt("id_camion"));
				c.setMarca(rs.getString("marca"));
				c.setModelo(rs.getString("modelo"));
				c.setPatente(rs.getString("patente"));
				c.setKmRecorridos(rs.getDouble("kmRecorridos"));
				c.setCostoHora(rs.getDouble("costoHora"));
				c.setCostoKM(rs.getDouble("costoKM"));
				c.setFechaCompra(rs.getDate("fechaCompra").toLocalDate());
			}
		
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(consulta!=null) consulta.close();
				if(conn!=null) conn.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return c;
	}
	
	private Map<Insumo,Integer> traerOrdenesInsumos(Integer id) {
		Map<Insumo,Integer> insumos = new HashMap<Insumo,Integer>();
		Connection con = null;
		PreparedStatement pr = null;
		ResultSet rs = null;
		try {
			con = Conexion.getConexion();
			pr = con.prepareStatement("select i.*, oi.cantidadInsumo as cantidad from ordeninsumos oi, insumo i where i.id_insumo = oi.id_insumo and id_ordenPedido = ? ");
			pr.setInt(1, id);
			rs = pr.executeQuery();
			while(rs.next()) {
				Double den = rs.getDouble("densidad");
				if(!(den == 0)) {
					InsumoLiquido i = new InsumoLiquido();
					i.setId(rs.getInt("id_insumo"));
					i.setDescripcion(rs.getString("descripcion"));
					i.setDensidad(den);
					i.setuMedida(rs.getString("unidadMedida"));
					i.setCosto(rs.getInt("costoUnidad"));
					insumos.put(i,(rs.getInt("cantidad")));
				}
				else {
					InsumoGeneral i = new InsumoGeneral();
					i.setId(rs.getInt("id_insumo"));
					i.setDescripcion(rs.getString("descripcion"));
					i.setuMedida(rs.getString("unidadMedida"));
					i.setCosto(rs.getInt("costoUnidad"));
					i.setPeso(rs.getDouble("peso"));
					insumos.put(i,(rs.getInt("cantidad")));
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pr!=null) pr.close();
				if(con!=null) con.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return insumos;
		
		
	}

	public Planta obtenerPlantaPorId(Integer planta) {
		Connection conn = null;
		PreparedStatement consulta = null;
		ResultSet rs = null;
		Planta p = new Planta();
		try {
			conn = Conexion.getConexion();
			consulta = conn.prepareStatement(obtenerPlanta);
			consulta.setString(1, planta.toString());
			rs = consulta.executeQuery();
			if(rs.next()) {
			p.setId(rs.getInt("id_planta"));
			p.setNombrePlanta(rs.getString("nombrePlanta"));
			}
			PlantaStockDao psd = new PlantaStockDao();
			p.setStockInsumos(psd.traerTodos(p.getId()));
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(consulta!=null) consulta.close();
				if(conn!=null) conn.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return p;
		
	}

	public Integer obtenerStock(Integer idInsumo, Integer idPlanta) {
		Connection conn = null;
		PreparedStatement consulta = null;
		ResultSet rs = null;
		Integer n = 0;
		try {
			conn = Conexion.getConexion();
			consulta = conn.prepareStatement("select cantidad from plantastock where id_planta = ? and id_insumo = ?");
			consulta.setInt(1, idPlanta);
			consulta.setInt(2, idInsumo);
			rs = consulta.executeQuery();
			if(rs.next()) {
				n = rs.getInt("cantidad");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(consulta!=null) consulta.close();
				if(conn!=null) conn.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return n;
	}

	public void cambiarEstadoOrden(Integer nroOrden, Integer estado) {
		Connection conn = null;
		PreparedStatement consulta = null;
		try {
			conn = Conexion.getConexion();
			consulta = conn.prepareStatement(cambiarEstadoOrden);
			consulta.setInt(1, estado);
			consulta.setInt(2, nroOrden);
			consulta.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(consulta!=null) consulta.close();
				if(conn!=null) conn.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public PriorityQueue<Camion> traerCamiones() {
		Connection conn = null;
		PreparedStatement consulta = null;
		ResultSet rs = null;
		PriorityQueue<Camion> camionesOrdenados = new PriorityQueue<Camion>(new Comparator<Camion>() {
			@Override
			public int compare(Camion o1, Camion o2) {
				return (int) (o1.getKmRecorridos() - o2.getKmRecorridos());
			}
		});
		try {
			conn = Conexion.getConexion();
			consulta = conn.prepareStatement(camionesNoAsignados);
			rs = consulta.executeQuery();
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
				camionesOrdenados.add(c);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(consulta!=null) consulta.close();
				if(conn!=null) conn.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return camionesOrdenados;
	}

	public void procesarOrden(Camion c, OrdenDePedido orden, Integer id) {
		Connection conn = null;
		PreparedStatement pr = null;
		PreparedStatement pr2 = null;
		try {
			conn = Conexion.getConexion();
			pr = conn.prepareStatement("update camion set kmRecorridos = ? where id_camion = ?");
			pr.setDouble(1, c.getKmRecorridos());
			pr.setInt(2, c.getId());
			pr.executeUpdate();
			pr2 = conn.prepareStatement("update ordenpedido set camionAsignado = ?, costoEnvio = ?, plantaOrigen = ?, estado = 2 where id_ordenPedido = ?");
			pr2.setInt(1, c.getId());
			pr2.setDouble(2, orden.getCostoEnvio());
			pr2.setInt(3, id);
			pr2.setInt(4, orden.getNroOrden());
			pr2.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pr!=null) pr.close();
				if(pr2!=null) pr2.close();
				if(conn!=null) conn.close();				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
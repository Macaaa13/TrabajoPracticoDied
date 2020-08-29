package died.tp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import died.tp.dao.OrdenDePedidoDao;
import died.tp.dao.PlantaStockDao;
import died.tp.dao.RutaDao;
import died.tp.dominio.Camion;
import died.tp.dominio.Insumo;
import died.tp.dominio.OrdenDePedido;
import died.tp.dominio.Planta;
import died.tp.grafos.GrafoRutas;
import died.tp.grafos.Vertice;
import died.tp.jpanel.InformacionOrdenPedido.PanelInformacionOrden;
import died.tp.jpanel.InformacionOrdenPedido.PanelProcesarOrden;

public class InformacionOrdenController {

	//Atributos
	private List<OrdenDePedido> ordenes;
	private List<Planta> plantas;
	private List<List<Vertice<Planta>>> caminosDeOrden;
	private GrafoRutas grafo;
	private OrdenDePedido orden;
	private PanelInformacionOrden panelInfo;
	private OrdenDePedidoDao ordendao;
	private RutaDao rutadao;
	private List<Insumo> insumosPorOrden;
	private PanelProcesarOrden ppo;
	
	
	//Constructor
	public InformacionOrdenController(PanelInformacionOrden pio) {
		panelInfo = pio;
		ordenes = new ArrayList<OrdenDePedido>();
		ordendao = new OrdenDePedidoDao();
		plantas = new ArrayList<Planta>();
		grafo = new GrafoRutas();
		rutadao = new RutaDao();
	}


	//Métodos
	/* Retorna una lista con las ordenes existentes en la base de datos
	 */
	public List<OrdenDePedido> traerTodasOrdenes(Integer estado) {
		ordenes = ordendao.traerTodas(estado);
		if(ordenes != null) {
			return ordenes;
		}
		panelInfo.informarSituacion("No hay resultados");
		return null;
	}

	/* Retorna la orden cuyo id coincide con el pasado por parámetro
	 */
	public OrdenDePedido obtenerOrdenSeleccionada(String id) {
		for(OrdenDePedido opd: ordenes) {
			if(opd.getNroOrden().equals(Integer.valueOf(id))) {
				return opd;
			}
		}
		return null;
	}

	
	/* POR CADA PLANTA HAY QUE VERIFICAR QUE:
	 * EL STOCK CONTENGA A LOS PRODUCTOS
	 * Y QUE LA CANTIDAD DEL STOCK SEA MAYOR AL DEL PEDIDO
	 */
	public boolean controlarStock(String idOrden) {
		PlantaStockDao psd = new PlantaStockDao();
		boolean respuesta = false;
		orden = this.obtenerOrdenSeleccionada(idOrden);
		insumosPorOrden = orden.getInsumos().keySet().stream().collect(Collectors.toList());
		List<Planta> plantasBD = psd.traerListaPlantas();
		for(Planta p: plantasBD) {
			List<Insumo> insporPlanta = ordendao.traerInsumosPorPlanta(p.getId());
			if((convertir(insporPlanta)).containsAll(convertir(insumosPorOrden))) {
				Integer contadorInsumosStock = 0;
				for(Insumo i: insumosPorOrden) {
					if(orden.getInsumos().get(i) < ordendao.obtenerStock(i.getId(),p.getId())) {
						contadorInsumosStock++;
					}
				}
				if(contadorInsumosStock.equals(insumosPorOrden.size())){
					respuesta = true;
					this.plantas.add(p);
					
				}
			}
		}
		return respuesta;
	}
	
	/* Convierte una lista de insumos en una lista de strings, donde éstos son nombres de insumos
	 */
	private List<String> convertir(List<Insumo> lista) {
		List<String> array = new ArrayList<>();
		for(Insumo i: lista) {
			array.add(i.getNombre());
		}
		return array;

	}

	/* HAY QUE CAMBIAR EL ESTADO
	 * HAY QUE MODIFICAR LA TABLA
	 */
	public void cambiarEstadoOrden(Integer nroOrden, Integer estado) {
		ordendao.cambiarEstadoOrden(nroOrden,estado);
		
	}
	
	public List<Planta> getPlantas(){
		plantas.remove(orden.getDestino());
		return this.plantas;
	}
	
	// TRAE LOS CAMIONES QUE NO ESTÁN ASIGNADOS A UNA ORDEN 
	public PriorityQueue<Camion> traerCamionesNoAsig(){
		return ordendao.traerCamiones();
	}

	/* Busca entre las plantas existentes aquella planta cuyo nombre coincide con el pasado por parámetro
	 */
	private Planta obtenerPlantaSeleccionada(String nombre) {
		for(Planta p: plantas) {
			if(p.getNombrePlanta().equals(nombre)) {
				return p;
			}
		}
		return null;
	}

	/* Si hay camiones no asignados se procesa la orden
	 */
	public void procesarOrden(Integer caminoActual) {
		Camion c = traerCamionesNoAsig().poll();
		if(c != null) {
			Planta p = this.caminosDeOrden.get(caminoActual).get(0).getValor();
			Double km =  grafo.calcularKmHs(this.caminosDeOrden.get(caminoActual),"Corta");
			Double duracion = grafo.calcularKmHs(this.caminosDeOrden.get(caminoActual),"Rápida");
			c.setKmRecorridos(c.getKmRecorridos()+km);
			orden.setCostoEnvio(c.getCostoHora()*duracion +c.getCostoKM()*km);
			ordendao.procesarOrden(c,orden,p.getId());
			JOptionPane.showMessageDialog(null, "Orden procesada con éxito");
			ppo.regresar();
		}
		else {
			JOptionPane.showMessageDialog(null, "No hay camiones disponibles actualmente");
		}
	}

	/* Retorna el tamaño máximo de las listas, para saber cuántas columnas deben mostrarse en la tabla 
	 */
	public Integer getValorMaximo() {
		Integer valMax = 0;
		for(List<Vertice<Planta>> v: this.caminosDeOrden) {
			if(valMax < v.size()) {
				valMax = v.size();
			}
		}
		return valMax;
	}

	public List<List<Vertice<Planta>>> getListaPlantas() {
		return this.caminosDeOrden;
	}

	/* Si existen rutas entre las plantas correspondientes, retorna true
	 */
	public boolean buscarRutas(String tipoRuta, String planta) {
		Planta plantaOrigen = this.obtenerPlantaSeleccionada(planta);
		this.grafo.armarGrafo(rutadao.traerRutas());	
		List<List<Vertice<Planta>>> verticesPlantas = grafo.getRutaCorta(plantaOrigen, orden.getDestino(), tipoRuta, null);
		if(verticesPlantas == null) {
			JOptionPane.showMessageDialog(null, "No existen rutas posibles para las plantas seleccionadas");
			return false;
		}
		else {
			this.caminosDeOrden = verticesPlantas;
			if(tipoRuta.equals("Corta")) {
				ppo.setDistancia(grafo.calcularKmHs(caminosDeOrden.get(0), "Corta"));
				ppo.limpiarDuracion();
			}
			else {
				ppo.setDuracion(grafo.calcularKmHs(caminosDeOrden.get(0), "Rápida"));
				ppo.limpiarDistancia();
			}
			return true;
		}
		
	}
	
	//Métodos
	public void setPanelProcesarOrden(PanelProcesarOrden panel) {
		this.ppo = panel;
	}
}
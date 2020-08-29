package died.tp.controllers;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import died.tp.dao.InsumoDao;
import died.tp.dao.OrdenDePedidoDao;
import died.tp.dao.PlantaStockDao;
import died.tp.dominio.Insumo;
import died.tp.dominio.OrdenDePedido;
import died.tp.dominio.Planta;
import died.tp.jpanel.RegistrarPedido.PanelRegistrarOrden;

public class OrdenPedidoController {

	//Atributos
	Map<Insumo,Integer> insumosOrden;
	Map<Insumo,Integer> insumosAntesSeleccion;
	List<Planta> plantas;
	List<Integer> cantidades;
	PanelRegistrarOrden pro;
	PlantaStockDao psd;
	OrdenDePedidoDao opd;
	InsumoDao isd;
	Integer totalCompra;
	
	
	//Constructor
	public OrdenPedidoController(PanelRegistrarOrden pr) {
		 pro = pr;
		 insumosOrden = new HashMap<Insumo,Integer>(); 
		 plantas = new ArrayList<Planta>();
		 cantidades = new ArrayList<Integer>();
		 psd = new PlantaStockDao();
		 isd = new InsumoDao();
		 opd = new OrdenDePedidoDao();
		 totalCompra = 0;
	}

	
	//Métodos
	/* Retorna todas las plantas existentes
	 */
	public List<String> traerPlantas() {
		return psd.traerPlantas();
	}

	/* Retorna un Map con todos los insumos existentes
	 */
	public Map<Insumo,Integer> traerInsumos() {
		this.insumosAntesSeleccion = isd.buscarTodos("");
		return insumosAntesSeleccion;
	}

	/* Si el insumo ya existe, se actualiza el valor. 
	 * De lo contrario, lo añade
	 */
	public Insumo nuevoInsumo(Integer fila, Integer cantidad) {
		Insumo i = insumosAntesSeleccion.keySet().stream().collect(Collectors.toList()).get(fila);
		if(insumosOrden.containsKey(i)) {
			cantidad += insumosOrden.get(i);
			insumosOrden.replace(i, cantidad);
		}
		else {
			this.insumosOrden.put(i,cantidad);
		}
		return this.insumosAntesSeleccion.keySet().stream().collect(Collectors.toList()).get(fila);
	}

	public void actualizarValorCompra(Integer cantidad, Integer precio) {
		totalCompra+= cantidad * precio;
		cantidades.add(cantidad);
		pro.actualizarCompra(totalCompra);
	}

	/* Si se realizó un pedido de compra y se le asignó una fecha máxima, ésta se crea y retorna verdadero. De lo contrario, notifica
	 * el error correspondiente
	 */
	public boolean agregarOrden(Date fecha, String planta) {
		if(totalCompra!=0) {
			OrdenDePedido op = new OrdenDePedido();
			op.setDestino(psd.getPlanta(planta));
			op.setInsumos(insumosOrden);
			op.setEst(OrdenDePedido.estado.CREADA);
			if(fecha != null ) {
				op.setFechaEntrega(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				opd.agregarOrdenPedido(op);
				return true;
			}
			else {
				pro.informarSituacion("No seleccionó ninguna fecha");
				return false;
			}
		}
		else {
			pro.informarSituacion("No seleccionó ningún insumo a agregar.");
			return false;
		}
	}
	
}

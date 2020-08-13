package died.tp.controllers;


import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import died.tp.dao.PlantaStockDao;
import died.tp.dao.InsumoDao;
import died.tp.dao.OrdenDePedidoDao;
import died.tp.dominio.Insumo;
import died.tp.dominio.OrdenDePedido;
import died.tp.dominio.Planta;
import died.tp.jpanel.RegistrarPedido.PanelRegistrarOrden;

public class OrdenPedidoController {

	Map<Insumo,Integer> insumosOrden;
	Map<Insumo,Integer> insumosAntesSeleccion;
	List<Planta> plantas;
	List<Integer> cantidades;
	PanelRegistrarOrden pro;
	PlantaStockDao psd;
	OrdenDePedidoDao opd;
	InsumoDao isd;
	Integer totalCompra;
	
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
	

	public List<String> traerPlantas() {
		List<Planta> plantas = psd.traerPlantas();
		List<String> resultado = new ArrayList<String>();
		for(Planta p: plantas) {
			resultado.add(p.getNombrePlanta());
		}
		return resultado;
	}

	public Map<Insumo,Integer> traerInsumos() {
		this.insumosAntesSeleccion = isd.buscarTodos("");
		return insumosAntesSeleccion;
	}

	//AGREGO UN NUEVO MAP PARA LOS INSUMOS DE LA PRIMERA TABLA (LOS QUE SE MUESTRAN SIEMPRE)
	public Insumo nuevoInsumo(Integer fila) {
		return this.insumosAntesSeleccion.keySet().stream().collect(Collectors.toList()).get(fila);
	}

	public void actualizarValorCompra(Integer cantidad, Integer precio) {
		totalCompra+= cantidad * precio;
		cantidades.add(cantidad);
		pro.actualizarCompra(totalCompra);
	}


	public boolean agregarOrden() {
		//TODO TENGO QUE VALIDAR: SI TIENE LA PLANTA ESE INSUMO Y, SI LO TIENE, QUE TENGA EL STOCK SUFICIENTE. ESO HAY QUE HACERLO EN LA PANTALLA 2
		if(totalCompra!=0) {
			OrdenDePedido op = new OrdenDePedido();
			op.setDestino(psd.getPlanta(pro.getComboBoxPlanta().getSelectedItem().toString()));
			op.setInsumos(insumosOrden);
			op.setEst(OrdenDePedido.estado.CREADA);
			if(pro.getDateChooserFechaMaxima().getDate() != null ) {
				op.setFechaEntrega(pro.getDateChooserFechaMaxima().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
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

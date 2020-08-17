package died.tp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import died.tp.dao.InsumoDao;
import died.tp.dominio.Insumo;
import died.tp.dominio.InsumoGeneral;
import died.tp.dominio.InsumoLiquido;
import died.tp.excepciones.DatosObligatoriosException;
import died.tp.excepciones.FormatoNumeroException;
import died.tp.jpanel.insumo.PanelInsumos;



public class InsumoController {

	
	private Insumo i;
	private Map<Insumo, Integer> listaInsumos;
	private PanelInsumos pi;
	private InsumoDao ind;
	
	public InsumoController(PanelInsumos pi) {
		this.pi = pi;
		listaInsumos = new HashMap<Insumo,Integer>();
		ind = new InsumoDao();
		i = new InsumoGeneral();
	}
	


	public boolean camposVacios() {
		if(pi.getTextFieldCosto().getText().isEmpty() &&
		pi.getTextFieldUnidadMedida().getText().isEmpty() &&
		pi.getTextFieldDescripcion().getText().isEmpty() &&
		pi.getTextFieldDensidad().getText().isEmpty() &&
		pi.getTextFieldPeso().getText().isEmpty()
		) {
			return false;
		}
		return true;
	}
	
	public boolean validacionVacios() {
		if(!this.camposVacios()) {
			pi.informarSituacion("Por favor, complete todos los campos antes de continuar.");
			return false;
		}
		return true;
	}
	
	
	public boolean camposCorrectos() {
		if(!this.isDouble(pi.getTextFieldCosto().getText())) { pi.informarSituacion("Error, el campo costo debe ser un número"); return false;}
		if(pi.getTextFieldPeso().isEditable()) {
			if(!this.isDouble(pi.getTextFieldPeso().getText())) { pi.informarSituacion("Error, el campo peso debe ser un número"); return false;}
		}
		else {
			if(!this.isDouble(pi.getTextFieldDensidad().getText())) { pi.informarSituacion("Error, el campo densidad debe ser un número"); return false;}
		}
		return true;
	}

	public void actualizarModelo() {
		if(!(pi.getTextFieldDensidad().getText().isEmpty())) {
			i = new InsumoLiquido();
			i.setPesoDensidad(Double.valueOf(pi.getTextFieldDensidad().getText()));
		}
		else if(!(pi.getTextFieldPeso().getText().isEmpty())) {
			i = new InsumoGeneral();
			i.setPesoDensidad(Double.valueOf(pi.getTextFieldPeso().getText()));
		}
		if(!pi.getTextFieldCosto().getText().isEmpty()) i.setCosto(Integer.valueOf(pi.getTextFieldCosto().getText()));
		if(!pi.getTextFieldDescripcion().getText().isEmpty()) i.setDescripcion(pi.getTextFieldDescripcion().getText());
		if(!pi.getTextFieldUnidadMedida().getText().isEmpty()) i.setuMedida(pi.getTextFieldUnidadMedida().getText());
		
		
	}
	
	public void guardar(List<Integer> s) throws DatosObligatoriosException, FormatoNumeroException{
		if(validacionVacios()) {
			if(camposCorrectos()) {
				this.actualizarModelo();
				this.listaInsumos.clear();
				this.listaInsumos = ind.buscarTodos("");
				ind.altaActualizacionInsumo(i);
				pi.limpiar();
			}
		}
	}

	public void eliminarInsumo(int id) {
		ind.eliminarI(id);
		
	}

	public Map<Insumo,Integer> buscar(){
		Map<Insumo,Integer> insumos = new HashMap<Insumo,Integer>();
		if(!camposVacios()) {
			insumos = ind.buscarTodos("");
		}
		else {
			actualizarModelo();
			insumos = ind.buscarTodos(this.armarString());
		}
		if(insumos.isEmpty()) {
			pi.informarSituacion("No hay resultados");
		}
		return insumos;
			
	}
	

	public Map<Insumo,Integer> traerDatos() {
		return ind.buscarTodos("");
	}
	
	
	
	public boolean isDouble(String numero){
	    try{
	        Double.parseDouble(numero);
	        return true;
	    }catch(NumberFormatException e){
	        return false;
	    }
	}

	public void actualizar(Integer id) {
		if(validacionVacios()) {
			if(camposCorrectos()) {
				this.actualizarModelo();
				i.setId(id);
				ind.altaActualizacionInsumo(i);
				this.listaInsumos.clear();
				this.listaInsumos = (ind.buscarTodos(""));
				pi.informarSituacion("Insumo modificado exitosamente");
			}
		}
	}
	
	public String armarString() {
		String s = new String();
		if(i.getNombre()!=null) s+= "descripcion = '"+ i.getNombre() + "' AND ";
		if(i.getCosto()!= null) s+= "costoUnidad = '"+ i.getCosto().toString() + "' AND ";
		if(i.getuMedida()!= null) s+= "unidadMedida = '"+ i.getuMedida() + "' AND ";
		if(i.esGeneral() && i.getPesoDensidad()!= null) s+= "peso = '"+ i.getPesoDensidad()+ "' AND ";
		if(i.esLiquido() && i.getPesoDensidad() != null) s+= "densidad = '"+ i.getPesoDensidad()+"' AND ";
		return s.substring(0,s.length()-4);
	}

}

	



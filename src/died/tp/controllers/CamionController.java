package died.tp.controllers;

import died.tp.dao.CamionDao;
import died.tp.dominio.*;
import died.tp.excepciones.*;
import died.tp.jpanel.camion.*;

import java.time.ZoneId;
import java.util.*;
	

public class CamionController {

	//Atributos
	private Camion c;
	private List<Camion> lista;
	private PanelCamiones pc;
	private CamionDao cd;

	
	//----- Constructores -----
	// Constructor para Agregar Camión
	public CamionController(PanelCamiones pc) {
		lista = new ArrayList<Camion>();
		this.pc = pc;
		c = new Camion();
		cd = new CamionDao();
	}
	
	//Métodos
	public void guardar() throws DatosObligatoriosException, FormatoNumeroException {
		if(validacionVacios()) {
				if(camposCorrectos()) {
				this.actualizarModelo();
				cd.altaActualizacionCamion(c);
				lista.clear();
				lista.addAll(cd.buscarTodos(""));
				pc.informarSituacion("Camión guardado exitosamente");
				pc.limpiar();
			}
		}
	}
	
	public List<Camion> buscar() {
		List<Camion> camiones = null;
		if(!camposVacios()) {
			camiones = cd.buscarTodos("");
		}
		else {
			actualizarModelo();
			camiones =  cd.buscarTodos(armarString());
		}
		if(camiones.isEmpty()) {
			pc.informarSituacion("No hay resultados");
		}
		return camiones;
	}
	
	public List<Camion> traerDatos() {
		return cd.buscarTodos("");
	}

	public void eliminarCamion(Integer id) {
		cd.eliminarCamion(id);
	}
	
	
	public void actualizar(Integer id) {
		if(validacionVacios()) {
			if(camposCorrectos()) {
				this.actualizarModelo();
				c.setId(id);
				cd.altaActualizacionCamion(c);
				lista.clear();
				lista.addAll(cd.buscarTodos(""));
				pc.informarSituacion("Camión modificado exitosamente");
			}
		}
	}

	public void actualizarModelo() {
		c = new Camion();
		if(!pc.getTextFieldPatente().getText().isEmpty()) c.setPatente(pc.getTextFieldPatente().getText()); 
		if(!pc.getTextFieldMarca().getText().isEmpty()) c.setMarca(pc.getTextFieldMarca().getText());
		if(!pc.getTextFieldModelo().getText().isEmpty()) c.setModelo(pc.getTextFieldModelo().getText()); 
		if(!pc.getTextFieldKMRecorridos().getText().isEmpty())  c.setKmRecorridos(Double.valueOf(pc.getTextFieldKMRecorridos().getText())); 	
		if(!pc.getTextFieldCostoKM().getText().isEmpty()) c.setCostoKM(Double.valueOf(pc.getTextFieldCostoKM().getText())); 
		if(!pc.getTextFieldCostoHora().getText().isEmpty())  c.setCostoHora(Double.valueOf(pc.getTextFieldCostoHora().getText())); 
		if(pc.getDateChooserFechaCompra().getDate() != null) c.setFechaCompra(pc.getDateChooserFechaCompra().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}
	
	public boolean camposVacios() {
		if(pc.getTextFieldPatente().getText().isEmpty() &&   
		pc.getTextFieldMarca().getText().isEmpty()  &&
		pc.getTextFieldModelo().getText().isEmpty() &&
		pc.getTextFieldKMRecorridos().getText().isEmpty() && 
		pc.getTextFieldCostoKM().getText().isEmpty() &&
		(pc.getDateChooserFechaCompra().getDate() == null)) {
			return false;
		}
		
		return true;
	}
	
	public boolean validacionVacios() {
	
	if(!this.camposVacios()) {
		pc.informarSituacion("Por favor, complete todos los campos antes de continuar.");
		return false;
		}
		return true;
	}

	public boolean camposCorrectos() {
		if(!this.isDouble(pc.getTextFieldCostoHora().getText())){ pc.informarSituacion("Error, el campo costo Hora no es un número"); return false;}
		if(!this.isDouble(pc.getTextFieldCostoKM().getText())){ pc.informarSituacion("Error, el campo  costo por km no es un número"); return false;}
		if(!this.isDouble(pc.getTextFieldKMRecorridos().getText())){ pc.informarSituacion("Error, el campo km recorridos no es un número"); return false;}

		return true;
	}

	public boolean isDouble(String numero){
	    try{
	        Double.parseDouble(numero);
	        return true;
	    }catch(NumberFormatException e){
	        return false;
	    }
	}

	
	public String armarString() {
		String s = new String();
		if(c.getPatente()!=null) s+= "PATENTE = '" + c.getPatente() + "' AND " ;
		if(c.getModelo()!=null) s+= "MODELO = '"+ c.getModelo() + "' AND ";
		if(c.getMarca()!=null) s+= "MARCA = '" + c.getMarca() + "' AND ";
		if(c.getKmRecorridos()!=null) s+= "KMRECORRIDOS = '" + c.getKmRecorridos().toString() + "' AND ";
		if(c.getCostoKM()!=null) s+= "COSTOKM = '" + c.getCostoKM().toString() + "' AND ";
		if(c.getCostoHora()!=null) s+= "COSTOHORA = '" + c.getCostoHora().toString() + "' AND ";
		if(c.getFechaCompra()!=null) s+= "FECHACOMPRA = '" + c.getFechaCompra().toString() + "' AND ";
		return s.substring(0, s.length()-4);
	}
	
}

package died.tp.controllers;

import java.util.*;

import died.tp.dao.*;
import died.tp.dominio.Planta;
import died.tp.jpanel.planta.PanelPlantas;
import died.tp.jpanel.ruta.PanelRutas;

public class PlantaController {

	//Atributos
	 private PlantaDao pd;
	 private PanelPlantas pp;
	
	 
	 //Constructores
	 public PlantaController(){
		 pd = new PlantaDao();
	 }
	 
	 public PlantaController(PanelPlantas ps) {
		 pd = new PlantaDao();
		 pp = ps;
	 }
	 
	 public PlantaController(PanelRutas pr) {
		 pd = new PlantaDao();
	 }
	 
	 
	 //Métodos
	 /* Retorna todas las plantas existentes en la base de datos
	  */
	 public List<Planta> getPlantas() {
		 PlantaStockDao psd = new PlantaStockDao();
		return psd.traerListaPlantas();
	}
	 
	 /* Se traen de la base de datos todas las plantas existentes.
	  * Si la planta que se desea agregar no existe, es decir, si no hay ninguna otra planta con ese mismo nombre, se agrega.
	  */
	 public void agregarPlanta() {	
		String nombrePlanta = pp.getTextFieldPlanta().getText();
		if(nombrePlanta.isEmpty()) {
			pp.informarSituacion("Debe ingresar el nombre de la planta que desea agregar");
		}
		else {
			boolean existe = false;
			for(Planta p: getPlantas()) {
				if(p.getNombrePlanta().equals(nombrePlanta)) {
					existe = true;
					pp.informarSituacion("La planta ya existe");
				}
			}
			if(!existe) {
				pd.altaPlanta(nombrePlanta);
				pp.informarSituacion("Planta agregada exitosamente");
			}
		}
	}
	 
	 /* Trae de la base de datos planta cuyo nombre coincide con el pasado por parámetro.
	  * En caso de no existir dicha planta, retorna null
	  */
	 public Planta traerPlanta(String planta) {
		 for(Planta p: this.getPlantas()) {
			 if(p.getNombrePlanta().equals(planta)) {
				 return p;
			 }
		 }
		 return null;
	 }
	 
}
	 
	 

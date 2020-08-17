package died.tp.controllers;

import java.util.*;
import died.tp.dao.PlantaDao;
import died.tp.dao.PlantaStockDao;
import died.tp.dominio.Planta;
import died.tp.jpanel.planta.PanelPlantas;
import died.tp.jpanel.ruta.PanelRutas;

public class PlantaController {

	//Atributos
	 private PlantaDao pd;
	 private PanelPlantas pp;
	
	 
	 //Constructor
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
	 
	 public List<Planta> getPlantas() {
		 	PlantaStockDao psd = new PlantaStockDao();
			return psd.traerListaPlantas();
		}
	 
	 public boolean agregarPlanta() {	
		boolean b = true;
		String nombrePlanta = pp.getTextFieldPlanta().getText();
		for(Planta p: getPlantas()) {
			if(p.getNombrePlanta().equals(nombrePlanta)) {
				b = false;
			}
		}
		if(b == true) {
			pd.altaPlanta(nombrePlanta);
		}
		return b;
	}
	 
	 public Planta traerPlanta(String planta) {
		 for(Planta p: this.getPlantas()) {
			 if(p.getNombrePlanta().equals(planta)) {
				 return p;
			 }
		 }
		 return null;
	 }
	 
}
	 
	 

package died.tp.jpanel.ruta;

import javax.swing.table.DefaultTableModel;

import died.tp.dominio.Planta;
import died.tp.grafos.Vertice;

import java.util.*;


public class ModeloTablaFlujoMax extends DefaultTableModel {

	//Atributos
	private Integer maxColumnas;
	private List<List<Vertice<Planta>>> plantas;
	
	
	//Constructores
	public ModeloTablaFlujoMax() {}
	
	
	//Métodos
	public void actualizar(Integer max, List<List<Vertice<Planta>>> lista) {
		maxColumnas = max;
		plantas = lista;
	}
	
	public void agregarATabla() {
		for(int i=0; i<maxColumnas; i++) {
			this.addColumn("Planta n°: "+(i+1));
		}
		for(int j=0; j<plantas.size(); j++){
			String[] s = new String[plantas.get(j).size()];
			for(int k=0; k<plantas.get(j).size(); k++) {
				s[j] = plantas.get(j).get(k).getValor().getNombrePlanta();
			}
			this.addRow(s);
		}
	}
	

}

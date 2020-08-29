package died.tp.jpanel.InformacionOrdenPedido;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import died.tp.dominio.*;
import died.tp.grafos.Vertice;

public class ModeloTablaProcesarOrden extends DefaultTableModel {

	//Atributos
	private Integer valorMaximo;
	private List<List<Vertice<Planta>>> verticesPlantas;
	
	
	//Constructor
	public ModeloTablaProcesarOrden(Integer maximo, List<List<Vertice<Planta>>> vertices) {
		this.valorMaximo = maximo;
		this.verticesPlantas = vertices;
		this.añadirATabla();
	}
	
	
	//Métodos
	public void añadirATabla() {
		this.addColumn("Camino n°");
		for(int i = 0; i < valorMaximo ; i++) {
			this.addColumn("Planta n°"+ (i+1));
		}
		
		for(Integer i= 0; i < verticesPlantas.size(); i++) {
			String[] s = new String[verticesPlantas.get(i).size()+1];
			i++;
			s[0] = i.toString();
			i--;
			for(int j = 0, k = 1; j < verticesPlantas.get(i).size() ; j++ , k++) {
				s[k] = verticesPlantas.get(i).get(j).getValor().getNombrePlanta();
			}
			this.addRow(s);
		}
	}
	
}
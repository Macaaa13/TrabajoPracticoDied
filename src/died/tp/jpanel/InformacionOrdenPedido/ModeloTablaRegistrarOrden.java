package died.tp.jpanel.InformacionOrdenPedido;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.DefaultTableModel;

import died.tp.dominio.*;
import died.tp.grafos.Vertice;;
public class ModeloTablaRegistrarOrden extends DefaultTableModel {

	private Integer valorMaximo;
	private List<List<Vertice<Planta>>> verticesPlantas;
	
	public ModeloTablaRegistrarOrden(Integer maximo, List<List<Vertice<Planta>>> vertices) {
		this.valorMaximo = maximo;
		this.verticesPlantas = vertices;
		this.añadirATabla();
	}
	
	public void añadirATabla() {
		for(int i = 0; i < valorMaximo ; i++) {
			this.addColumn("Planta n°"+ (i+1));
		}
		
		for(int i= 0; i < verticesPlantas.size(); i++) {
			String[] s = new String[verticesPlantas.get(i).size()];
			for(int j = 0; j < verticesPlantas.get(i).size() ; j++) {
				s[j] = verticesPlantas.get(i).get(j).getValor().getNombrePlanta();
			}
			this.addRow(s);
		}
	}
	
}

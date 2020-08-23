package died.tp.jpanel.ruta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import died.tp.dominio.Planta;

public class ModeloTablaPageRank extends AbstractTableModel {

	//Atributos
	private String[] columnNames = {"Planta", "Page Rank"};
	private List<Planta> data = new ArrayList<Planta>();
	private List<Double> pageRank = new ArrayList<Double>();
	
	
	//Métodos
	public void mostrar(LinkedHashMap<Planta, Double> map) {
		if(!map.isEmpty()) {
			data = map.keySet().stream().collect(Collectors.toList());
			pageRank = map.values().stream().collect(Collectors.toList());
			Collections.reverse(data);
			Collections.reverse(pageRank);
		}
	}
	
	 @Override
	public Object getValueAt(int fila, int columna) {
		switch(columna) {
		case 0:
			return data.get(fila).getNombrePlanta();
		case 1:
			return pageRank.get(fila);
		}
		return null;
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0,c).getClass();
	}


}

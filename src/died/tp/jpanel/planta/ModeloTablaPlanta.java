package died.tp.jpanel.planta;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import died.tp.dominio.Planta;

public class ModeloTablaPlanta extends AbstractTableModel{

	//Atributos
	private String[] columnNames = {"Id Planta", "Nombre planta"};
	private List<Planta> data = new ArrayList<Planta>();


	//Métodos	
	/* Limpia la lista que contiene los datos que se muestran en la tabla
	 */
	public void limpiar() {
		data.clear();
	}

	/* Si la lista de plantas pasada por parámetro no esta vacía, la lista que contiene los datos que se muestran en la tabla
	 * se actualiza
	 */
	public void mostrar(List<Planta> lista) {
		if(lista!=null) {
			data = lista;
		}
	}


	/* Elimina la fila indicada por parámetro
	 */
	public int eliminarFila(int fila) {
		int id = (int)getValueAt(fila,0);
		data.remove(fila);
		return id;
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		Planta p = data.get(fila);
		switch(columna) {
			case 0:
				return p.getId();
			case 1:
				return p.getNombrePlanta();
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

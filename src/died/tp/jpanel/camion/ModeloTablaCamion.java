package died.tp.jpanel.camion;

import java.util.*;

import javax.swing.table.AbstractTableModel;

import died.tp.dominio.Camion;

public class ModeloTablaCamion extends AbstractTableModel{


	//Atributos
	private String[] columnNames = {"ID_Camión", "Patente", "Marca", "Modelo", "KM Recorridos", "Costo KM", "Costo Hora", "Fecha Compra"};
	private List<Camion> data = new ArrayList<Camion>();


	//Métodos	
	/* Limpia la lista que contiene los datos que se muestran en la tabla
	 */
	public void limpiar() {
		data.clear();
	}

	/* Verifica si la lista de camiones pasada por parámetro no esta vacía. En ese caso, actualiza la lista que contiene los datos
	 * que se muestran en la tabla
	 */
	public boolean mostrar(List<Camion> lista) {
		if(!lista.isEmpty()) {
			data = lista;
			return true;
		}
		else {
			return false;
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
		Camion c = data.get(fila);
		switch(columna) {
			case 0:
				return c.getId();
			case 1:
				return c.getPatente();
			case 2:
				return c.getMarca();
			case 3:
				return c.getModelo();
			case 4:
				return c.getKmRecorridos();
			case 5:
				return c.getCostoKM();
			case 6:
				return c.getCostoHora();
			case 7:
				return c.getFechaCompra();
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
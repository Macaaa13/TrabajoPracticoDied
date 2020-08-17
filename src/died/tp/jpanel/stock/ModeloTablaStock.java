package died.tp.jpanel.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import died.tp.dominio.Insumo;
import died.tp.dominio.Stock;

public class ModeloTablaStock extends AbstractTableModel{

	private String[] columnNames = {"Planta", "Insumo", "Stock", "Punto pedido", "Stock total"};
	private List<Stock> data = new ArrayList<Stock>();
	private String planta;
	private List<Integer> totales;
	void limpiar() {
		data.clear();
	}

	public void mostrar(Map<Stock,Integer> lista, String p) {
		if(lista!=null) {
			data = lista.keySet().stream().collect(Collectors.toList());
			planta = p;
			totales = lista.values().stream().collect(Collectors.toList());
		}	
	}

	public int eliminarFila(int fila) {
		int id = (int)getValueAt(fila,0);
		data.remove(fila);
		return id;
	}
	
	public Object getValueAt(int fila, int columna) {
		Stock s = data.get(fila);
		switch(columna) {
		case 0:
			return planta;
		case 1:
			return s.getProducto().getNombre();
		case 2: 
			return s.getCantidad();
		case 3:
			return s.getPuntoPedido();
		case 4:
			return totales.get(fila);
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

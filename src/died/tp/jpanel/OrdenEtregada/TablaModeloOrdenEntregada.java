package died.tp.jpanel.OrdenEtregada;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import died.tp.dominio.OrdenDePedido;

public class TablaModeloOrdenEntregada extends AbstractTableModel{

	private String[] columnNames = {"Nro Orden","Planta origen","Planta destino","Fecha entrega","Costo envío"};
	private List<OrdenDePedido> data = new ArrayList<OrdenDePedido>();

	public void mostrar(List<OrdenDePedido> lista) {
		if(lista != null) {
			data = lista;
		}
	}

	
	public Object getValueAt(int rowIndex, int columnIndex) {
		OrdenDePedido odp = data.get(rowIndex);
		switch(columnIndex) {
			case 0: return odp.getNroOrden();
			case 1: return odp.getOrigen().getNombrePlanta();
			case 2: return odp.getDestino().getNombrePlanta();
			case 3: return odp.getFechaEntrega().toString();
			case 4: return odp.getCostoEnvio().toString();
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

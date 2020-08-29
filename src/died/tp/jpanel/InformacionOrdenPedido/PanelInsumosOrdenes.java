package died.tp.jpanel.InformacionOrdenPedido;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import died.tp.dominio.OrdenDePedido;
import died.tp.jframes.MenuPedidos;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelInsumosOrdenes extends JPanel {

	/**
	 * Create the panel.
	 */
	public PanelInsumosOrdenes(OrdenDePedido ordenPedido, MenuPedidos mp, PanelInformacionOrden pio) {
		setLayout(null);
		setSize(635, 450);
		
		//Tabla
		TablaParaInsumos tablaModelo = new TablaParaInsumos();
		JTable tablaDatos = new JTable(tablaModelo);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scrollPanel = new JScrollPane(tablaDatos);
		scrollPanel.setBounds(19, 25, 580, 329);
		add(scrollPanel, BorderLayout.CENTER);
		
		
		//Botones
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mp.setContentPane(pio);
			}
		});
		btnVolver.setBounds(510, 365, 89, 23);
		add(btnVolver);
		tablaModelo.mostrar(ordenPedido.getInsumos(), ordenPedido.getNroOrden());
		
	}
}

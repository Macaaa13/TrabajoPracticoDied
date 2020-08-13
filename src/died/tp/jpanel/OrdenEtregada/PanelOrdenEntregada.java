package died.tp.jpanel.OrdenEtregada;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import died.tp.controllers.InformacionOrdenController;
import died.tp.jframes.MenuPedidos;


public class PanelOrdenEntregada extends JPanel {
	
	
	public PanelOrdenEntregada() {
		
		setLayout(null);
		setSize(750,400);
		
		InformacionOrdenController infoController = new InformacionOrdenController(null);
		
		TablaModeloOrdenEntregada tablaModelo = new TablaModeloOrdenEntregada();
		JTable tablaDatos = new JTable(tablaModelo);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tablaDatos.setDefaultRenderer(String.class, centerRenderer);
		tablaModelo.mostrar(infoController.traerTodasOrdenes(2));
		
		JScrollPane scrollPanel = new JScrollPane(tablaDatos);
		scrollPanel.setBounds(19, 25, 580, 329);
		add(scrollPanel, BorderLayout.CENTER);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(PanelOrdenEntregada.this);
				w.dispose();
				MenuPedidos mp = new MenuPedidos();
				mp.setVisible(true);
			}
		});
		btnVolver.setBounds(510, 365, 89, 23);
		add(btnVolver);
		
		JButton btnMarcarEntrega = new JButton("Marcar como entregada");
		btnMarcarEntrega.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos.getSelectedRow() != -1) {
					infoController.cambiarEstadoOrden(Integer.valueOf(tablaModelo.getValueAt(tablaDatos.getSelectedRow(), 0).toString()), 3);
				}
				
				
			}
		});
		btnMarcarEntrega.setBounds(19, 365, 175, 23);
		add(btnMarcarEntrega);
	
	}
}

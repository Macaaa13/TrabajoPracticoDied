package died.tp.jpanel.InformacionOrdenPedido;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import died.tp.controllers.InformacionOrdenController;
import died.tp.dominio.OrdenDePedido;
import died.tp.jframes.MenuPedidos;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

public class PanelInformacionOrden extends JPanel{

	//Atributos
	private InformacionOrdenController ordenController;
	
	
	/**
	 * Create the panel.
	 */
	public PanelInformacionOrden(MenuPedidos mp) {
		setLayout(null);
		setSize(635,450);
		
		ordenController = new InformacionOrdenController(this);
		
		//Tabla
		ModeloTablaInfoOrden tablaModelo = new ModeloTablaInfoOrden();
		JTable tablaDatos = new JTable(tablaModelo);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scrollPanel = new JScrollPane(tablaDatos);
		scrollPanel.setBounds(226, 71, 340, 280);
		add(scrollPanel, BorderLayout.CENTER);
		
		//Botones
		JButton btnCargarOrdenes = new JButton("Cargar ordenes");
		btnCargarOrdenes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<OrdenDePedido> op = ordenController.traerTodasOrdenes(1);
				if(!op.isEmpty()) {
					tablaModelo.mostrar(op);
				}
				else {
					JOptionPane.showMessageDialog(null, "No hay resultados por mostrar");
				}
				tablaModelo.fireTableDataChanged();
			}
		});
		btnCargarOrdenes.setBounds(40, 95, 148, 30);
		add(btnCargarOrdenes);
		
		JButton btnDisponibilidad = new JButton("Procesar orden ");
		btnDisponibilidad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos.getSelectedRow() != -1) {
					int rta = JOptionPane.showConfirmDialog(null, "�Est� seguro que quiere continuar?","Advertencia", JOptionPane.OK_CANCEL_OPTION);
					if(rta == JOptionPane.OK_OPTION) {
						if(ordenController.controlarStock(tablaModelo.getValueAt(tablaDatos.getSelectedRow(), 0).toString())){
							mp.setContentPane(new PanelProcesarOrden(ordenController.obtenerOrdenSeleccionada(tablaModelo.getValueAt(tablaDatos.getSelectedRow(), 0).toString()),mp,getPanel(),ordenController));
						}
						else {
							JOptionPane.showMessageDialog(null, "Actualmente no contamos con la cantidad de stock requerida, su orden ser� cancelada");
							ordenController.cambiarEstadoOrden(tablaModelo.eliminarFila(tablaDatos.getSelectedRow()),4);
							tablaModelo.fireTableDataChanged();
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar una orden de pedido");

				}
			}
		});
		btnDisponibilidad.setBounds(40, 136, 148, 30);
		add(btnDisponibilidad);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rta = JOptionPane.showConfirmDialog(null, "�Desea volver al menu principal? \n Los datos no guardados se perder�n", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
				if(rta == JOptionPane.OK_OPTION) {
					Window w = SwingUtilities.getWindowAncestor(PanelInformacionOrden.this);
					w.dispose();
					MenuPedidos mp = new MenuPedidos();
					mp.setVisible(true);
				}
			}
		});
		btnVolver.setBounds(477, 362, 89, 30);
		add(btnVolver);
		
		JButton btnInsumosAsociados = new JButton("Insumos asociados");
		btnInsumosAsociados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos.getSelectedRow() != -1) {
				mp.setContentPane(new PanelInsumosOrdenes(ordenController.obtenerOrdenSeleccionada(tablaModelo.getValueAt(tablaDatos.getSelectedRow(), 0).toString()),mp,getPanel()));
				
				}
				else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar una orden de pedido");
				}
			}
		});
		btnInsumosAsociados.setBounds(40, 177, 148, 30);
		add(btnInsumosAsociados);
	
		//Labels
		JLabel lblNewLabel = new JLabel("Informaci\u00F3n sobre las ordenes creadas");
		lblNewLabel.setBounds(226, 46, 239, 14);
		add(lblNewLabel);
	
	}
	
	//M�todos
	/* Permite informar diferentes tipos de situaciones
	 */
	public void informarSituacion(String st) {
		JOptionPane.showMessageDialog(null, st);
	}
	
	/* Retorna el panel actual
	 */
	public PanelInformacionOrden getPanel() {
		return this;
	}
}
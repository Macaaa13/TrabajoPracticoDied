package died.tp.jpanel.InformacionOrdenPedido;

import javax.swing.JPanel;

import died.tp.controllers.InformacionOrdenController;
import died.tp.dominio.OrdenDePedido;
import died.tp.dominio.Planta;
import died.tp.jframes.MenuPedidos;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class PanelProcesarOrden extends JPanel {

	private JComboBox<String> comboBoxPlantas;
	InformacionOrdenController ordenController;
	JComboBox<String> comboBoxTipoRuta;
	JScrollPane scrollPane;
	

	public PanelProcesarOrden(OrdenDePedido op, MenuPedidos mp, PanelInformacionOrden pio, InformacionOrdenController odc) {
		setLayout(null);
		setSize(645,400);
		
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(221, 10, 395, 345);
		add(scrollPane);
		
		
		this.ordenController = odc;
		
		comboBoxPlantas = new JComboBox<String>();
		comboBoxPlantas.setBounds(120, 32, 90, 22);
		add(comboBoxPlantas);
		cargarPlantas();
		
		JLabel lblNewLabel = new JLabel("Plantas con stock");
		lblNewLabel.setBounds(18, 36, 106, 14);
		add(lblNewLabel);
		
		JComboBox<String> comboBoxTipoRuta = new JComboBox<String>();
		comboBoxTipoRuta.setBounds(120, 70, 90, 22);
		add(comboBoxTipoRuta);
		comboBoxTipoRuta.addItem("Corta");
		comboBoxTipoRuta.addItem("Rápida");
		
		
		
		JLabel lblNewLabel_1 = new JLabel("Tipo de ruta:");
		lblNewLabel_1.setBounds(18, 74, 90, 14);
		add(lblNewLabel_1);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				odc.getPlantas().clear();
				mp.setContentPane(pio);
				
			}
		});
		btnVolver.setBounds(502, 366, 89, 23);
		add(btnVolver);
		
		JButton btnNewButton = new JButton("Buscar rutas");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ordenController.buscarRutas(comboBoxTipoRuta.getSelectedItem().toString(),comboBoxPlantas.getSelectedItem().toString())) {
					ModeloTablaProcesarOrden tablaModelo = new ModeloTablaProcesarOrden(ordenController.getValorMaximo(),ordenController.getListaPlantas());
					JTable tablaDatos = new JTable(tablaModelo);
					tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
					centerRenderer.setHorizontalAlignment( JLabel.CENTER );
					tablaDatos.setDefaultRenderer(String.class, centerRenderer);
					scrollPane = new JScrollPane(tablaDatos);
					scrollPane.setBounds(221, 10, 395, 345);
					add(scrollPane);
					}
				}
		});
		
		btnNewButton.setBounds(77, 111, 134, 23);
		add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Procesar orden");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(odc.getListaPlantas().size() > 1) {
					String camino = JOptionPane.showInputDialog("Seleccione el camino");
					if(camino != null && camino.length()!=0) {
						ordenController.procesarOrden(Integer.valueOf(camino)-1);
					}
				}
				else {
					if(odc.getListaPlantas().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Primero debe buscar las rutas");
					}
					else {
						int rta = JOptionPane.showConfirmDialog(null, "Está seguro de que quiere continuar?", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
						if(rta == JOptionPane.OK_OPTION) {
							ordenController.procesarOrden(0);
						}
					}
				}
			}
		});
		btnNewButton_1.setBounds(77, 145, 134, 23);
		add(btnNewButton_1);
		
		
	}
	
	public JComboBox<String> getComboBoxTipoRuta() {
		return comboBoxTipoRuta;
	}

	public void setComboBoxTipoRuta(JComboBox<String> comboBoxTipoRuta) {
		this.comboBoxTipoRuta = comboBoxTipoRuta;
	}

	public void cargarPlantas() {
		if(ordenController.getPlantas() != null) {
			for(Planta s: ordenController.getPlantas()) {
				comboBoxPlantas.addItem(s.getNombrePlanta());
				}
			}
	}

	public JComboBox<String> getComboBoxPlantas() {
		return comboBoxPlantas;
	}
	

	public void setComboBoxPlantas(JComboBox<String> comboBoxPlantas) {
		this.comboBoxPlantas = comboBoxPlantas;
	}
}
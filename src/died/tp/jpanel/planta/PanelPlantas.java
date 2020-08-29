package died.tp.jpanel.planta;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import died.tp.controllers.PlantaController;
import died.tp.dominio.Planta;
import died.tp.jframes.MenuPrincipal;


import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class PanelPlantas extends JPanel{

	//Atributos
	private PlantaController pc;
	private JTextField textFieldPlanta;
	
	
	//Getters y Setters
	public PlantaController getPc() {
		return pc;
	}

	public void setPc(PlantaController pc) {
		this.pc = pc;
	}

	public JTextField getTextFieldPlanta() {
		return textFieldPlanta;
	}

	public void setTextFieldPlanta(JTextField textField) {
		this.textFieldPlanta = textField;
	}
	
	
	/**
	 * Create the panel.
	 */
	public PanelPlantas() {
		setLayout(null);
		setSize(1100,405);
		
		pc = new PlantaController(this);
		
		//Tabla
		ModeloTablaPlanta tablaModelo = new ModeloTablaPlanta();
		JTable tablaDatos = new JTable(tablaModelo);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollPanel = new JScrollPane(tablaDatos);
		scrollPanel.setBounds(299, 59, 411, 280);
		add(scrollPanel, BorderLayout.CENTER);
	
		//Botones
		JButton btnAgregarPlanta = new JButton("Agregar Planta");
		btnAgregarPlanta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pc.agregarPlanta();
				textFieldPlanta.setText(null);
			}
		});
		btnAgregarPlanta.setBounds(22, 268, 130, 30);
		add(btnAgregarPlanta);
		
		JButton btnBuscar = new JButton("Buscar Plantas");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Planta> plantas = pc.getPlantas();
				if(!plantas.isEmpty()) {
				tablaModelo.mostrar(plantas);
				}
				else {
					JOptionPane.showMessageDialog(null, "No hay resultados por mostrar");
				}
				tablaModelo.fireTableDataChanged();
			}
		});
		btnBuscar.setBounds(22, 309, 130, 30);
		add(btnBuscar);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rta = JOptionPane.showConfirmDialog(null, "¿Desea volver al menu principal? \n Los datos no guardados se perderán", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
				if(rta == JOptionPane.OK_OPTION) {
					Window w = SwingUtilities.getWindowAncestor(PanelPlantas.this);
					w.dispose();
					MenuPrincipal mp = new MenuPrincipal();
					mp.setVisible(true);
				}
			}
		});
		btnVolver.setBounds(590, 352, 120, 30);
		add(btnVolver);
		
		//TextFields
		textFieldPlanta = new JTextField();
		textFieldPlanta.setBounds(140, 59, 120, 20);
		add(textFieldPlanta);
		textFieldPlanta.setColumns(10);
		
		//Labels
		JLabel lblNewLabel = new JLabel("Nombre de Planta");
		lblNewLabel.setBounds(22, 60, 129, 14);
		add(lblNewLabel);	
		
	}
	
	//Métodos
		/* Permite informar diferentes tipos de situaciones
		 */
		public void informarSituacion(String situacion) {
			JOptionPane.showMessageDialog(null, situacion);
		}
	
}

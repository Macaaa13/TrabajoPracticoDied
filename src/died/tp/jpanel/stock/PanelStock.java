package died.tp.jpanel.stock;

import javax.swing.JPanel;

import javax.swing.*;

import died.tp.controllers.StockController;
import died.tp.dominio.Stock;
import died.tp.jframes.MenuPrincipal;

import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class PanelStock extends JPanel {
	
	//Atributos
	private JTextField textFieldInsumo;
	private JTextField textFieldCantidad;
	private JTextField textFieldPuntoPedido;
	private StockController sc;
	JComboBox<String> comboBoxPlanta;
	ModeloTablaStock tablaModelo;
	
	
	//Getters y Setters
	public JTextField getTextFieldInsumo() {
		return textFieldInsumo;
	}

	public void setTextFieldInsumo(JTextField textFieldInsumo) {
		this.textFieldInsumo = textFieldInsumo;
	}

	public JTextField getTextFieldCantidad() {
		return textFieldCantidad;
	}

	public void setTextFieldCantidad(JTextField textFieldCantidad) {
		this.textFieldCantidad = textFieldCantidad;
	}

	public JTextField getTextFieldPuntoPedido() {
		return textFieldPuntoPedido;
	}

	public void setTextFieldPuntoPedido(JTextField textFieldPuntoPedido) {
		this.textFieldPuntoPedido = textFieldPuntoPedido;
	}

	public StockController getSc() {
		return sc;
	}

	public void setSc(StockController sc) {
		this.sc = sc;
	}

	public JComboBox<String> getComboBoxPlanta() {
		return comboBoxPlanta;
	}

	public void setComboBoxPlanta(JComboBox<String> comboBoxPlanta) {
		this.comboBoxPlanta = comboBoxPlanta;
	}
	
	/**
	 * Create the panel.
	 */
	public PanelStock() {
		setLayout(null);
		setSize(900,400);
		
		sc = new StockController(this);
		
		//Tabla
		tablaModelo = new ModeloTablaStock();
		JTable tablaDatos = new JTable(tablaModelo);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tablaDatos.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				if(tablaDatos.getSelectedRow() != -1) {
					cargarFilaSeleccionada(tablaModelo, tablaDatos.getSelectedRow());
				}
			}		
		});
		JScrollPane scrollPanel = new JScrollPane(tablaDatos);
		scrollPanel.setBounds(298, 53, 567, 280);
		add(scrollPanel, BorderLayout.CENTER);
		
		//Botones
		JButton btnCancelar = new JButton("Cancelar");
		
		JButton btnActualizarStock = new JButton("Actualizar Stock");
		btnActualizarStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos.getSelectedRow() != -1) {
					int rta = JOptionPane.showConfirmDialog(null, "¿Está seguro de que quiere modificar el stock?", "Advertencia", JOptionPane.YES_NO_OPTION);
					if(rta == JOptionPane.YES_OPTION) {
						if(sc.validacionVacios()) {
							if(sc.validarCampos()) {
								sc.actualizar();
								tablaModelo.mostrar(sc.traerTodos(), comboBoxPlanta.getSelectedItem().toString());
								tablaModelo.fireTableDataChanged();
								JOptionPane.showMessageDialog(null, "Stock Modificado", "Acción exitosa", JOptionPane.PLAIN_MESSAGE);
								limpiar();
							}
						}
					} 
				}
				else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar el camión que desea modificar", "Advertencia", JOptionPane.OK_OPTION);
				}
				
				
			}
		});
		btnActualizarStock.setBounds(47, 303, 145, 30);
		add(btnActualizarStock);
		
		JButton btnAgregarPlanta = new JButton("Agregar a Planta");
		btnAgregarPlanta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sc.agregarInsumoPlanta();
			}
		});
		btnAgregarPlanta.setBounds(47, 221, 145, 30);
		add(btnAgregarPlanta);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rta = JOptionPane.showConfirmDialog(null, "¿Desea volver al menu principal? \n Los datos no guardados se perderán", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
				if(rta == JOptionPane.OK_OPTION) {
					Window w = SwingUtilities.getWindowAncestor(PanelStock.this);
					w.dispose();
					MenuPrincipal mp = new MenuPrincipal();
					mp.setVisible(true);
				}
			}
		});
		btnVolver.setBounds(745, 344, 120, 30);
		add(btnVolver);
		
		JButton btnCargarStocks = new JButton("Cargar stocks");
		btnCargarStocks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<Stock,Integer> stocks = sc.traerTodos();
				if(!stocks.isEmpty()) {
					tablaModelo.mostrar(stocks, comboBoxPlanta.getSelectedItem().toString());
					tablaModelo.fireTableDataChanged();
					btnAgregarPlanta.setEnabled(false);
					btnCargarStocks.setEnabled(false);
					btnCancelar.setEnabled(true);
				}
				else {
					tablaModelo.limpiar();
					tablaModelo.fireTableDataChanged();
					JOptionPane.showMessageDialog(null, "No hay resultados ");
				}
			}
		});
		btnCargarStocks.setBounds(47, 262, 145, 30);
		add(btnCargarStocks);
		
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiar();
				tablaModelo.limpiar();
				tablaModelo.fireTableDataChanged();
				btnAgregarPlanta.setEnabled(true);
				btnCargarStocks.setEnabled(true);
				btnCancelar.setEnabled(false);
			}
		});
		btnCancelar.setBounds(615, 344, 120, 30);
		add(btnCancelar);
		
		btnCancelar.setEnabled(false);
		
		//TextFields		
		textFieldInsumo = new JTextField();
		textFieldInsumo.setBounds(143, 90, 130, 20);
		add(textFieldInsumo);
		textFieldInsumo.setColumns(10);
		
		textFieldCantidad = new JTextField();
		textFieldCantidad.setBounds(143, 120, 130, 20);
		add(textFieldCantidad);
		textFieldCantidad.setColumns(10);
		
		textFieldPuntoPedido = new JTextField();
		textFieldPuntoPedido.setBounds(143, 150, 130, 20);
		add(textFieldPuntoPedido);
		textFieldPuntoPedido.setColumns(10);
		
		//Labels
		JLabel lblNewLabel = new JLabel("Planta");
		lblNewLabel.setBounds(47, 60, 75, 14);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Insumo");
		lblNewLabel_1.setBounds(47, 90, 75, 14);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Cantidad");
		lblNewLabel_2.setBounds(47, 120, 75, 14);
		add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Punto Pedido");
		lblNewLabel_3.setBounds(47, 150, 83, 14);
		add(lblNewLabel_3);
		
		//comboBox
		comboBoxPlanta = new JComboBox<String>();
		comboBoxPlanta.setBounds(143, 60, 130, 22);
		add(comboBoxPlanta);
		cargarComboBox();
	}
	
	
	//Métodos
	/* Carga el comboBox con las plantas existentes
	 */
	private void cargarComboBox() {
		List<String> plantas = sc.traerPlantas();
		if(plantas != null) {
		for(String s: plantas) {
			comboBoxPlanta.addItem(s);
			}
		}
	}

	/* Limpia los textFields
	 */
	public void limpiar() {
		textFieldCantidad.setText(null);
		textFieldInsumo.setText(null);
		textFieldPuntoPedido.setText(null);
	}

	/* Al seleccionar una fila, sus datos son colocados en los textFields para que el usuario pueda modificarlos
	 */
	private void cargarFilaSeleccionada(ModeloTablaStock mts, int fila) {
		textFieldInsumo.setText(mts.getValueAt(fila, 1).toString());
		textFieldCantidad.setText(mts.getValueAt(fila, 2).toString());
		textFieldPuntoPedido.setText(mts.getValueAt(fila, 3).toString());
	}
	
	/* Permite informar errores
	 */
	public void informarError(String error) {
		JOptionPane.showMessageDialog(null, error);
		
	}
}

package died.tp.jpanel.RegistrarPedido;

import com.toedter.calendar.JDateChooser;

import died.tp.controllers.OrdenPedidoController;
import died.tp.jframes.MenuPedidos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class PanelRegistrarOrden extends JPanel{
	
	//Atributos
	private JTextField textFieldPrecioTotal;
	private OrdenPedidoController opc;
	private JComboBox<String> comboBoxPlanta;
	private JDateChooser dateChooserFechaMaxima;
	
	
	//Getters y Setters
	public void actualizarCompra(Integer costo ) {
		textFieldPrecioTotal.setText(costo.toString());
		
	}
	
	public JTextField getTextFieldPrecioTotal() {
		return textFieldPrecioTotal;
	}
	
	public void setTextFieldPrecioTotal(JTextField textFieldPrecioTotal) {
		this.textFieldPrecioTotal = textFieldPrecioTotal;
	}
	
	public JComboBox<String> getComboBoxPlanta() {
		return comboBoxPlanta;
	}
	
	public void setComboBoxPlanta(JComboBox<String> comboBoxPlanta) {
		this.comboBoxPlanta = comboBoxPlanta;
	}
	
	public void informarSituacion(String string) {
		JOptionPane.showMessageDialog(null, string);
		
	}
	public JDateChooser getDateChooserFechaMaxima() {
		return dateChooserFechaMaxima;
	}
	
	public void setDateChooserFechaMaxima(JDateChooser dateChooserFechaMaxima) {
		this.dateChooserFechaMaxima = dateChooserFechaMaxima;
	}
	
	
	/**
	 * Create the panel.
	 */
	public PanelRegistrarOrden () {
		setLayout(null);
		setSize(900,450);
		setSize(900,450);
		
		opc = new OrdenPedidoController(this);
		
		//TABLA DE INSUMOS SELECCIONADOS
		ModeloTablaRegistrarOrden tablaModelo2 = new ModeloTablaRegistrarOrden(false);
		JTable tablaDatos2 = new JTable(tablaModelo2);
		tablaDatos2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDatos2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			
		JScrollPane scrollPanel_1 = new JScrollPane(tablaDatos2);
		scrollPanel_1.setBounds(416, 46, 474, 311);
		add(scrollPanel_1);
		
		// TABLA DE INSUMOS NO SELECCIONADOS
		ModeloTablaRegistrarOrden tablaModelo = new ModeloTablaRegistrarOrden(true);
		JTable tablaDatos = new JTable(tablaModelo);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tablaDatos.setDefaultRenderer(String.class, centerRenderer);
		
		JScrollPane scrollPanel = new JScrollPane(tablaDatos);
		scrollPanel.setBounds(26, 211, 355, 228);
		add(scrollPanel);
		tablaModelo.mostrar(opc.traerInsumos());
		tablaModelo.fireTableDataChanged();
		tablaDatos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if(tablaDatos.getSelectedRow() != -1) {
					String cantidad = JOptionPane.showInputDialog("Ingrese la cantidad a agregar");
					if(cantidad != null && cantidad.length() != 0) {		
						tablaModelo2.agregar(opc.nuevoInsumo(tablaDatos.getSelectedRow(),Integer.valueOf(cantidad)),Integer.valueOf(cantidad));
						tablaModelo2.fireTableDataChanged();
						opc.actualizarValorCompra(Integer.valueOf(cantidad),Integer.valueOf(tablaModelo.getValueAt(tablaDatos.getSelectedRow(), 2).toString()));
					}
				}
			}
		});
		
		
		//Botones
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rta = JOptionPane.showConfirmDialog(null, "¿Desea volver al menu principal? \n Los datos no guardados se perderán", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
				if(rta == JOptionPane.OK_OPTION) {
					Window w = SwingUtilities.getWindowAncestor(PanelRegistrarOrden.this);
					w.dispose();
					MenuPedidos mp = new MenuPedidos();
					mp.setVisible(true);
				}
			}
		});
		btnVolver.setBounds(801, 409, 89, 30);
		add(btnVolver);
		
		JButton btnRegistrarOrden = new JButton("Registrar Orden");
		btnRegistrarOrden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(opc.agregarOrden(dateChooserFechaMaxima.getDate(),comboBoxPlanta.getSelectedItem().toString())) {		
					JOptionPane.showMessageDialog(null, "Orden de pedido registrada");
					tablaModelo2.limpiar();
					tablaModelo2.fireTableDataChanged();
				}
			}
		});
		btnRegistrarOrden.setBounds(759, 368, 131, 30);
		add(btnRegistrarOrden);
		
		JButton btnQuitar = new JButton("Quitar");
		btnQuitar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos2.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Debe seleccionar un insumo de la tabla para poder quitarlo");
				}
				else {
					Integer rta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres quitar este insumo?","Confirmación",JOptionPane.YES_NO_OPTION);
					if(rta.equals(JOptionPane.YES_OPTION)) {
					opc.actualizarValorCompra(Integer.valueOf(tablaModelo2.getValueAt(tablaDatos2.getSelectedRow(), 3).toString()), - Integer.valueOf(tablaModelo2.getValueAt(tablaDatos2.getSelectedRow(), 2).toString()));
					tablaModelo2.eliminarFila(tablaDatos2.getSelectedRow());
					tablaModelo2.fireTableDataChanged();
					}
				}
				
				
			}
		});
		btnQuitar.setBounds(660, 368, 89, 30);
		add(btnQuitar);
		
		
		//JtextFields
		textFieldPrecioTotal = new JTextField();
		textFieldPrecioTotal.setBounds(231, 127, 150, 20);
		add(textFieldPrecioTotal);
		textFieldPrecioTotal.setColumns(10);
		textFieldPrecioTotal.setEditable(false);
		textFieldPrecioTotal.setText("0");
		
		
		//Labels
		JLabel lblNewLabel_1 = new JLabel("Fecha de entrega m\u00E1xima:");
		lblNewLabel_1.setBounds(30, 90, 176, 14);
		add(lblNewLabel_1);	
		
		Label label = new Label("Seleccione los productos:");
		label.setBounds(26, 186, 155, 22);
		add(label);
		
		JLabel lblNewLabel_2 = new JLabel("Precio total:");
		lblNewLabel_2.setBounds(30, 130, 131, 14);
		add(lblNewLabel_2);
		
		JLabel lblNewLabel = new JLabel("Seleccionar la planta:");
		lblNewLabel.setBounds(30, 50, 131, 14);
		add(lblNewLabel);
		
		//ComboBox
		comboBoxPlanta = new JComboBox<String>();
		comboBoxPlanta.setBounds(231, 46, 150, 22);
		add(comboBoxPlanta);
		cargarComboBox();
			
		
		//DateChooser
		dateChooserFechaMaxima = new JDateChooser();
		dateChooserFechaMaxima.setBounds(231, 90, 150, 20);
		add(dateChooserFechaMaxima);
		
	}
	
	//Métodos
	/* Se cargan en el comboBox los nombres de todas las plantas existentes
	 */
	private void cargarComboBox() {
		List<String> plantas = opc.traerPlantas();
		if(plantas != null) {
		for(String s: plantas) {
			comboBoxPlanta.addItem(s);
			}
		}
	}	
	
	/* Limpia el textField y el dataChooser
	 */
	public void limpiar(){
		dateChooserFechaMaxima.setDate(null);
		textFieldPrecioTotal.setText(null);
	}
	
}

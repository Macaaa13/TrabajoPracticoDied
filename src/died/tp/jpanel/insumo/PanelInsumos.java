package died.tp.jpanel.insumo;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import died.tp.controllers.InsumoController;
import died.tp.dominio.Insumo;
import died.tp.excepciones.DatosObligatoriosException;
import died.tp.excepciones.FormatoNumeroException;
import died.tp.jframes.MenuPrincipal;
import died.tp.jpanel.camion.ModeloTablaCamion;
import died.tp.jpanel.camion.PanelCamiones;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class PanelInsumos extends JPanel {
	
	//Atributos
	private InsumoController ic;
	private JTextField textFieldPeso;
	private JTextField textFieldCosto;
	private JTextField textFieldUnidadMedida;
	private JComboBox<String> comboBoxTipo;
	private JTextField textFieldDensidad;
	private JTextField textFieldDescripcion;
	
 
	//Getters and setters
		public InsumoController getIc() {
			return ic;
		}

		public void setIc(InsumoController ic) {
			this.ic = ic;
		}

		public JTextField getTextFieldPeso() {
			return textFieldPeso;
		}

		public void setTextFieldPeso(JTextField textFieldPeso) {
			this.textFieldPeso = textFieldPeso;
		}

		public JTextField getTextFieldCosto() {
			return textFieldCosto;
		}

		public void setTextFieldCosto(JTextField textFieldCosto) {
			this.textFieldCosto = textFieldCosto;
		}

		public JTextField getTextFieldUnidadMedida() {
			return textFieldUnidadMedida;
		}

		public void setTextFieldUnidadMedida(JTextField textFieldUnidadMedida) {
			this.textFieldUnidadMedida = textFieldUnidadMedida;
		}

		public JComboBox<String> getComboBoxTipo() {
			return comboBoxTipo;
		}

		public void setComboBoxTipo(JComboBox<String> comboBoxTipo) {
			this.comboBoxTipo = comboBoxTipo;
		}

		public JTextField getTextFieldDensidad() {
			return textFieldDensidad;
		}

		public void setTextFieldDensidad(JTextField textFieldDensidad) {
			this.textFieldDensidad = textFieldDensidad;
		}

		public JTextField getTextFieldDescripcion() {
			return textFieldDescripcion;
		}

		public void setTextFieldDescripcion(JTextField textFieldDescripcion) {
			this.textFieldDescripcion = textFieldDescripcion;
		}
	

	public PanelInsumos () {
		setLayout(null);
		setSize(1050,400);
		setSize(1050,400);
		
		ic = new InsumoController(this);
		
	//Tabla
	ModeloTablaInsumo tablaModelo = new ModeloTablaInsumo();
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
	scrollPanel.setBounds(310, 52, 710, 280);
	add(scrollPanel, BorderLayout.CENTER);
	
	
	
	// Botones
	JButton btnAgregar = new JButton("Agregar");
	btnAgregar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				ic.guardar(null);
				JOptionPane.showMessageDialog(null, "Insumo agregado", "Acción exitosa", JOptionPane.PLAIN_MESSAGE);
				limpiar();
			} catch(DatosObligatoriosException | FormatoNumeroException ex) {
				System.out.println(ex.getMessage());;
			}
		}
	});
	btnAgregar.setBounds(30, 225, 120, 30);
	add(btnAgregar);
	
	

	JButton btnEliminar = new JButton("Eliminar");	
	btnEliminar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(tablaDatos.getSelectedRow() != -1) {
				int rta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el insumo?", "Advertencia", JOptionPane.YES_NO_OPTION);
				if(rta == JOptionPane.YES_OPTION) {
					ic.eliminarInsumo(tablaModelo.eliminarFila(tablaDatos.getSelectedRow()));
					tablaModelo.fireTableDataChanged();
					JOptionPane.showMessageDialog(null, "Insumo eliminado", "Acción exitosa", JOptionPane.PLAIN_MESSAGE);
					limpiar();
				} 
			}
			else {
				JOptionPane.showMessageDialog(null, "Debe seleccionar el insumo que desea eliminar", "Advertencia", JOptionPane.OK_OPTION);
			}
		}
	});
	btnEliminar.setBounds(30, 266, 120, 30);
	add(btnEliminar);
	

	
	JButton btnVolver = new JButton("Volver");
	btnVolver.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int rta = JOptionPane.showConfirmDialog(null, "¿Desea volver al menu principal? \n Los datos no guardados se perderán", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
			if(rta == JOptionPane.OK_OPTION) {
				Window w = SwingUtilities.getWindowAncestor(PanelInsumos.this);
				w.dispose();
				MenuPrincipal mp = new MenuPrincipal();
				mp.setVisible(true);
			}
		}
	});
	btnVolver.setBounds(888, 343, 120, 30);
	add(btnVolver);
	
	JButton btnModificar = new JButton("Modificar");
	btnModificar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			if(tablaDatos.getSelectedRow() != -1) {
				int rta = JOptionPane.showConfirmDialog(null, "¿Está seguro de modificar el insumo?", "Advertencia", JOptionPane.YES_NO_OPTION);
				if(rta == JOptionPane.YES_OPTION) {
					ic.actualizar((Integer)tablaModelo.getValueAt(tablaDatos.getSelectedRow(), 0));
					tablaModelo.mostrar(ic.traerDatos());
					tablaModelo.fireTableDataChanged();
				}
			}	
			
			else {
				JOptionPane.showMessageDialog(null, "Debe seleccionar el insumo que desea modificar", "Advertencia", JOptionPane.OK_OPTION);
			}
		}
	});

	btnModificar.setBounds(180, 266, 120, 30);
	add(btnModificar);

	JButton btnCancelar = new JButton("Cancelar");
	btnCancelar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			tablaModelo.limpiar();
			tablaModelo.fireTableDataChanged();
			btnModificar.setEnabled(false);
			btnEliminar.setEnabled(false);
			btnCancelar.setEnabled(false);
			btnAgregar.setEnabled(true);
			comboBoxTipo.setEnabled(true);
			limpiar();
		}
	});
	btnCancelar.setBounds(758, 343, 120, 30);
	add(btnCancelar);

	JButton btnBuscar = new JButton("Buscar");
	btnBuscar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			boolean respuesta = tablaModelo.mostrar(ic.buscar());
			if(respuesta) {
				tablaModelo.fireTableDataChanged();
				btnCancelar.setEnabled(true);
				btnAgregar.setEnabled(false);
				btnModificar.setEnabled(true);
				btnEliminar.setEnabled(true);
				comboBoxTipo.setEnabled(false);
			}
		}
	});
	btnBuscar.setBounds(180, 225, 120, 30);
	add(btnBuscar);

	btnModificar.setEnabled(false);
	btnEliminar.setEnabled(false);
	btnCancelar.setEnabled(false);

	
	//Textfields
	textFieldPeso = new JTextField();
	textFieldPeso.setColumns(10);
	textFieldPeso.setBounds(180, 110, 120, 20);
	add(textFieldPeso);
	
	textFieldCosto = new JTextField();
	textFieldCosto.setColumns(10);
	textFieldCosto.setBounds(180, 80, 120, 20);
	add(textFieldCosto);
	
	textFieldUnidadMedida = new JTextField();
	textFieldUnidadMedida.setColumns(10);
	textFieldUnidadMedida.setBounds(180, 50, 120, 20);
	add(textFieldUnidadMedida);
	
	JLabel lblUnidadMedida = new JLabel("Unidad de Medida");
	lblUnidadMedida.setBounds(31, 50, 139, 14);
	add(lblUnidadMedida);
	
	JLabel lblCosto = new JLabel("Costo");
	lblCosto.setBounds(30, 80, 140, 14);
	add(lblCosto);
	
	JLabel lblPeso = new JLabel("Peso");
	lblPeso.setBounds(30, 110, 140, 14);
	add(lblPeso);
	
	
	JLabel lblTipo = new JLabel("Tipo");
	lblTipo.setHorizontalAlignment(SwingConstants.LEFT);
	lblTipo.setBounds(30, 170, 140, 14);
	add(lblTipo);
	
	textFieldDensidad = new JTextField();
	textFieldDensidad.setColumns(10);
	textFieldDensidad.setBounds(180, 140, 120, 20);
	add(textFieldDensidad);
	
	JLabel lblDensidad = new JLabel("Densidad");
	lblDensidad.setBounds(30, 140, 140, 14);
	add(lblDensidad);
	
	textFieldDescripcion = new JTextField();
	textFieldDescripcion.setBounds(180, 200, 120, 20);
	add(textFieldDescripcion);
	textFieldDescripcion.setColumns(10);
	
	JLabel lblDescripcion = new JLabel("Nombre");
	lblDescripcion.setBounds(30, 200, 140, 14);
	add(lblDescripcion);
	
	comboBoxTipo = new JComboBox<String>();
	comboBoxTipo.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(comboBoxTipo.getSelectedItem().equals("General")) {
				textFieldDensidad.setText("");
				textFieldDensidad.setEditable(false);
				textFieldPeso.setEditable(true);
			}
			else {
				textFieldPeso.setText("");
				textFieldPeso.setEditable(false);
				textFieldDensidad.setEditable(true);
			}
		
		
		}
	});
	comboBoxTipo.setEditable(false);
	comboBoxTipo.setBounds(180, 170, 110, 22);
	add(comboBoxTipo);
	comboBoxTipo.addItem("General");
	comboBoxTipo.addItem("Liquido");
	}
	
	public void informarSituacion(String error) {
		JOptionPane.showMessageDialog(null, error);
	}
	
	public void limpiar() {
		textFieldCosto.setText(null);
		textFieldPeso.setText(null);
		textFieldUnidadMedida.setText(null);
		textFieldDescripcion.setText(null);
		textFieldDensidad.setText(null);
	}

	public void cargarFilaSeleccionada(ModeloTablaInsumo mti, int fila) {
		textFieldDescripcion.setText(mti.getValueAt(fila, 1).toString());
		textFieldUnidadMedida.setText(mti.getValueAt(fila, 2).toString());
		textFieldCosto.setText(mti.getValueAt(fila, 3).toString());
		if(mti.getValueAt(fila, 4).equals(0)) {
			textFieldDensidad.setText(mti.getValueAt(fila, 5).toString());
			textFieldPeso.setEditable(false);
			textFieldDensidad.setEditable(true);
			textFieldPeso.setText("");
		}
		else {
			textFieldPeso.setText(mti.getValueAt(fila, 4).toString());
			textFieldDensidad.setEditable(false);
			textFieldPeso.setEditable(true);
			textFieldDensidad.setText("");

		}
		
	}
	
}

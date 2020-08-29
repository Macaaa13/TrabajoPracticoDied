
package died.tp.jpanel.camion;

import java.awt.event.ActionEvent;


import java.awt.event.*;
import java.sql.Date;

import javax.swing.*;

import com.toedter.calendar.*;

import died.tp.excepciones.*;
import died.tp.controllers.CamionController;
import died.tp.jframes.MenuPrincipal;

import java.awt.*;


public class PanelCamiones extends JPanel {
	
	//Atributos
	private JTextField textFieldPatente;
	private JTextField textFieldCostoHora;
	private JTextField textFieldCostoKM;
	private JTextField textFieldKMRecorridos;
	private JTextField textFieldModelo;
	private JTextField textFieldMarca;
	private JDateChooser dateChooserFechaCompra;
	private CamionController cc;

	
	//Getters y Setters
	public JTextField getTextFieldPatente() {
		return textFieldPatente;
	}

	public void setTextFieldPatente(JTextField textFieldPatente) {
		this.textFieldPatente = textFieldPatente;
	}

	public JTextField getTextFieldCostoHora() {
		return textFieldCostoHora;
	}

	public void setTextFieldCostoHora(JTextField textFieldCostoHora) {
		this.textFieldCostoHora = textFieldCostoHora;
	}

	public JTextField getTextFieldCostoKM() {
		return textFieldCostoKM;
	}

	public void setTextFieldCostoKM(JTextField textFieldCostoKM) {
		this.textFieldCostoKM = textFieldCostoKM;
	}

	public JTextField getTextFieldKMRecorridos() {
		return textFieldKMRecorridos;
	}

	public void setTextFieldKMRecorridos(JTextField textFieldKMRecorridos) {
		this.textFieldKMRecorridos = textFieldKMRecorridos;
	}

	public JTextField getTextFieldModelo() {
		return textFieldModelo;
	}

	public void setTextFieldModelo(JTextField textFieldModelo) {
		this.textFieldModelo = textFieldModelo;
	}
	public JTextField getTextFieldMarca() {
		return textFieldMarca;
	}

	public void setTextFieldMarca(JTextField textFieldMarca) {
		this.textFieldMarca = textFieldMarca;
	}

	public JDateChooser getDateChooserFechaCompra() {
		return dateChooserFechaCompra;
	}

	public void setDateChooserFechaCompra(JDateChooser dateChooserFechaCompra) {
		this.dateChooserFechaCompra = dateChooserFechaCompra;
	}

	
	/**
	 * Create the panel.
	 */
	public PanelCamiones() {
		setLayout(null);
		setSize(1200,400);
		
		cc = new CamionController(this);
		
		//Tabla
		ModeloTablaCamion tablaModelo = new ModeloTablaCamion();
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
		scrollPanel.setBounds(350, 53, 800, 280);
		add(scrollPanel, BorderLayout.CENTER);


		//Botones
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					cc.guardar();
			}
		});
		btnAgregar.setBounds(50, 312, 120, 30);
		add(btnAgregar);
		
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(380, 131, 120, 30);
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos.getSelectedRow() != -1) {
					int rta = JOptionPane.showConfirmDialog(null, "�Est� seguro de eliminar el cami�n?", "Advertencia", JOptionPane.YES_NO_OPTION);
					if(rta == JOptionPane.YES_OPTION) {
						cc.eliminarCamion(tablaModelo.eliminarFila(tablaDatos.getSelectedRow()));
						tablaModelo.fireTableDataChanged();
						JOptionPane.showMessageDialog(null, "Cami�n eliminado", "Acci�n exitosa", JOptionPane.PLAIN_MESSAGE);
						limpiar();
					} 
				}
				else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar el cami�n que desea eliminar", "Advertencia", JOptionPane.OK_OPTION);
				}
			}
		});
		btnEliminar.setBounds(50, 359, 120, 30);
		add(btnEliminar);
		
		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos.getSelectedRow() != -1) {
					int rta = JOptionPane.showConfirmDialog(null, "�Est� seguro de modificar el cami�n?", "Advertencia", JOptionPane.YES_NO_OPTION);
					if(rta == JOptionPane.YES_OPTION) {
						cc.actualizar((Integer)tablaModelo.getValueAt(tablaDatos.getSelectedRow(), 0));
						tablaModelo.mostrar(cc.traerDatos());
						tablaModelo.fireTableDataChanged();
					} 
				}
				else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar el cami�n que desea modificar", "Advertencia", JOptionPane.OK_OPTION);
				}
			}
		});
		btnModificar.setBounds(180, 359, 120, 30);
		add(btnModificar);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rta = JOptionPane.showConfirmDialog(null, "�Desea volver al menu principal? \n Los datos no guardados se perder�n", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
				if(rta == JOptionPane.OK_OPTION) {
					Window w = SwingUtilities.getWindowAncestor(PanelCamiones.this);
					w.dispose();
					MenuPrincipal mp = new MenuPrincipal();
					mp.setVisible(true);
				}
			}
		});
		btnVolver.setBounds(1030, 359, 120, 30);
		add(btnVolver);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiar();
				tablaModelo.limpiar();
				tablaModelo.fireTableDataChanged();
				btnModificar.setEnabled(false);
				btnEliminar.setEnabled(false);
				btnCancelar.setEnabled(false);
				btnAgregar.setEnabled(true);
			}
		});
		btnCancelar.setBounds(900, 359, 120, 30);
		add(btnCancelar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean respuesta = tablaModelo.mostrar(cc.buscar());
				if(respuesta) {
					tablaModelo.fireTableDataChanged();
					btnCancelar.setEnabled(true);
					btnAgregar.setEnabled(false);
					btnModificar.setEnabled(true);
					btnEliminar.setEnabled(true);
				}
			}
		});
		btnBuscar.setBounds(180, 312, 120, 30);
		add(btnBuscar);

		btnModificar.setEnabled(false);
		btnEliminar.setEnabled(false);
		btnCancelar.setEnabled(false);
		
	
		//TextFields
		textFieldPatente = new JTextField();
		textFieldPatente.setColumns(10);
		textFieldPatente.setBounds(180, 51, 120, 20);
		add(textFieldPatente);
		
		
		dateChooserFechaCompra = new JDateChooser();
		dateChooserFechaCompra.setBounds(180, 261, 120, 20);
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooserFechaCompra.getDateEditor();
		editor.setEditable(false);
		add(dateChooserFechaCompra);
		
		
		textFieldCostoHora = new JTextField();
		textFieldCostoHora.setColumns(10);
		textFieldCostoHora.setBounds(180, 226, 120, 20);
		add(textFieldCostoHora);
		
		textFieldCostoKM = new JTextField();
		textFieldCostoKM.setColumns(10);
		textFieldCostoKM.setBounds(180, 191, 120, 20);
		add(textFieldCostoKM);
		
		textFieldKMRecorridos = new JTextField();
		textFieldKMRecorridos.setColumns(10);
		textFieldKMRecorridos.setBounds(180, 156, 120, 20);
		add(textFieldKMRecorridos);
		
		textFieldModelo = new JTextField();
		textFieldModelo.setColumns(10);
		textFieldModelo.setBounds(180, 121, 120, 20);
		add(textFieldModelo);
		
		textFieldMarca = new JTextField();
		textFieldMarca.setColumns(10);
		textFieldMarca.setBounds(180, 86, 120, 20);
		add(textFieldMarca);
		
		//Labels
		JLabel lblPatente = new JLabel("Patente:");
		lblPatente.setBounds(50, 51, 120, 20);
		add(lblPatente);
		
		JLabel lblMarca = new JLabel("Marca:");
		lblMarca.setBounds(50, 86, 120, 20);
		add(lblMarca);
		
		JLabel lblModelo = new JLabel("Modelo:");
		lblModelo.setBounds(50, 121, 120, 20);
		add(lblModelo);
		
		JLabel lblKMRecorridos = new JLabel("KM recorridos:");
		lblKMRecorridos.setBounds(50, 156, 120, 20);
		add(lblKMRecorridos);
		
		JLabel lblCostoKM = new JLabel("Costo por KM:");
		lblCostoKM.setBounds(50, 191, 120, 20);
		add(lblCostoKM);
		
		JLabel lblCostoHora = new JLabel("Costo por hora:");
		lblCostoHora.setBounds(50, 226, 120, 20);
		add(lblCostoHora);
		
		JLabel lblFechaCompra = new JLabel("Fecha de compra:");
		lblFechaCompra.setBounds(50, 261, 120, 20);
		add(lblFechaCompra);
		
	}
	
	//M�todos
	/* Permite informar diferentes tipos de situaciones
	 */
	public void informarSituacion(String situacion) {
		JOptionPane.showMessageDialog(null, situacion);
	}
	
	/* Limpia los textFields y el dataChooser
	 */
	public void limpiar() {
		textFieldPatente.setText(null);
		textFieldMarca.setText(null);
		textFieldModelo.setText(null);
		textFieldKMRecorridos.setText(null);
		textFieldCostoHora.setText(null);
		textFieldCostoKM.setText(null);
		dateChooserFechaCompra.setDate(null);
	}
	
	/* Al seleccionar una fila, sus datos son colocados en los textFields y el dataChooser para que el usuario pueda modificarlos
	 */
	public void cargarFilaSeleccionada(ModeloTablaCamion mtc, int fila) {
		textFieldPatente.setText(mtc.getValueAt(fila, 1).toString()); 
		textFieldMarca.setText(mtc.getValueAt(fila, 2).toString());
		textFieldModelo.setText(mtc.getValueAt(fila, 3).toString());
		textFieldKMRecorridos.setText(mtc.getValueAt(fila, 4).toString());
		textFieldCostoKM.setText(mtc.getValueAt(fila, 5).toString());
		textFieldCostoHora.setText(mtc.getValueAt(fila, 6).toString());
		dateChooserFechaCompra.setDate(Date.valueOf(mtc.getValueAt(fila, 7).toString()));
	}
	
}
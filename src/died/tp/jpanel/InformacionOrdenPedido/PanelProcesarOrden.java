package died.tp.jpanel.InformacionOrdenPedido;

import died.tp.controllers.InformacionOrdenController;
import died.tp.dominio.*;
import died.tp.jframes.MenuPedidos;
import died.tp.jframes.MenuPrincipal;
import died.tp.jpanel.camion.PanelCamiones;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.Window;
import java.awt.event.*;

public class PanelProcesarOrden extends JPanel {

	//Atributos
	private JComboBox<String> comboBoxPlantas;
	InformacionOrdenController ordenController;
	JComboBox<String> comboBoxTipoRuta;
	JScrollPane scrollPane;
	private JTextField textFieldDistancia;
	private JTextField textFieldDuracion;
	private MenuPedidos mp;
	
	
	//Getters y Setters
	public JComboBox<String> getComboBoxTipoRuta() {
		return comboBoxTipoRuta;
	}

	public void setComboBoxTipoRuta(JComboBox<String> comboBoxTipoRuta) {
		this.comboBoxTipoRuta = comboBoxTipoRuta;
	}

	public JComboBox<String> getComboBoxPlantas() {
		return comboBoxPlantas;
	}	

	public void setComboBoxPlantas(JComboBox<String> comboBoxPlantas) {
		this.comboBoxPlantas = comboBoxPlantas;
	}
	
	public void setDuracion(Double duracion) {
		textFieldDuracion.setText(duracion.toString());
	}
	
	public void setDistancia(Double distancia) {
		textFieldDistancia.setText(distancia.toString());
	}
	
	
	/**
	 * Create the panel.
	 */
	public PanelProcesarOrden(OrdenDePedido op, MenuPedidos mp, PanelInformacionOrden pio, InformacionOrdenController odc) {
		setLayout(null);
		setSize(645,400);
		
		ordenController = odc;
		ordenController.setPanelProcesarOrden(this);
		this.mp = mp;
		
		//Tabla
		scrollPane = new JScrollPane();
		scrollPane.setBounds(221, 10, 375, 325);
		add(scrollPane);
		
		
		//Botones
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				odc.getPlantas().clear();
				mp.setContentPane(pio);
				
			}
		});
		btnVolver.setBounds(507, 346, 89, 30);
		add(btnVolver);
		
		JButton btnBuscarRutas = new JButton("Buscar rutas");
		btnBuscarRutas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBoxTipoRuta.getSelectedItem()!=null || comboBoxPlantas.getSelectedItem()!=null) {
					if(ordenController.buscarRutas(comboBoxTipoRuta.getSelectedItem().toString(),comboBoxPlantas.getSelectedItem().toString())) {
						ModeloTablaProcesarOrden tablaModelo = new ModeloTablaProcesarOrden(ordenController.getValorMaximo(),ordenController.getListaPlantas());
						JTable tablaDatos = new JTable(tablaModelo);
						tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
						DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						centerRenderer.setHorizontalAlignment( JLabel.CENTER );
						tablaDatos.setDefaultRenderer(String.class, centerRenderer);
						scrollPane = new JScrollPane(tablaDatos);
						scrollPane.setBounds(221, 10, 375, 325);
						add(scrollPane);
						}
					}
				else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar una planta y un tipo de ruta.");
				}
			}
		});
		
		btnBuscarRutas.setBounds(18, 305, 134, 30);
		add(btnBuscarRutas);
		
		JButton btnProcesarOrden = new JButton("Procesar orden");
		btnProcesarOrden.addActionListener(new ActionListener() {
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
						int rta = JOptionPane.showConfirmDialog(null, "¿Está seguro de que quiere continuar?", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
						if(rta == JOptionPane.OK_OPTION) {
							ordenController.procesarOrden(0);
						}
					}
				}
			}
		});
		btnProcesarOrden.setBounds(18, 346, 134, 30);
		add(btnProcesarOrden);
		
		
		//TextFields
		textFieldDistancia = new JTextField();
		textFieldDistancia.setBounds(122, 160, 89, 20);
		add(textFieldDistancia);
		textFieldDistancia.setColumns(10);
		textFieldDistancia.setEditable(false);
		
		textFieldDuracion = new JTextField();
		textFieldDuracion.setColumns(10);
		textFieldDuracion.setBounds(122, 188, 89, 20);
		add(textFieldDuracion);
		textFieldDuracion.setEditable(false);
		
		//Labels
		JLabel lblNewLabel = new JLabel("Plantas con stock:");
		lblNewLabel.setBounds(18, 36, 106, 14);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Tipo de ruta:");
		lblNewLabel_1.setBounds(18, 93, 90, 14);
		add(lblNewLabel_1);
		
		JLabel lblDuracionhs = new JLabel("Duracion [hs]:");
		lblDuracionhs.setBounds(18, 191, 106, 14);
		add(lblDuracionhs);
		
		JLabel lblDistancia = new JLabel("Distancia [km]:");
		lblDistancia.setBounds(18, 163, 106, 14);
		add(lblDistancia);
		
		//ComboBox
		comboBoxPlantas = new JComboBox<String>();
		comboBoxPlantas.setBounds(66, 60, 145, 22);
		add(comboBoxPlantas);
		cargarPlantas();
		
		comboBoxTipoRuta = new JComboBox<String>();
		comboBoxTipoRuta.setBounds(66, 115, 145, 22);
		comboBoxTipoRuta.addItem("Corta");
		comboBoxTipoRuta.addItem("Rápida");
		add(comboBoxTipoRuta);
		
	}
	
	//Métodos
	/* Carga las plantas en el comboBox
	 */
	public void cargarPlantas() {
		if(ordenController.getPlantas() != null) {
			for(Planta s: ordenController.getPlantas()) {
				comboBoxPlantas.addItem(s.getNombrePlanta());
			}
		}
	}
	
	public void limpiarDuracion() {
		this.textFieldDuracion.setText(null);
	}
	
	public void limpiarDistancia() {
		this.textFieldDistancia.setText(null);
	}
	
	public void regresar() {
		Window w = SwingUtilities.getWindowAncestor(this);
		w.dispose();
		mp.setContentPane(new PanelInformacionOrden(mp));
		mp.setVisible(true);
	}

}
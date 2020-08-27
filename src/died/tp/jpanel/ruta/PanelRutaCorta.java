package died.tp.jpanel.ruta;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import died.tp.controllers.PlantaController;
import died.tp.dao.RutaDao;
import died.tp.dominio.Planta;
import died.tp.grafos.GrafoRutas;
import died.tp.grafos.Vertice;
import died.tp.jframes.MenuRutas;
import died.tp.jpanel.InformacionOrdenPedido.ModeloTablaProcesarOrden;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.*;
import javax.swing.JTextField;

public class PanelRutaCorta extends JPanel {

	//Atributos
	JComboBox<String> comboBoxDestino = new JComboBox<String>();
	JComboBox<String> comboBoxOrigen = new JComboBox<String>();
	JComboBox<String> comboBoxTipoRuta = new JComboBox<String>();
	private GrafoRutas gr;
	private RutaDao rd;
	private PlantaController pc;
	private JScrollPane scrollPane;
	private JTextField textFieldDistancia;
	private JTextField textFieldDuracion;
	
	/**
	 * Create the panel.
	 */
	public PanelRutaCorta() {
		setLayout(null);
		setSize(1000,400);
		
		pc = new PlantaController();
		gr = new GrafoRutas();
		rd = new RutaDao();
		
		PanelRutaCorta prc = this;
		
		//Labels
		JLabel lblOrigen = new JLabel("Origen:");
		lblOrigen.setBounds(30, 50, 46, 14);
		add(lblOrigen);
						
		JLabel lblDestino = new JLabel("Destino:");
		lblDestino.setBounds(30, 90, 46, 14);
		add(lblDestino);
		
		JLabel lblTipoRuta = new JLabel("Tipo de Ruta:");
		lblTipoRuta.setBounds(30, 130, 73, 14);
		add(lblTipoRuta);
		
		JLabel lblDuracin = new JLabel("Duraci\u00F3n [hs]:");
		lblDuracin.setBounds(30, 210, 94, 14);
		add(lblDuracin);
		
		JLabel lblDistancia = new JLabel("Distancia [km]:");
		lblDistancia.setBounds(30, 170, 94, 14);
		add(lblDistancia);
		
		
		//Buttons
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBoxOrigen.getSelectedIndex()!=-1 && comboBoxDestino.getSelectedIndex()!=-1 && comboBoxTipoRuta.getSelectedIndex()!=-1) {
					gr.armarGrafo(rd.traerRutas());
					List<List<Vertice<Planta>>> listaRutas = gr.getRutaCorta(pc.traerPlanta(comboBoxOrigen.getSelectedItem().toString()), pc.traerPlanta(comboBoxDestino.getSelectedItem().toString()), comboBoxTipoRuta.getSelectedItem().toString(), prc);
					if(listaRutas==null) {
						JOptionPane.showMessageDialog(null, "No se encontraron rutas entre ambas plantas"," ", JOptionPane.OK_OPTION);	
					}
					else {
						ModeloTablaProcesarOrden tablaModelo = new ModeloTablaProcesarOrden(valorMax(listaRutas),listaRutas);
						JTable tablaDatos = new JTable(tablaModelo);
						tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
						DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						centerRenderer.setHorizontalAlignment( JLabel.CENTER );
						tablaDatos.setDefaultRenderer(String.class, centerRenderer);
						scrollPane = new JScrollPane(tablaDatos);
						scrollPane.setBounds(300, 45, 650, 280);
						add(scrollPane);
						
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar un Origen, un Destino y un Tipo de Ruta", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBuscar.setBounds(30, 260, 120, 30);
		add(btnBuscar);		
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(PanelRutaCorta.this);
				w.dispose();
				MenuRutas mr = new MenuRutas();
				mr.setVisible(true);
			}
		});
		btnVolver.setBounds(30, 300, 120, 30);
		add(btnVolver);
		
		//ComboBox
		comboBoxOrigen.setBounds(100, 46, 149, 22);
		for(Planta p: pc.getPlantas()) {
			comboBoxOrigen.addItem(p.getNombrePlanta());
		}
		comboBoxOrigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBoxOrigen.getSelectedIndex() != -1) {
					comboBoxDestino.setEnabled(true);
					List<Planta> lista = pc.getPlantas();
					lista.remove(comboBoxOrigen.getSelectedIndex());
					comboBoxDestino.removeAllItems();
					for(Planta p: lista) {
						comboBoxDestino.addItem(p.getNombrePlanta());
					}
					comboBoxDestino.setSelectedIndex(-1);
				}
			}
		});
		add(comboBoxOrigen);
		
		comboBoxDestino.setBounds(100, 86, 149, 22);
		add(comboBoxDestino);
		
		comboBoxTipoRuta.setBounds(124, 126, 125, 22);
		add(comboBoxTipoRuta);
		comboBoxTipoRuta.addItem("Corta");
		comboBoxTipoRuta.addItem("Rápida");
		
		comboBoxDestino.setEnabled(false);
		comboBoxOrigen.setSelectedIndex(-1);
		

		//TextField
		textFieldDistancia = new JTextField();
		textFieldDistancia.setBounds(124, 167, 125, 20);
		add(textFieldDistancia);
		textFieldDistancia.setColumns(10);
		textFieldDistancia.setEditable(false);
		
		textFieldDuracion = new JTextField();
		textFieldDuracion.setColumns(10);
		textFieldDuracion.setBounds(124, 207, 125, 20);
		add(textFieldDuracion);
		textFieldDuracion.setEnabled(true);
		textFieldDuracion.setEditable(false);

	}

	public Integer valorMax(List<List<Vertice<Planta>>> lista) {
		Integer valMax = 0;
		for(List<Vertice<Planta>> v: lista) {
			if(valMax < v.size()) {
				valMax = v.size();
			}
		}
		return valMax;
	}
	
	public void setDuracion(String duracion) {
		textFieldDuracion.setText(duracion);
	}
	
	public void setDistancia(String distancia) {
		textFieldDistancia.setText(distancia);
	}
	
}

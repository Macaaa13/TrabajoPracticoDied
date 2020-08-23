package died.tp.jpanel.ruta;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import died.tp.controllers.PlantaController;
import died.tp.dao.RutaDao;
import died.tp.dominio.Planta;
import died.tp.grafos.GrafoRutas;
import died.tp.grafos.Vertice;
import died.tp.jframes.MenuRutas;

public class PanelFlujoMax extends JPanel {

	//Atributos
	JComboBox<String> comboBoxDestino = new JComboBox<String>();
	JComboBox<String> comboBoxOrigen = new JComboBox<String>();
	private PlantaController pc;
	private GrafoRutas gr;
	private RutaDao rd;
	
	/**
	 * Create the panel.
	 */
	public PanelFlujoMax() {
		setLayout(null);
		setSize(550, 400);
		
		pc = new PlantaController();
		gr = new GrafoRutas();
		rd = new RutaDao();
		
		
		//Labels
		JLabel lblOrigen = new JLabel("Origen:");
		lblOrigen.setBounds(50, 50, 46, 14);
		add(lblOrigen);
				
		JLabel lblDestino = new JLabel("Destino:");
		lblDestino.setBounds(50, 90, 46, 14);
		add(lblDestino);
		
		
		//Buttons
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(50, 142, 120, 30);
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBoxOrigen.getSelectedIndex()!=-1 && comboBoxDestino.getSelectedIndex()!=-1) {
					gr.armarGrafo(rd.traerRutas());
					List<List<Vertice<Planta>>> listaFlujoMax = gr.flujoMax(pc.traerPlanta(comboBoxOrigen.getSelectedItem().toString()), pc.traerPlanta(comboBoxDestino.getSelectedItem().toString()));
					if(listaFlujoMax == null) {
						JOptionPane.showMessageDialog(null, "No se encontraron rutas entre ambas plantas"," ", JOptionPane.OK_OPTION);	
					}	
					else {
						for(List<Vertice<Planta>> lista: listaFlujoMax) {
							System.out.println("\nLista");
							for(Vertice<Planta> planta: lista) {
								System.out.println(planta.getValor().getNombrePlanta());
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar un Origen y un Destino", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		add(btnBuscar);		
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(PanelFlujoMax.this);
				w.dispose();
				MenuRutas mr = new MenuRutas();
				mr.setVisible(true);
			}
		});
		btnVolver.setBounds(50, 183, 120, 30);
		add(btnVolver);
		
		
		//ComboBox
		comboBoxOrigen.setBounds(159, 46, 149, 22);
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
				
		comboBoxDestino.setBounds(159, 86, 149, 22);
		add(comboBoxDestino);
				
		comboBoxDestino.setEnabled(false);
		comboBoxOrigen.setSelectedIndex(-1);
		
	}

}

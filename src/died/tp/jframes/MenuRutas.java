package died.tp.jframes;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import died.tp.controllers.PlantaController;
import died.tp.dao.RutaDao;
import died.tp.jpanel.ruta.*;
import died.tp.grafos.*;

public class MenuRutas extends JFrame {
	
	//Atributos
	private JPanel panelInicial;
	private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); 
	private PlantaController pc;

	/**
	 * Create the frame.
	 */
	public MenuRutas() {
		setVisible(true);
		setTitle("Rutas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(dim.width/2-275,dim.height/2-200, 550, 400);
		panelInicial = new JPanel();
		panelInicial.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelInicial);
		panelInicial.setLayout(null);
		
		pc = new PlantaController();
		
		JButton btnCrearRuta = new JButton("Crear Ruta");
		btnCrearRuta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pc.getPlantas().size()>1) {
					setContentPane(new PanelRutas());
				} else {
					JOptionPane.showMessageDialog(null, "No existen suficientes plantas como para crear una ruta", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		btnCrearRuta.setBounds(210, 85, 120, 30);
		panelInicial.add(btnCrearRuta);
		
		JButton btnFlujoMax = new JButton("Flujo M\u00E1ximo");
		btnFlujoMax.setBounds(210, 125, 120, 30);
		btnFlujoMax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBounds(dim.width/2-500,dim.height/2-200, 1000, 400);
				setContentPane(new PanelFlujoMax());
			}
			
		});
		panelInicial.add(btnFlujoMax);
		
		JButton btnPageRank = new JButton("Page Rank");
		btnPageRank.setBounds(210, 165, 120, 30);
		btnPageRank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GrafoRutas gr = new GrafoRutas();
				RutaDao rd = new RutaDao();
				gr.armarGrafo(rd.traerRutas());
				if(gr.getVertices()!=null) {
					setContentPane(new PanelPageRank(gr));
				}	
				else {
					JOptionPane.showMessageDialog(null,"No existen rutas que permitan calcular el Page Rank");
				}
			}
		});
		panelInicial.add(btnPageRank);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MenuPrincipal mp = new MenuPrincipal();
				mp.setVisible(true);
			}
		});
		btnVolver.setBounds(210, 271, 120, 30);
		panelInicial.add(btnVolver);
		
		JButton btnRutaCorta = new JButton("Ruta Corta");
		btnRutaCorta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBounds(dim.width/2-500,dim.height/2-200, 1000, 400);
				setContentPane(new PanelRutaCorta());
			}
		});
		btnRutaCorta.setBounds(210, 205, 120, 30);
		panelInicial.add(btnRutaCorta);
	}
}
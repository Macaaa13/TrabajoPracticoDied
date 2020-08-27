package died.tp.jpanel.ruta;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import died.tp.dao.RutaDao;
import died.tp.grafos.GrafoRutas;
import died.tp.jframes.MenuRutas;

public class PanelPageRank extends JPanel {

	//Atributos
	private GrafoRutas gr;
	
	/**
	 * Create the panel.
	 */
	public PanelPageRank(GrafoRutas grafo) {
		setLayout(null);
		setSize(550,400);
		
		gr = grafo;
		
		//Tabla
		ModeloTablaPageRank tablaModelo = new ModeloTablaPageRank();
		JTable tablaDatos = new JTable(tablaModelo);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollPanel = new JScrollPane(tablaDatos);
		scrollPanel.setBounds(50,50,450,250);
		add(scrollPanel, BorderLayout.CENTER);
		
		//Cargar tabla
		tablaModelo.mostrar(gr.pageRank());
		tablaModelo.fireTableDataChanged();
		
		//Botones
		JButton btnNewButton = new JButton("Volver");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(PanelPageRank.this);
				w.dispose();
				MenuRutas mr = new MenuRutas();
				mr.setVisible(true);
			}
		});
		btnNewButton.setBounds(409, 320, 89, 23);
		add(btnNewButton);
		
	}

}

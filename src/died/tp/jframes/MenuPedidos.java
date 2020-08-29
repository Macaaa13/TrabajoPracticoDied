package died.tp.jframes;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import died.tp.jpanel.InformacionOrdenPedido.PanelInformacionOrden;
import died.tp.jpanel.OrdenEtregada.PanelOrdenEntregada;
import died.tp.jpanel.RegistrarPedido.PanelRegistrarOrden;

public class MenuPedidos extends JFrame {

	//Atributos
	private JPanel contentPane;
	private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	
	/**
	 * Create the frame.
	 */
	public MenuPedidos() {
		setTitle("Pedidos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(dim.width/2-275,dim.height/2-200, 550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Botones
		JButton volver = new JButton("Volver");
		volver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuPrincipal mp = new MenuPrincipal();
				mp.setVisible(true);
				dispose();
			}
		});
		volver.setBounds(190, 238, 140, 30);
		contentPane.add(volver);
		
		JButton btnRegistrarOrden = new JButton("Registrar orden");
		btnRegistrarOrden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBounds(dim.width/2-450,dim.height/2-225, 950, 500);
				setContentPane(new PanelRegistrarOrden());
				setTitle("Registrar orden pedido");
			}
		});
		btnRegistrarOrden.setBounds(190, 90, 140, 30);
		contentPane.add(btnRegistrarOrden);
		
		JButton btnInformacionOrden =new JButton("Procesar orden");
		btnInformacionOrden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBounds(dim.width/2-310,dim.height/2-225, 635, 450);
				setContentPane(new PanelInformacionOrden(getMenu()));
				setTitle("Procesar Orden de pedido");
			}
		});
		btnInformacionOrden.setBounds(190, 131, 140, 30);
		contentPane.add(btnInformacionOrden);
		
		JButton btnNewButton = new JButton("Registrar entrega");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setBounds(dim.width/2-300,dim.height/2-225, 650, 450);
				setContentPane(new PanelOrdenEntregada());
				setTitle("Registrar orden pedido");
				
			}
		});
		btnNewButton.setBounds(190, 172, 140, 30);
		contentPane.add(btnNewButton);
	}
	
	
	//Métodos
	public MenuPedidos getMenu() {
		return this;
	}
}

package died.tp.test.ejercicio6;

import static org.junit.jupiter.api.Assertions.*;


import java.util.*;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import died.tp.dominio.Planta;
import died.tp.dominio.Ruta;
import died.tp.grafos.GrafoRutas;
import died.tp.grafos.Vertice;
import died.tp.jpanel.ruta.PanelFlujoMax;

class FlujoMaxTest {

	//Atributos
	Planta p1 = new Planta("Planta 1");
	Planta p2 = new Planta("Planta 2");
	Planta p3 = new Planta("Planta 3");
	Planta p4 = new Planta("Planta 4");
	Planta p5 = new Planta("Planta 5");
	GrafoRutas gr = new GrafoRutas();
	List<Ruta> listaRutas = new ArrayList<Ruta>();
	
	@BeforeEach
	public void setup() {
		p1.setId(1);
		p2.setId(2);
		p3.setId(3);
		p4.setId(4);
		p5.setId(5);
		//Creacion de rutas
		Ruta r1 = new Ruta(p1,p2,0,0.0,300);
		Ruta r2 = new Ruta(p1,p3,0,0.0,425);
		Ruta r3 = new Ruta(p1,p5,0,0.0,200);
		Ruta r4 = new Ruta(p2,p5,0,0.0,250);
		Ruta r5 = new Ruta(p3,p2,0,0.0,315);
		Ruta r6 = new Ruta(p2,p4,0,0.0,270);
		Ruta r7 = new Ruta(p4,p5,0,0.0,300);
		//Se agregan las rutas a una lista
		listaRutas.add(r1);
		listaRutas.add(r2);
		listaRutas.add(r3);
		listaRutas.add(r4);
		listaRutas.add(r5);
		listaRutas.add(r6);
		listaRutas.add(r7);
		// Armado del grafo
		gr.armarGrafo(listaRutas);
	}
	
	
	// 1) Test del método armarGrafo ---------------
	
	@Test
	// Al armar el grafo, todas las plantas que forman parte de las rutas se guardan como vértices en una lista.
	// Si creo una lista con las plantas como vértices, esta debe contener todos los elementos de la lista de vértices del grafo.
	void testArmarGrafo() {
		Vertice<Planta> v1 = new Vertice<Planta>(p1);
		Vertice<Planta> v2 = new Vertice<Planta>(p2);
		Vertice<Planta> v3 = new Vertice<Planta>(p3);
		Vertice<Planta> v4 = new Vertice<Planta>(p4);
		Vertice<Planta> v5 = new Vertice<Planta>(p5);
		List<Vertice<Planta>> listaVertices = new ArrayList<Vertice<Planta>>();
		listaVertices.add(v1);
		listaVertices.add(v2);
		listaVertices.add(v3);
		listaVertices.add(v4);
		listaVertices.add(v5);
		assertTrue(listaVertices.containsAll(gr.getVertices()));
	}
	
	
	// 2) Test del método caminos ---------------
	
	@Test
	// La función devuelve una lista de listas de vértices que representan las plantas por las que hay que pasar para llegar
	// desde una planta A a una B, incluyendo éstas últimas.
	// Existen 2 caminos entre la planta 1 y la 2:
	// 1 - 2 || 1 - 3 - 2 
	// La lista que retorna la función debe contener listas con esas plantas
	// Ya que equals y contains entre el tipo de datos Vertice<Planta> 
	public void caminos() {
		List<List<Vertice<Planta>>> listaResultado = gr.caminos(p1, p2);
		int iguales = 0;
		List<Integer> l1 = new ArrayList<Integer>();
		List<Integer> l2 = new ArrayList<Integer>();
		l2.add(p1.getId());
		l2.add(p2.getId());
		List<Integer> l3 = new ArrayList<Integer>();
		l3.add(p1.getId());
		l3.add(p3.getId());
		l3.add(p2.getId());
		for(List<Vertice<Planta>> lista: listaResultado) {
			l1.clear();
			for(Vertice<Planta> vertice: lista) {
				l1.add(vertice.getValor().getId());
			}
			if(l1.equals(l2) || l1.equals(l3)) {
				iguales++;
			}
		}
		assertEquals(2, iguales);
	}
	
	
	// 3) Test del método calcularMin ---------------
	
	@Test
	// La función recibe una lista de rutas entre 2 plantas, cada una con un peso máximo característico, y calcula cuál de
	// esos pesos máximos es el mínimo
	public void calcularMin() {
		// Ejemplo: entre las plantas 1 y 5 está el camino 1 - 3 - 2 - 4 - 5, y el mínimo peso máximo sería 270 kg
		List<Vertice<Planta>> listaPlantas = new ArrayList<Vertice<Planta>>();
		listaPlantas.add(gr.getNodo(p1));
		listaPlantas.add(gr.getNodo(p3));
		listaPlantas.add(gr.getNodo(p2));
		listaPlantas.add(gr.getNodo(p4));
		listaPlantas.add(gr.getNodo(p5));
		assertEquals(270, gr.calcularMin(listaPlantas));
	}
	
	// 4) Test del método flujoMax ---------------
	@Test
	// La función retorna una lista de listas con las rutas que tienen el flujo máximo en términos del peso máximo 
	// que es posible transportar
	// Para el caso donde las plantas origen y destino son 1 y 5, el flujo máximo es 270 kg y existen 2 rutas que cumplen
	// con este criterio: 1 - 2 - 4 - 5 y 1 - 3 - 2 - 4 - 5 
	public void flujoMax() {
		List<List<Vertice<Planta>>> listaResultado = gr.flujoMax(p1, p5, null);
		List<Integer> l1 = new ArrayList<Integer>();
		List<Integer> l2 = new ArrayList<Integer>();
		l2.add(p1.getId());
		l2.add(p2.getId());
		l2.add(p4.getId());
		l2.add(p5.getId());
		List<Integer> l3 = new ArrayList<Integer>();
		l3.add(p1.getId());
		l3.add(p3.getId());
		l3.add(p2.getId());
		l3.add(p4.getId());
		l3.add(p5.getId());
		int iguales = 0;
		for(List<Vertice<Planta>> lista: listaResultado) {
			l1.clear();
			for(Vertice<Planta> vertice: lista) {
				l1.add(vertice.getValor().getId());
			}
			if(l1.equals(l2) || l1.equals(l3)) {
				iguales++;
			}
		}
		assertEquals(2, iguales);
	}

}

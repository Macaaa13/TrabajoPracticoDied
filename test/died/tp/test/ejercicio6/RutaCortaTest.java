package died.tp.test.ejercicio6;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import died.tp.dominio.Planta;
import died.tp.dominio.Ruta;
import died.tp.grafos.GrafoRutas;
import died.tp.grafos.Vertice;

class RutaCortaTest {

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
		Ruta r1 = new Ruta(p1,p2,10,0.1,200);
		Ruta r2 = new Ruta(p2,p1,10,0.1,200);
		Ruta r3 = new Ruta(p1,p3,150,1.5,270);
		Ruta r4 = new Ruta(p1,p4,350,3.5,400);
		Ruta r5 = new Ruta(p1,p5,300,3.0,500);
		Ruta r6 = new Ruta(p3,p4,190,2.25,150);
		Ruta r7 = new Ruta(p3,p5,100,1.0,115);
		Ruta r8 = new Ruta(p5,p4,100,1.0,300);
		//Se agregan las rutas a una lista
		listaRutas.add(r1);
		listaRutas.add(r2);
		listaRutas.add(r3);
		listaRutas.add(r4);
		listaRutas.add(r5);
		listaRutas.add(r6);
		listaRutas.add(r7);
		listaRutas.add(r8);
		// Armado del grafo
		gr.armarGrafo(listaRutas);
	}
	
	// 1) Test del método calcularKmHs ---------------
	
	@Test
	// Si el tipo de ruta es Corta, la función calcula los kilometros totales desde una planta a otra
	public void calcularKmHsKilometros() {
		// Para el camino 1 - 3 - 4, los kilometros entre las plantas 1 y 4 son 340. La función debe devolver dicha cantidad
		List<Vertice<Planta>> lista = new ArrayList<Vertice<Planta>>();
		lista.add(gr.getNodo(p1));
		lista.add(gr.getNodo(p3));
		lista.add(gr.getNodo(p4));
		assertEquals(340, gr.calcularKmHs(lista, "Corta"));
	}
	
	@Test
	// Si el tipo de ruta es Rápida, la función calcula las horas totales desde una planta a la otra
	public void calcularKmHsHoras() {
		// Para el camino 1 - 3 - 5 - 4, la duración total entre las plantas 1 y 4 es de 3.5 horas. La funciónd ebe devolver dicha cantidad
		List<Vertice<Planta>> lista = new ArrayList<Vertice<Planta>>();
		lista.add(gr.getNodo(p1));
		lista.add(gr.getNodo(p3));
		lista.add(gr.getNodo(p5));
		lista.add(gr.getNodo(p4));
		assertEquals(3.5, gr.calcularKmHs(lista, "Rápida"));
	}

	// 2) Test del método getRutaCorta ---------------
	
	@Test
	// Entre las plantas 1 y 4, el camino más corto en kilómetros es 1 - 3 - 4, y los kilómetros totales son 340
	// La función entonces devuelve una lista de listas con ese camino
	public void getRutaCortaKilometros() {
		List<List<Vertice<Planta>>> listaResultado = gr.getRutaCorta(p1, p4, "Corta", null);
		List<Integer> l1 = new ArrayList<Integer>();
		List<Integer> l2 = new ArrayList<Integer>();
		l2.add(p1.getId());
		l2.add(p3.getId());
		l2.add(p4.getId());
		int iguales = 0;
		for(List<Vertice<Planta>> lista: listaResultado) {
			l1.clear();
			for(Vertice<Planta> vertice: lista) {
				l1.add(vertice.getValor().getId());
			}
			if(l1.equals(l2)) {
				iguales++;
			}
		}
		assertEquals(1, iguales);
	}
	
	@Test
	// Entre las plantas 1 y 4, existen 2 caminos cortos en duración: 1 - 4 y 1 - 3 - 5 - 4, y las horas totales son 3.5
	// La función entonces devuelve una lista de listas con esos caminos
	public void getRutaCortaHoras() {
		List<List<Vertice<Planta>>> listaResultado = gr.getRutaCorta(p1, p4, "Rápida", null);
		List<Integer> l1 = new ArrayList<Integer>();
		List<Integer> l2 = new ArrayList<Integer>();
		l2.add(p1.getId());
		l2.add(p4.getId());
		List<Integer> l3 = new ArrayList<Integer>();
		l3.add(p1.getId());
		l3.add(p3.getId());
		l3.add(p5.getId());
		l3.add(p4.getId());
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

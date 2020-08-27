package died.tp.test.ejercicio6;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import died.tp.dominio.Planta;
import died.tp.dominio.Ruta;
import died.tp.grafos.GrafoRutas;

class PageRankTest {

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
	
	// Test del método pageRank
	
	@Test
	// La función devuelve un LinkedHashMap que contiene cada planta con su correspondiente page rank en orden decreciente
	// Según la fórmula para calcular el page rank, los valores para cada planta son:
	// PR(p1) = 1.75 | PR(p2) = 0.625 | PR(p3) = 1 | PR(p4) = 0.5 = PR(p5)
	void test() {
		LinkedHashMap<Planta, Double> plantasPageRank = gr.pageRank();
		LinkedHashMap<Planta, Double> resultado = gr.pageRank();
		plantasPageRank.put(p1, 1.75);
		plantasPageRank.put(p3, 1.0);
		plantasPageRank.put(p2, 0.625);
		plantasPageRank.put(p4, 0.5);
		plantasPageRank.put(p5, 0.5);
		assertEquals(plantasPageRank, resultado);
	}

}

package died.tp.grafos;


import died.tp.dominio.Planta;
import died.tp.dominio.Ruta;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class GrafoRutas extends Grafo<Planta> {

	
	//Constructor
	public GrafoRutas() { 
	}

	
	//Métodos
	/**  El método cumple la función de agregar un vértice y crear una arista si es posible
	 * 	 Se agrega la última planta de la lista, es decir, la última planta creada
	 *   Si hay más de una planta, comienzan a crearse las rutas:
	 *   - Como planta origen se usa la última planta creada
	 *   - Como planta destino se elige una al azar entre las plantas restantes
	 *   Una vez agregada la ruta al grafo, se busca la última arista agregada que contiene todos los datos necesarios
	 *   para crear la ruta, y el controller se ocupa de indicarle al dao que debe guardar los datos en la base de datos
	 */

	public List<Planta> getRutaCorta(Planta destino){
		List<Planta> camino = new ArrayList<Planta>();

		Collections.reverse(camino);
		return camino;
	}

	
	public List<Vertice<Planta>> caminoMasCorto(Planta p1, Planta p2, String tipo){
		List<List<Vertice<Planta>>> todosCaminos = this.caminos(p1, p2);
		Integer pos = 0;
		Double maximo = 0.0;
		for(int i = 0; i < todosCaminos.size(); i++) {
			Double aux = this.sumarKmTiempo(todosCaminos.get(i), tipo);
			if(maximo < aux) {
				maximo = aux;
				pos = i;
			}
		}
		return todosCaminos.get(pos);
	}
	
	public Double sumarKmTiempo(List<Vertice<Planta>> listaVertices,String tipo) {
		Double c = 0.0;
		if(tipo.equals("mas corto")) {
			for(int i = 0; i < listaVertices.size()-1; i++) {
				c+= this.arista(listaVertices.get(i),listaVertices.get(i+1)).getDistancia();
			}
			return c;
		}
		else {
			for(int i = 0; i < listaVertices.size()-1; i++) {
				c+= this.arista(listaVertices.get(i),listaVertices.get(i+1)).getDuracionEstimada();
			}
		}
		return null;
	}
	
	public List<List<Vertice<Planta>>> getRutaCorta(Planta p1, Planta p2, String tipo){ 
        List<List<Vertice<Planta>>> listaCaminos = caminos(getNodo(p1),getNodo(p2));
        Map<List<Vertice<Planta>>, Double> map = new HashMap<List<Vertice<Planta>>, Double>();
        for(List<Vertice<Planta>> listaVert: listaCaminos) {
            map.put(listaVert, calcularKmHs(listaVert, tipo));
        }
        return calcularListaFinal(map);
    }
	
	public List<List<Vertice<Planta>>> calcularListaFinal(Map<List<Vertice<Planta>>, Double> map){
        List<List<Vertice<Planta>>> listaFinal = new ArrayList<List<Vertice<Planta>>>();
        Double min = Collections.max(map.values());
        for(Entry<List<Vertice<Planta>>, Double> entry: map.entrySet()) {
            if(entry.getValue().equals(min)) {
                listaFinal.add(entry.getKey());
            }
        }
        return listaFinal;
    }

    public Double calcularKmHs(List<Vertice<Planta>> lv, String tipo) {
        Double dist = 0.0;
        if(tipo.equals("mas corto")) {
            for(int i=0; i<lv.size()-1; i++) {
                dist += arista(lv.get(i), lv.get(i+1)).getDistancia();
            }
        } else {
            for(int i=0; i<lv.size()-1; i++) {
                dist += arista(lv.get(i), lv.get(i+1)).getDuracionEstimada();
            }
        }
        return dist;
    }

	
	public List<List<Vertice<Planta>>> caminos(Planta p1, Planta p2){
		return this.caminos(new Vertice<Planta>(p1), new Vertice<Planta>(p2));
	}
	
	
	
	public List<List<Vertice <Planta>>> caminos (Vertice <Planta> v1 ,Vertice<Planta> v2){
		List<List<Vertice<Planta>>> salida = new ArrayList<List<Vertice<Planta>>>();
		List<Vertice<Planta>> marcados = new ArrayList <Vertice<Planta>>();
		marcados.add(v1);
		buscarCaminosAux(v1,v2,marcados,salida);
		return salida;
	}
	
	private void buscarCaminosAux(Vertice<Planta> v1, Vertice<Planta> v2, List<Vertice<Planta>> marcados,List<List<Vertice<Planta>>> salida) {
		List<Vertice<Planta>> adyacentes = this.getAdyacentesV(v1);
		List<Vertice<Planta>> copiaMarcados = null;
		for(Vertice<Planta> ady: adyacentes) {
			copiaMarcados = marcados.stream().collect(Collectors.toList());
			if(ady.equals(v2)) {
				copiaMarcados.add(v2);
				salida.add(new ArrayList<Vertice<Planta>>(copiaMarcados));
			}
			else {
				if(!copiaMarcados.contains(ady)) {
					copiaMarcados.add(ady);
					this.buscarCaminosAux(ady, v2, copiaMarcados, salida);
				}
			}

		}
	}

	public void armarGrafo(List<Ruta> rutas) {
		for(Ruta r: rutas) {
			if(!this.getVertices().isEmpty()) {
				if(!this.getVertices().contains(this.getNodo(r.getOrigen()))) { this.addNodo(r.getOrigen());};
				if(!this.getVertices().contains(this.getNodo(r.getDestino()))) { this.addNodo(r.getDestino());}
			}
			else {
				this.addNodo(r.getOrigen());
				this.addNodo(r.getDestino());
			}
			this.conectar(r.getOrigen(), r.getDestino(), r.getDistancia(), r.getDuracionEstimada(), r.getPesoMax());	
		}
	}
}
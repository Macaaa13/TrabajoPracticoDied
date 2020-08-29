package died.tp.test.ejercicio5;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.jupiter.api.*;

import died.tp.controllers.*;
import died.tp.dao.*;
import died.tp.dominio.*;


class Ejercicio5Test {

	//Atributos
	InsumoDao insumoDao;
	PlantaDao pd ;
	Planta p;
	Planta p2;
	OrdenDePedido orden;
	InformacionOrdenController infoController;
	OrdenPedidoController ordenController;
	Map<Insumo,Integer> insumos;
	InsumoLiquido insumoLiquido;
	InsumoGeneral insumoGeneral;
	Date date = new Date(System.currentTimeMillis());
	
	@BeforeEach
	public void setup() {
		insumos = new HashMap<Insumo,Integer>();
		ordenController = new OrdenPedidoController(null);
		infoController = new InformacionOrdenController(null);
		insumoLiquido = new InsumoLiquido("insumoGenerico1","cm3",150,150.00);
		insumoGeneral = new InsumoGeneral("insumoGenerico2","kg",200,150.00);
		pd = new PlantaDao();
		insumoDao = new InsumoDao();
	}
	
	@Test
	void testAgregarOrden() {
		p = new Planta ("planta nro 1");
		pd.altaPlanta(p.getNombrePlanta());

		
		ordenController.actualizarValorCompra(5, 5);
		assertTrue(ordenController.agregarOrden(date, p.getNombrePlanta()));
	}
	
	@Test
	void testControlarStock(){
		insumoDao.altaActualizacionInsumo(insumoGeneral);
		insumoDao.altaActualizacionInsumo(insumoLiquido);	
		insumos.put(insumoGeneral, 5);
		insumos.put(insumoLiquido, 6);
		p2 = new Planta("planta nro 2");
		ordenController.agregarOrden(date, p2.getNombrePlanta());
		infoController.traerTodasOrdenes(1);
		// no contiene insumos la orden
		assertFalse(infoController.controlarStock("2"));
		
	}
}

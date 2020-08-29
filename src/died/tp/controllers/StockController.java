package died.tp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import died.tp.dao.PlantaStockDao;
import died.tp.dominio.Insumo;
import died.tp.dominio.Planta;
import died.tp.dominio.Stock;
import died.tp.jpanel.stock.PanelStock;

public class StockController {

	//Atributos
	private Stock s;
	private Planta p;
	private Map<Stock,Integer> listaStock;
	private PanelStock ps;
	private PlantaStockDao psd;
	
	
	//Constructor
	public StockController(PanelStock ps) {
		this.ps = ps;
		this.listaStock = new HashMap<Stock,Integer>();
		this.psd = new PlantaStockDao();
		this.s = new Stock();
	}

	
	//Métodos
	/* Controla que el insumo exista en la planta especificada
	 */
	public boolean controlarInsumoPlanta(String textFieldInsumo) {
		return psd.obtenerInsumo(textFieldInsumo, p.getId());
	}
	
	/* Se busca la planta pasada por argumento, se setea y de ella se obtiene el stock correspondiente
	 */
	public void setPlanta(String planta) {
		this.p = psd.getPlanta(planta);
		listaStock = p.getStockInsumos();
	}
	
	/* Retorna un Map con los stocks existentes
	 */
	public Map<Stock,Integer> traerTodos(){
		setPlanta(ps.getComboBoxPlanta().getSelectedItem().toString());
		return listaStock;
	}

	/* Si los campos no estan vacíos y tienen el formato correcto, 
	 */
	public void agregarInsumoPlanta() {
		setPlanta(ps.getComboBoxPlanta().getSelectedItem().toString());
		if(!controlarInsumoPlanta(ps.getTextFieldInsumo().getText())) {
			if(validacionVacios()) {
				if(validarCampos()) {
					Insumo i = this.existeInsumo();
					if(i != null) {
						actualizarModelo(i);
						psd.darAltaStock(p.getNombrePlanta(),s);
						JOptionPane.showMessageDialog(null, "Agregado el insumo "+ ps.getTextFieldInsumo().getText()+" a la planta "+ps.getComboBoxPlanta().getSelectedItem().toString());
						ps.limpiar();
					}
					else {
						ps.informarError("El insumo que desea agregar no existe.\n Por favor, agregarlo desde la pestaña Insumos y volver a intentar.");
					}
				}
			}
			else {
				ps.informarError("Por favor, complete todos los campos antes de continuar.");
			}
		}
		else {
			ps.informarError("El insumo ya existe en la planta "+ps.getComboBoxPlanta().getSelectedItem().toString());
		}
	}
	
	/* Retorna el insumo si éste existe o null en caso contrario
	 */
	public Insumo existeInsumo() {
		return psd.traerInsumo(ps.getTextFieldInsumo().getText());	
	}

	/* Valida que los campos no estén vacíos
	 */
	public boolean validacionVacios() {
		if(ps.getTextFieldCantidad().getText().isEmpty()) {return false;}
		if(ps.getTextFieldInsumo().getText().isEmpty()) {return false;}
		if(ps.getTextFieldPuntoPedido().getText().isEmpty()) {return false;}
		return true;
	}

	/* Valida que los campos tengan el formato correcto
	 */
	public boolean validarCampos() {
		if(!isInt(ps.getTextFieldCantidad().getText())) { ps.informarError("El campo \"Cantidad\" debe ser un número"); return false;}
		if(!isInt(ps.getTextFieldPuntoPedido().getText())) { ps.informarError("El campo \"Punto de pedido\" debe ser un número"); return false;}
		return true;
	}
	
	/* Verifica si el string ingresado puede convertirse en int y si ocurre la excepción es porque la cadena que se intentó
	 * convertir no es un número 
	 */
	public boolean isInt(String numero){
	    try{
	        Integer.parseUnsignedInt(numero);
	        return true;
	    }catch(NumberFormatException e){
	        return false;
	    }
	}

	/* Trae todas las plantas de la base de datos
	 */
	public List<String> traerPlantas() {		
		return psd.traerPlantas();
	}

	public void actualizar() {
		actualizarModelo(existeInsumo());
		psd.actualizarStock(p.getId(), s);
		listaStock.clear();
		listaStock = traerTodos();	
	}

	private void actualizarModelo(Insumo i) {
		s.setCantidad(Integer.valueOf(ps.getTextFieldCantidad().getText()));
		s.setProducto(i);
		s.setPuntoPedido(Integer.valueOf(ps.getTextFieldPuntoPedido().getText()));
		
	}
}

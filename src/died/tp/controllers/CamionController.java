package died.tp.controllers;

import died.tp.dao.CamionDao;
import died.tp.dominio.*;
import died.tp.excepciones.*;
import died.tp.jpanel.camion.*;

import java.time.ZoneId;
import java.util.*;
	

public class CamionController {

	//Atributos
	private Camion c;
	private List<Camion> lista;
	private PanelCamiones pc;
	private CamionDao cd;

	
	//Constructor
	public CamionController(PanelCamiones pc) {
		lista = new ArrayList<Camion>();
		this.pc = pc;
		c = new Camion();
		cd = new CamionDao();
	}
	
	
	//Métodos
	/* Si los campos no están vacios y los datos ingresados tienen el formato correcto, se le indica al dao que cree el camión,   
	 * se actuliza la lista de camiones actuales y se informa el éxito en el panel correspondiente
	 */ 
	public void guardar() {
		if(validacionVacios()) {
			if(camposCorrectos()) {
				if(camionYaExistente()) {
					pc.informarSituacion("Ya existe un camión con esas características");
				}
				else {
					this.actualizarModelo();
					cd.altaActualizacionCamion(c);
					lista.clear();
					lista.addAll(cd.buscarCamiones(""));
					pc.informarSituacion("Camión creado exitosamente");
					pc.limpiar();
				}
			}
		}
	}
	
	/* Obtiene del panel correspondiente la información del camión que se desea crear
	 */
	public void actualizarModelo() {
		c = new Camion();
		if(!pc.getTextFieldPatente().getText().isEmpty()) c.setPatente(pc.getTextFieldPatente().getText()); 
		if(!pc.getTextFieldMarca().getText().isEmpty()) c.setMarca(pc.getTextFieldMarca().getText());
		if(!pc.getTextFieldModelo().getText().isEmpty()) c.setModelo(pc.getTextFieldModelo().getText()); 
		if(!pc.getTextFieldKMRecorridos().getText().isEmpty())  c.setKmRecorridos(Double.valueOf(pc.getTextFieldKMRecorridos().getText())); 	
		if(!pc.getTextFieldCostoKM().getText().isEmpty()) c.setCostoKM(Double.valueOf(pc.getTextFieldCostoKM().getText())); 
		if(!pc.getTextFieldCostoHora().getText().isEmpty())  c.setCostoHora(Double.valueOf(pc.getTextFieldCostoHora().getText())); 
		if(pc.getDateChooserFechaCompra().getDate() != null) c.setFechaCompra(pc.getDateChooserFechaCompra().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}
	
	/* Permite determinar si la búsqueda de camiones es por campo o no. 
	 * Una vez realizada se verifica que la lista de camiones obtenidos no esté vacía, informando en la pantalla correspondiente
	 * en caso de que lo esté
	 */
	public List<Camion> buscar() {
		List<Camion> camiones = null;
		actualizarModelo();
		if(camposVacios()) {
			camiones = cd.buscarCamiones("");
		}
		else {
			camiones = cd.buscarCamiones(armarString());
		}
	    if(camiones.isEmpty()) {
			pc.informarSituacion("No se encontraron resultados");
		}
		return camiones;
	}
	
	/* Retorta una lista con todos los camiones almacenados en la base de datos
	 */
	public List<Camion> traerDatos() {
		return cd.buscarCamiones("");
	}

	/* Indica al dao que elimine el camión cuyo id coincide con el pasado por por parámetro
	 */
	public void eliminarCamion(Integer id) {
		cd.eliminarCamion(id);
	}
	
	/* Si los campos no están vacios y los datos ingresados tienen el formato correcto, se le indica al dao que actualice el camión   
	 * correspondente, se actualiza la lista de camiones y se informa el éxito en el panel correspondiente
	 */
	public void actualizar(Integer id) {
		if(validacionVacios()) {
			if(camposCorrectos()) {
				if(camionYaExistente()) {
					pc.informarSituacion("No ha modificado los datos o ya existe un camión con esas características");
				}
				else {
					this.actualizarModelo();
					c.setId(id);
					cd.altaActualizacionCamion(c);
					lista.clear();
					lista.addAll(cd.buscarCamiones(""));
					pc.informarSituacion("Camión modificado exitosamente");
				}
			}
		}
		pc.limpiar();
	}
	
	/* Si al menos un campo está vacío, se indica el error en el panel correspondiente
	 */
	public boolean validacionVacios() {
		if(this.algunCampoVacio()) {
			pc.informarSituacion("Por favor, complete todos los campos antes de continuar.");
			return false;
		}
		else {
			return true;
		}
	}
	
	/* Si al menos un campo está vacío, retorna true
	 */
	public boolean algunCampoVacio() {
		if(pc.getTextFieldPatente().getText().isEmpty() ||  
		   pc.getTextFieldMarca().getText().isEmpty()  ||
		   pc.getTextFieldModelo().getText().isEmpty() ||
		   pc.getTextFieldKMRecorridos().getText().isEmpty() ||
		   pc.getTextFieldCostoKM().getText().isEmpty() ||
		   (pc.getDateChooserFechaCompra().getDate() == null)) {
		   return true;
		}
		return false;
	}
	
	/* Verifica que ningun campo esté vacío
	 */
	public boolean camposVacios() {
		if(pc.getTextFieldPatente().getText().isEmpty() && 
		   pc.getTextFieldMarca().getText().isEmpty()  &&
		   pc.getTextFieldModelo().getText().isEmpty() &&
		   pc.getTextFieldKMRecorridos().getText().isEmpty() &&
		   pc.getTextFieldCostoKM().getText().isEmpty() &&
		   (pc.getDateChooserFechaCompra().getDate() == null)) {
		   return true;
		}
		return false;
	}

	/* Verifica que los campos tengan el formato correcto
	 */
	public boolean camposCorrectos() {
		if(!this.isDouble(pc.getTextFieldKMRecorridos().getText())){ pc.informarSituacion("Error, el campo \"KM recorridos\" no es un número"); return false;}
		if(!this.isDouble(pc.getTextFieldCostoKM().getText())){ pc.informarSituacion("Error, el campo \"Costo por KM\" no es un número"); return false;}
		if(!this.isDouble(pc.getTextFieldCostoHora().getText())){ pc.informarSituacion("Error, el campo \"Costo por hora\" no es un número"); return false;}
		return true;
	}

	/* Verifica si el string ingresado puede convertirse en double, y si ocurre la excepción es porque la cadena que se intentó
	 * convertir no es un número 
	 */
	public boolean isDouble(String numero){
	    try{
	        Double.parseDouble(numero);
	        return true;
	    }catch(NumberFormatException e){
	        return false;
	    }
	}

	/* Si el usuario desea buscar un camión por uno o más campos, la función arma el string que permite realizar la
	 * consulta en la base de datos
	 */
	public String armarString() {
		String s = new String();
		if(c.getPatente()!=null) s+= "PATENTE = '" + c.getPatente() + "' AND " ;
		if(c.getModelo()!=null) s+= "MODELO = '"+ c.getModelo() + "' AND ";
		if(c.getMarca()!=null) s+= "MARCA = '" + c.getMarca() + "' AND ";
		if(c.getKmRecorridos()!=null) s+= "KMRECORRIDOS = '" + c.getKmRecorridos().toString() + "' AND ";
		if(c.getCostoKM()!=null) s+= "COSTOKM = '" + c.getCostoKM().toString() + "' AND ";
		if(c.getCostoHora()!=null) s+= "COSTOHORA = '" + c.getCostoHora().toString() + "' AND ";
		if(c.getFechaCompra()!=null) s+= "FECHACOMPRA = '" + c.getFechaCompra().toString() + "' AND ";
		return s.substring(0, s.length()-4);
	}
	
	/* Permite que no se creen 2 camiones con los mismo datos (sin considerar el id)
	 * También permite implicar que no se cambiaron los datos del camión que se desea modificar
	 */
	public boolean camionYaExistente() {
		actualizarModelo();
		for(Camion camion: cd.buscarCamiones("")) {
			if(camion.equals(c)) {
				return true;
			}
		}
		return false;
	}
	
}

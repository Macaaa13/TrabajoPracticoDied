package died.tp.controllers;

import java.util.*;

import died.tp.dao.InsumoDao;
import died.tp.dominio.*;
import died.tp.jpanel.insumo.PanelInsumos;

public class InsumoController {

	//Atributos
	private Insumo i;
	private Map<Insumo, Integer> listaInsumos;
	private PanelInsumos pi;
	private InsumoDao ind;
	
	
	//Constructor
	public InsumoController(PanelInsumos pi) {
		this.pi = pi;
		listaInsumos = new HashMap<Insumo,Integer>();
		ind = new InsumoDao();
		i = new InsumoGeneral();
	}
	
	
	//M�todos
	/* Si uno de los campos Nombre, Costo y Unidad de Medida est� vac�o, retorna true.
	 * Si ninguno est� vaci�, dependiendo de si el insumo es general o l�quido, verifica que peso o densidad respectivamente no est�n vac�os.
	 * Si el campo correspondiente de esos 2 est� vac�o, retorna true. Si ninguno de los campos est� vac�o retorna false
	 */
	public boolean algunCampoVacio() {
		if(pi.getTextFieldCosto().getText().isEmpty() ||
		   pi.getTextFieldUnidadMedida().getText().isEmpty() ||
		   pi.getTextFieldDescripcion().getText().isEmpty()) {
		   return true;
		}
		if(pi.getComboBoxTipo().getSelectedItem().toString().equals("General")) {
			   if(pi.getTextFieldPeso().getText().isEmpty()) {
				   return true;
			   }
		   }
		   else {
			   if(pi.getTextFieldDensidad().getText().isEmpty()) {
				   return true;
			   }
		   }
		return false;
	}
	
	/* Verifica que ninguno de los campos est� vac�o
	 */
	public boolean camposVacios() {
		if(pi.getTextFieldCosto().getText().isEmpty() &&
		   pi.getTextFieldUnidadMedida().getText().isEmpty() &&
		   pi.getTextFieldDescripcion().getText().isEmpty()) {
			if(pi.getComboBoxTipo().getSelectedItem().toString().equals("General")) {
				if(pi.getTextFieldPeso().getText().isEmpty()) {
					return true;
				}
			}
			else {
				if(pi.getTextFieldDensidad().getText().isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/* Si al menos un campo est� vac�o, se indica el error en el panel correspondiente
	 */
	public boolean validacionVacios() {
		if(this.algunCampoVacio()) {
			pi.informarSituacion("Por favor, complete todos los campos antes de continuar.");
			return false;
		}
		return true;
	}
	
	/* Verifica que los datos ingresados tengan el formato correcto
	 */
	public boolean camposCorrectos() {
		if(!this.isDouble(pi.getTextFieldCosto().getText())) { pi.informarSituacion("Error, el campo costo debe ser un n�mero"); return false;}
		if(pi.getTextFieldPeso().isEditable()) {
			if(!this.isDouble(pi.getTextFieldPeso().getText())) { pi.informarSituacion("Error, el campo peso debe ser un n�mero"); return false;}
		}
		else {
			if(!this.isDouble(pi.getTextFieldDensidad().getText())) { pi.informarSituacion("Error, el campo densidad debe ser un n�mero"); return false;}
		}
		return true;
	}

	/* Dependiendo de si el insumo es L�quido o General, se crea el insumo correspondiente y se almacenan los datos ingresados por el usuario
	 */
	public void actualizarModelo() {
		i = new InsumoGeneral();
		if(!(pi.getTextFieldDensidad().getText().isEmpty())) {
			i = new InsumoLiquido();
			i.setPesoDensidad(Double.valueOf(pi.getTextFieldDensidad().getText()));
		}
		else if(!(pi.getTextFieldPeso().getText().isEmpty())) {
			i.setPesoDensidad(Double.valueOf(pi.getTextFieldPeso().getText()));
		}
		if(!pi.getTextFieldCosto().getText().isEmpty()) i.setCosto(Integer.valueOf(pi.getTextFieldCosto().getText()));
		if(!pi.getTextFieldDescripcion().getText().isEmpty()) i.setDescripcion(pi.getTextFieldDescripcion().getText());
		if(!pi.getTextFieldUnidadMedida().getText().isEmpty()) i.setuMedida(pi.getTextFieldUnidadMedida().getText());
	}
	
	/* Si los campos no est�n vacios y los datos ingresados tienen el formato correcto, se le indica al dao que cree el insumo,   
	 * se actuliza la lista de insumos actuales y se informa el �xito en el panel correspondiente
	 */ 
	public void guardar() {
		if(validacionVacios()) {
			if(camposCorrectos()) {
				if(insumoYaExistente()) {
					pi.informarSituacion("El insumo que desea agregar ya existe");
				}
				else {
					this.actualizarModelo();
					ind.altaActualizacionInsumo(i);
					this.listaInsumos.clear();
					this.listaInsumos = ind.buscarTodos("");
					pi.informarSituacion("Insumo agregado exitosamente");
				}
			}
		}
		pi.limpiar();
	}

	/* Indica al dao que elimine el insumo cuyo id coincide con el pasado por por par�metro
	 */
	public void eliminarInsumo(int id) {
		ind.eliminarI(id);
	}

	/* Retorna un Map que permite cargar la tabla de insumos
	 */
	public Map<Insumo,Integer> buscar(){
		Map<Insumo,Integer> insumos = new HashMap<Insumo,Integer>();
		actualizarModelo();
		if(camposVacios()) {
			insumos = ind.buscarTodos("");
		}
		else {
			insumos = ind.buscarTodos(armarString());
		}
		if(insumos.isEmpty()) {
			pi.informarSituacion("No se encontraron resultados");
		}
		return insumos;
			
	}
	
	/* Retorna un Map con todos los insumos almacenados en la base de datos
	 */
	public Map<Insumo,Integer> traerDatos() {
		return ind.buscarTodos("");
	}
	
	/* Verifica si el string ingresado puede convertirse en double, y si ocurre la excepci�n es porque la cadena que se intent�
	 * convertir no es un n�mero 
	 */
	public boolean isDouble(String numero){
	    try{
	        Double.parseDouble(numero);
	        return true;
	    }catch(NumberFormatException e){
	        return false;
	    }
	}

	/* Si los campos no est�n vacios y los datos ingresados tienen el formato correcto, se le indica al dao que actualice el insumo   
	 * correspondente, se actualiza la lista de insumos y se informa el �xito en el panel correspondiente
	 */
	public void actualizar(Integer id) {
		if(!camposVacios()) {
			if(camposCorrectos()) {
				if(insumoYaExistente()) {
					pi.informarSituacion("2 insumos no pueden tener el mismo nombre");
				}
				else {
					this.actualizarModelo();
					i.setId(id);
					ind.altaActualizacionInsumo(i);
					this.listaInsumos.clear();
					this.listaInsumos = (ind.buscarTodos(""));
					pi.informarSituacion("Insumo modificado exitosamente");
				}
			}
		}
		pi.limpiar();
	}
	
	/* Si el usuario desea buscar un insumo por uno o m�s campos, la funci�n arma el string que permite realizar la
	 * consulta en la base de datos
	 */
	public String armarString() {
		String s = new String();
		if(i.getNombre()!=null) s+= "descripcion = '"+ i.getNombre() + "' AND ";
		if(i.getCosto()!= null) s+= "costoUnidad = '"+ i.getCosto().toString() + "' AND ";
		if(i.getuMedida()!= null) s+= "unidadMedida = '"+ i.getuMedida() + "' AND ";
		if(i.esGeneral() && i.getPesoDensidad()!= null) s+= "peso = '"+ i.getPesoDensidad()+ "' AND ";
		if(i.esLiquido() && i.getPesoDensidad() != null) s+= "densidad = '"+ i.getPesoDensidad()+"' AND ";
		return s.substring(0,s.length()-4);
	}
	
	/* Permite que:
	 * - Al crear un insumo, �ste no tenga el nombre de otro ya existente
	 * - Al modificar un insumo, �ste no intente cambiar su nombre por el de otro ya existente distinto a �l
	 */
	public boolean insumoYaExistente() {
		boolean b = false;
		Map<Insumo,Integer> map = traerDatos();
		for(Insumo insumo: map.keySet()) {
			if(insumo.getNombre().equals(i.getNombre())) {
				if(i.getId()!=null && insumo.getId()!=i.getId()) {
					b = true;
				}
				b = true;
			}
		}
		return b;
	}

}

	



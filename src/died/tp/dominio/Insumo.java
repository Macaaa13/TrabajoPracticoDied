package died.tp.dominio;

public abstract class Insumo {

	//Atributos
	private Integer id;
	private String nombre;
	private String uMedida;
	private Integer costo;
	
	
	
	
	public abstract Double pesoPorUnidad();
	public abstract boolean esLiquido();
	public abstract boolean esGeneral();
	public abstract void setPesoDensidad(Double d);
	public abstract Double getPesoDensidad();
	
	
	//Constructor
	public Insumo(String d, String u, Integer c) {
		this.nombre = d;
		this.uMedida = u;
		this.costo = c;
	}
	
	
	//Getters and setters
		
	public Insumo() {
	
	}
	public String getNombre() {
		return nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDescripcion(String descripcion) {
		this.nombre = descripcion;
	}

	public String getuMedida() {
		return uMedida;
	}

	public void setuMedida(String uMedida) {
		this.uMedida = uMedida;
	}

	public Integer getCosto() {
		return costo;
	}

	public void setCosto(Integer costo) {
		this.costo = costo;
	}
	 
	
	
	
}

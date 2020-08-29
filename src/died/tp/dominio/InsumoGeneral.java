package died.tp.dominio;

public class InsumoGeneral extends Insumo{

	//Atributos
	private Double peso;
	
	
	//Constructores
	public InsumoGeneral(String d, String u, Integer c, Double p) {
		super(d, u, c);
		this.peso = p;
	}

	public InsumoGeneral() {
		// TODO Auto-generated constructor stub
	}


	//Getters y Setters
	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	
	//M�todos
	@Override
	public boolean esLiquido() {
		return false;
	}

	@Override
	public boolean esGeneral() {
		return true;
	}

	@Override
	public void setPesoDensidad(Double peso) {
		this.peso = peso;
		
	}

	@Override
	public Double getPesoDensidad() {
		return this.peso;
	}
	
}

package died.tp.dominio;

public class Stock {

	private Insumo producto;
	private int cantidad;
	private int puntoPedido;
	
	public Stock(Insumo insumo, int cant, int puntoPedido) {
		this.producto = insumo;
		this.cantidad = cant;
		this.puntoPedido = puntoPedido;
	}
	public Stock() {}

	public Insumo getProducto() {
		return producto;
	}

	public void setProducto(Insumo producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public int getPuntoPedido() {
		return puntoPedido;
	}
	public void setPuntoPedido(int puntoPedido) {
		this.puntoPedido = puntoPedido;
	}
	
	
}

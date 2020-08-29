package died.tp.dominio;

import java.time.*;
import java.util.Date;

public class Camion {

	//Atributos
	private Integer id;
	private String patente;
	private String modelo;
	private String marca;
	private Double kmRecorridos;
	private Double costoKM;
	private Double costoHora;
	private LocalDate fechaCompra;
	
	
	//Constructores
	public Camion () { }
	
	public Camion(String patente, String modelo, String marca, Double km, Double costokm, Double costoHora, LocalDate fc) {
		this.patente = patente;
		this.modelo = modelo;
		this.marca = marca;
		this.kmRecorridos = km;
		this.costoKM = costokm;
		this.costoHora = costoHora;
		this.fechaCompra = fc;
	}
	
	
	// Getters y Setters
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPatente() {
		return patente;
	}
	
	public void setPatente(String patente) {
		this.patente = patente;
	}
	
	public String getModelo() {
		return modelo;
	}
	
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public String getMarca() {
		return marca;
	}
	
	public void setMarca(String marca) {
		this.marca = marca;
	}
	
	public Double getKmRecorridos() {
		return kmRecorridos;
	}
	
	public void setKmRecorridos(Double kmRecorridos) {
		this.kmRecorridos = kmRecorridos;
	}
	
	public Double getCostoKM() {
		return costoKM;
	}
	
	public void setCostoKM(Double costoKM) {
		this.costoKM = costoKM;
	}
	
	public Double getCostoHora() {
		return costoHora;
	}
	
	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}
	
	public LocalDate getFechaCompra() {
		return fechaCompra;
	}
	
	public void setFechaCompra(LocalDate fechaCompra) {
		this.fechaCompra = fechaCompra;
	}
	
	public Date getFechaCasteada() {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date date = Date.from(this.fechaCompra.atStartOfDay(defaultZoneId).toInstant());
		return date;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Camion other = (Camion) obj;
		if (costoHora == null) {
			if (other.costoHora != null)
				return false;
		} else if (!costoHora.equals(other.costoHora))
			return false;
		if (costoKM == null) {
			if (other.costoKM != null)
				return false;
		} else if (!costoKM.equals(other.costoKM))
			return false;
		if (fechaCompra == null) {
			if (other.fechaCompra != null)
				return false;
		} else if (!fechaCompra.equals(other.fechaCompra))
			return false;
		if (kmRecorridos == null) {
			if (other.kmRecorridos != null)
				return false;
		} else if (!kmRecorridos.equals(other.kmRecorridos))
			return false;
		if (marca == null) {
			if (other.marca != null)
				return false;
		} else if (!marca.equals(other.marca))
			return false;
		if (modelo == null) {
			if (other.modelo != null)
				return false;
		} else if (!modelo.equals(other.modelo))
			return false;
		if (patente == null) {
			if (other.patente != null)
				return false;
		} else if (!patente.equals(other.patente))
			return false;
		return true;
	}
	
	
	
}

package model;

import java.io.Serializable;

public class DataIOA implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8722402255520111605L;
	private int intentos;
	private String mensaje;
	private long tiempo;
	private int n_aleatorio;
	
	public DataIOA(int n_intentos, String mensaje, long tiempo, int n_aletorio) {
		this.intentos = n_intentos;
		this.mensaje = mensaje;
		this.tiempo = tiempo;
		this.n_aleatorio = n_aletorio;
	}
	
	public DataIOA() {}
	
	//Serializar objeto
	
	
	//Metodos get y set
	public int getIntentos() {
		return intentos;
	}
	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public long getTiempo() {
		return tiempo;
	}
	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}

	public int getN_aleatorio() {
		return n_aleatorio;
	}

	public void setN_aleatorio(int n_aleatorio) {
		this.n_aleatorio = n_aleatorio;
	}
	
	
	

}

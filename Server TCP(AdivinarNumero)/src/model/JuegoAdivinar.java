package model;

import java.util.Scanner;

public class JuegoAdivinar 
{
	private int numero_aleatorio;
	private String jugador;
	private int intentos;
	private boolean estaAdivinado; 
	
	private logRegister tiempo;
	public JuegoAdivinar(String jugador, logRegister tiempo) 
	{
		this.tiempo = tiempo;
		this.jugador = jugador;
		this.intentos = 1;
		this.estaAdivinado = false;
		this.numero_aleatorio = this.generarNumeroAleatorio();
		System.out.println(tiempo.getTiempo()+"Nuevo juego creado, valores estandar definidos");
		System.out.println(tiempo.getTiempo()+"Numero Aleatorio generado:"+this.getNumero_aleatorio());
	}
	
	public String jugar(int numero_leido) 
	{
		System.out.println("Número de intento de "+this.getJugador()+":"+this.getIntentos());
		String mensaje = this.adivinar(numero_leido);
		System.out.println("Mensaje a retornar:"+mensaje);
		System.out.println("Estado de la partida:"+this.isEstaAdivinado());
		return mensaje;
	}
	
	public void jugar() 
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Número de intento de "+this.getJugador()+":"+this.getIntentos());
		//System.out.print(">");
		String mensaje = this.adivinar(input.nextInt());
		System.out.println(this.getIntentos());
		System.out.println(mensaje);
		System.out.println("Estado de la partida:"+this.isEstaAdivinado());
	}
	/**
	 * Genera un número aleatorio entre 1 y el 12
	 * @return Retorna un numero entero del 1 al 12;
	 */
	public int generarNumeroAleatorio() 
	{
		return (int) (Math.floor(Math.random()*12+1));
	}
	
	public String adivinar(int numero_prueba)
	{
		if (this.getNumero_aleatorio() == numero_prueba) 
		{
			this.setEstaAdivinado(true);
			this.disminuirIntentos();
			return "Gano";
		}else{
			this.disminuirIntentos();
			return this.numeroEsta(numero_prueba);
		}
	}
	
	
	
	public String numeroEsta(int numero_prueba) 
	{
		if(this.getIntentos() > 10) 
		{
			return "perdio";
			
		}else {
			return numero_prueba > this.numero_aleatorio ? "debajo":"arriba";
		}
		
	}
	
	public void disminuirIntentos() 
	{
		this.setIntentos(this.getIntentos()+1); 
	}
	
	public int getNumero_aleatorio() {
		return numero_aleatorio;
	}

	public void setNumero_aleatorio(int numero_aleatorio) {
		this.numero_aleatorio = numero_aleatorio;
	}

	public String getJugador() {
		return jugador;
	}

	public void setJugador(String jugador) {
		this.jugador = jugador;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public boolean isEstaAdivinado() {
		return estaAdivinado;
	}

	public void setEstaAdivinado(boolean estaAdivinado) {
		this.estaAdivinado = estaAdivinado;
	}
	
	
}

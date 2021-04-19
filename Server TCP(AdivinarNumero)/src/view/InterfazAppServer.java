package view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

import model.DataIOA;
import model.JuegoAdivinar;
import model.Serializador;
import model.logRegister;

public class InterfazAppServer {
	public static void main(String[] args) {

		
		logRegister in = new logRegister();
		// System.exit(0);
		try {
			ServerSocket server = new ServerSocket(60000);
			System.out.println(in.getTiempo() + "Servidor creado en el puerto "
					+ server.getInetAddress().getHostAddress() + ":" + server.getLocalPort());

			while (!(server.isClosed())) {

				try {
					System.out.println(in.getTiempo() + "Esperando Cliente");
					Socket cliente = server.accept();
					System.out.println(in.getTiempo() + "Cliente aceptado " + cliente.getInetAddress().getHostAddress()
							+ ":" + cliente.getPort());
					DataInputStream dis = new DataInputStream(cliente.getInputStream());
					DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
					JuegoAdivinar nJuego = new JuegoAdivinar("DarkWizard_GM", in);
					do {
						byte[] data = new byte[256];
						long i = System.currentTimeMillis();
						dis.read(data);
						long f = System.currentTimeMillis();
						String mensaje = new String(data).trim();
						if (mensaje.equalsIgnoreCase("Terminar")) {
							dos.write("Terminar".getBytes());
							dis.close();
							dos.close();
							cliente.close();
							System.out.println(in.getTiempo() + "Cliente desconectado");
						} else {
							int numberUser = Integer.parseInt(mensaje);
							if ((!nJuego.isEstaAdivinado()) && nJuego.getIntentos() <= 10) {
								String msgreturn = nJuego.jugar(numberUser);
								
								//Se serializa la data
								DataIOA dataOut = new DataIOA(nJuego.getIntentos(), msgreturn, (f - i),nJuego.getNumero_aleatorio());
								Serializador s = new Serializador();
								//Serializa la informacion a enviar
								s.serializar(dataOut); 
								byte[] arr = s.serializar(dataOut);
								dos.write(arr);
								System.out.println("Mensaje enviado");
							}
						}

					} while (!(cliente.isClosed()));

				} catch (IOException e) {
					System.err.println("Error en la input o output con el cliente" + e);
				}

			}

		} catch (BindException e) {
			// TODO Auto-generated catch block
			System.err.println("La IP ya se encuentre en uso" + e);
		} catch (IOException e) {
			System.err.println("Error en la input o output" + e);
		}

	}

	public String getTiempo() {
		Calendar tiempo = Calendar.getInstance();
		int h = tiempo.get(Calendar.HOUR_OF_DAY);
		int m = tiempo.get(Calendar.MINUTE);
		int s = tiempo.get(Calendar.SECOND);

		return "[" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s) + "]";
	}

}

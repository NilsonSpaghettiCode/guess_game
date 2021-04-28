package view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import model.Config;
import model.DataIOA;
import model.JuegoAdivinar;
import model.Serializador;
import model.logRegister;
import model.readJson;

public class InterfazAppServer {
	public static void main(String[] args) {
		
		
		readJson rj = new readJson();
		Config x = rj.cargarJSON();
		
		logRegister in = new logRegister();
		// System.exit(0);
		int port = x.getPuerto();
		String ip = x.getIp();
		try {
			ServerSocket server = new ServerSocket(port,5,InetAddress.getByName(ip));
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
					byte[] msgName = new byte[256];
					dis.read(msgName);
					
					
					JuegoAdivinar nJuego = new JuegoAdivinar(new String(msgName).trim(), in);
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
			System.err.println("La IP ya se encuentre en uso " + e);
		} catch (IOException e) {
			System.err.println("Error en la input o output" + e);
		} catch (NullPointerException e) {
			// TODO: handle exception
			System.err.println("Algun argumento es nulo");
		}

	}

}

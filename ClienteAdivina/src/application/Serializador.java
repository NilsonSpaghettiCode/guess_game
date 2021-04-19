package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.DataIOA;


public class Serializador {
	
	public byte[] serializar(DataIOA dataout) 
	{
		byte[] data = new byte[1024];
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos); 
			oos.writeObject(dataout);
			data = baos.toByteArray(); 
		} catch (Exception e) {
			// TODO: handle exception
		}
		return data;
	}
	
	public DataIOA desSerializar(byte[] objectArray) 
	{
		
		DataIOA x = new DataIOA(0, "", 0,0);
		ByteArrayInputStream bais = new ByteArrayInputStream(objectArray);
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			try {
				x= (DataIOA)ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("Error, clase no encontrada "+e);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error parametro nulo"+e);
		}
		return x;
	}

}

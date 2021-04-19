package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;


public class readJson 
{
	private String jsonPATH;
	private String jsonContent;
	public readJson() 
	{
		jsonPATH = ".\\filesconfig\\config.json";
		jsonContent = "";
		
	}
	
	public void leerJSON() 
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(jsonPATH));
			String linea ="";
				
			while ((linea = br.readLine()) != null ) {
				jsonContent += linea;
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("El archivo config.json no se ha encontrado");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("El archivo config.json no tiene el contenido esperado");
		}
		
	}
	
	public Config cargarJSON() 
	{
		leerJSON();
		Gson gs = new Gson();
		Config config = gs.fromJson(jsonContent, Config.class);
		return config;
	}
	
	
	public String ConfigToJSON(Config conf) 
	{
		Gson gs = new Gson();
		return gs.toJson(conf);
		
	}
	public void escribirJSON(Config conf) 
	{
		String content = ConfigToJSON(conf);
		System.out.println(content);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(jsonPATH));
			bw.write(content);
			bw.close();
			System.out.println("");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package model;

import java.util.Calendar;

public class logRegister {
	
	public logRegister() 
	{
		
	}
	
	public String getTiempo() {
		Calendar tiempo = Calendar.getInstance();
		int h = tiempo.get(Calendar.HOUR_OF_DAY);
		int m = tiempo.get(Calendar.MINUTE);
		int s = tiempo.get(Calendar.SECOND);

		return "[" + String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s) + "]";
	}

}

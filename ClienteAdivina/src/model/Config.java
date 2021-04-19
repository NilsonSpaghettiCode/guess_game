package model;

public class Config {
	
	private String nickname;
	private String ip;
	private int puerto;
	
	public Config() 
	{
		
	}
	public Config(String Nickname, String ip, int puerto) 
	{
		this.nickname = Nickname;
		this.ip = ip;
		this.puerto = puerto;
	}
	
	
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	

}

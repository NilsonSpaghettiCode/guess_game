package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Config;
import model.readJson;

public class ConfiguracionController implements Initializable{
	@FXML
	private Pane paneScene;

	@FXML
	private Button btnGuardar;

	@FXML
	private Button btnVolver;

	@FXML
	private Text labelTitulo;

	@FXML
	private Text labelNickname;

	@FXML
	private Text labelServer;

	@FXML
	private TextField textFieldApodo;

	@FXML
	private TextField textFieldPuerto;

	@FXML
	private TextField textFieldIP;

	@FXML
	private Text labelPuerto;

	private boolean guardado = false;
	private Config config;
	private readJson rj;
	
	

	@FXML
	void btnGuardarOnAction(ActionEvent event) {
		System.out.println("Hola");
		String nickname ="";
		String ip = "";
		int puerto = 0;
		nickname = textFieldApodo.getText();
		ip = textFieldIP.getText();
		try {
			puerto = Integer.parseInt(textFieldPuerto.getText());
			rj.escribirJSON(new Config(nickname, ip, puerto));
			guardado = true;
			btnGuardar.setDisable(true);
		} catch (Exception e) {
			// TODO: handle exception
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("En el campo Puerto solamente los valores enteros positivo son validos");
			alert.setContentText("Solo funcionana valores enteros positivos hasta 65535");
			alert.show();
		}
		
		

	}

	@FXML
	void btnVolverOnAction(ActionEvent event) {

		if (!(isGuardado())) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("¿Seguro desea salir sin guardar cambios?");
			alert.setContentText("Si sale sin guardar ningun cambio se vera reflejado");
			Optional<ButtonType> decide = alert.showAndWait();
			if (decide.get() == ButtonType.OK) {
				volver(event);
			}

		}else 
		{
			volver(event);
		}

	}
	
	public void volver(ActionEvent event) 
	{
		try {
			Parent pane = FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene nwScene = new Scene(pane, 600, 600);
			Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
			stage.setScene(nwScene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean isGuardado() {
		return guardado;
	}

	public void setGuardado(boolean guardado) {
		this.guardado = guardado;
	}
	
	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		rj = new readJson();
		
		config = rj.cargarJSON();
		
		textFieldApodo.setPromptText(config.getNickname());
		
		textFieldIP.setPromptText(config.getIp());
		
		textFieldPuerto.setPromptText(String.valueOf(config.getPuerto()));
		
		
	}

}

package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Config;
import model.DataIOA;
import model.readJson;

public class InterfazGameController implements Initializable {
	@FXML
	private Text labelAdivinar;

	@FXML
	private Text labelArriba;

	@FXML
	private Text labelAbajo;

	@FXML
	private Text labelRespuestaServidor;

	@FXML
	private Button btnVolver;

	@FXML
	private Button btnProbar;

	@FXML
	private Button btnReplay;

	@FXML
	private Text labelConexion;

	@FXML
	private Text labelIntentos;

	@FXML
	private ChoiceBox<String> cartaChoiseBox;

	@FXML
	private Text labelSelect;

	@FXML
	private Text labelTiempo;

	@FXML
	private ImageView imgViewCarta;

	@FXML
	private ImageView imgViewArriba;

	@FXML
	private ImageView imgViewAbajo;

	@FXML
	private Text labelIntentoX;

	@FXML
	private Text labelTiempoX;
	
	@FXML
    private Text labelEstadoConectionT;

	private Socket cliente;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Glow effectoGlow = new Glow(1);
	private int tiempo;

	private Config config;

	public InterfazGameController() {
		readJson rj = new readJson();
		this.config = rj.cargarJSON();

		this.connectar(config.getIp(), config.getPuerto(), config.getNickname());
		tiempo = 0;

	}

	public void cargarOpciones() {
		for (int i = 1; i <= 12; i++) {
			cartaChoiseBox.getItems().add(String.valueOf(i));
		}
	}

	public void connectar(String ip, int puerto, String name) {
		cliente = new Socket();
		try {
			cliente.connect(new InetSocketAddress(ip, puerto), 2000);

			dis = new DataInputStream(cliente.getInputStream());
			dos = new DataOutputStream(cliente.getOutputStream());
			dos.write(name.getBytes());
			System.out.println("Nombre de usuario enviado");

		} catch (SocketTimeoutException e) {
			Alert alertaTI = new Alert(AlertType.ERROR);
			alertaTI.setContentText("Error al intentar conectar, el tiempo de espera se agoto, ERROR 504");
			alertaTI.show();
		} catch (ConnectException e) {
			Alert alertaCE = new Alert(AlertType.ERROR);
			alertaCE.setContentText("Error al intentar conectar, el tiempo de espera se agoto, ERROR 504");
			alertaCE.show();
		} catch (UnknownHostException e) {
			Alert alertaIO = new Alert(AlertType.ERROR);
			alertaIO.setContentText("Host desconocido");
			alertaIO.show();

		} catch (IOException e) {
			// TODO: handle exception
			Alert alertaIO = new Alert(AlertType.ERROR);
			alertaIO.setContentText("Error al intentar conectar, el input o el output son nulos o son invalido");
			alertaIO.show();
		} catch (SecurityException e) {
			Alert alertaIO = new Alert(AlertType.ERROR);
			alertaIO.setContentText("La seguridad del host no permite la conexion");
			alertaIO.show();
		}

	}

	@FXML
	void probarOnAction(ActionEvent event) {
		if (cliente.isConnected()) {
			if (!(cliente.isClosed())) {
				String number = cartaChoiseBox.getValue();
				System.out.println("Respuesta a enviar:" + number);
				try {
					byte[] data = new byte[1024];
					dos.write(number.getBytes());
					dis.read(data);
					System.out.println("Respuesta Recibida");
					Serializador s = new Serializador();
					DataIOA inputData = s.desSerializar(data);

					String msg = inputData.getMensaje();
					int intentos = inputData.getIntentos();
					tiempo += inputData.getTiempo();

					this.cambiarEtiquetas((intentos - 1), (tiempo / 1000));

					if (msg.equalsIgnoreCase("gano")) {

						Alert alerta = new Alert(AlertType.INFORMATION);
						alerta.setHeaderText("Usted " + msg);
						alerta.setContentText(this.getMensaje((intentos - 1), inputData.getN_aleatorio()));
						alerta.show();
						btnProbar.setDisable(true);
						btnReplay.setDisable(false);
					} else if (msg.equalsIgnoreCase("perdio")) {
						Alert alerta = new Alert(AlertType.INFORMATION);
						alerta.setHeaderText("Usted " + msg);
						alerta.setContentText(this.getMensaje((intentos - 1), inputData.getN_aleatorio()));
						alerta.show();
						btnProbar.setDisable(true);
						btnReplay.setDisable(false);

					} else {
						imgViewArriba.setEffect(null);
						imgViewAbajo.setEffect(null);
						if (msg.equalsIgnoreCase("arriba")) {
							imgViewArriba.setEffect(effectoGlow);

						} else if (msg.equalsIgnoreCase("debajo")) {
							imgViewAbajo.setEffect(effectoGlow);
						}

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("Error con los valores " + e);
					this.cerrarIO();
					this.setLabelConexion();
					Alert alertaIO = new Alert(AlertType.ERROR);
					alertaIO.setContentText("Error al intentar conectar, el input o el output son nulos o son invalido, revise que el servidor este ON");
					alertaIO.show();
					this.btnProbar.setDisable(true);
				}
			} else {
				this.setLabelConexion();
			}
			
		}
		
		

	}

	@FXML
	void btnReplayOnAction(ActionEvent event) {
		this.cerrarConexion();
		try {
			Parent pane = FXMLLoader.load(getClass().getResource("xd.fxml"));
			Scene sceneGame = new Scene(pane, 600,600);
			Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
			sceneGame.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(sceneGame);
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}

	public String getMensaje(int intentos, int na) {
		System.out.println("En mensaje intentos: " + intentos);
		if (intentos >= 8 && intentos <= 10) {
			return "Eres un ingenuo, te he vencido, el número secreto es: " + na;
		} else if (intentos >= 6 && intentos <= 7) {
			return "Eres un novato, has tardado mucho el número secreto adivinado es: " + na;

		} else if (intentos >= 4 && intentos <= 5) {
			return "Eres un Aprendiz, podrías haberlo descuibierto en menor número de intentos, el número secreto adivinado es: "
					+ na;
		} else if (intentos > 1 && intentos <= 3) {
			return "Eres un Magister has gastado muy pocos intentos, el número secreto adivinado es: " + na;
		} else {
			return "Me has vencido en el primer intento, realmente eres un Maestro, el número secreto adivinado es: "
					+ na;
		}
	}

	public void cambiarEtiquetas(int intentos, int tiempo) {
		this.labelIntentoX.setText(String.valueOf(intentos));
		this.labelTiempoX.setText(String.valueOf(tiempo) + " s");
	}
	
	public void cerrarIO() 
	{
		try {
			this.dis.close();
			this.dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("IOs nunca fueron creados "+ e);
		}
	}

	@FXML
	void volverOnAction(ActionEvent event) {
		Alert alerta = new Alert(AlertType.CONFIRMATION,
				"Si vuelves a la pantalla anterior se cerrara la conexion con el servidor");
		Optional<ButtonType> seleccion = alerta.showAndWait();

		if (seleccion.get() == ButtonType.OK) {
			if (cliente.isConnected()) {
				this.cerrarConexion();
			} else {
				System.out.println("No ha existido una conexion");
			}

			try {
				Parent pane = FXMLLoader.load(getClass().getResource("Sample.fxml"));
				Scene nwScene = new Scene(pane, 600, 600);
				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
				stage.setScene(nwScene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Error al cargar la interfaz "+e);
			}
		}
	}

	/**
	 * Este metodo cierra la conexion en caso de existir alguna conexion activa con
	 * el servidor
	 */
	public void cerrarConexion() {
		try {
			dos.write("Terminar".getBytes());
			byte[] confirm = new byte[256];
			dis.read(confirm);
			String msg = new String(confirm).trim();
			if (msg.equalsIgnoreCase("Terminar")) {
				dos.close();
				dis.close();
				cliente.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error en la IO, no esta creada, o perdio su asociacion "+e);
		}

	}

	public void setLabelConexion() {
		if (cliente.isConnected()) {
			if (!(cliente.isClosed())) {
				labelEstadoConectionT.setText("Conectado");
			} else {
				labelEstadoConectionT.setText("Desconectado");
			}
		}else 
		{
			labelEstadoConectionT.setText("Desconectado");
		}

	}

	/**
	 * Este metodo inicializa todos los objetos del interfaz y sus componentes
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.cargarOpciones();
		this.setLabelConexion();
		this.labelIntentoX.setText("0");
		this.labelTiempoX.setText("0");
		this.labelEstadoConectionT.setText(" ");
		this.cartaChoiseBox.setOnAction((event) -> {
			seleccionarCarta(cartaChoiseBox.getValue());
		});
		
		this.cartaChoiseBox.setValue("1");
		this.setLabelConexion();
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void seleccionarCarta(String numero) {
		switch (numero) {
		case "1":
			Image img = new Image("/cartas/AC.png");
			imgViewCarta.setImage(img);
			break;
		case "2":
			Image img2 = new Image("/cartas/2C.png");
			imgViewCarta.setImage(img2);
			break;
		case "3":
			Image img3 = new Image("/cartas/3C.png");
			imgViewCarta.setImage(img3);
			break;
		case "4":
			Image img4 = new Image("/cartas/4C.png");
			imgViewCarta.setImage(img4);
			break;
		case "5":
			Image img5 = new Image("/cartas/5C.png");
			imgViewCarta.setImage(img5);
			break;
		case "6":
			Image img6 = new Image("/cartas/6C.png");
			imgViewCarta.setImage(img6);
			break;
		case "7":
			Image img7 = new Image("/cartas/7C.png");
			imgViewCarta.setImage(img7);
			break;
		case "8":
			Image img8 = new Image("/cartas/8C.png");
			imgViewCarta.setImage(img8);
			break;
		case "9":
			Image img9 = new Image("/cartas/9C.png");
			imgViewCarta.setImage(img9);
			break;
		case "10":
			Image img10 = new Image("/cartas/0C.png");
			imgViewCarta.setImage(img10);
			break;
		case "11":
			Image img11 = new Image("/cartas/JC.png");
			imgViewCarta.setImage(img11);
			break;
		case "12":
			Image img12 = new Image("/cartas/KC.png");
			imgViewCarta.setImage(img12);
			break;
		default:
			break;
		}

	}

}

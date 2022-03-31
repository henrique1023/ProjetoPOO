package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemPaciente;
	
	@FXML
	private MenuItem menuItemConsulta;
	
	@FXML
	private MenuItem menuItemEspecializacao;
	
	@FXML
	private MenuItem menuItemMedico;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	private void onMenuItemPacienteAction() {
		System.out.println("OK");
	}
	
	@FXML
	private void onMenuItemConsultaAction() {
		System.out.println("OK");
	}
	
	@FXML
	private void onMenuItemEspecializacaoAction() {
		System.out.println("OK");
	}
	
	@FXML
	private void onMenuItemMedicoAction() {
		System.out.println("OK");
	}
	
	@FXML
	private void onMenuItemSobreAction() {
		loadView("/gui/about.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	// criando a tela dentro da tela e obrigando a ela ser sincronizado
		public synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
				VBox newVBox = loader.load();
				
				//Instancia a cena principal
				Scene mainScene = Main.getMainScene();
				
				//pega o elemento VBox da tela principal
				VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
				
				//Pega o primeiro elemento children da tela e salva tudo na variavel
				Node mainMenu = mainVBox.getChildren().get(0);
				
				//Limpa a tela
				mainVBox.getChildren().clear();
				
				//Recoloca a primeira cena
				mainVBox.getChildren().add(mainMenu);
				
				//Adiciona os elementos da VBox nova
				mainVBox.getChildren().addAll(newVBox.getChildren());
				
				//toda função irá receber getcontroller
				T controller = loader.getController();
				//essa parte que inicia a função passada
				initializingAction.accept(controller);
				
			} catch (IOException e) {
				Alerts.showAlert("IO Exception", "Error loading vier", e.getMessage(), AlertType.ERROR);
			}
		}

}

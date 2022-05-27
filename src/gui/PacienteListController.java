package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Paciente;
import model.services.PacienteService;

public class PacienteListController implements Initializable, DataChangeListener{
	
	private PacienteService paciente;
	
	@FXML
	private TableView<Paciente> tableViewPaciente;
	
	@FXML
	private TableColumn<Paciente, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Paciente, String> tableColumnTel;
	
	@FXML
	private TableColumn<Paciente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Paciente, Date> tableColumnData;
	
	@FXML
	private TableColumn<Paciente, Paciente> tableColumnEDIT;

	@FXML
	private TableColumn<Paciente, Paciente> tableColumnREMOVE;
	
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Paciente> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Paciente obj = new Paciente();
		createDialogForm(obj, "/gui/PacienteForm.fxml", parentStage);
		
	}
	
	public void setPacienteService(PacienteService p) {
		this.paciente = p;
	}
	
	private void initializeNodes() {
		// essa função inicia os valores dentro das tabelas
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nomePaciente"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataAniversario"));
		tableColumnTel.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		Utils.formatTableColumnTelefone(tableColumnTel);
		Utils.formatTableColumnCpf(tableColumnCpf);
		Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");

		// essa metodo faz a tabela acompanhar o tamanho da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPaciente.prefHeightProperty().bind(stage.heightProperty());
	}
	
	private void createDialogForm(Paciente obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// esse metodo injeta o paciente iniciando o objeto
			// sempre que tem que ter um objeto no formulario
			// precisa injetar ele aqui
			PacienteFormController controller = loader.getController();
			controller.setEntidade(obj);
			controller.setService(new PacienteService());
			// esse metodo que gera a atualização após salvar um novo departamento
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();

			dialogStage.setTitle("Entre com um Paciente");
			// passa o FXML departmentForm como cena
			dialogStage.setScene(new Scene(pane));
			// Essa janela não pode ser redimencionada
			dialogStage.setResizable(false);
			// Informa qual é o stage pai dessa nova janela
			dialogStage.initOwner(parentStage);
			// informa que não pode mexer no programa sem fechar essa janela
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	
	
	public void updateTableView() {
		if (paciente == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Paciente> list = paciente.findAll();
		// somente um ObservableList pode passar parametros para o setItems
		obsList = FXCollections.observableArrayList(list);
		tableViewPaciente.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	// ESSE METODO CRIA OS BOTÕES DE EDITAR NO PAINEL
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Paciente, Paciente>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Paciente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/PacienteForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// ESSE METODO CRIA OS BOTÕES DE REMOVER NO PAINEL
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Paciente, Paciente>() {
			private final Button button = new Button("Delete");

			@Override
			protected void updateItem(Paciente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}

		});
	}
	
	private void removeEntity(Paciente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if(result.get() == ButtonType.OK) {
			if(paciente == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				paciente.remove(obj);
				updateTableView();
			}catch (DbException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}

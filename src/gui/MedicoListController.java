package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.mainfx.Main;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Especializacao;
import model.entities.Profissional;
import model.services.EspecializacaoService;
import model.services.ProfissionalService;

public class MedicoListController implements Initializable, DataChangeListener{


	private ProfissionalService medicoService;

	@FXML
	private Button btNovo;
	
	@FXML
	private Button btBuscar;
	
	@FXML
	private TextField txtCampoBuscar;

	@FXML
	private TableView<Profissional> tableViewProfissional;

	@FXML
	private TableColumn<Profissional, String> tableColumnNome;

	@FXML
	private TableColumn<Profissional, String> tableColumnEmail;

	@FXML
	private TableColumn<Profissional, Date> tableColumnData;

	@FXML
	private TableColumn<Profissional, Double> tableColumnSalario;

	@FXML
	private TableColumn<Profissional, Especializacao> tableColumnEspec;
	
	@FXML
	private TableColumn<Profissional, Profissional> tableColumnEDIT;

	@FXML
	private TableColumn<Profissional, Profissional> tableColumnREMOVE;
	
	private ObservableList<Profissional> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Profissional obj = new Profissional();
		createDialogForm(obj, "/gui/MedicoForm.fxml", parentStage);
		
	}
	
	@FXML
	public void onBtBuscar(ActionEvent event) {
		if(txtCampoBuscar.getText() == null || txtCampoBuscar.getText().trim().equals("")) {
			updateTableView();
			initializeNodes();
		}else {
			List<Profissional> list = medicoService.findByNome(txtCampoBuscar.getText());
			obsList = FXCollections.observableArrayList(list);
			tableViewProfissional.setItems(obsList);
			initializeNodes();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}
	
	public void setMedicoService(ProfissionalService service) {
		this.medicoService = service;
	}
	
	public void updateTableView() {
		if (medicoService == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Profissional> list = medicoService.findAll();
		// somente um ObservableList pode passar parametros para o setItems
		obsList = FXCollections.observableArrayList(list);
		tableViewProfissional.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void initializeNodes() {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataAniver"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnSalario.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		tableColumnEspec.setCellValueFactory(new PropertyValueFactory<>("especializacao"));
		Utils.formatTableColumnDouble(tableColumnSalario, 2);
		Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");
		// essa metodo faz a tabela acompanhar o tamanho da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProfissional.prefHeightProperty().bind(stage.heightProperty());
	}
	
	private void createDialogForm(Profissional obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// esse metodo injeta o paciente iniciando o objeto
			// sempre que tem que ter um objeto no formulario
			// precisa injetar ele aqui
			MedicoFormController controller = loader.getController();
			controller.setEntidade(obj);
			controller.setService(new ProfissionalService(), new EspecializacaoService());
			controller.loadAssociatedObjects();
			// esse metodo que gera a atualização após salvar um novo departamento
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();

			dialogStage.setTitle("Modificar Médico");
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

	// ESSE METODO CRIA OS BOTÕES DE EDITAR NO PAINEL
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Profissional, Profissional>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Profissional obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/MedicoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// ESSE METODO CRIA OS BOTÕES DE REMOVER NO PAINEL
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Profissional, Profissional>() {
			private final Button button = new Button("Delete");

			@Override
			protected void updateItem(Profissional obj, boolean empty) {
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
	
	private void removeEntity(Profissional obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if(result.get() == ButtonType.OK) {
			if(medicoService == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				medicoService.remove(obj);
				updateTableView();
			}catch (DbException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}

package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Paciente;
import model.services.PacienteService;

public class PacienteListController implements Initializable, DataChangeListener{
	
	private PacienteService paciente;
	
	@FXML
	private TableView<Paciente> tableViewPaciente;
	
	@FXML
	private TableColumn<Paciente, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Paciente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Paciente, Date> tableColumnData;
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Paciente> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		
	}
	
	public void setPacienteService(PacienteService p) {
		this.paciente = p;
	}
	
	private void initializeNodes() {
		// essa função inicia os valores dentro das tabelas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("IdPaciente"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nomePaciente"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataAniversario"));
		Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");

		// essa metodo faz a tabela acompanhar o tamanho da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPaciente.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (paciente == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Paciente> list = paciente.findAll();
		// somente um ObservableList pode passar parametros para o setItems
		obsList = FXCollections.observableArrayList(list);
		tableViewPaciente.setItems(obsList);
//		initEditButtons();
//		initRemoveButtons();
	}
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}

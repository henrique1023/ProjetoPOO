package gui;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Especializacao;
import model.entities.Profissional;
import model.exceptions.ValidationException;
import model.services.EspecializacaoService;
import model.services.ProfissionalService;

public class MedicoFormController implements Initializable {

	private Profissional entidade;

	private ProfissionalService service;

	private EspecializacaoService especService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtSalario;

	@FXML
	private DatePicker dpDataAniv;

	@FXML
	private ComboBox<Especializacao> comboBoxEspec;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorData;

	@FXML
	private Label labelErrorSalario;

	@FXML
	private Label labelErrorEspec;

	@FXML
	private ObservableList<Especializacao> obsList;

	public void setEntidade(Profissional entidade) {
		this.entidade = entidade;
	}

	public void setService(ProfissionalService service, EspecializacaoService service2) {
		this.service = service;
		this.especService = service2;
	}

	// esse metodo inscreve a outra interface para ouvir essa interface
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// esse metodo que gera o novo alerta para o sistema
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entidade == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entidade = getFormData();
			service.saveOrUptade(entidade);
			// quando é confirmado o salvar ele emite um alerta para atualizar a pagina
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorsMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error savings Object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private Profissional getFormData() {
		Profissional obj = new Profissional();

		ValidationException exception = new ValidationException("Validation Errors");

		obj.setIdProfi(entidade.getIdProfi());
		obj.setNome(txtNome.getText());
		obj.setEmail(txtEmail.getText());
		obj.setSalarioBase(Utils.tryParseToDouble(txtSalario.getText()));
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campo está vazio!!");
		}

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Campo está vazio!!");
		}

		if (txtSalario.getText() == null || txtSalario.getText().trim().equals("")) {
			exception.addError("salario", "Campo está vazio!!");
		}

		if (dpDataAniv.getValue() == null) {
			exception.addError("dataAniv", "Campo está vazio!!");
		} else {
			Instant instant = Instant.from(dpDataAniv.getValue().atStartOfDay(ZoneId.systemDefault()));

			obj.setDataAniver(Date.from(instant));
		}

		obj.setEspecializacao(comboBoxEspec.getValue());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void initializeNodes() {
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldMaxLength(txtEmail, 100);
		Utils.formatDatePicker(dpDataAniv, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtSalario);

		initializeComboBoxEspec();
	}

	private void initializeComboBoxEspec() {
		Callback<ListView<Especializacao>, ListCell<Especializacao>> factory = lv -> new ListCell<Especializacao>() {
			@Override
			protected void updateItem(Especializacao item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeEspeci());
			}
		};
		comboBoxEspec.setCellFactory(factory);
		comboBoxEspec.setButtonCell(factory.call(null));
	}

	public void loadAssociatedObjects() {
		if (especService == null) {
			throw new IllegalStateException("Sem Especializações!");
		}
		List<Especializacao> list = especService.findAll();

		// esse função pega os itens da lista e passa para obsList
		obsList = FXCollections.observableArrayList(list);

		// comboBox só aceita itens que estejam no ObservableList
		comboBoxEspec.setItems(obsList);
	}

	// Esse metodo inicia os valores no campos
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entity was null!!");
		}
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		txtSalario.setText("" + entidade.getSalarioBase());

		// se a data na entidade estiver vazia ele manda nulo para a tela
		if (entidade.getDataAniver() != null) {
			dpDataAniv.setValue(LocalDate.parse(sdf.format(entidade.getDataAniver()).toString()));

		}

		if (entidade.getEspecializacao() == null) {
			comboBoxEspec.getSelectionModel().selectFirst();
		} else {
			comboBoxEspec.setValue(entidade.getEspecializacao());
		}
	}

	// esse metodo verifica se tem o erro e manda ele para o label
	public void setErrorsMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorNome.setText(fields.contains("nome") ? errors.get("nome") : "");
		
		labelErrorEmail.setText(fields.contains("email") ? errors.get("email") : "");

		labelErrorSalario.setText(fields.contains("salario") ? errors.get("salario") : "");
		
		labelErrorData.setText(fields.contains("dataAniv") ? errors.get("dataAniv") : "");
	}

}

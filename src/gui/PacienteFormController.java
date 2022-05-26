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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Paciente;
import model.exceptions.ValidationException;
import model.services.PacienteService;

public class PacienteFormController implements Initializable {

	private Paciente entidade;

	private PacienteService service;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private DatePicker dpDataAniv;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Label labelErrorData;

	public void setEntidade(Paciente entidade) {
		this.entidade = entidade;
	}

	public void setService(PacienteService service) {
		this.service = service;
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
			// quando � confirmado o salvar ele emite um alerta para atualizar a pagina
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorsMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error savings Object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private Paciente getFormData() {
		Paciente obj = new Paciente();

		ValidationException exception = new ValidationException("Validation Errors");

//		obj.setIdPaciente(Utils.tryParseToInt(txtId.getText()));

		obj.setNomePaciente(txtNome.getText());
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Fields can't be emply");
		}
		
		if (dpDataAniv.getValue() == null) {
			exception.addError("dataAniv", "Fields can't be emply");
		}else {
			Instant instant = Instant.from(dpDataAniv.getValue().atStartOfDay(ZoneId.systemDefault()));
		
			obj.setDataAniversario(Date.from(instant));
		}

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
		Utils.formatDatePicker(dpDataAniv, "dd/MM/yyyy");
	}

	// Esse metodo inicia os valores no campos
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entity was null!!");
		}
		txtNome.setText(entidade.getNomePaciente());

		// se a data na entidade estiver vazia ele manda nulo para a tela
		if (entidade.getDataAniversario() != null) {
			dpDataAniv.setValue(LocalDate.parse(sdf.format(entidade.getDataAniversario()).toString()));
			
		}
	}
	
	//esse metodo verifica se tem o erro e manda ele para o label
		public void setErrorsMessages(Map<String, String> errors) {
			Set<String> fields = errors.keySet();
			
			if(fields.contains("nome")) {
				labelErrorNome.setText(errors.get("nome"));
			}
			
			if(fields.contains("dataAniv")) {
				labelErrorNome.setText(errors.get("dataAniv"));
			}
		}

}
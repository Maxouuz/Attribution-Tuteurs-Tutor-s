package sae_ihm;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import sae_201_02.Subject;

public class ChooseSubjectController {
	
	private Subject subject;
	@FXML ComboBox<Subject> cbSubject;
	
	public Subject getSubject() {
		return subject;
	}
	
	public void initialize() {
		cbSubject.getItems().addAll(Subject.values());
		cbSubject.getSelectionModel().select(0);
	}
	
	@FXML
	public void confirm() {
		subject = cbSubject.getSelectionModel().getSelectedItem();
		Stage stage = (Stage) cbSubject.getScene().getWindow();
		stage.close();
	}
}

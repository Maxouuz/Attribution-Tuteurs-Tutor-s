package sae_ihm;

import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sae_201_02.ExceptionPromo;
import sae_201_02.Student;
import sae_201_02.Tutoring;

public class AddStudentController {
	
	private Tutoring tutoring;
	
	final ContextMenu ineValidator = new ContextMenu();
	final ContextMenu forenameValidator = new ContextMenu();
	final ContextMenu nameValidator = new ContextMenu();
	
	@FXML Spinner<Integer> spinnerINE;
	@FXML TextField tfForename;
	@FXML TextField tfName;
	@FXML Spinner<Integer> spinnerPromo;
	@FXML Spinner<Integer> spinnerAbsences;
	@FXML Spinner<Double> spinnerMoyenne;
	
	public void setTutoring(Tutoring tutoring) {
		this.tutoring = tutoring;
	}
	
	public void initialize() {
		spinnerINE.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, Student.getNonUsedINE()));
		spinnerPromo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3));
		spinnerAbsences.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
		spinnerMoyenne.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 20));
	}
	
	@FXML
	public void confirm() {		
		forenameValidator.setAutoHide(false);
		nameValidator.setAutoHide(false);
		
		if (tfForename.getText().isBlank()) {
			forenameValidator.getItems().clear();
			forenameValidator.getItems().add(
                    new MenuItem("Veuillez entrer un prénom."));
			forenameValidator.show(tfForename, Side.RIGHT, 10, 0);
        } else if (tfName.getText().isBlank()) {
			nameValidator.getItems().clear();
			nameValidator.getItems().add(
                    new MenuItem("Veuillez entrer un nom."));
			nameValidator.show(tfName, Side.RIGHT, 10, 0);
        } else {
        	try {
				Student student = Student.createStudent(
						spinnerINE.getValue(),
						tfForename.getText(),
						tfName.getText(),
						spinnerPromo.getValue(),
						spinnerAbsences.getValue()
				);
				
				student.setMoyenne(tutoring.getSubject(), spinnerMoyenne.getValue());
				tutoring.addStudent(student);
				Stage stage = (Stage) spinnerINE.getScene().getWindow();
			    stage.close();
			} catch (IllegalArgumentException | ExceptionPromo e) {
				ineValidator.getItems().clear();
				ineValidator.getItems().add(
	                    new MenuItem("L'INE existe déjà!"));
				ineValidator.show(spinnerINE, Side.RIGHT, 10, 0);
				return;
			}
        }
	}
}

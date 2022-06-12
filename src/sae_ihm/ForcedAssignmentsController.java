package sae_ihm;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sae_201_02.ExceptionNotInTutoring;
import sae_201_02.ExceptionPromo;
import sae_201_02.ExceptionTooManyAssignments;
import sae_201_02.Student;
import sae_201_02.Tutoring;

public class ForcedAssignmentsController extends StudentsTable {
	
	private Tutoring tutoring;
	private Student selected;
		
	@FXML Button assignmentButton;

	public void setTutoring(Tutoring tutoring) {
		this.tutoring = tutoring;
	}

	public void setSelected(Student selected) {
		this.selected = selected;
		assignmentButton.setText(assignmentButton.getText() + selected.getForename() + " " + selected.getName());
	}
	
	@Override
	public void updateTable() {
		studentsTable.getItems().clear();
		
		if (selected.isTutor()) {
			studentsTable.getColumns().remove(promoCol);
			studentsTable.getItems().addAll(tutoring.getTutees());
		} else {
			if (!studentsTable.getColumns().contains(promoCol)) studentsTable.getColumns().add(2, promoCol);
			studentsTable.getItems().addAll(tutoring.getTutors());
		}
		
		// On ne met pas dans la liste tous les étudiants qu'on ne peut plus forcer ou qui sont à ne pas affecter
		Set<Student> toRemove = new HashSet<>();
		for (Student student: studentsTable.getItems()) {
			if (!student.canAddMoreForcedAssignment(tutoring) || selected.getStudentsToNotAssign(tutoring).contains(student)) {
				toRemove.add(student);
			}
		}
		studentsTable.getItems().removeAll(toRemove);
	}
	
	@FXML
	public void confirm() throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		for (Student student: studentsTable.getSelectionModel().getSelectedItems()) {
			selected.forceAssignment(tutoring, student);
		}
		
	    Stage stage = (Stage) assignmentButton.getScene().getWindow();
	    stage.close();
	}
}

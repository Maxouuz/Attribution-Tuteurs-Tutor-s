package sae_ihm;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.json.JSONException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import sae_201_02.ExceptionNotInTutoring;
import sae_201_02.ExceptionPromo;
import sae_201_02.ExceptionTooManyAssignments;
import sae_201_02.Motivation;
import sae_201_02.Student;
import sae_201_02.Tutoring;
import sae_201_02.TutoringSave;

public class MainController {
	@FXML TableView<Student> allTable;
	@FXML TableView tutorsTable;
	@FXML TableView tuteesTable;
	 
	@FXML TableColumn<Student, String> forenameCol;
	@FXML TableColumn<Student, String> nameCol;
	@FXML TableColumn<Student, Integer> promoCol;
	@FXML TableColumn<Student, Integer> absencesCol;
	@FXML TableColumn<Student, Integer> assignmentsCol;
	
	@FXML VBox rightSide;
	@FXML VBox profileView;
	
	@FXML Label labelName;
	@FXML Label	labelMail;
	
	@FXML Label labelMoyenne;
	@FXML Label labelPromo;
	@FXML Label labelAbsences;
	@FXML ChoiceBox<Motivation> cbMotivation;
	
	@FXML GridPane gridAssignments; 
	
	@FXML ToggleGroup widthAbsences;
	@FXML RadioButton notImportantAbsences;
	@FXML RadioButton normalAbsences;
	@FXML RadioButton importantAbsences;
	
	@FXML ToggleGroup widthMoyenne;
	@FXML RadioButton notImportantNote;
	@FXML RadioButton normalNote;
	@FXML RadioButton importantNote;
	
	Student selected;
	
	Tutoring tutoring;
	
	class MotivationListener implements ChangeListener<Motivation> {
		public void changed(ObservableValue<? extends Motivation> observable, Motivation oldValue, Motivation newValue) {
	    	selected.setMotivation(tutoring, newValue);
		}
	}
	
	class WidthAbsencesListener implements ChangeListener<Toggle> {
		public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
	    	if (newValue == notImportantAbsences) {
	    		tutoring.setAbsenceWidth(1);
	    	} else if (newValue == normalAbsences) {
	    		tutoring.setAbsenceWidth(2);
	    	} else {
	    		tutoring.setAbsenceWidth(3);
	    	}
		}
	}
	
	class WidthMoyenneListener implements ChangeListener<Toggle> {
		public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
			if (newValue == notImportantNote) {
	    		tutoring.setAbsenceWidth(1);
	    	} else if (newValue == normalNote) {
	    		tutoring.setAbsenceWidth(2);
	    	} else {
	    		tutoring.setAbsenceWidth(3);
	    	}
		}
	}
	
	public void updateLabelAssignedStudent(Label label, Student student) {
		label.setText(student.getForename() + " " + student.getName());
		if (selected.getStudentsToNotAssign(tutoring).contains(student)) {
			label.setText(label.getText() + " (annulée)");
			label.setTextFill(Color.RED);
		} else {
			label.setTextFill(Color.BLACK);
		}
	}
	
	public void updateTables() {
		allTable.getItems().clear();
		forenameCol.setCellValueFactory(new PropertyValueFactory<>("forename"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		promoCol.setCellValueFactory(new PropertyValueFactory<>("promo"));
		absencesCol.setCellValueFactory(new PropertyValueFactory<>("nbAbsences"));
		assignmentsCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getAssignments(tutoring).size()).asObject());
		allTable.getItems().addAll(tutoring.getTutees());
		allTable.getItems().addAll(tutoring.getTutors());
	}
	
	public void updateProfileView() {
		labelName.setText(selected.getForename() + " " + selected.getName());
    	labelMail.setText(selected.getLogin() + "@univ-lille.fr");
    	labelMoyenne.setText(""+selected.getMoyenne(tutoring.getSubject()));
    	labelPromo.setText(""+selected.getPromo());
    	labelAbsences.setText(""+selected.getNbAbsences());
    	cbMotivation.setValue(selected.getMotivation(tutoring));
    	
    	gridAssignments.getChildren().clear();
    	int rowCount = 0;
    	Set<Student> assignments = selected.getAssignments(tutoring);
    	assignments.addAll(selected.getStudentsToNotAssign(tutoring));
    	for (Student assigned: assignments) {
    		Label assignedName = new Label();
    		updateLabelAssignedStudent(assignedName, assigned);
    		assignedName.setLabelFor(assignedName);
    		Label doNotAssign = new Label("X");
    		gridAssignments.add(assignedName, 0, rowCount);
    		gridAssignments.add(doNotAssign, 1, rowCount);
    		doNotAssign.setOnMouseClicked(e -> {
    			if (!selected.getStudentsToNotAssign(tutoring).contains(assigned)) {
	    			try {
						selected.doNotAssign(tutoring, assigned);
					} catch (ExceptionNotInTutoring | ExceptionPromo e1) {
						e1.printStackTrace();
					}
    			} else {
    				selected.removeStudentToNotAssign(tutoring, assigned);
    			}
    			updateLabelAssignedStudent(assignedName, assigned);
    		});
    		rowCount++;
    	}
	}
	
    public void initialize() throws ExceptionPromo {
		try {
			File testFilePath = new File(System.getProperty("user.dir") + File.separator + "res" + File.separator + "tutoring_save.json");
			tutoring = TutoringSave.load(testFilePath);
			updateTables();
		} catch (IOException e) {
			System.out.println("Fichier de test non trouvé");
		} catch (JSONException | ExceptionPromo | ExceptionNotInTutoring | ExceptionTooManyAssignments e) {
			e.printStackTrace();
		}
		closeProfileView();
		
		cbMotivation.getItems().addAll(Motivation.values());
		cbMotivation.getSelectionModel().selectedItemProperty().addListener(new MotivationListener());
		
		widthAbsences.selectedToggleProperty().addListener(new WidthAbsencesListener());
		widthMoyenne.selectedToggleProperty().addListener(new WidthMoyenneListener());
    }
    
    @FXML
    public void openTutoring() {
    	FileChooser fileChooser = new FileChooser();
    	File choice = fileChooser.showOpenDialog(allTable.getScene().getWindow());
    	try {
    		Student.resetUsedINE();
			tutoring = TutoringSave.load(choice);
			updateTables();
		} catch (JSONException | IOException | ExceptionPromo | ExceptionNotInTutoring
				| ExceptionTooManyAssignments e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void studentSelected(MouseEvent event) {
    	selected = allTable.getSelectionModel().getSelectedItem();
    	if (profileView.getParent() == null) rightSide.getChildren().add(0, profileView);
    	updateProfileView();
    }
    
    @FXML
    public void closeProfileView() {
    	rightSide.getChildren().remove(profileView);
    	selected = null;
    }
    
    @FXML
    public void computeAssignments() throws ExceptionPromo {
    	tutoring.createAssignments();
    	if (selected != null) updateProfileView();
    	updateTables();
    }
}

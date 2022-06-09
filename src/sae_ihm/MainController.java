package sae_ihm;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.json.JSONException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
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
	@FXML TableView<Student> studentsTable;
	 
	@FXML TabPane tabFilter;
	@FXML Tab tabAll;
	@FXML Tab tabTutors;
	@FXML Tab tabTutees;
	
	@FXML TableColumn<Student, String> forenameCol;
	@FXML TableColumn<Student, String> nameCol;
	@FXML TableColumn<Student, Integer> promoCol;
	@FXML TableColumn<Student, Integer> absencesCol;
	@FXML TableColumn<Student, String> assignmentsCol;
	
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
	
	@FXML Slider sliderMaxNote;
	@FXML Slider sliderMinNote;
	@FXML Slider sliderAbsencesMax;
	
	@FXML TextField fieldMaxAssignments;
	@FXML TextField fieldMaxNote;
	@FXML TextField fieldMinNote;
	@FXML TextField fieldAbsencesMax;
	
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
	
	class UpdateTextField implements ChangeListener<Number> {
		private TextField tfield;
		private int round;
		
		public UpdateTextField(TextField tfield, int round) {
			this.tfield = tfield;
			this.round = round;
		}
		
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			double tfValue = newValue.doubleValue();
			tfValue = Math.round(tfValue * Math.pow(10, round)) / Math.pow(10, round);
			if (Double.valueOf(tfield.getText()) != tfValue) {
				tfield.setText("" + tfValue);
			}
		}
	}
	
	class UpdateSlider implements ChangeListener<String> {
		private Slider slider;
		private int round;
		
		public UpdateSlider(Slider slider, int round) {
			this.slider = slider;
			this.round = round;
		}
		
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {	
			try {
				double tfValue = Double.valueOf(newValue);
	
				if (tfValue < slider.getMin()) {
					tfValue = slider.getMin();
				}
				
				tfValue = Math.round(tfValue * Math.pow(10, round)) / Math.pow(10, round);
				if (slider.getValue() != tfValue) {
					slider.setValue(tfValue);
				}
			} catch(NumberFormatException e) { System.out.println("invalide"); } // S'enclenche quand l'entrée ne peut pas être converti en nombre
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
	
	public void updateTable() {
		studentsTable.getItems().clear();
		Tab tabSelected = tabFilter.getSelectionModel().getSelectedItem();
		
		if (tabSelected == tabTutees) {
			studentsTable.getColumns().remove(promoCol);
			studentsTable.getItems().addAll(tutoring.getTutees());
		} else {
			if (!studentsTable.getColumns().contains(promoCol)) studentsTable.getColumns().add(2, promoCol);
			if (tabSelected == tabAll) {
				Set<Student> students = tutoring.getTutors();
				students.addAll(tutoring.getTutees());
				studentsTable.getItems().addAll(students);
			} else {
				studentsTable.getItems().addAll(tutoring.getTutors());
			}
		}
	}
	
	public void updateProfileView() {
		if (profileView.getParent() == null) rightSide.getChildren().add(0, profileView);
		
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
			updateTable();
		} catch (IOException e) {
			System.out.println("Fichier de test non trouvé");
		} catch (JSONException | ExceptionPromo | ExceptionNotInTutoring | ExceptionTooManyAssignments e) {
			e.printStackTrace();
		}
		closeProfileView();
		
		forenameCol.setCellValueFactory(new PropertyValueFactory<>("forename"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		promoCol.setCellValueFactory(new PropertyValueFactory<>("promo"));
		absencesCol.setCellValueFactory(new PropertyValueFactory<>("nbAbsences"));
		assignmentsCol.setCellValueFactory(student -> new SimpleStringProperty(toStringAssignmentsCol(student.getValue())));
		
		studentsTable.getSelectionModel().selectedItemProperty().addListener(e -> studentSelected());
		
		cbMotivation.getItems().addAll(Motivation.values());
		cbMotivation.getSelectionModel().selectedItemProperty().addListener(new MotivationListener());
		
		widthAbsences.selectedToggleProperty().addListener(new WidthAbsencesListener());
		widthMoyenne.selectedToggleProperty().addListener(new WidthMoyenneListener());
		
		sliderMaxNote.valueProperty().addListener(new UpdateTextField(fieldMaxNote, 2));
		sliderMinNote.valueProperty().addListener(new UpdateTextField(fieldMinNote, 2));
		sliderAbsencesMax.valueProperty().addListener(new UpdateTextField(fieldAbsencesMax, 0));
		
		fieldMaxNote.textProperty().addListener(new UpdateSlider(sliderMaxNote, 2));
		fieldMinNote.textProperty().addListener(new UpdateSlider(sliderMinNote, 2));
		fieldAbsencesMax.textProperty().addListener(new UpdateSlider(sliderAbsencesMax, 0));
		
		fieldMaxAssignments.setText(""+tutoring.getMaxTuteesForTutor());
		fieldMaxNote.setText("20");
		fieldMinNote.setText("0");
		fieldAbsencesMax.setText(""+tutoring.getAbsenceWidth());
		
		tabFilter.getSelectionModel().selectedItemProperty().addListener(e -> {
			updateTable();
		});
    }
    
    @FXML
    public void openTutoring() {
    	FileChooser fileChooser = new FileChooser();
    	File choice = fileChooser.showOpenDialog(studentsTable.getScene().getWindow());
    	try {
    		Student.resetUsedINE();
			tutoring = TutoringSave.load(choice);
			updateTable();
		} catch (JSONException | IOException | ExceptionPromo | ExceptionNotInTutoring
				| ExceptionTooManyAssignments e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void studentSelected() {
    	Student selection = studentsTable.getSelectionModel().getSelectedItem();
    	if (selection != null) {
    		selected = selection;
    		updateProfileView();
    	}
    }
    
    @FXML
    public void closeProfileView() {
    	rightSide.getChildren().remove(profileView);
    	selected = null;
    }
    
    @FXML
    public void computeAssignments() throws ExceptionPromo {
    	// TODO: Vérifier l'entrée de fieldMaxAssignments
    	tutoring.setMaxTuteesForTutor(Integer.valueOf(fieldMaxAssignments.getText()));
    	tutoring.setMoyenneMaxTutee(sliderMaxNote.getValue());
    	tutoring.setMoyenneMinTutor(sliderMinNote.getValue());
    	tutoring.setNbAbsencesMax((int) sliderAbsencesMax.getValue());
    	tutoring.createAssignments();
    	if (selected != null) updateProfileView();
    	updateTable();
    }
    
    @FXML
    public String toStringAssignmentsCol(Student student) {
    	String res;
    	
    	if (student.isTutee() && !student.getAssignments(tutoring).isEmpty()) {
    		Student tutor = (Student) student.getAssignments(tutoring).toArray()[0];
    		res = tutor.getForename() + " " + tutor.getName();
    	} else {
    		res = ""+student.getAssignments(tutoring).size();
    	}
    	
    	return res;
    }
}

package sae_ihm;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

import org.json.JSONException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sae_201_02.ExceptionNotInTutoring;
import sae_201_02.ExceptionPromo;
import sae_201_02.ExceptionTooManyAssignments;
import sae_201_02.Motivation;
import sae_201_02.Student;
import sae_201_02.Subject;
import sae_201_02.Tutoring;
import sae_201_02.TutoringSave;

public class MainController extends StudentsTable {	 
	@FXML TabPane tabFilter;
	@FXML Tab tabAll;
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
	@FXML Spinner<Integer> spinnerAbsences;
	@FXML ChoiceBox<Motivation> cbMotivation;
	@FXML Button forceAssignmentButton;
	
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
	
	@FXML Spinner<Integer> spinnerMaxAssignments;
	@FXML Spinner<Double> spinnerMaxNote;
	@FXML Spinner<Double> spinnerMinNote;
	@FXML Spinner<Integer> spinnerAbsencesMax;
	
	@FXML Label btnCloseProfile;
		
	Student selected;
	
	Tutoring tutoring;
	
	boolean fileHasChanged = false;
	
	File openedFile;
	
	class MotivationListener implements ChangeListener<Motivation> {
		public void changed(ObservableValue<? extends Motivation> observable, Motivation oldValue, Motivation newValue) {
			if (selected.getMotivation(tutoring) != newValue)
				fileHasChanged = true;
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
	    	fileHasChanged = true;
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
			fileHasChanged = true;
		}
	}
	
	class UpdateSpinner implements ChangeListener<Number> {
		private Spinner<Number> spin;
		private int round;
		
		public UpdateSpinner(Spinner<? extends Number> spin, int round) {
			this.spin = (Spinner<Number>) spin;
			this.round = round;
		}
		
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			if (spin.getValue() != newValue) {
				if (spin.getValue() instanceof Double) {
					spin.getValueFactory().setValue(newValue.doubleValue());
				} else {
					spin.getValueFactory().setValue(newValue.intValue());
				}
			}
			fileHasChanged = true;
		}
	}
	
	
	class UpdateSlider implements ChangeListener<Number> {
		private Slider slider;
		
		public UpdateSlider(Slider slider) {
			this.slider = slider;
		}
		
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {	
			try {
				double tfValue = newValue.doubleValue();
				
				if (slider.getValue() != tfValue) {
					slider.setValue(tfValue);
				}
			} catch(NumberFormatException e) { System.out.println("invalide"); } // S'enclenche quand l'entrée ne peut pas être converti en nombre
		}
	}
	
	class CancelAssignment implements EventHandler<MouseEvent> {
		
		private Label assignedName;
		private Student assigned;
		
		public CancelAssignment(Label assignedName, Student assigned) {
			this.assignedName = assignedName;
			this.assigned = assigned;
		}

		@Override
		public void handle(MouseEvent arg0) {
			if (selected.getForcedAssignments(tutoring).contains(assigned)) {
				selected.removeForcedAssignment(tutoring, assigned);
			} else if (!selected.getStudentsToNotAssign(tutoring).contains(assigned)) {
				try {
						selected.doNotAssign(tutoring, assigned);
					} catch (ExceptionNotInTutoring | ExceptionPromo e1) {
						e1.printStackTrace();
					}
			} else {
				selected.removeStudentToNotAssign(tutoring, assigned);
			}
			updateLabelAssignedStudent(assignedName, assigned);
			fileHasChanged = true;
		}
	}
	
	public void updateLabelAssignedStudent(Label label, Student student) {
		label.setText(student.getForename() + " " + student.getName());
		if (selected.getStudentsToNotAssign(tutoring).contains(student)) {
			label.setText(label.getText() + " (annulée)");
			label.setTextFill(Color.RED);
		} else if (selected.getForcedAssignments(tutoring).contains(student)) {
			label.setText(label.getText() + " (forcée)");
			label.setTextFill(Color.GREEN);
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
    	spinnerAbsences.getValueFactory().setValue(selected.getNbAbsences());
    	spinnerAbsences.valueProperty().addListener(e -> {
			selected.setNbAbsences(spinnerAbsences.getValue());
			updateTable();
		});

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
    		changeCursorOnHover(assignedName);
    		changeCursorOnHover(doNotAssign);
    		assignedName.setOnMouseClicked(e -> {
    			selected = assigned;
        		updateProfileView();
    		});
    		doNotAssign.setOnMouseClicked(new CancelAssignment(assignedName, assigned));
    		rowCount++;
    	}
    	
    	// On grise le bouton pour affecter à un autre étudiant si on ne peut pas faire cette action
    	if (selected.canAddMoreForcedAssignment(tutoring)) {
    		forceAssignmentButton.setDisable(false);
    	} else {
    		forceAssignmentButton.setDisable(true);
    	}
	}
	
	public void changeCursorOnHover(Node node) {
		node.setOnMouseEntered(e -> node.getScene().setCursor(Cursor.HAND));
		node.setOnMouseExited(e -> node.getScene().setCursor(Cursor.DEFAULT));
	}
	
	@FXML
    public void initialize() {
    	super.initialize();
    	
    	tutoring = new Tutoring(Subject.R101);
		try {
			File testFilePath = new File(System.getProperty("user.dir") + File.separator + "res" + File.separator + "tutoring_save3.json");
			tutoring = TutoringSave.load(testFilePath);
			openedFile = testFilePath;
			updateTable();
		} catch (IOException e) {
			System.out.println("Fichier de test non trouvé");
		} catch (JSONException | ExceptionPromo | ExceptionNotInTutoring | ExceptionTooManyAssignments e) {
			e.printStackTrace();
		}
		closeProfileView();
		
		assignmentsCol.setCellValueFactory(student -> new SimpleStringProperty(toStringAssignmentsCol(student.getValue())));
		
		studentsTable.getSelectionModel().selectedItemProperty().addListener(e -> studentSelected());
		
		cbMotivation.getItems().addAll(Motivation.values());
		cbMotivation.getSelectionModel().selectedItemProperty().addListener(new MotivationListener());
		
		widthAbsences.selectedToggleProperty().addListener(new WidthAbsencesListener());
		widthMoyenne.selectedToggleProperty().addListener(new WidthMoyenneListener());
			
		sliderMaxNote.valueProperty().addListener(new UpdateSpinner(spinnerMaxNote, 2));
		sliderMinNote.valueProperty().addListener(new UpdateSpinner(spinnerMinNote, 2));
		sliderAbsencesMax.valueProperty().addListener(new UpdateSpinner(spinnerAbsencesMax, 0));
		
		spinnerMaxNote.valueProperty().addListener(new UpdateSlider(sliderMaxNote));
		spinnerMinNote.valueProperty().addListener(new UpdateSlider(sliderMinNote));
		spinnerAbsencesMax.valueProperty().addListener(new UpdateSlider(sliderAbsencesMax));
		
		spinnerAbsences.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
		spinnerMaxAssignments.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, tutoring.maxTuteesForTutor));
		spinnerMaxNote.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 20.0, 20));
		spinnerMinNote.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 20.0, 0));
		spinnerAbsencesMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 50));
		
		if (tutoring.getMoyenneMaxTutee() != null) {
			spinnerMaxNote.getValueFactory().setValue(tutoring.getMoyenneMaxTutee());
		}
		if (tutoring.getMoyenneMinTutor() != null) {
			spinnerMinNote.getValueFactory().setValue(tutoring.getMoyenneMinTutor());
		}
		if (tutoring.getNbAbsencesMax() != null) {
			spinnerAbsencesMax.getValueFactory().setValue(tutoring.getNbAbsencesMax());
		}
		
		spinnerMaxAssignments.getValueFactory().setValue(tutoring.getMaxTuteesForTutor());
		spinnerMaxNote.getValueFactory().setValue(20.0);
		spinnerMinNote.getValueFactory().setValue(0.0);
		spinnerAbsencesMax.getValueFactory().setValue((int) tutoring.getAbsenceWidth());
		
		tabFilter.getSelectionModel().selectedItemProperty().addListener(e -> {
			searchBar.setText("");
			updateTable();
		});
		
		studentsTable.setRowFactory(
		    new Callback<TableView<Student>, TableRow<Student>>() {
		        @Override
		        public TableRow<Student> call(TableView<Student> tableView) {
		            final TableRow<Student> row = new TableRow<>();
		            final ContextMenu rowMenu = new ContextMenu();
		            MenuItem editItem = new MenuItem("Edit");
		            // editItem.setOnAction();
		            MenuItem remove = new MenuItem("Supprimer l'étudiant");
		            remove.setOnAction(e -> deleteStudent());
		            
		            rowMenu.getItems().addAll(editItem, remove);
		            
		            // only display context menu for non-empty rows:
		            row.contextMenuProperty().bind(
		              Bindings.when(row.emptyProperty())
		              .then((ContextMenu) null)
		              .otherwise(rowMenu));
		            return row;
		    }
		});
		fileHasChanged = false;
		
		changeCursorOnHover(btnCloseProfile);
    }

	@FXML
    public void openTutoring() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier JSON", "*.json"));
    	File choice = fileChooser.showOpenDialog(studentsTable.getScene().getWindow());
    	try {
    		if (choice != null) {
	    		Student.resetUsedINE();
				tutoring = TutoringSave.load(choice);
				openedFile = choice;
				updateTable();
				fileHasChanged = false;
    		}
		} catch (JSONException | IOException | ExceptionPromo | ExceptionNotInTutoring
				| ExceptionTooManyAssignments e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Impossible d'ouvrir le fichier");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
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
    	tutoring.setMaxTuteesForTutor(Integer.valueOf((int) spinnerMaxAssignments.getValue()));
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
    
    @FXML
    public void forcedAssignmentMenu() throws IOException {
    	final Stage dialog = new Stage();
    	dialog.initModality(Modality.APPLICATION_MODAL);
    	
    	FXMLLoader loader = new FXMLLoader();
        URL fxmlFileUrl = getClass().getResource("forceAssignmentMenu.fxml");
        if (fxmlFileUrl == null) {
                System.out.println("Impossible de charger le fichier fxml");
                return;
        }
        loader.setLocation(fxmlFileUrl);
        
        Parent root = loader.load();
        
        ForcedAssignmentsController controller = loader.getController();
        controller.setSelected(selected);
        controller.setTutoring(tutoring);
        controller.updateTable();
        
        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.setTitle("Créer une affectation forcée");
        dialog.setMinWidth(360);
        dialog.setMinHeight(550);        
        dialog.show();
        
        // Quand la fenêtre est fermée, on update la liste des affectations
        dialog.setOnHiding(e -> {
        	updateProfileView();
        	fileHasChanged = true;
        });
    }
    
    @FXML
    public void addStudentMenu() throws IOException {
    	final Stage dialog = new Stage();
    	dialog.initModality(Modality.APPLICATION_MODAL);
    	
    	FXMLLoader loader = new FXMLLoader();
        URL fxmlFileUrl = getClass().getResource("addStudentMenu.fxml");
        if (fxmlFileUrl == null) {
        	System.out.println("Impossible de charger le fichier fxml");
            return;
        }
        loader.setLocation(fxmlFileUrl);
        
        Parent root = loader.load();
        
        AddStudentController controller = loader.getController();
        controller.setTutoring(tutoring);
        
        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.setTitle("Ajouter un étudiant");
        dialog.setWidth(332);
        dialog.setHeight(400);
        dialog.setResizable(false);
        dialog.show();
        
        // Quand la fenêtre est fermée, on update la liste des affectations
        dialog.setOnHiding(e -> {
        	updateTable();
        	fileHasChanged = true;
        });
    }
    
    @FXML
    public void deleteStudent() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Supprimer un étudiant");
    	alert.setHeaderText("Êtes-vous sûr de vouloir supprimer l'étudiant?");
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	 
    	if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
    		tutoring.removeStudent(selected);
    		closeProfileView();
    		updateTable();
    		fileHasChanged = true;
    	}
    }
    
    @FXML
    public void newFile() throws IOException {
    	if (!confirmBeforeClosing()) return;
    	
    	final Stage dialog = new Stage();
    	dialog.initModality(Modality.APPLICATION_MODAL);
    	
    	FXMLLoader loader = new FXMLLoader();
        URL fxmlFileUrl = getClass().getResource("chooseSubject.fxml");
        if (fxmlFileUrl == null) {
        	System.out.println("Impossible de charger le fichier fxml");
            return;
        }
        loader.setLocation(fxmlFileUrl);
        
        Parent root = loader.load();
        
        ChooseSubjectController controller = loader.getController();
        
        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.setTitle("Créer un nouveau tutorat");
        dialog.setWidth(241);
        dialog.setHeight(140);
        dialog.setResizable(false);
        dialog.show();
        
        dialog.setOnHiding(e -> {
        	Subject choice = controller.getSubject();
        	if (choice != null) {
	        	Student.resetUsedINE();
	    		tutoring = new Tutoring(choice);
	    		openedFile = null;
	    		updateTable();
	    		fileHasChanged = false;
        	}
        });
    }
    
    @FXML
    public void saveTutoring() {
    	if (openedFile == null) {
    		saveAsTutoring();
    	}
    	try {
			TutoringSave.save(tutoring, openedFile);
			fileHasChanged = false;
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void saveAsTutoring() {
    	FileChooser fileChooser = new FileChooser();
    	File choice = fileChooser.showSaveDialog(studentsTable.getScene().getWindow());
    	fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Fichier JSON", "*.json"));
		
    	if (choice != null) {
			openedFile = choice;
			saveTutoring();
		}
    }
    
    @FXML
    public void quit() {
    	if (confirmBeforeClosing()) {
	    	Stage stage = (Stage) tabFilter.getScene().getWindow();
	    	stage.close();
    	}
    }
    
    @FXML
    public boolean confirmBeforeClosing() {
    	if (!fileHasChanged) {
    		return true;
    	}
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Sauvegarder le document?");
    	alert.setHeaderText("Sauvegarder les changements avant de quitter?");
    	
    	ButtonType buttonYes = new ButtonType("Sauvegarder");
    	ButtonType buttonNo = new ButtonType("Ne pas sauvegarder");
    	
    	alert.getButtonTypes().removeAll(ButtonType.OK);
    	alert.getButtonTypes().addAll(buttonYes, buttonNo);
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	boolean res = false;
    	
    	if (result.isPresent()) {
    		if (result.get() == buttonYes) {
    			saveTutoring();
    		}
    		res = (result.get() == buttonNo) || (result.get() == buttonYes);
    	}
    	return res;
    }
}

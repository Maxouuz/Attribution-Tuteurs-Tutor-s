package sae_ihm;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import sae_201_02.ExceptionNotInTutoring;
import sae_201_02.ExceptionPromo;
import sae_201_02.ExceptionTooManyAssignments;
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
	
	Student selected;
	
	Tutoring tutoring;
	
	public void updateTables() {
		forenameCol.setCellValueFactory(new PropertyValueFactory<>("forename"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		promoCol.setCellValueFactory(new PropertyValueFactory<>("promo"));
		absencesCol.setCellValueFactory(new PropertyValueFactory<>("nbAbsences"));
		assignmentsCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getAssignments(tutoring).size()).asObject());
		allTable.getItems().addAll(tutoring.getTutees());
		allTable.getItems().addAll(tutoring.getTutors());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    public void studentSelected(MouseEvent event) {
    	selected = allTable.getSelectionModel().getSelectedItem();
    	if (profileView.getParent() == null) rightSide.getChildren().add(0, profileView);
    }
    
    @FXML
    public void closeProfileView() {
    	rightSide.getChildren().remove(profileView);
    }

     //public void pressedButtonPlus(ActionEvent event) {
             // int newValue = Integer.parseInt(monLabel.getText()) + 1;
             // monLabel.setText(String.valueOf(newValue));
     //}
}

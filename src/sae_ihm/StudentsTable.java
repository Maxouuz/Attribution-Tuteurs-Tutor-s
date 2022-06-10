package sae_ihm;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sae_201_02.Student;

public abstract class StudentsTable {
	
	@FXML TableView<Student> studentsTable;
	
	@FXML TableColumn<Student, String> forenameCol;
	@FXML TableColumn<Student, String> nameCol;
	@FXML TableColumn<Student, Integer> promoCol;
	@FXML TableColumn<Student, Integer> absencesCol;
	
	@FXML TextField searchBar;
	
	public void initialize() {
		forenameCol.setCellValueFactory(new PropertyValueFactory<>("forename"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		promoCol.setCellValueFactory(new PropertyValueFactory<>("promo"));
		absencesCol.setCellValueFactory(new PropertyValueFactory<>("nbAbsences"));
		
		searchBar.textProperty().addListener(new SearchBarListener(this, studentsTable));
	}
	
	public abstract void updateTable();
}

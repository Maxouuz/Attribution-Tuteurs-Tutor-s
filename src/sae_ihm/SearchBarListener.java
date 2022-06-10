package sae_ihm;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableView;
import sae_201_02.Student;

class SearchBarListener implements ChangeListener<String> {
	
	private StudentsTable controller;
	private TableView<Student> studentsTable;	
	
	public SearchBarListener(StudentsTable controller, TableView<Student> studentsTable) {
		this.controller = controller;
		this.studentsTable = studentsTable;
	}
	
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		controller.updateTable();
		int i = 0;
    	while (i < studentsTable.getItems().size()) {
    		String entry = newValue.toLowerCase();
    		String forename = studentsTable.getItems().get(i).getForename().toLowerCase();
    		String name = studentsTable.getItems().get(i).getName().toLowerCase();
    		String forenameName = forename + " " + name;
    		String nameForename = name + " " + forename;
    		if (!forenameName.contains(entry) && !nameForename.contains(entry)) {
    			studentsTable.getItems().remove(i);
    		} else {
    			i++;
    		}
    	}
    	
	}
}

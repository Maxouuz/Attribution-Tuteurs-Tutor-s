package sae_ihm;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import sae_201_02.Tutoring;

public class AddStudentController {
	
	private Tutoring tutoring;
	
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
		System.out.println("Exécuté à chaque ouverture de la fenêtre (optionnel)");
		
		final int initialValue = 3;
		
		SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, initialValue);

		spinnerINE.setValueFactory(valueFactory);
	}
	
	@FXML
	public void confirm() {
		System.out.println("Exécuté à chaque clic sur le bouton");
		System.out.println(spinnerINE.getValue());
	}
}

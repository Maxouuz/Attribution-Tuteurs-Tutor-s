package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TutoringSaveTest {
	/** Variable qui représente le tutorat */
	private static Tutoring tutoring;
	/** Variables de tuteurs */
	private static Student tutor1, tutor2, tutor3;
	/** Variables de tutorés */
	private static Student tutee1, tutee2, tutee3, tutee4;
	
	@BeforeAll
	static void initialize() throws ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		tutoring = new Tutoring(Subject.R101);
		
		tutor1 = Student.createStudent("Sophie", "Vallee", 2, 3, 15.5);
		tutor2 = Student.createStudent("Nicolas", "Roche", 3, 6, 16.5);
		tutor3 = Student.createStudent("Maurice", "Fernandez", 3, 4, 14.5);
		
		tutee1 = Student.createStudent("Charles", "Letellier", 1, 0, 8);
		tutee2 = Student.createStudent("Daniel", "Daniel", 1, 3, 9);
		tutee3 = Student.createStudent("François", "Bertin", 1, 9, 7);
		tutee4 = Student.createStudent("Sabine", "Leleu", 1, 1, 5.5);
		
		tutoring.addAllStudents(tutee1, tutee2, tutee3, tutee4, tutor1, tutor2, tutor3);
	
		tutor3.setMotivation(tutoring, Motivation.MOTIVATED);
		
		tutee4.forceAssignment(tutoring, tutor3);
		tutee3.doNotAssign(tutoring, tutor2);
	}
	
	@Test
	void saveAndLoadTest() throws JSONException, IOException, ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		String path = System.getProperty("user.dir") + File.separator + "res" + File.separator;
		File save1 = new File(path + "tutoring_save.json");
		File save2 = new File(path + "tutoring_save2.json");
		TutoringSave.save(tutoring, save1);
		Person.resetUsedINE();
		Tutoring loaded = TutoringSave.load(save1);
		TutoringSave.save(loaded, save2);
		
		BufferedReader brSave1 = new BufferedReader(new FileReader(save1));
		BufferedReader brSave2 = new BufferedReader(new FileReader(save2));
		
		String line1 = brSave1.readLine();
		String line2 = brSave2.readLine();
		while (line1 != null) {
			assertEquals(line1, line2);
			line1 = brSave1.readLine();
			line2 = brSave2.readLine();
		}
				
		// Pas de la même taille
		if (line2 != null) {
			fail();
		}
		
		brSave1.close();
		brSave2.close();
	}
}

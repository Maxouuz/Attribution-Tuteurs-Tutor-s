package sae_201_02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TutoringSave {
	/**
	 * Méthode pour sauvegarder le tutorat dans un fichier json
	 * @param path
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static void save(Tutoring tutoring, File path) throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("subject", tutoring.getSubject());
		// json.put("tutees", tutoring.getTutees());
		// json.put("tutors", tutoring.getTutors());
		
		json.put("moyenneMaxTutee", tutoring.getMoyenneMaxTutee());
		json.put("moyenneMinTutor", tutoring.getMoyenneMinTutor());
		json.put("nbAbsencesMax", tutoring.getNbAbsencesMax());
		
		json.put("maxTuteesForTutor", tutoring.getMaxTuteesForTutor());
		json.put("moyenneWidth", tutoring.getMoyenneWidth());
		json.put("absenceWidth", tutoring.getAbsenceWidth());
		
		Set<Student> students = tutoring.getTutees();
		students.addAll(tutoring.getTutors());
		
		JSONArray jsonStudents = new JSONArray();
		for (Student student: students) {
			JSONObject jsonStudent = new JSONObject();
			jsonStudent.put("forename", student.getForename());
			jsonStudent.put("name", student.getName());
			jsonStudent.put("ine", student.getINE());
			jsonStudent.put("promo", student.getPromo());
			jsonStudent.put("nbAbsences", student.getNbAbsences());
			jsonStudent.put("motivation", student.getMotivation(tutoring));
			jsonStudent.put("moyennes", student.getMoyennes());
			
			JSONArray jsonForcedAssignments = new JSONArray();
			for (Student other: student.getForcedAssignments(tutoring)) {
				jsonForcedAssignments.put(other.getINE());
			}
			jsonStudent.put("forcedAssignments", jsonForcedAssignments);
			
			JSONArray jsonStudentsToNotAssign = new JSONArray();
			for (Student other: student.getStudentsToNotAssign(tutoring)) {
				jsonStudentsToNotAssign.put(other.getINE());
			}
			jsonStudent.put("studentsToNotAssign", jsonStudentsToNotAssign);
			
			jsonStudents.put(jsonStudent);
		}
		json.put("students", jsonStudents);
		
		Writer writer = new FileWriter(path);
		json.write(writer, 4, 0);
		writer.close();
	}
	
	private static void loadStudents(Tutoring tutoring, JSONObject json, Map<Integer, Student> oldINEToStudent) throws JSONException, ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		JSONArray students = (JSONArray) json.get("students");
		for (Object element: students) {
			JSONObject elementJSON = (JSONObject) element;
			Student studentCopy;
			studentCopy = Student.createStudent(elementJSON.getString("forename"),
									  elementJSON.getString("name"),
									  elementJSON.getInt("promo"),
									  elementJSON.getInt("nbAbsences"));
			JSONObject moyennes = (JSONObject) elementJSON.get("moyennes");
			for (String moyenne: moyennes.keySet()) {
				studentCopy.setMoyenne(Subject.valueOf(moyenne), moyennes.getDouble(moyenne));
			}
			tutoring.addStudent(studentCopy);
			oldINEToStudent.put(elementJSON.getInt("ine"), studentCopy);

			studentCopy.addMotivation(tutoring, Motivation.valueOf(elementJSON.getString("motivation")));
		}
	}
	
	private static void loadAssignments(Tutoring tutoring, JSONObject json, Map<Integer, Student> oldINEToStudent) throws JSONException, ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		for (Object student: (JSONArray) json.get("students")) {
			for (Object element: (JSONArray) ((JSONObject) student).get("forcedAssignments")) {
				oldINEToStudent.get(((JSONObject) student).getInt("ine")).forceAssignment(tutoring, oldINEToStudent.get(element));
			}
			for (Object element: (JSONArray) ((JSONObject) student).get("studentsToNotAssign")) {
				oldINEToStudent.get(((JSONObject) student).getInt("ine")).doNotAssign(tutoring, oldINEToStudent.get(element));
			}
		}
	}
	
	/**
	 * Méthode pour charger un tutorat précédemment sauvegardé dans un fichier json
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws ExceptionPromo
	 * @throws ExceptionNotInTutoring
	 * @throws ExceptionTooManyAssignments
	 */
	public static Tutoring load(File path) throws IOException, JSONException, ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		// Récupère le fichier json
		BufferedReader reader = new BufferedReader(new FileReader(path));
		
		StringBuilder text = new StringBuilder();
		while (reader.ready()) {
			text.append(reader.readLine());
		}
		reader.close();
		
		JSONObject json = new JSONObject(text.toString());
		Tutoring tutoring = new Tutoring(
				Subject.valueOf(json.getString("subject")),
				json.getInt("maxTuteesForTutor"),
				json.getDouble("moyenneWidth"),
				json.getDouble("absenceWidth")
		);
		
		Map<Integer, Student> oldINEToStudent = new HashMap<>();
		
		loadStudents(tutoring, json, oldINEToStudent);
		loadAssignments(tutoring, json, oldINEToStudent);		
		return tutoring;
	}
}

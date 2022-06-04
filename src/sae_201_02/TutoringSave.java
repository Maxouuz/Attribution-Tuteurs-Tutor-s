package sae_201_02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

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
	/**
	public void save(File path) throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("subject", subject);
		json.put("tutees", tutees);
		json.put("tutors", tutors);
		
		JSONObject motivationJSON = new JSONObject();
		for (Student std: motivations.keySet()) {
			motivationJSON.put(String.valueOf(std.getINE()), motivations.get(std));
		}
		json.put("motivations", motivationJSON);
		
		json.put("moyenneMaxTutee", moyenneMaxTutee);
		json.put("moyenneMinTutor", moyenneMinTutor);
		json.put("nbAbsencesMax", nbAbsencesMax);
		
		json.put("forcedAssignment", forcedAssignment);
		json.put("studentsToNotAssign", studentsToNotAssign);
		json.put("maxTuteesForTutor", maxTuteesForTutor);
		json.put("moyenneWidth", moyenneWidth);
		json.put("absenceWidth", absenceWidth);
		
		Writer writer = new FileWriter(path);
		json.write(writer, 4, 0);
		writer.close();
	}
	
	private static void loadStudents(Tutoring tutoring, JSONObject json, Map<Integer, Student> oldINEToStudent) throws JSONException, ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		JSONArray students = (JSONArray) json.get("tutees");
		students.putAll(json.get("tutors"));
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
		}
	}
	
	private static void loadForcedAssignment(Tutoring tutoring, JSONObject json, Map<Integer, Student> oldINEToStudent) throws JSONException, ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		JSONObject forcedAssignment = (JSONObject) json.get("forcedAssignment");
		for (String element: forcedAssignment.keySet()) {
			oldINEToStudent.get(Integer.parseInt(element)).forceAssignment(tutoring,
										 					oldINEToStudent.get(Integer.parseInt((String) forcedAssignment.get(element))));
		}
	}
	
	private static void loadMotivations(Tutoring tutoring, JSONObject json, Map<Integer, Student> oldINEToStudent) throws JSONException, ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		JSONObject motivations = (JSONObject) json.get("motivations");
		for (String element: motivations.keySet()) {
			tutoring.addStudentMotivation(oldINEToStudent.get(Integer.parseInt(element)),
										  Motivation.valueOf((String) motivations.get(element)));
		}
	}
	
	private static void loadStudentsToNotAssign(Tutoring tutoring, JSONObject json, Map<Integer, Student> oldINEToStudent) throws JSONException, ExceptionPromo, ExceptionNotInTutoring, ExceptionTooManyAssignments {
		JSONObject studentsToNotAssign = (JSONObject) json.get("studentsToNotAssign");
		for (String element: studentsToNotAssign.keySet()) {
			tutoring.doNotAssign(oldINEToStudent.get(Integer.parseInt(element)),
								 oldINEToStudent.get(Integer.parseInt((String) studentsToNotAssign.get(element))));
		}
	}*/
	
	/**
	 * Méthode pour charger un tutorat précédemment sauvegardé dans un fichier json
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws ExceptionPromo
	 * @throws ExceptionNotInTutoring
	 * @throws ExceptionTooManyAssignments
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
		loadForcedAssignment(tutoring, json, oldINEToStudent);
		loadMotivations(tutoring, json, oldINEToStudent);
		loadStudentsToNotAssign(tutoring, json, oldINEToStudent);
		
		return tutoring;
	}*/
}

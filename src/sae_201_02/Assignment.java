package sae_201_02;

/**
 * Classe qui règle le problème d'affectation
 * @author lezlon
 *
 */
public abstract class Assignment {
	
	public static void main(String[] args) {
		Tutoring tutoring = new Tutoring();
		
		/**
		 * Ajout de tous les élèves dans l'instance de Tutoring
		 */
		tutoring.addStudent(new Student("Sophie", "Vallee", 15.5, 2, 0));
		tutoring.addStudent(new Student("Nicolas", "Roche", 16.5, 3, 0));
		tutoring.addStudent(new Student("Maurice", "Fernandez", 14.5, 3, 3));
		tutoring.addStudent(new Student("William", "Letellier", 18.5, 2, 2));
		tutoring.addStudent(new Student("Paul", "Sanchez", 13, 3, 0));
		
		tutoring.addStudent(new Student("Charles", "Letellier", 8, 1, 0));
		tutoring.addStudent(new Student("Daniel", "Daniel", 9.5, 1, 1));
		tutoring.addStudent(new Student("François", "Bertin", 7, 1, 1));
		tutoring.addStudent(new Student("Sabine", "Leleu", 5.5, 1, 0));
		tutoring.addStudent(new Student("Gabriel", "Marin", 9, 1, 8));
		tutoring.addStudent(new Student("Juliette", "Traore", 12, 1, 0));
		tutoring.addStudent(new Student("Franck", "Hebert", 2.5, 1, 0));
		
		/** for (String[] student: DonneesPourTester.studentData) {
			Student s = new Student(student[0], student[1], Double.valueOf(student[2]), Integer.parseInt(student[3]));
			tutoring.addStudent(s);
		} */
		
		tutoring.createAssignments();
		
		System.out.println(tutoring.toStringTutees());
	}
}

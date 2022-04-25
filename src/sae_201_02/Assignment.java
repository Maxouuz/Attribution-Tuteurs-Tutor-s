package sae_201_02;

import fr.ulille.but.sae2_02.graphes.*;
import fr.ulille.but.sae2_02.donnees.DonneesPourTester;

public class Assignment {
	public static void main(String[] args) {
		Tutoring tutoring = new Tutoring();
		GrapheNonOrienteValue<Student> graphe = new GrapheNonOrienteValue<Student>();
		
		/**
		 * Ajout de tous les élèves dans l'instance de Tutoring et dans le Graphe
		 */
		for (String[] student: DonneesPourTester.studentData) {
			Student s = new Student(student[0], student[1], Double.valueOf(student[2]), Integer.parseInt(student[3]));
			tutoring.addStudent(s);
			graphe.ajouterSommet(s);
		}
		
		/**
		 * Ajout des arêtes
		 */
		for (Student tutee: tutoring.getTutees()) {
			for (Student tutor: tutoring.getTutors()) {
				// FORMULE:
				// PromoTuteur*16 + MoyenneTuteur*4 + (20 - NoteTutoré) * 2
				graphe.ajouterArete(tutee, tutor, tutor.getPROMO() * 16 + tutor.getMoyenne() * 4 + (20 - tutee.getMoyenne()) * 2); 
			}
		}
		
		CalculAffectation<Student> calcul = new CalculAffectation<Student>(graphe, tutoring.getTutees(), tutoring.getTutors());
		
		System.out.println(calcul.getAffectation());
		System.out.println(calcul.getCout());
	}
}

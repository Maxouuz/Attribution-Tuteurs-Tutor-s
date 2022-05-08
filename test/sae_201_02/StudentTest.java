package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Student
 * 
 * @author lezlon
 *
 */
class StudentTest {

	@Test
	void equalsTest() throws ExceptionPromo {
		Student st1 = new Student("Jean", "Dupont", 6.9, 1, 0);
		Student st2 = new Student("Jean", "Dupont", 6.9, 1, 0);

		// Même instance
		assertEquals(st1, st1);
		// Même attributs mais INE différent
		assertFalse(st1.equals(st2));
	}

	@Test
	void scoreSameYearTest() throws ExceptionPromo {
		/**
		 * Rappel: Plus le score est haut, plus l'étudiant est considéré comme meilleur
		 */

		Student st1 = new Student("Jean", "Dupont", 10, 3, 0);
		Student st2 = new Student("Hugues", "Bigot", 9, 3, 0);
		Student st3 = new Student("Franck", "Hebert", 10, 3, 1);

		// Note supérieure
		assertTrue(st1.getScore() > st2.getScore());
		// Score moins bon pour un étudiant ayant des absences
		assertTrue(st3.getScore() < st1.getScore());
	}

	@Test
	void errorWithWrongPromoTest() {
		// Promo inférieur à 1
		boolean res = false;
		try {
			new Student("Jean", "Dupont", 10, 0, 0);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertTrue(res);

		// Promo supérieur à 3
		res = false;
		try {
			new Student("Jean", "Dupont", 10, 4, 0);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertTrue(res);

		// Promo entre 1 et 3
		res = false;
		try {
			new Student("Jean", "Dupont", 10, 1, 0);
			new Student("Hugues", "Bigot", 10, 2, 0);
			new Student("Franck", "Hebert", 10, 3, 0);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertFalse(res);
	}

	@Test
	void youngerStudentAlwaysLowerScoreTest() throws ExceptionPromo {
		// Étudiant de 1ère année avec le score le plus haut possible
		Student st1 = new Student("Jean", "Dupont", 20, 1, 0);
		// Étudiant de 2ème année avec le score le plus mauvais possible
		Student st2 = new Student("Hugues", "Bigot", 0, 2, Integer.MAX_VALUE);

		assertTrue(st1.getScore() < st2.getScore());

		// Étudiant de 2ème année avec le score le plus haut possible
		Student st3 = new Student("Charles", "Letellier", 20, 2, 0);
		// Étudiant de 3èle année avec le score le plus mauvais possible
		Student st4 = new Student("Franck", "Hebert", 0, 3, Integer.MAX_VALUE);
		assertTrue(st3.getScore() < st4.getScore());
	}

	@Test
	void tuteeCantBeTutorAndViceVersa() throws ExceptionPromo {
		/**
		 * Rappel: Un étudiant de 1ere année ne peut pas etre tuteur et un étudiant de
		 * 2eme ou 3eme année ne peut pas etre tutoré.
		 */

		// Étudiants de 1ère année avec paramètres aléatoire
		Student st10 = new Student("Jean", "Dupont", 16, 1, 0);
		Student st11 = new Student("Jules", "Demain", 18, 1, 2);
		// Étudiants de 2ème année avec paramètres aléatoire
		Student st20 = new Student("Jordan", "Coubert", 15, 2, 0);
		Student st21 = new Student("Jade", "Jadot", 14, 2, 3);
		// Étudiants de 3ème année avec paramètres aléatoire
		Student st30 = new Student("jacques", "Petard", 12, 3, 0);
		Student st31 = new Student("Franck", "Michalak", 19, 3, 0);

		// Vérification pour les 1ere année
		assertTrue(st10.canBeTutee());
		assertTrue(st11.canBeTutee());
		assertFalse(st10.canBeTutor());
		assertFalse(st11.canBeTutor());

		// Vérification pour les 2eme/3eme année
		assertTrue(st20.canBeTutor());
		assertTrue(st21.canBeTutor());
		assertTrue(st30.canBeTutor());
		assertTrue(st31.canBeTutor());
		assertFalse(st20.canBeTutee());
		assertFalse(st21.canBeTutee());
		assertFalse(st30.canBeTutee());
		assertFalse(st31.canBeTutee());
	}

	@Test
	void tuteeAndTutorsetNegativeNbAbsences() throws ExceptionPromo {
		/**
		 * Un étudiant à un compteur d'absence, ici nous vérifions qu'il ne peut pas
		 * etre négatif.
		 */
		boolean res = false;
		// Étudiants avec nombre d'absence nul ou positif
		Student st1 = new Student("Jean", "Dupont", 16, 1, 0);
		Student st2 = new Student("Jean", "Dupont", 16, 1, 2);

		// Test du getNbAbsences()
		assertEquals(st1.getNbAbsences(), 0);
		assertEquals(st2.getNbAbsences(), 2);

		// Nombre d'absence négative
		try {
			st1.setNbAbsences(-1);
			st2.setNbAbsences(-10);
		} catch (IllegalArgumentException e) {
			res = true;
		}
		assertTrue(res);
	}

	@Test
	void scoreComparatorTest() throws ExceptionPromo {
		/**
		 * 
		 */
		
		//Création de tableaux d'étudiants
		ArrayList<Student> groupeDe1ereAnnee = new ArrayList<Student>();
		ArrayList<Student> groupeDe1ereAnneeTrié = new ArrayList<Student>();
		ArrayList<Student> groupeDe2Et3emeAnnee = new ArrayList<Student>();
		ArrayList<Student> groupeDe2Et3emeAnneeTrié = new ArrayList<Student>();
		
		//Initialisation des étudiants de 1ere année
		Student st1 = new Student("Jean", "Dupont", 12, 1, 0);
		Student st2 = new Student("Jordan", "Coubert", 15, 1, 0);
		Student st3 = new Student("Franck", "Michalak", 19, 1, 0);
		
		//Remplissage d'un tableau sans trier : 
		groupeDe1ereAnnee.add(st3);
		groupeDe1ereAnnee.add(st1);
		groupeDe1ereAnnee.add(st2);
		
		//Remplissage d'un tableau en le triant à la main :
		groupeDe1ereAnneeTrié.add(st1);
		groupeDe1ereAnneeTrié.add(st2);
		groupeDe1ereAnneeTrié.add(st3);
		
		//Tri des étudiants par score croissant
		Collections.sort(groupeDe1ereAnnee, new ScoreComparator());
		
		//Comparaison avec le groupe trier à la main : 
		assertTrue(groupeDe1ereAnnee.equals(groupeDe1ereAnneeTrié));
		
		//Même chose mais avec des étudiants de 2eme et 3eme année :
		Student st11 = new Student("Jean", "Dupont", 12, 2, 0);
		Student st21 = new Student("Jordan", "Coubert", 16, 3, 0);
		Student st31 = new Student("Franck", "Michalak", 20, 2, 0);
		groupeDe2Et3emeAnnee.add(st31);
		groupeDe2Et3emeAnnee.add(st11);
		groupeDe2Et3emeAnnee.add(st21);
		groupeDe2Et3emeAnneeTrié.add(st11);
		groupeDe2Et3emeAnneeTrié.add(st31);
		groupeDe2Et3emeAnneeTrié.add(st21);
		
		Collections.sort(groupeDe2Et3emeAnnee, new ScoreComparator());
		assertTrue(groupeDe2Et3emeAnnee.equals(groupeDe2Et3emeAnneeTrié));

		
	}
}

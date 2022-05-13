package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

		// Étudiant de 1ère année avec paramètres aléatoire
		Student st10 = new Student("Jean", "Dupont", 16, 1, 0);
		// Étudiant de 2ème année avec paramètres aléatoire
		Student st20 = new Student("Jordan", "Coubert", 15, 2, 0);
		// Étudiant de 3ème année avec paramètres aléatoire
		Student st30 = new Student("jacques", "Petard", 12, 3, 0);

		// Vérification pour les 1ere année
		assertTrue(st10.canBeTutee());
		assertFalse(st10.canBeTutor());

		// Vérification pour les 2eme/3eme année
		assertTrue(st20.canBeTutor());
		assertTrue(st30.canBeTutor());
		assertFalse(st20.canBeTutee());
		assertFalse(st30.canBeTutee());
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
	void scoreComparatorTest1st2ndYear() throws ExceptionPromo {
		/**
		 * Rappel : scoreComparator() trie les étudiants par score dans l'ordre croissant.
		 */

		//Création de tableaux d'étudiants
		List<Student> groupeDe1ereAnnee = new ArrayList<>();
		List<Student> groupeDe1ereAnneeTrie = new ArrayList<>();
		
		
		//Initialisation des étudiants de 1ere année
		Student st1 = new Student("Jean", "Dupont", 12, 1, 0);
		Student st2 = new Student("Jordan", "Coubert", 15, 1, 0);
		Student st3 = new Student("Franck", "Michalak", 19, 1, 0);
		
		//Remplissage d'un tableau sans trier : 
		groupeDe1ereAnnee.add(st3);
		groupeDe1ereAnnee.add(st1);
		groupeDe1ereAnnee.add(st2);
		
		//Remplissage d'un tableau en le triant à la main :
		groupeDe1ereAnneeTrie.add(st1);
		groupeDe1ereAnneeTrie.add(st2);
		groupeDe1ereAnneeTrie.add(st3);
		
		//Trie des étudiants par score croissant
		Collections.sort(groupeDe1ereAnnee, new ScoreComparator());
		
		//Comparaison avec le groupe trier à la main : 
		assertEquals(groupeDe1ereAnnee, groupeDe1ereAnneeTrie);
	}

	@Test
	void scoreComparatorTest2nd3rdYear() throws ExceptionPromo {
		/**
		 * Rappel : scoreComparator() trie les étudiants par score dans l'ordre croissant.
		 */
		
		List<Student> groupeDe2Et3emeAnnee = new ArrayList<>();
		List<Student> groupeDe2Et3emeAnneeTrie = new ArrayList<>();

		//Même chose mais avec des étudiants de 2eme et 3eme année :
		Student st11 = new Student("Jean", "Dupont", 12, 2, 0);
		Student st21 = new Student("Jordan", "Coubert", 16, 3, 0);
		Student st31 = new Student("Franck", "Michalak", 20, 2, 0);

		groupeDe2Et3emeAnnee.add(st31);
		groupeDe2Et3emeAnnee.add(st11);
		groupeDe2Et3emeAnnee.add(st21);
		groupeDe2Et3emeAnneeTrie.add(st11);
		groupeDe2Et3emeAnneeTrie.add(st31);
		groupeDe2Et3emeAnneeTrie.add(st21);
		
		Collections.sort(groupeDe2Et3emeAnnee, new ScoreComparator());
		assertEquals(groupeDe2Et3emeAnnee, groupeDe2Et3emeAnneeTrie);
	}
}

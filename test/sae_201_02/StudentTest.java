package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Student
 * 
 * @author Rémi VAUTIER, Nathan HALLEZ, Maxence STIEVENARD
 *
 */
class StudentTest {
	
	/** Attribut tutoring utilisé pour tester les scores */
	static Tutoring emptyTutoring = new Tutoring(Subject.R101);
	
	@Test
	void equalsTest() throws ExceptionPromo {
		Student st1 = Student.createStudent("Jean", "Dupont", 1, 0, 6.9);
		Student st2 = Student.createStudent("Jean", "Dupont", 1, 0, 6.9);

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

		Student st1 = Student.createStudent("Jean", "Dupont", 3, 0, 10);
		Student st2 = Student.createStudent("Hugues", "Bigot", 3, 0, 9);
		Student st3 = Student.createStudent("Franck", "Hebert", 3, 1, 10);

		// Note supérieure
		assertTrue(emptyTutoring.getScore(st1) > emptyTutoring.getScore(st2));
		// Score moins bon pour un étudiant ayant des absences
		assertTrue(emptyTutoring.getScore(st3) < emptyTutoring.getScore(st1));
	}

	@Test
	void errorWithWrongPromoTest() {
		// Promo inférieur à 1
		boolean res = false;
		try {
			Student.createStudent("Jean", "Dupont", 0, 0);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertTrue(res);

		// Promo supérieur à 3
		res = false;
		try {
			Student.createStudent("Jean", "Dupont", 4, 0);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertTrue(res);

		// Promo entre 1 et 3
		res = false;
		try {
			Student.createStudent("Jean", "Dupont", 1, 0);
			Student.createStudent("Hugues", "Bigot", 2, 0);
			Student.createStudent("Franck", "Hebert", 3, 0);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertFalse(res);
	}

	@Test
	void youngerStudentAlwaysLowerScoreTest() throws ExceptionPromo {
		// Étudiant de 1ère année avec le score le plus haut possible
		Student st1 = Student.createStudent("Jean", "Dupont", 1, 0, 20);
		// Étudiant de 2ème année avec le score le plus mauvais possible
		Student st2 = Student.createStudent("Hugues", "Bigot", 2, Integer.MAX_VALUE, 0);

		assertTrue(emptyTutoring.getScore(st1) < emptyTutoring.getScore(st2));

		// Étudiant de 2ème année avec le score le plus haut possible
		Student st3 = Student.createStudent("Charles", "Letellier", 2, 0, 20);
		// Étudiant de 3èle année avec le score le plus mauvais possible
		Student st4 = Student.createStudent("Franck", "Hebert", 3, Integer.MAX_VALUE, 0);
		assertTrue(emptyTutoring.getScore(st3) < emptyTutoring.getScore(st4));
	}

	@Test
	void tuteeCantBeTutorAndViceVersa() throws ExceptionPromo {
		/**
		 * Rappel: Un étudiant de 1ere année ne peut pas etre tuteur et un étudiant de
		 * 2eme ou 3eme année ne peut pas etre tutoré.
		 */

		// Étudiant de 1ère année avec paramètres aléatoire
		Student st10 = Student.createStudent("Jean", "Dupont", 1, 0, 16);
		// Étudiant de 2ème année avec paramètres aléatoire
		Student st20 = Student.createStudent("Jordan", "Coubert", 2, 0, 15);
		// Étudiant de 3ème année avec paramètres aléatoire
		Student st30 = Student.createStudent("jacques", "Petard", 3, 0, 12);

		// Vérification pour les 1ere année
		assertTrue(st10.isTutee());
		assertFalse(st10.isTutor());

		// Vérification pour les 2eme/3eme année
		assertTrue(st20.isTutor());
		assertTrue(st30.isTutor());
		assertFalse(st20.isTutee());
		assertFalse(st30.isTutee());
	}

	@Test
	void tuteeAndTutorsetNegativeNbAbsences() throws ExceptionPromo {
		/**
		 * Un étudiant à un compteur d'absence, ici nous vérifions qu'il ne peut pas
		 * etre négatif.
		 */
		boolean res = false;
		// Étudiants avec nombre d'absence nul ou positif
		Student st1 = Student.createStudent("Jean", "Dupont", 1, 0, 16);
		Student st2 = Student.createStudent("Jean", "Dupont", 1, 2, 16);

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
		Student st1 = Student.createStudent("Jean", "Dupont", 1, 0, 12);
		Student st2 = Student.createStudent("Jordan", "Coubert", 1, 0, 15);
		Student st3 = Student.createStudent("Franck", "Michalak", 1, 0, 19);
		
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
		Student st11 = Student.createStudent("Jean", "Dupont", 2, 0, 12);
		Student st21 = Student.createStudent("Jordan", "Coubert", 3, 0, 16);
		Student st31 = Student.createStudent("Franck", "Michalak", 2, 0, 20);

		groupeDe2Et3emeAnnee.add(st31);
		groupeDe2Et3emeAnnee.add(st11);
		groupeDe2Et3emeAnnee.add(st21);
		groupeDe2Et3emeAnneeTrie.add(st11);
		groupeDe2Et3emeAnneeTrie.add(st31);
		groupeDe2Et3emeAnneeTrie.add(st21);
		
		Collections.sort(groupeDe2Et3emeAnnee, new ScoreComparator());
		assertEquals(groupeDe2Et3emeAnnee, groupeDe2Et3emeAnneeTrie);
	}
	
	@Test
	void compareToEqualsTest() throws ExceptionPromo {
		/**
		 * Rappel: La fonction compareTo compare les prénoms et nom de deux étudiants.
		 * S'ils sont identiques, la fonction renvoie 0, sinon, un nombres inférieures à 0.
		 */
		//Je créé 2 fois 2 personnes, avec les memes prénom et nom
		Student std1 = Student.createStudent("Maxence","Stievenard", 3, 11, 15);
		Student std1bis = Student.createStudent("Maxence","Stievenard", 2, 0, 12);
		Student std2 = Student.createStudent("Nathan","Hallez", 3, 9, 10);
		Student std2bis = Student.createStudent("Nathan","Hallez", 1, 0, 8);
		// je compare les personnes deux par deux, et je vérifie si la fonction envoie 0 en cas d'égalité.
		assertEquals(0,std1.compareTo(std1bis));
		assertEquals(0,std2.compareTo(std2bis));
		assertEquals(0,std1bis.compareTo(std1));
		assertEquals(0,std2bis.compareTo(std2));
			
	}
	
	@Test
	void compareToDifferentTest() throws ExceptionPromo {
		Student std1 = Student.createStudent("Maxence","Stievenard", 3, 11, 15);
		Student std2 = Student.createStudent("Nathan","Hallez", 3, 9, 10);
		
		// Je vérifie si la fonction ne renvoie pas 0 en cas d'inégalité
		assertNotEquals(0,std1.compareTo(std2));
		assertNotEquals(0,std2.compareTo(std1));
	}
}

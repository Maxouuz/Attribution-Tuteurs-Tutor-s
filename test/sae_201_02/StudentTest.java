package sae_201_02;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Student
 * @author lezlon
 *
 */
class StudentTest {

	@Test
	void equalsTest() throws ExceptionPromo {
		Student st1 = new Student("Jean","Dupont",6.9,1,0);
		Student st2 = new Student("Jean","Dupont",6.9,1,0);
		
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
		
		Student st1 = new Student("Jean","Dupont",10,3,0);
		Student st2 = new Student("Hugues","Bigot",9,3,0);
		Student st3 = new Student("Franck","Hebert",10,3,1);
		
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
			new Student("Hugues","Bigot", 10, 2, 0);
			new Student("Franck","Hebert", 10, 3, 0);
		} catch (ExceptionPromo e) {
			res = true;
		}
		assertFalse(res);
	}
	
	@Test
	void youngerStudentAlwaysLowerScoreTest() throws ExceptionPromo  {
		// Étudiant de 1ère année avec le score le plus haut possible
		Student st1 = new Student("Jean","Dupont",20,1,0);
		// Étudiant de 2ème année avec le score le plus mauvais possible
		Student st2 = new Student("Hugues","Bigot",0,2,Integer.MAX_VALUE);
		
		assertTrue(st1.getScore() < st2.getScore());
		
		// Étudiant de 2ème année avec le score le plus haut possible
		Student st3 = new Student("Charles", "Letellier",20,2,0);
		// Étudiant de 3èle année avec le score le plus mauvais possible
		Student st4 = new Student("Franck","Hebert",0,3,Integer.MAX_VALUE);
		assertTrue(st3.getScore() < st4.getScore());
	}
}

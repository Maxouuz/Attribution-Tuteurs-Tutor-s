package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Teacher
 * @author nathan.hallez.etu
 */
class TeacherTest {
	/** Variable de test */
	Teacher teacher;

	@BeforeEach
	/**
	 * Initialisation de la variable test
	 */
	public void initialization() {
		teacher = new Teacher("Jean", "Dupont", "mdp123");
	}
	
	@Test
	void addSubjectTeachedTest() {
		teacher.addSubjectTeached(Subject.R101);
		assertEquals(teacher.getSubjectsTeached().size(), 1);
		assertTrue(teacher.getSubjectsTeached().contains(Subject.R101));
	}
	
	@Test
	void removeSubjectTeached() {
		teacher.addSubjectTeached(Subject.R101);
		assertFalse(teacher.removeSubjectTeached(Subject.R102));
		assertTrue(teacher.removeSubjectTeached(Subject.R101));
		assertFalse(teacher.getSubjectsTeached().contains(Subject.R102));
	}
	
	@Test
	void passwordTest() {
		assertTrue(teacher.checkPassword("mdp123"));
		assertFalse(teacher.checkPassword("password"));
	}
}

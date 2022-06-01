package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

/**
 * Classe de test sur la classe Subject
 * @author nathan.hallez.etu
 */
public class SubjectTest {
	@Test
	/**
	 * Test de la méthode toString de Subject Test
	 */
	public void testGetName() {
		assertEquals("Initiation au développement", Subject.R101.getName());
	}
}

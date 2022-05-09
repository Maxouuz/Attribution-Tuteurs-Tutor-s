package sae_201_02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

/**
 * Classe de test sur la classe Subject
 * @author nathan.hallez.etu
 */
public class SubjectTest {
	@Test
	public void testToString() {
		assertEquals("R101 - Initiation au développement", Subject.R101.toString());
	}
}

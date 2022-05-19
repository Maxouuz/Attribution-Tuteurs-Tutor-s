package sae_201_02;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe générique permettant de créer une map accessible dans les deux sens
 * @author nathan.hallez.etu
 *
 * @param <T1> Peut être associé à un T2 uniquement
 * @param <T2> Peut être associé à plusieurs T1
 */
public class BidirectionalMap<T1, T2> {
	private Map<T1, T2> t1ToT2;
	private Map<T2, List<T1>> t2ToT1;
	
	public BidirectionalMap() {
		this.t1ToT2 = new HashMap<>();
		this.t2ToT1 = new HashMap<>();
	}
}

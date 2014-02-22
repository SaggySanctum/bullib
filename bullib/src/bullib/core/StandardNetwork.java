package bullib.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import bullib.network.Network;

public class StandardNetwork<N extends Serializable> extends Network<N, StandardWeight, Double>{
	private static final long serialVersionUID = 1L;
	
	public static Comparator<Entry<?, StandardWeight>> comparator = new Comparator<Entry<?, StandardWeight>>(){
		@Override
		public int compare(Entry<?, StandardWeight> o1, Entry<?, StandardWeight> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};

	public StandardNetwork(N root, int max) {
		super(root, max);
	}

	@Override
	protected Collection<N> prune(Set<Entry<N, StandardWeight>> links) {
		TreeSet<N> removals = new TreeSet<N>();
		LinkedList<Entry<N,StandardWeight>> sorted = new LinkedList<Entry<N,StandardWeight>>();
		sorted.addAll(links);
		Collections.sort((List<Entry<N,StandardWeight>>)sorted, comparator);
		for(int index = 0; index < 3; index++){
			removals.add(sorted.get(index).getKey());
		}
		return removals;
	}
}

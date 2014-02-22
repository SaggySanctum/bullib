package bullib.network;

import bullib.network.Weight;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

// Representation of a network accessed via unique generic nodes. No error checking, exceptions fall through.
public abstract class Network<N extends Serializable, W extends Weight<D>, D extends Serializable> implements Serializable{
	private static final long serialVersionUID = 1L;
	public final N root;
	private int maxlinks;
	private HashMap<N, HashMap<N, W>> graph;
	private HashMap<N, HashSet<N>> callback;

	public Network(N aroot, int max){
		root = aroot;
		maxlinks = max;
		graph = new HashMap<N, HashMap<N, W>>();	
		callback = new HashMap<N, HashSet<N>>();
		
		graph.put(root, new HashMap<N, W>());
		callback.put(root, new HashSet<N>());
	} 
	
	// implement in a derived class to choose which connection to remove
	protected abstract Collection<N> prune(Set<Map.Entry<N, W>> links);

	public synchronized boolean contains(N location){
		return graph.containsKey(location);
	}
	
	public synchronized boolean contains(N location, N destination){
		if(!contains(location)){
			return false;
		}
		return graph.get(location).containsKey(destination);
	}
	
	public synchronized Set<N> getNodes(){
		return graph.keySet();
	}
	
	public synchronized Set<N> getIncomingLinks(N location){
		return callback.get(location); 
	}

	public synchronized Set<Map.Entry<N, W>> getOutgoingLinks(N location){
		return graph.get(location).entrySet();
	}

	public synchronized void addLink(N location, N destination, W weight) throws Exception{
		// Grab the set of nodes adjacent to location. Add location and instantiate the set if it doesn't exist.
		HashMap<N, W> adjacent;
		if(graph.containsKey(location)){
			adjacent = graph.get(location);
		}
		else{
			adjacent = new HashMap<N, W>();
			graph.put(location, adjacent);
		}

		// Grab and modify the weight for the path to the destination node. Add destination and instantiate the weight if it doesn't exist.
		if(adjacent.containsKey(destination)){
			adjacent.get(destination).collide(weight);
		}
		else{
			checkPrune(adjacent, location);
			adjacent.put(destination, weight);
		}
		
		// Add the destination and its map of links if it doesn't exist.
		if(!graph.containsKey(destination)){
			graph.put(destination, new HashMap<N, W>());
		}
		
		registerCallback(location, destination);
	}
	
	public synchronized void advanceCounter(N location, N destination){
		graph.get(location).get(destination).advanceCounter();
	}
	
	public synchronized void resetCounter(N location, N destination){
		graph.get(location).get(destination).resetCounter();
	}
	
	
	private void checkPrune(HashMap<N, W> adjacent, N location){
		if(adjacent.size() + 1 > maxlinks){
			for(N r : prune(getOutgoingLinks(location))){
				adjacent.remove(r);
				callback.get(r).remove(location);
				// if the node is alienated, remove it
				if(callback.get(r).size() == 0){
					graph.remove(r);
					callback.remove(r);
				}
			}
		}
	}
	
	private void registerCallback(N location, N destination){
		HashSet<N> back;
		if(callback.containsKey(destination)){
			back = callback.get(destination);
			back.add(location);
		}
		else{
			back = new HashSet<N>();
			back.add(location);
			callback.put(destination, back);
		}
		if(!callback.containsKey(location)){
			callback.put(location, new HashSet<N>());
		}
	}
}

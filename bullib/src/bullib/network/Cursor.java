package bullib.network;

import bullib.network.Network;

import java.io.Serializable;
import java.util.LinkedList;

// Used to iterate, navigate, and manage a Network
public class Cursor<N extends Serializable, W extends Weight<D>, D extends Serializable> {
	// shortest path, can also be used to find the shortest path to express an idea.
	// find cycles
	// remember path and backpropagate influences on weight

	private Network<N, Weight<D>, D> network;
	
	private N location;
	private LinkedList<N> history;

	public Cursor(Network<N, Weight<D>, D> anetwork){
		network = anetwork;
		reset();
	}
	
	public void reset(){
		location = network.root;
		history = new LinkedList<N>();
	}

	public N getLocation(){
		return location;
	}

	public LinkedList<N> getHistory(){
		return history;
	}

	public void move(N next) throws Exception{
		if(!network.contains(location, next)){
			throw new Exception("'"+next+"' is not adjacent to location, still performing the move.");
		}
		location = next;
		record();
	}

	public void place(N destination, Weight<D> weight) throws Exception{
		network.addLink(location, destination, weight);
	}

	public void placeAndMove(N destination, Weight<D> weight) throws Exception{
		place(destination, weight);
		location = destination;
		record();
	}

	private void record(){
		network.advanceCounter(history.getLast(), location);
		history.addLast(location);
	}
}
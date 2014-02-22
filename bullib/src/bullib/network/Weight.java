package bullib.network;

import java.io.Serializable;
import java.util.Set;

// Represents the weight of a path, has fields to represent the value and the number of additions.
public abstract class Weight<D extends Serializable> implements Serializable, Comparable<Weight<D>>{
	private static final long serialVersionUID = 1L;
	
	protected int encounters;
	
	// external, collides the weight with another, modifying it's value
	public void collideWith(Weight<D> novel, Set<Weight<D>> intertwined){
		collide(novel);
		correct(novel, intertwined);
	}
	
	public void resetCounter(){
		encounters = 0;
	}
	
	@Override
	// greater values more likely
	public int compareTo(Weight<D> o) {
		return compare(o) - encounters;
	}
	
	// advances encounters to help avoid cycles, pushed self farther down in sort order as it increases
	public abstract void advanceCounter();
	
	// changes the value of the current weight by an introduced value
	protected abstract void collide(Weight<D> novel);
	
	// corrects weight if it is invalid
	protected abstract void correct(Weight<D> novel, Set<Weight<D>> intertwined);
	
	// determines the sort order of weights, greater is lower relevance, so it is sorted lower
	protected abstract int compare(Weight<D> other);
}
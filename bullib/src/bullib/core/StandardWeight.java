package bullib.core;

import java.util.Set;

import bullib.network.Weight;

public class StandardWeight extends Weight<Double>{
	private static final long serialVersionUID = 1L;
	
	public double value;
	public double size;
	
	public StandardWeight(double v){
		value = v;
		size = 1;
	}
	
	@Override
	public void advanceCounter() {
		encounters *= 2;
	}

	@Override
	protected void collide(Weight<Double> novel) {
		value = (value / size++) + ((StandardWeight)novel).value / size;
	}

	@Override
	protected void correct(Weight<Double> novel, Set<Weight<Double>> intertwined) {
		// salvage weight, and scale all other weights
		StandardWeight w;
		if(value == Double.POSITIVE_INFINITY){
			value = Double.MAX_VALUE;
			for(Weight<Double> cast : intertwined){
				w = (StandardWeight)cast;
				w.value /= 2;
				if(w.value == Double.NEGATIVE_INFINITY){
					w.value = 0.0;
				}
			}
			// attempt the collision again
			collide(novel);
		}
		else if(value == Double.NEGATIVE_INFINITY){
			value = Double.MIN_VALUE;
			for(Weight<Double> cast : intertwined){
				w = (StandardWeight)cast;
				w.value /= 2;
				if(w.value == Double.NEGATIVE_INFINITY){
					w.value = 0.0;
				}
			}
			// attempt the collision again
			collide(novel);
		}
		
		// ensure the weight is still valid
		if(value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY || value == Double.NaN){
			throw new IllegalArgumentException("Weight "+this+" failed it's validity check. The previous collision made it invalid");
		}
	}

	@Override
	protected int compare(Weight<Double> other) {
		return (int)(value - ((StandardWeight)other).value);
	}
}

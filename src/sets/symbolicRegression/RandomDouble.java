package sets.symbolicRegression;

import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class RandomDouble extends Terminal{

	
	private double value;
	private double min, max;
	private static int KIND;
	
	public RandomDouble(double min, double max, GPConfig conf) {
		super(ReturnDouble.TYPENUM, "RandomDouble");
		this.min = min;
		this.max = max;
		
		double range = max - min;
		value = conf.randomNumGenerator.nextDouble()*(range + 1) + min;
	}

	
	public void print(StringBuilder s) {
		s.append(String.format("%sx%f", getName(), value));
	}


	@Override
	public RandomDouble getNew(GPConfig config) {
		return new RandomDouble(min,max,config);
	}


	@Override
	public void evaluate(ReturnData out) {
		((ReturnDouble) out).setValue(value);
	}


	@Override
	public Node copy(GPConfig conf) {
		 return ((RandomDouble)NodeFactory.newNode(getKind(), conf)).init(value);
	}

	private RandomDouble init(double v){this.value = v; return this;}

	@Override
	public Node setKind(int kind) {
		KIND= kind;
		return this;
	}


	@Override
	public int getKind() {
		return KIND;
	}


	@Override
	public RandomDouble generate(String s, GPConfig conf) {
		if(s.startsWith(getName())){
			String v = s.substring(getName().length() + 1);
			return ((RandomDouble)NodeFactory.newNode(getKind(), conf)).init(Double.parseDouble(v));
		}
		return null;
	}


	@Override
	public RandomDouble generate(GPConfig conf) {
		return ((RandomDouble)NodeFactory.newNode(getKind(), conf));
	}
	
}

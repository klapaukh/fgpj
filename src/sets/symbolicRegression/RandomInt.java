package sets.symbolicRegression;

import library.GPConfig;
import library.Node;
import library.NodeFactory;
import library.ReturnData;
import library.Terminal;

public class RandomInt extends Terminal{

	
	private int value;
	private static int KIND;
	
	public RandomInt(GPConfig conf) {
		super(ReturnDouble.TYPENUM, "Random");
		value = (int) (conf.randomNumGenerator.nextDouble() * 6);
	}

	
	public void print(StringBuilder s) {
		s.append(String.format("%sx%d", getName(), value));
	}


	@Override
	public RandomInt getNew(GPConfig config) {
		return new RandomInt(config);
	}


	@Override
	public void evaluate(ReturnData out) {
		((ReturnDouble) out).setValue(value);
	}


	@Override
	public Node copy(GPConfig conf) {
		 return ((RandomInt)NodeFactory.newNode(getKind(), conf)).init(value);
	}

	private RandomInt init(int v){this.value = v; return this;}

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
	public RandomInt generate(String s, GPConfig conf) {
		if(s.startsWith(getName())){
			String v = s.substring(getName().length() + 1);
			return ((RandomInt)NodeFactory.newNode(getKind(), conf)).init(Integer.parseInt(v));
		}
		return null;
	}


	@Override
	public RandomInt generate(GPConfig conf) {
		return ((RandomInt)NodeFactory.newNode(getKind(), conf));
	}
	
}

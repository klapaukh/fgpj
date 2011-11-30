package nz.ac.vuw.ecs.fgpj.examples.symbolicRegression;
/*
FGPJ Genetic Programming library
Copyright (C) 2011  Roman Klapaukh

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.Node;
import nz.ac.vuw.ecs.fgpj.core.NodeFactory;
import nz.ac.vuw.ecs.fgpj.core.ReturnData;
import nz.ac.vuw.ecs.fgpj.core.Terminal;

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

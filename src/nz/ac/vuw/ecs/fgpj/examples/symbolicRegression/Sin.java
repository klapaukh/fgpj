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
import nz.ac.vuw.ecs.fgpj.core.Function;
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.Node;
import nz.ac.vuw.ecs.fgpj.core.NodeFactory;
import nz.ac.vuw.ecs.fgpj.core.ReturnData;

public class Sin extends Function{

	private static int KIND;

	public Sin() {
		super(ReturnDouble.TYPENUM, 1, "Sin");
		for (int i = 0; i < numArgs; i++) {
			setArgNReturnType(i, ReturnDouble.TYPENUM);
		}
	}

	@Override
	public Sin getNew(GPConfig config) {
		return new Sin();
	}

	@Override
	public void evaluate(ReturnData out) {
		ReturnDouble d = (ReturnDouble) out;
		getArgN(0).evaluate(d);
		d.setValue(Math.sin(d.value()));
	}

	@Override
	public Sin copy(GPConfig conf) {
		Sin a = (Sin) NodeFactory.newNode(getKind(), conf);
		for (int i = 0; i < getNumArgs(); i++) {
			a.setArgN(i, getArgN(i).copy(conf));
		}
		return a;

	}

	@Override
	public Node setKind(int kind) {
		KIND = kind;
		return this;
	}

	@Override
	public int getKind() {
		return KIND;
	}

	@Override
	public Sin generate(String s, GPConfig conf) {
		if (s.startsWith(getName())) {
			return (Sin) NodeFactory.newNode(getKind(), conf);
		}
		return null;
	}

	@Override
	public Sin generate(GPConfig conf) {
		return (Sin) NodeFactory.newNode(getKind(), conf);
	}

}
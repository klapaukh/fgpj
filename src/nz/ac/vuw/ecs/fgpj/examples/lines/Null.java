package nz.ac.vuw.ecs.fgpj.examples.lines;
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

public class Null extends Terminal {

	/**
	 * 
	 */
	public static int kind;
	public static final long serialVersionUID = 5747126155380314948L;

	public Null(GPConfig conf) {
		super(ReturnImage.TYPENUM, "null");
	}

	public Terminal generate(GPConfig conf) {
		return (Terminal) NodeFactory.newNode(getKind(), conf);
		// return new Null(conf);
	}

	public Terminal generate(String name, GPConfig conf) {
		if(name.startsWith(getName()))
			return generate(conf);
		return null;
	}

	public void evaluate(ReturnData out) {
		if (out.getTypeNum() != (ReturnImage.TYPENUM)) throw new IllegalArgumentException("Incorrect ReturnData type");

	}

	public void print(StringBuffer s) {
		s.append(getName());
	}

	public Node copy(GPConfig config) {
		return NodeFactory.newNode(getKind(), config);
		// return new Null(config);
	}

	@Override
	public Null getNew(GPConfig config) {
		return new Null(config);
	}

	@Override
	public Null setKind(int kind) {
		Null.kind = kind;
		return this;
	}

	@Override
	public int getKind() {
		return kind;
	}
}
package nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.bad;
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
import nz.ac.vuw.ecs.fgpj.core.ReturnData;

public class UnsafeReturnDouble extends ReturnData{

	public final static int TYPENUM = 1;
	private double value;
	
	public UnsafeReturnDouble() {
		super(TYPENUM);
	}
	
	public void setValue(double val){
		this.value = val;
	}
	
	public double value(){
		return value;
	}
}

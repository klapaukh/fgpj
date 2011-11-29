package sets.symbolicRegression.Bad;

import library.ReturnData;

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

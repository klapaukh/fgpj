package sets.symbolicRegression;

import library.ReturnData;

public class ReturnDouble extends ReturnData{

	public final static int TYPENUM = 1;
	private double value;
	
	public ReturnDouble() {
		super(TYPENUM);
	}
	
	public void setValue(double val){
		this.value = val;
	}
	
	public double value(){
		return value;
	}
}

package sets.symbolicRegression;

import library.ReturnData;

public class ReturnDouble extends ReturnData{
	
	private double x,y;

	
	public final static int TYPENUM = 4;
	private double value;
	
	
	public void setValue(double val){
		this.value = val;
	}
	
	public double value(){
		return value;
	}
	
	public ReturnDouble() {
		super(TYPENUM);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
}

package sets.lines;

import java.awt.Color;

import library.ReturnData;

public class ReturnColor extends ReturnData {
	private Color col;
	public static final int TYPENUM = 3;

	public ReturnColor() {
		initTypeNum();
	}

	public void initTypeNum() {
		setTypeNum(TYPENUM);
	}

	public void setColor(Color c) {
		col = c;
	}
	
	public Color getColor(){
		return col;
	}

}

package library;

public abstract class ReturnData {
	private int typeNum;

	public ReturnData() {
		this.typeNum = -1;
	}

	public int getTypeNum() {
		return typeNum;
	}

	public void setTypeNum(int num) {
		typeNum = num;
	}
}

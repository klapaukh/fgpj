package library;

/**
 * ReturnData represents values and their types in the program tree. Each type is assigned an unique integer
 * 
 * @author Roma
 * 
 */
public abstract class ReturnData {

	private int typeNum;

	/**
	 * Create a new Return data with type num
	 * 
	 * @param num
	 *            the type of this ReturnData
	 */
	public ReturnData(int num) {
		this.typeNum = num;
	}

	/**
	 * Get the type of this ReturnData
	 * 
	 * @return the type
	 */
	public int getTypeNum() {
		return typeNum;
	}

}

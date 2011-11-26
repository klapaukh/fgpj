package library;

import java.util.List;

/**
 * The Terminal class represents a terminal is a GP program tree
 * 
 * @author Roma
 *
 */
public abstract class Terminal extends Node {

	/**
	 * Make a new terminal. It has a return type and a name
	 * @param type The return type
	 * @param name name of the terminal
	 */
	public Terminal(int type, String name) 
	{
		super(type, 0, name);
	}

	public int computeSize()
	{
	   return 1;
	}

	public int computeDepth(int curDepth)
	{
	   setDepth(curDepth + 1);
	   return 1;
	}

	public void addTreeToVector(List<Node> list)
	{
	   list.add(this);
	}

	public void addTreeToVector(List<Node> list, int typeNum)
	{
	   if (getReturnType() == typeNum)
	      list.add(this);
	}

	public void print(StringBuffer s)
	{
	   s.append(getName());  
	}

}

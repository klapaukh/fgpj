package library;

import java.util.Vector;

public abstract class Terminal extends Node {

	public Terminal(int type, String n) 
	{
		super(type, 0, n);
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

	public void addTreeToVector(Vector<Node> vec)
	{
	   vec.add(this);
	}

	public void addTreeToVector(Vector<Node> vec, int typeNum)
	{
	   if (getReturnType() == typeNum)
	      vec.add(this);
	}

	public void print(StringBuffer s)
	{
	   s.append(getName());  
	}

}

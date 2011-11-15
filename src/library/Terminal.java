package library;

import java.util.Vector;

public abstract class Terminal extends Node {

	public Terminal(int type, String n, GPConfig conf) 
	{
		super(type, 0, n, conf);
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

	public void addToVector(Vector<Node> vec)
	{
	   vec.add(this);
	}

	public void addToVector(Vector<Node> vec, int typeNum)
	{
	   if (getReturnType() == typeNum)
	      vec.add(this);
	}

	public void print(StringBuffer s)
	{
	   s.append(getName());  
	}

}

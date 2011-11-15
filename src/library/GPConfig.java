package library;

import java.security.NoSuchAlgorithmException;

public class GPConfig {
	public Random randomNumGenerator;

	private int numParts;

	public int minDepth;

	public int maxDepth;

	public FuncSet funcSet;

	public TermSet termSet;

	public Crossover crossoverOperator;

	public Mutation mutationOperator;

	public Selection selectionOperator;

	public Fitness fitnessObject;

	public ProgramGenerator programGenerator;

	public GPConfig() {
		this(1);
	}

	public GPConfig(int numParts) {
		if (numParts < 1)
			throw new IllegalArgumentException("Num Parts < 1: " + numParts);
		this.numParts = numParts;
		minDepth = 0;
		maxDepth = 0;
		funcSet = new FuncSet(this);
		termSet = new TermSet(this);
	}

	public int getNumParts() {
		return numParts;
	}

	/*
	 * Copy Constructor Makes a complete copy of the function set and the
	 * terminal set too
	 */
	public GPConfig(GPConfig c) 
	 {
		 numParts=(c.numParts);
		 minDepth=(c.minDepth);
		 maxDepth=(c.maxDepth);
		 funcSet = new FuncSet(this);
		 termSet = new TermSet(this);
		 randomNumGenerator=(c.randomNumGenerator);
		 crossoverOperator=(c.crossoverOperator);
		 mutationOperator=(c.mutationOperator);
		 selectionOperator=(c.selectionOperator);
		 fitnessObject=(c.fitnessObject);
		 programGenerator=(c.programGenerator);
		 
	 int i;
	 Node tmp;
	 if (c.funcSet.size() > 0)
	 {
	 for (i=0;i<c.funcSet.size();i++)
	 {
	 tmp = c.funcSet.getNodeByNumber(i);
	 funcSet.addNodeToSet(tmp.getReturnType(),c.funcSet.getGenFunction(i));
	 }
	 }
	
	 if (c.termSet.size() > 0)
	 {
	 for (i=0;i<c.termSet.size();i++)
	 {
	 tmp = c.termSet.getNodeByNumber(i);
	 termSet.addNodeToSet(tmp.getReturnType(),c.termSet.getGenFunction(i));
	 }
	 }
	 }

	// Initialises the random number generator, the crossover operator,
	// the mutation operator, and the selection operator to the standard
	// base objects.
	public void defaultInit() {
		try {
			randomNumGenerator = new Random();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			randomNumGenerator = new Random(System.currentTimeMillis());
		}
		crossoverOperator = new Crossover();
		mutationOperator = new Mutation();
		selectionOperator = new Selection();
	}

}

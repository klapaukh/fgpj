package library;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * The GPConfig class holds the current settings used by the GP algorithm. It has fields for all the operators, the
 * terminal and function sets, the program generator, the settings, and an algorithm to change itself as it goes along
 * (which can just do nothing for standard GP).
 * 
 * @author Roma
 * 
 */
public class GPConfig {
	public Random randomNumGenerator;

	private int numParts;

	private int minDepth;

	private int maxDepth;

	public NodeVector<Function> funcSet;

	public NodeVector<Terminal> termSet;

	public Crossover crossoverOperator;

	public Mutation mutationOperator;

	public Selection selectionOperator;

	public Fitness fitnessObject;

	public ProgramGenerator programGenerator;

	public ConfigModifier configModifier;

	/**
	 * 
	 */
	public GPConfig() {
		this(1,1,10);
	}

	public GPConfig(int numParts, int minDepth, int maxDepth) {
		if (numParts < 1) throw new IllegalArgumentException("Num Parts < 1: " + numParts);
		this.numParts = numParts;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		funcSet = new NodeVector<Function>(this);
		termSet = new NodeVector<Terminal>(this);
	}

	/**
	 * Returns the number of root nodes that each GP Program has
	 * 
	 * @return the number of root nodes
	 */
	public int getNumParts() {
		return numParts;
	}
	
	/**
	 * Get the min depth
	 * 
	 * @return min depth of tree
	 */
	public int minDepth(){
		return minDepth();
	}

	/**
	 *  Get the maximum tree depth
	 * @return max tree depth
	 */
	public int maxDepth(){
		return maxDepth;
	}

	/**
	 * Copy Constructor Makes a complete copy of the function set and the terminal set too. The function set and
	 * terminal set will new objects
	 * 
	 * @param c
	 *            the GPConfig that this will be an exact copy of
	 */
	public GPConfig(GPConfig c) {
		numParts = (c.numParts);
		minDepth = (c.minDepth);
		maxDepth = (c.maxDepth);
		funcSet = new NodeVector<Function>(this);
		termSet = new NodeVector<Terminal>(this);
		randomNumGenerator = (c.randomNumGenerator);
		crossoverOperator = (c.crossoverOperator);
		mutationOperator = (c.mutationOperator);
		selectionOperator = (c.selectionOperator);
		fitnessObject = (c.fitnessObject);
		programGenerator = (c.programGenerator);
		configModifier = c.configModifier;

		int i;
		Node tmp;
		if (c.funcSet.size() > 0) {
			for (i = 0; i < c.funcSet.size(); i++) {
				tmp = c.funcSet.getNodeByNumber(i);
				funcSet.addNodeToSet(tmp.getReturnType(), c.funcSet.getGenFunction(i));
			}
		}

		if (c.termSet.size() > 0) {
			for (i = 0; i < c.termSet.size(); i++) {
				tmp = c.termSet.getNodeByNumber(i);
				termSet.addNodeToSet(tmp.getReturnType(), c.termSet.getGenFunction(i));
			}
		}
	}

	/**
	 * Initialises the random number generator, the crossover operator,
	 * the mutation operator, the selection operator, and the config modifier to the standard base objects.
	 * 
	 */
	public void defaultInit() {
		try {
			randomNumGenerator = new Random(ByteBuffer.wrap(SecureRandom.getInstance("SHA1PRNG").generateSeed(8))
					.getLong());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			randomNumGenerator = new Random();
		}
		crossoverOperator = new Crossover();
		mutationOperator = new Mutation();
		selectionOperator = new Selection();
		configModifier = new ConfigModifier(this) {
			@Override
			public void ModifyConfig() {
			}
		};
	}

}

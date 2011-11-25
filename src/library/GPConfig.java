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
 * The depth limit for the population technically does not have limit to how deep the depthLimit can be, however if your
 * limit is set too deep you may run out of memory on your machine. There are also other factors to consider, such as
 * the arity of your functions (are your trees fat or skinny), and the population size.
 * 
 * @author Roma
 * 
 */
public class GPConfig {
	public Random randomNumGenerator;

	private int numParts;

	private int minDepth;

	private int maxDepth;

	public final NodeVector<Function> funcSet;

	public final NodeVector<Terminal> termSet;

	public Crossover crossoverOperator;

	public Mutation mutationOperator;

	public Selection selectionOperator;

	public Fitness fitnessObject;

	public ProgramGenerator programGenerator;

	public ConfigModifier configModifier;

	/**
	 * Initialise a GPConfig with 1 root, mindepth of 1 and maxdepth of 10
	 */
	public GPConfig() {
		this(1, 1, 10);
	}

	/**
	 * Make a new GPConfig with the specified settings
	 * 
	 * @param numParts
	 *            The number of root nodes each GP program has
	 * @param minDepth
	 *            The minimum depth of a program
	 * @param maxDepth
	 *            The maximum depth of a program
	 */
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
	public int minDepth() {
		return minDepth();
	}

	/**
	 * Get the maximum tree depth
	 * 
	 * @return max tree depth
	 */
	public int maxDepth() {
		return maxDepth;
	}

	public void maxDepth(int max) {
		this.maxDepth = max;
	}

	public void minDepth(int min) {
		this.minDepth = min;
	}

	
	/**
	 * Initialises the random number generator, the crossover operator, the mutation operator, the selection operator,
	 * and the config modifier to the standard base objects.
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

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
 * The depth limit for the population is needed due to memory and time constraints. Trees can get very big, very
 * quickly. The mutation, crossover and elitism rates must sum to 1.
 * 
 * @author Roma
 * 
 */
public class GPConfig {
	public Random randomNumGenerator;

	private int numParts;

	private int minDepth;

	private int maxDepth;

	private double mutationRate;

	private double crossoverRate;

	private double elitismRate;

	public final NodeVector<Function> funcSet;

	public final NodeVector<Terminal> termSet;

	public Crossover crossoverOperator;

	public Mutation mutationOperator;

	public Selection selectionOperator;

	public Fitness fitnessObject;

	public ProgramGenerator programGenerator;

	public ConfigModifier configModifier;

	/**
	 * Initialise a GPConfig with 1 root, mindepth of 1 and maxdepth of 10. The rates must add to 1. Intialises the
	 * config with all the default operators.
	 * 
	 * @param mutationRate
	 *            The mutation rate
	 * @param crossoverRate
	 *            The crossover rate
	 * @param elitismRate
	 *            The elitism rate
	 */
	public GPConfig(double mutationRate, double crossoverRate, double elitismRate) {
		this(1, 1, 10, mutationRate, crossoverRate, elitismRate);
	}

	/**
	 * Make a new GPConfig with the specified settings. The rates must add to 1. Intialises the config with all the
	 * default operators.
	 * 
	 * @param numParts
	 *            The number of root nodes each GP program has
	 * @param minDepth
	 *            The minimum depth of a program
	 * @param maxDepth
	 *            The maximum depth of a program
	 * @param mutationRate
	 *            The mutation rate
	 * @param crossoverRate
	 *            The crossover rate
	 * @param elitismRate
	 *            The elitism rate
	 */
	public GPConfig(int numParts, int minDepth, int maxDepth, double mutationRate, double crossoverRate,
			double elitismRate) {
		if (numParts < 1) throw new IllegalArgumentException("Num Parts < 1: " + numParts);
		this.numParts = numParts;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		this.setRates(mutationRate, crossoverRate, elitismRate);
		funcSet = new NodeVector<Function>();
		termSet = new NodeVector<Terminal>();

		defaultInit();
	}

	/**
	 * Returns the number of root nodes that each GP Program has
	 * 
	 * @return the number of root nodes
	 */
	public int getNumRoots() {
		return numParts;
	}

	/**
	 * Get the program depth
	 * 
	 * @return min depth of program
	 */
	public int minDepth() {
		return minDepth;
	}

	/**
	 * Get the maximum program depth
	 * 
	 * @return max program depth
	 */
	public int maxDepth() {
		return maxDepth;
	}

	/**
	 * Set the maximum program depth
	 * 
	 * @param max
	 *            the maximum program depth
	 */

	public void maxDepth(int max) {
		this.maxDepth = max;
	}

	/**
	 * Set the minimum program depth
	 * 
	 * @param min
	 *            the new minimun program depth
	 */
	public void minDepth(int min) {
		this.minDepth = min;
	}

	/**
	 * Get the elitism rate in [0,1.0]
	 * 
	 * @return the elitism rate
	 */
	public double elitismRate() {
		return elitismRate;
	}

	/**
	 * Get the mutation rate in [0,1.0]
	 * 
	 * @return the mutation rate
	 */
	public double mutationRate() {
		return mutationRate;
	}

	/**
	 * Get the crossover rate in [0,1.0]
	 * 
	 * @return The crossover rate
	 */
	public double crossoverRate() {
		return crossoverRate;
	}

	/**
	 * Set the rates for each of the genetic operators. The rates must add to 1.
	 * 
	 * @param mutationRate
	 *            The mutation rate
	 * @param crossoverRate
	 *            The crossover rate
	 * @param elitismRate
	 *            The elitism rate
	 */
	public void setRates(double mutationRate, double crossoverRate, double elitismRate) {

		double total = mutationRate + crossoverRate + elitismRate;

		if (Double.compare(total, 1.0) != 0) {
			System.err.println("Rates for mutation, crossover, and elitism don't add up to 1.0. Auto adjusting");
			if (Double.compare(total, 0) == 0) {
				mutationRate = crossoverRate = elitismRate = 1.0 / 3.0;
			} else {
				mutationRate /= total;
				crossoverRate /= total;
				elitismRate /= total;
			}
		}

		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismRate = elitismRate;
	}

	/**
	 * Initialises the random number generator, program generator (ramped half-half), the crossover operator, the
	 * mutation operator, the selection operator (roulette wheel), and the config modifier to the standard base objects.
	 * 
	 */
	private void defaultInit() {
		try {
			randomNumGenerator = new Random(ByteBuffer.wrap(SecureRandom.getInstance("SHA1PRNG").generateSeed(8))
					.getLong());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("SHA1PRNG not present for generating random seed. Using defaults");
			randomNumGenerator = new Random();
		}
		crossoverOperator = new Crossover();
		mutationOperator = new Mutation();
		selectionOperator = new Selection();
		programGenerator = new ProgramGenerator(this);
		configModifier = new ConfigModifier() {
			@Override
			public void ModifyConfig(GPConfig g, Population pop) {
			}
		};
	}

	/**
	 * Add a Terminal to the terminal set
	 * @param returnType The return type of this terminal
	 * @param n an instance of the terminal
	 */
	public void addTerminal(Terminal n){
		NodeFactory.teach(n,this);
		this.termSet.add(n);
	}
	
	/**
	 * Add a Function to the function set
	 * @param returnType the return type of this Function
	 * @param n an instance of the function
	 */
	public void addFunction(Function n){
		NodeFactory.teach(n,this);
		this.funcSet.add(n);
	}
}

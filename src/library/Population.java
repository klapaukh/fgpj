package library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Population {

	private List<GeneticProgram> pop; // The population
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	// Number of evaluations carried out so far
	private long evaluations;

	// Number of individuals in this population
	private int numIndividuals;

	// Used for Decimation
	private int initNumIndividuals;
	private int numGenerationBeforeDecimation;
	private int fallPerGeneration;

	// Should Decimation be performed?
	private boolean performDecimation;

	// The best, worst, and average fitness
	private double bestFitness;
	private double worstFitness;
	private double avgFitness;

	private double avgDepth;
	private double avgSize;

	// The return type for all the programs in this population
	private int returnType;

	// What generation number are we currently processing.
	private int generationNumber;

	// The loggingFrequency stores how often to write out
	// generation files.
	private int loggingFrequency;

	private PrintStream logFile;

	protected GPConfig config; // The configuration of this Population

	public Population(int size, String logFileName, GPConfig conf) {
		numIndividuals = (size);
		performDecimation = (false);
		bestFitness = (0.0);
		worstFitness = (0.0);
		avgFitness = (0.0);
		avgDepth = (0.0);
		avgSize = (0.0);
		returnType = (-1);
		generationNumber = (0);
		loggingFrequency = (1);
		config = (conf);
		int i;
		pop = new ArrayList<GeneticProgram>(numIndividuals);

		for (i = 0; i < numIndividuals; i++) {
			pop.add(new GeneticProgram(config));
		}

		try {
			logFile = new PrintStream(new File(logFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Population(int size, int initSize, String logFileName, GPConfig conf) {
		numIndividuals = (initSize);
		initNumIndividuals = (size);
		numGenerationBeforeDecimation = (0);
		performDecimation = (true);
		bestFitness = (0.0);
		worstFitness = (0.0);
		avgFitness = (0.0);
		avgDepth = (0.0);
		avgSize = (0.0);
		returnType = (-1);
		generationNumber = (0);
		loggingFrequency = (1);
		config = (conf);
		int i;
		fallPerGeneration = (int) ((initSize - size) / (numGenerationBeforeDecimation + 1));
		pop = new ArrayList<GeneticProgram>(numIndividuals);

		for (i = 0; i < numIndividuals; i++) {
			pop.add(new GeneticProgram(config));

		}

		try {
			logFile = new PrintStream(new File(logFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<GeneticProgram> getPopulation() {
		return pop;
	}

	public void setPopulation(List<GeneticProgram> newPop, int size) {
		int i;

		if (size != numIndividuals) throw new RuntimeException("Population::setPopulation error incorrect size");

		// copy new generation over old generation
		for (i = 0; i < numIndividuals; i++) {
			pop.set(i, newPop.get(i));
		}
	}

	public GeneticProgram getIndividual(int individual) {
		GeneticProgram tmp;

		if (individual < numIndividuals)
			tmp = pop.get(individual);
		else throw new RuntimeException("Population::getIndividual Error invalid individual");

		if (tmp == null) throw new RuntimeException("Population::getIndividual Error individual is NULL");

		return tmp;
	}

	public void generateInitialPopulation() {
		config.programGenerator.generateInitialPopulation(pop, numIndividuals, returnType);

	}

	public boolean evolve(int numGenerations) {
		int i = 0;
		evaluations = 0;
		for (; i < numGenerations; i++) {
			assignFitness(); // Evaluate the programs and assign their fitness values
			evaluations += numIndividuals; // Update the number of evaluations performed
			sortPopulation(); // Sort the population based on fitness
			computeStatistics(); // Calculate some statistics for the population
			writeLog(); // Write some information to the log file

			/*
			 * Decimation The actual removal of individuals from the bottom of the population and the freeing of the
			 * memory is carried out in the numGenerationBeforeDecimation method.
			 */
			if (performDecimation) {
				/*
				 * If the numGenerationBeforeDecimation set to a value greater than 0 progressive decimation is
				 * performed
				 */
				if (i < numGenerationBeforeDecimation)
					setNumIndividuals(numIndividuals - fallPerGeneration);
				else if (i == numGenerationBeforeDecimation) {
					// Decimation is completed in this step.
					setNumIndividuals(initNumIndividuals);
					performDecimation = false;
				}
			}

			// If the solution has been found quit and return true to
			// indicate success
			if (config.fitnessObject.solutionFound(pop)) {
				logFile.println("Solution found!");
				return true;
			}

			config.configModifier.ModifyConfig();

			nextGeneration();
		}
		assignFitness(); // Evaluate the programs and assign their fitness values
		sortPopulation();
		return false;
	}

	public void nextGeneration() {
		int indiv1, indiv2;
		int i;
		List<GeneticProgram> nextPop;

		// Write the pop to a file if it's time
		if ((generationNumber % loggingFrequency) == 0) writeToFile();

		nextPop = new ArrayList<GeneticProgram>(numIndividuals);

		adjustFitness();

		// copy some individuals (elitism)
		for (i = 0; i < getNumForElitism(); i++) {
			nextPop.add(pop.get(i).copy());
		}

		// crossover some individuals
		for (i = 0; i < getNumForCrossover(); i += 2) {
			indiv1 = config.selectionOperator.select(pop, numIndividuals, config);
			indiv2 = config.selectionOperator.select(pop, numIndividuals, config);
			nextPop.add(pop.get(indiv1).copy());
			nextPop.add(pop.get(indiv2).copy());

			config.crossoverOperator.crossover(nextPop.get(nextPop.size() - 2), (nextPop.get(nextPop.size() - 1)), 100,
					config);
		}

		// mutate some individuals
		for (i = 0; i < getNumForMutation(); i++) {
			indiv1 = config.selectionOperator.select(pop, numIndividuals, config);
			nextPop.add(pop.get(indiv1).copy());
			config.mutationOperator.mutate(nextPop.get(nextPop.size() - 1), config);
		}
		for (GeneticProgram m : pop) {
			for (int j = 0; j < config.getNumParts(); j++) {
				m.deleteTree(j);
			}
		}
		pop = nextPop;

		generationNumber++;
	}

	/*******************************************************************************************************************
	 * adjustFitness() Adjusts the raw fitness values for use with the roullette wheel selection operator. Gives
	 * programs with low (better) raw fitnesses higher adjusted fitnesses.
	 ******************************************************************************************************************/

	public void adjustFitness() {
		int i;
		double totalFitness = 0.0;

		for (i = 0; i < numIndividuals; i++) {
			pop.get(i).setAdjFitness(1.0 / (1.0 + pop.get(i).getFitness()));
			totalFitness += pop.get(i).getAdjFitness();
		}

		for (i = 0; i < numIndividuals; i++) {
			pop.get(i).setAdjFitness(pop.get(i).getAdjFitness() / totalFitness);
		}
	}

	/*******************************************************************************************************************
	 * setInitNumIndividuals makes sure that the initial population size is greater than the actual size of the
	 * population, for performing Decimation.
	 ******************************************************************************************************************/

	public void setNumGenerationBeforeDecimation(int num) {
		if (performDecimation) {
			// Negative values are not allowed for numGenerationBeforeDecimation
			if (num < 0) {
				num = 0;
			}

			numGenerationBeforeDecimation = num;
			fallPerGeneration = (numIndividuals - initNumIndividuals) / (numGenerationBeforeDecimation + 1);
		}
	}

	public int getNumGenerationBeforeDecimation() {
		if (performDecimation)
			return numGenerationBeforeDecimation;
		else return -1;
	}

	/*******************************************************************************************************************
	 * setNumIndiviuals currently resizes the population. If the new size is smaller it truncates the old population
	 * (the last N programs are lost). If the new size is bigger then new population contains N copies of the last
	 * program in the population.
	 * 
	 * If you want different behaviour, make a subclass of this class and overide this method.
	 * 
	 * N is the difference between old and new sizes.
	 ******************************************************************************************************************/

	public void setNumIndividuals(int num) {
		int i;
		List<GeneticProgram> tmp = new ArrayList<GeneticProgram>(num);

		for (i = 0; i < num && i < numIndividuals; i++)
			tmp.add(pop.get(i));

		if (num > numIndividuals) {
			for (; i < num; i++) {
				tmp.add(tmp.get(numIndividuals - 1).copy());
			}
		}

		numIndividuals = num;

		pop = tmp;
	}

	public int getNumIndividuals() {
		return numIndividuals;
	}

	public void setGenerationNumber(int num) {
		generationNumber = num;
	}

	public int getGenerationNumber() {
		return generationNumber;
	}

	public void setReturnType(int type) {
		int i;

		returnType = type;

		for (i = 0; i < numIndividuals; i++) {
			pop.get(i).setReturnType(type);
		}
	}

	public int getReturnType() {
		return returnType;
	}

	public double getMutationRate() {
		return config.mutationRate();
	}

	public int getNumForMutation() {
		return (int) (numIndividuals * config.mutationRate());
	}

	public double getCrossoverRate() {
		return config.crossoverRate();
	}

	public int getNumForCrossover() {
		return (int) (numIndividuals * config.crossoverRate());
	}

	public double getElitismRate() {
		return config.elitismRate();
	}

	public int getNumForElitism() {
		return (int) (numIndividuals * config.elitismRate());
	}

	public void assignFitness() {
		config.fitnessObject.assignFitness(pop,config);
	}

	private void sortPopulation() {
		Collections.sort(pop, config.fitnessObject);
	}

	public GeneticProgram getBest() {
		int index = 0;

		for (int i = 1; i < numIndividuals; i++) {
			if (config.fitnessObject.compare(pop.get(i), pop.get(index)) > 1) {
				index = i;
			}
		}

		return pop.get(index);
	}

	public GeneticProgram getWorst() {
		int index = 0;

		for (int i = 1; i < numIndividuals; i++) {
			if (config.fitnessObject.compare(pop.get(i), pop.get(index))< 0) {
				index = i;
			}
		}

		return pop.get(index);
	}

	public void computeStatistics() {
		double totalFitness = 0.0;
		double totalDepth = 0.0;
		double totalSize = 0.0;

		bestFitness = getBest().getFitness();
		worstFitness = getWorst().getFitness();

		for (int i = 0; i < numIndividuals; i++) {
			totalFitness += pop.get(i).getFitness();
			// find the average depth and size of the program
			for (int j = 0; j < config.getNumParts(); j++) {
				totalDepth += pop.get(i).getDepth(j);
				totalSize += pop.get(i).getSize(j);
			}
		}

		avgFitness = totalFitness / numIndividuals;
		avgDepth = totalDepth / (numIndividuals * config.getNumParts());
		avgSize = totalSize / (numIndividuals * config.getNumParts());
	}

	public void writeToFile() {
		String filename = String.format("gen_%06d.gen", generationNumber);
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new File(filename));

			outputFile.print(this);

			outputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void readFromFile(String filename) {
		try {
			Scanner scan = new Scanner(new File(filename));

			String programString;
			int individual = 0;

			int iValue = 0;
			double dValue = 0.0;
			double mutationRate = 0.0;
			double crossoverRate = 0.0;
			double elitismRate = 0.0;
			String line;

			// First loop reads the header info in the pop file
			while (true) {
				// Get a line of input from the stream
				if (!scan.hasNextLine()) {
					break;
				}
				line = scan.nextLine();

				// If we read a hash at the start of the line, then
				// the header info is finished
				if (line.startsWith("#")) break;

				if (line.startsWith("numIndividuals")) {
					iValue = Integer.parseInt(line.substring("numIndividuals".length() + 1));
					System.err.println("Setting numIndividuals to supplied value " + iValue);
					this.setNumIndividuals(iValue);
				} else if (line.startsWith("depthLimit")) {
					iValue = Integer.parseInt(line.substring("depthLimit".length() + 1));
					System.err.println("Setting depthLimit to supplied value " + iValue);
					config.maxDepth(iValue);
				} else if (line.startsWith("minDepth")) {
					iValue = Integer.parseInt(line.substring("minDepth".length() + 1));
					System.err.println("Setting minDepth to supplied value " + iValue);
					config.minDepth(iValue);
				} else if (line.startsWith("returnType")) {
					iValue = Integer.parseInt(line.substring("returnType".length() + 1));
					System.err.println("Setting returnType to supplied value " + iValue);
					this.setReturnType(iValue);
				} else if (line.startsWith("mutationRate")) {
					dValue = Double.parseDouble(line.substring("mutationRate".length() + 1));
					System.err.println("Setting mutationRate to supplied value " + dValue);
					;
					mutationRate = dValue;
				} else if (line.startsWith("crossoverRate")) {
					dValue = Double.parseDouble(line.substring("crossoverRate".length() + 1));
					System.err.println("Setting crossoverRate to supplied value " + dValue);
					crossoverRate = dValue;
				} else if (line.startsWith("elitismRate")) {
					dValue = Double.parseDouble(line.substring("elitismRate".length() + 1));
					System.err.println("Setting elitismRate to supplied value " + dValue);
					elitismRate = dValue;
				} else {
					System.err.println("Ignoring line " + line);
				}
			}

			config.setRates(mutationRate, crossoverRate, elitismRate);

			while (true) {
				if (individual > this.numIndividuals) break;

				if (!scan.hasNext()) {
					break;
				}
				line = scan.nextLine();

				if (line.startsWith("#")) {
					individual++;
					continue;
				}

				if (line.startsWith("Individual")) {
					iValue = Integer.parseInt(line.substring("Individual".length() + 1));
					System.err.println("Reading individual " + iValue + " into slot " + individual);
				} else if (line.startsWith("Fitness")) {
					dValue = Double.parseDouble(line.substring("Fitness".length() + 1));
					System.err.println("Setting fitness to " + dValue);
					pop.get(individual).setFitness(dValue);
				} else if (line.startsWith("Program")) {
					programString = line.substring("Program".length() + 1);

					pop.get(individual).parseProgram(programString);
				} else {
					System.err.println("Ignoring line " + line);
				}
			}

			if (individual < getNumIndividuals() - 1) {
				System.err.println("**** Warning ****");
				System.err.println("Number of programs in population file is less than " + getNumIndividuals());
			}

			scan.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("File created at " + now());
		s.append('\n');
		s.append("numIndividuals " + numIndividuals);
		s.append('\n');
		s.append("depthLimit " + config.maxDepth());
		s.append('\n');
		s.append("minDepth " + config.minDepth());
		s.append('\n');
		s.append("returnType " + returnType);
		s.append('\n');
		s.append("mutationRate " + config.mutationRate());
		s.append('\n');
		s.append("crossoverRate " + config.crossoverRate());
		s.append('\n');
		s.append("elitismRate " + config.elitismRate());
		s.append('\n');
		s.append("Population at generation " + generationNumber);
		s.append('\n');

		for (int i = 0; i < numIndividuals; i++) {
			StringBuffer s1 = new StringBuffer();
			pop.get(i).print(s1);
			s.append("###################################\n");
			s.append("Individual ");
			s.append(i);
			s.append('\n');
			s.append("Fitness ");
			s.append(pop.get(i).getFitness());
			s.append('\n');
			// TODO I changed this
			for (int j = 0; j < config.getNumParts(); j++) {
				s.append("Depth ");
				s.append(pop.get(i).getDepth(j));
				s.append('\n');
				s.append("Size ");
				s.append(pop.get(i).getSize(j));
				s.append('\n');
			}
			s.append("Program " + s1);
			s.append('\n');
		}

		return s.toString();
	}

	public void setLogFrequency(int freq) {
		loggingFrequency = freq;
	}

	public int getLogFrequency() {
		return loggingFrequency;
	}

	public void writeLog() {

		logFile.println("*****************************************");
		logFile.println("Generation " + generationNumber);
		logFile.println("Time " + now());
		logFile.println("BestFitness " + bestFitness);
		logFile.println("WorstFitness " + worstFitness);
		logFile.println("AverageFitness " + avgFitness);
		logFile.println("AverageDepth " + avgDepth);
		logFile.println("AverageSize " + avgSize);
		logFile.println("Evaluation " + evaluations);
	}

	public String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

}

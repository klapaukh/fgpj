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
	private List<GeneticProgram> nextPop; // Space saver for next generation

	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	// Number of evaluations carried out so far
	private long evaluations;

	// The best, worst, and average fitness
	private double bestFitness;
	private double worstFitness;
	private double avgFitness;

	private double avgDepth;
	private double avgSize;

	// The return type for all the programs in this population
	private int returnType[];

	// What generation number are we currently processing.
	private int generationNumber;

	// The loggingFrequency stores how often to write out
	// generation files.
	private int loggingFrequency;

	private PrintStream logFile;

	protected GPConfig config; // The configuration of this Population

	/**
	 * Create a new population
	 * @param size
	 * @param logFileName
	 * @param conf
	 */
	public Population(int size, String logFileName, GPConfig conf) {
		bestFitness = (0.0);
		worstFitness = (0.0);
		avgFitness = (0.0);
		avgDepth = (0.0);
		avgSize = (0.0);
		returnType = new int[conf.getNumRoots()];
		generationNumber = (0);
		loggingFrequency = (1);
		config = (conf);
		pop = new ArrayList<GeneticProgram>(size);
		nextPop = new ArrayList<GeneticProgram>(size);

		for (int i = 0; i < size; i++) {
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

	public void generateInitialPopulation() {
		config.programGenerator.generateInitialPopulation(pop, returnType, config);

	}

	public boolean evolve(int numGenerations) {
		evaluations = 0;
		for (int i = 0; i < numGenerations; i++) {
			config.fitnessObject.assignFitness(pop, config); // Evaluate the programs and assign their fitness values
			evaluations += pop.size(); // Update the number of evaluations performed
			Collections.sort(pop, config.fitnessObject); // Sort the population based on fitness
			computeStatistics(); // Calculate some statistics for the population
			writeLog(); // Write some information to the log file

			// If the solution has been found quit and return true to
			// indicate success
			if (config.fitnessObject.solutionFound(pop)) {
				config.fitnessObject.finish();
				logFile.println("Solution found!");
				return true;
			}

			config.configModifier.ModifyConfig(config, this);

			nextGeneration();
		}
		config.fitnessObject.assignFitness(pop, config);
		Collections.sort(pop, config.fitnessObject);
		config.fitnessObject.finish(); // Finshed with the fitness assessing now

		return false;
	}

	public void nextGeneration() {
		int indiv1, indiv2;
		int i;

		// Write the pop to a file if it's time
		if ((generationNumber % loggingFrequency) == 0) writeToFile();

		// copy some individuals (elitism)
		for (i = 0; i < getNumForElitism(); i++) {
			nextPop.add(pop.get(i).copy(config));
		}

		// crossover some individuals
		for (i = 0; i < getNumForCrossover(); i += 2) {
			indiv1 = config.selectionOperator.select(pop, config);
			indiv2 = config.selectionOperator.select(pop, config);
			nextPop.add(pop.get(indiv1).copy(config));
			nextPop.add(pop.get(indiv2).copy(config));

			config.crossoverOperator.crossover(nextPop.get(nextPop.size() - 2), (nextPop.get(nextPop.size() - 1)), 100,
					config);
		}

		// mutate some individuals
		for (i = 0; i < getNumForMutation(); i++) {
			indiv1 = config.selectionOperator.select(pop, config);
			nextPop.add(pop.get(indiv1).copy(config));
			config.mutationOperator.mutate(nextPop.get(nextPop.size() - 1), config);
		}
		for (GeneticProgram m : pop) {
			for (int j = 0; j < config.getNumRoots(); j++) {
				m.deleteTree(j);
			}
		}

		List<GeneticProgram> t = pop;
		pop = nextPop;
		nextPop = t;
		nextPop.clear();

		generationNumber++;
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
		while (num > pop.size()) {
			pop.add(pop.get(0).copy(config));
		}
		while (pop.size() > num) {
			GeneticProgram prog = pop.remove(0);
			for (int i = 0; i < config.getNumRoots(); i++) {
				prog.deleteTree(i);
			}
		}
	}

	public int getNumIndividuals() {
		return pop.size();
	}

	public void setGenerationNumber(int num) {
		generationNumber = num;
	}

	public int getGenerationNumber() {
		return generationNumber;
	}

	public void setReturnType(int root, int type) {
		returnType[root] = type;
		for (int i = 0; i < pop.size(); i++) {
			pop.get(i).setReturnType(root, type);
		}
	}

	public void setReturnType(int type) {
		for (int i = 0; i < config.getNumRoots(); i++) {
			returnType[i] = type;
		}
		for (int i = 0; i < pop.size(); i++) {
			for (int j = 0; j < config.getNumRoots(); j++) {
				pop.get(i).setReturnType(j, type);
			}
		}
	}

	public int getReturnType(int root) {
		return returnType[root];
	}

	public int getNumForMutation() {
		return (int) (pop.size() * config.mutationRate());
	}

	public int getNumForCrossover() {
		return (int) (pop.size() * config.crossoverRate());
	}

	public int getNumForElitism() {
		return (int) (pop.size() * config.elitismRate());
	}

	public GeneticProgram getBest() {
		int index = 0;

		for (int i = 1; i < pop.size(); i++) {
			if (config.fitnessObject.compare(pop.get(i), pop.get(index)) > 0) {
				index = i;
			}
		}

		return pop.get(index);
	}

	public GeneticProgram getWorst() {
		int index = 0;

		for (int i = 1; i < pop.size(); i++) {
			if (config.fitnessObject.compare(pop.get(i), pop.get(index)) < 0) {
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

		for (int i = 0; i < pop.size(); i++) {
			totalFitness += pop.get(i).getFitness();
			// find the average depth and size of the program
			for (int j = 0; j < config.getNumRoots(); j++) {
				totalDepth += pop.get(i).getDepth(j);
				totalSize += pop.get(i).getSize(j);
			}
		}

		avgFitness = totalFitness / pop.size();
		avgDepth = totalDepth / (pop.size() * config.getNumRoots());
		avgSize = totalSize / (pop.size() * config.getNumRoots());
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
				if (individual > this.pop.size()) break;

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

					pop.get(individual).parseProgram(programString, config);
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
		s.append("numIndividuals " + pop.size());
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

		for (int i = 0; i < pop.size(); i++) {
			StringBuffer s1 = new StringBuffer();
			pop.get(i).print(s1);
			s.append("###################################\n");
			s.append("Individual ");
			s.append(i);
			s.append('\n');
			s.append("Fitness ");
			s.append(pop.get(i).getFitness());
			s.append('\n');
			for (int j = 0; j < config.getNumRoots(); j++) {
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

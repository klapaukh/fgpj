package nz.ac.vuw.ecs.fgpj.core;
/*
FGPJ Genetic Programming library
Copyright (C) 2011  Roman Klapaukh

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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

/**
 * This class controls the pool of programs. It also has the main evolve loop
 * 
 * @author Roman Klapaukh
 * 
 */
public class Population {

	private List<GeneticProgram> pop; // The population
	private List<GeneticProgram> nextPop; // Space saver for next generation

	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

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

	protected GPConfig config; // The configuration of this Population

	/**
	 * Create a new population
	 * 
	 * @param size
	 *            number of individual in
	 * @param conf
	 *            config used
	 */
	public Population(int size, GPConfig conf) {
		bestFitness = (0.0);
		worstFitness = (0.0);
		avgFitness = (0.0);
		avgDepth = (0.0);
		avgSize = (0.0);
		returnType = new int[conf.getNumRoots()];
		generationNumber = (0);
		config = (conf);
		pop = new ArrayList<GeneticProgram>(size);
		nextPop = new ArrayList<GeneticProgram>(size);

		for (int i = 0; i < size; i++) {
			pop.add(new GeneticProgram(config));
		}

	}

	/**
	 * Get the whole population
	 * 
	 * @return a clone of the underlying population list
	 */
	public List<GeneticProgram> getPopulation() {
		List<GeneticProgram> l = new ArrayList<GeneticProgram>(pop.size());
		for (GeneticProgram p : pop) {
			l.add(p.copy(config));
		}
		return l;
	}

	
	/**
	 * Get the underlying population be Careful!
	 * 
	 * @return the underlying population list
	 */
	public List<GeneticProgram> getUnderlyingPopulation() {
		return pop;
	}

	/**
	 * Generate a random initial population using the ProgramGenerator specified in the config
	 */
	public void generateInitialPopulation() {
		config.programGenerator.generateInitialPopulation(pop, returnType, config);

	}

	/**
	 * Run the GP algorithm using the settings in the config for up to numGenerations generations. It will terminate
	 * early if the fitness function says that an acceptable solution was found.
	 * 
	 * @param numGenerations
	 *            numGenerations number of generations to run the algorithm for
	 * @return if an acceptable solution was found
	 */
	public int evolve(int numGenerations) {
		evaluations = 0;
		config.fitnessObject.initFitness();
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
				config.log("Solution found!\n");
				return i;
			}

			config.configModifier.ModifyConfig(config, this);

			nextGeneration();
		}
		config.fitnessObject.assignFitness(pop, config);
		Collections.sort(pop, config.fitnessObject);
		config.fitnessObject.finish(); // Finished with the fitness assessing now

		return numGenerations;
	}

	/**
	 * Generate the next generation from the current one, using the settings in config
	 */
	public void nextGeneration() {
		int indiv1, indiv2;

		// Write the pop to a file if it's time
		if ((generationNumber % config.loggingFrequency()) == 0) writeToFile();

		int numCrossover = (int) (pop.size() * config.crossoverRate());
		int numMutation = (int) (pop.size() * config.mutationRate());

		//Do crossover
		for (int i = 0; i < numCrossover; i += 2) {
			indiv1 = config.selectionOperator.select(pop, config);
			indiv2 = config.selectionOperator.select(pop, config);
			
			GeneticProgram p1 = pop.get(indiv1).copy(config);
			GeneticProgram p2 = pop.get(indiv2).copy(config);

			config.crossoverOperator.crossover(p1, p2, 100,
					config);
			
			p1.setNumCrossovers(p1.numCrossovers() +1); 
			p1.setNumMutations(p1.numMutations());
			p1.setNumElitisms(p1.numElitisms());
			p1.resetLastChange();

			p2.setNumCrossovers(p2.numCrossovers() +1); 
			p2.setNumMutations(p2.numMutations());
			p2.setNumElitisms(p2.numElitisms());
			p2.resetLastChange();
			
			nextPop.add(p2);
			nextPop.add(p1);
		}

		//Do mutation
		for (int i = 0; i < numMutation; i++) {
			indiv1 = config.selectionOperator.select(pop, config);
			GeneticProgram p = pop.get(indiv1).copy(config);
			config.mutationOperator.mutate(p, config);
			
			p.setNumMutations(p.numMutations() +1);
			p.resetLastChange();
			
			nextPop.add(p);
		}

		//Fill rest with elitism
		int i = pop.size() - 1;
		while (nextPop.size() < pop.size()) {
			GeneticProgram p = pop.get(i--).copy(config);
			
			p.setNumElitisms(p.numElitisms()+1);
			p.incrementLastChange();
			
			nextPop.add(p);
		}

		for (GeneticProgram m : pop) {
			for (int j = 0; j < config.getNumRoots(); j++) {
				m.deleteTree(j);
			}
		}

		// Reuse the same lists
		List<GeneticProgram> t = pop;
		pop = nextPop;
		nextPop = t;
		nextPop.clear();

		generationNumber++;
	}

	/**
	 * This resizes the population. Removal is from the from, weakest first. If Adding the last entry (best fitness) is
	 * duplicated
	 * 
	 * @param num
	 *            new size of population
	 */
	public void setNumIndividuals(int num) {
		while (num > pop.size()) {
			pop.add(pop.get(pop.size() - 1).copy(config));
		}
		while (pop.size() > num) {
			GeneticProgram prog = pop.remove(0);
			for (int i = 0; i < config.getNumRoots(); i++) {
				prog.deleteTree(i);
			}
		}
	}

	/**
	 * The size of the population
	 * 
	 * @return size of population
	 */
	public int getNumIndividuals() {
		return pop.size();
	}

	/**
	 * Set the generation number. Not a good idea unless you have a good reason
	 * 
	 * @param num
	 *            new generation number
	 */
	public void setGenerationNumber(int num) {
		generationNumber = num;
	}

	/**
	 * Get the current generation number
	 * 
	 * @return current generation number
	 */
	public int getGenerationNumber() {
		return generationNumber;
	}

	/**
	 * Set the return type for a particular subtree
	 * @param root which subtree to set the return type of
	 * @param type the return type
	 */
	public void setReturnType(int root, int type) {
		returnType[root] = type;
		for (int i = 0; i < pop.size(); i++) {
			pop.get(i).setReturnType(root, type);
		}
	}

	/**
	 * Set all roots to this return type
	 * @param type return type
	 */
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

	/**
	 * Get the return type for the root
	 * @param root root in question
	 * @return return type
	 */
	public int getReturnType(int root) {
		return returnType[root];
	}

	
	/**
	 * Get the program with the best fitness
	 * @return Program with best fitness in population
	 */
	public GeneticProgram getBest() {
		int index = 0;

		for (int i = 1; i < pop.size(); i++) {
			if (config.fitnessObject.compare(pop.get(i), pop.get(index)) > 0) {
				index = i;
			}
		}

		return pop.get(index);
	}

	/**
	 * Get program with worse fitness in population
	 * @return program with worst fitness
	 */
	public GeneticProgram getWorst() {
		int index = 0;

		for (int i = 1; i < pop.size(); i++) {
			if (config.fitnessObject.compare(pop.get(i), pop.get(index)) < 0) {
				index = i;
			}
		}

		return pop.get(index);
	}

	/**
	 * Computes statistics about the current population
	 */
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

	/**
	 * Write the population to a file
	 */
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

	/**
	 * Read the population from a file
	 * @param filename file to read from
	 */
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
			StringBuilder s1 = new StringBuilder();
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

	/**
	 * Write progress to log file if it is declared
	 */
	public void writeLog() {

		config.log("*****************************************\n");
		config.log("Generation " + generationNumber + "\n");
		config.log("Time " + now() + "\n");
		config.log("BestFitness " + bestFitness + "\n");
		config.log("WorstFitness " + worstFitness + "\n");
		config.log("AverageFitness " + avgFitness + "\n");
		config.log("AverageDepth " + avgDepth + "\n");
		config.log("AverageSize " + avgSize + "\n");
		config.log("Evaluation " + evaluations + "\n");
	}

	/**
	 * Formatted date string of time
	 * @return Formatted current time string
	 */
	public String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

}

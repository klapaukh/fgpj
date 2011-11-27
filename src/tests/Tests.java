package tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import library.GPConfig;
import library.GeneticProgram;
import library.Population;

import org.junit.Test;

import sets.symbolicRegression.Add;
import sets.symbolicRegression.RandomInt;
import sets.symbolicRegression.ReturnDouble;
import sets.symbolicRegression.SymbolicFitness;
import sets.symbolicRegression.Times;
import sets.symbolicRegression.X;

public class Tests {

	
	/**
	 * Current known problems:
	 * Mutation and crossover are probably keeping the tree full
	 * Grow program generation is actualy very full like, and can produce programs of invalid size
	 */
	
	
	@Test
	public void TestString1(){
		GPConfig conf = new GPConfig(1,2,8,0.50,0.4,0.1);
		conf.addTerminal(new X());
		conf.addTerminal(new RandomInt(conf));
		conf.addFunction(new Times());
		conf.addFunction(new Add());
		conf.fitnessObject = new SymbolicFitness();
		
		int size = 3;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		
		for(GeneticProgram pr: pop){
			StringBuilder sso = new StringBuilder();
			StringBuilder ssc = new StringBuilder();
			pr.print(sso);
			pr.parseProgram(sso.toString(), conf);
			pr.print(ssc);
			assertTrue(sso.toString().equals(ssc.toString()));
		}
	}
	
	@Test
	public void TestString2(){
		GPConfig conf = new GPConfig(1,2,8,0.50,0.4,0.1);
		conf.addTerminal(new X());
		conf.addTerminal(new RandomInt(conf));
		conf.addFunction(new Times());
		conf.addFunction(new Add());
		conf.fitnessObject = new SymbolicFitness();
		
		int size = 3;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		
		for(GeneticProgram pr: pop){
			StringBuilder ssc = new StringBuilder();
			String myString = "1 Program0  ( * X X )  |";
			pr.parseProgram(myString, conf);
			pr.print(ssc);
			assertTrue("\"" + ssc.toString().trim() + "\"" + "\"" + myString + "\"", ssc.toString().trim().equals(myString));
			assertTrue(pr.getDepth(0) == 2);
		}
	}
	
	
	@Test
	public void TestSize(){
		GPConfig conf = new GPConfig(1,2,8,0.50,0.4,0.1);
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();
		
		int size = 100;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		assertTrue(pop.size() == size);
		p.evolve(1);
		pop=p.getUnderlyingPopulation();
		assertTrue(pop.size() == size);
	}
	
	@Test
	public void TestDepthGeneration(){
		GPConfig conf = new GPConfig(1,2,8,0.50,0.4,0.1);
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();
		
		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		
		for(GeneticProgram pr : pop){
			assertTrue("depth: " + pr.getDepth(0),pr.getDepth(0) >= conf.minDepth() && pr.getDepth(0) <= conf.maxDepth());
		}
	}
	
	@Test
	public void TestDepthMutation(){
		GPConfig conf = new GPConfig(1,2,8,0.50,0.4,0.1);
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();
		
		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		
		for(GeneticProgram pr : pop){
			conf.mutationOperator.mutate(pr, conf);
			assertTrue("depth: " + pr.getDepth(0), pr.getDepth(0) <= conf.maxDepth());
		}
	}
	
	
	@Test
	public void TestDepthCrossover(){
		GPConfig conf = new GPConfig(1,2,8,0.50,0.4,0.1);
		conf.addTerminal(new X());
		conf.addFunction(new Times());
		conf.fitnessObject = new SymbolicFitness();
		
		int size = 1000;
		Population p = new Population(size, conf);
		p.setReturnType(ReturnDouble.TYPENUM);
		p.generateInitialPopulation();
		List<GeneticProgram> pop = p.getUnderlyingPopulation();
		
		for(GeneticProgram pr : pop){
			conf.crossoverOperator.crossover(pr,pr, 100, conf);
			assertTrue("depth: " + pr.getDepth(0), pr.getDepth(0) <= conf.maxDepth());
		}
	}
}

package library;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class ParallelFitness<T extends Fitness> extends Fitness {
	public final T fitness;
	private int gen;
	private final static int numThreads = 8;
	private CyclicBarrier eval, main;
	private Worker<T>[] workers;


	public ParallelFitness(GPConfig conf, T fitness) {
		super(conf);
		this.fitness = fitness;
		gen = 0;
		workers = new Worker[numThreads];
	}

	public void initFitness() {
		fitness.initFitness();
		eval = new CyclicBarrier(numThreads + 1);
		main = new CyclicBarrier(numThreads + 1);

		// start threads
		for (int i = 0; i < numThreads; i++) {
			workers[i] = new Worker<T>(eval, main,fitness);
			workers[i].start();
		}
	}

	public void assignFitness(List<GeneticProgram> pop, int popSize) {
		// set all things
		int numJobs = popSize / numThreads;
		int excessJobs = popSize % numThreads;

		int min = 0, max;
		for (int i = 0; i < numThreads; i++) {
			max = min + numJobs;
			if (i < excessJobs) {
				max += 1;
			}
			//System.out.println("Worker " + i + " min: " + min + " max: " + max);
			workers[i].setParams(pop, min, max);
			min = max;
		}

		try {
			main.await(); // wake up all threads
			eval.await(); // go to sleep yourself
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		gen++;
	}

	// This has been fixed
	// There is no possible solution worthy of an early end point.
	// May need to be reinabled, but in principle, this is human guided not controlled
	// So it probably shouldn't terminate eary
	public boolean solutionFound(List<GeneticProgram> pop, int popSize) {
		return fitness.solutionFound(pop, popSize);
	}

	public boolean isBetter(GeneticProgram gp1, GeneticProgram gp2) {
		return fitness.isBetter(gp1, gp2);
	}

	public boolean isWorse(GeneticProgram gp1, GeneticProgram gp2) {
		return fitness.isWorse(gp1, gp2);
	}

	public boolean isEqual(GeneticProgram gp1, GeneticProgram gp2) {
		return fitness.isEqual(gp1, gp2);
	}

	public double best() {
		return fitness.best();
	}

	public double worst() {
		return fitness.worst();
	}

	// I realise, I am intentionally making square images.
	public void finish() {
		for (int i = 0; i < workers.length; i++) {
			workers[i].done();
		}
		try {
			main.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}

	}

	private static class Worker<F extends Fitness> extends Thread {

		private List<GeneticProgram> l;
		private int min, max;
		private boolean done;
		private CyclicBarrier eval, main;
		private F fitness;

		public Worker(CyclicBarrier eval, CyclicBarrier main, F fit) {
			this.eval = eval;
			this.main = main;
			this.fitness =fit;

		}

		public void setParams(List<GeneticProgram> l, int min, int max) {
			this.l = l;
			this.min = min;
			this.max = max;
			done = false;
		}

		public void done() {
			done = true;
		}

		public void run() {
			try {
				main.await();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (!done) {
				fitness.assignFitness(l.subList(min, max), max - min);

				try {
					eval.await();
					main.await();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

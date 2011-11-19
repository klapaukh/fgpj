package sets.lines;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import library.Fitness;
import library.GPConfig;
import library.GeneticProgram;

public class DistributedImageFitness extends Fitness {
	private ImageFitness imfit;
	private int gen;
	private final static int numThreads = 8;
	private CyclicBarrier eval, main;
	private Worker[] workers;

	// LindaServer* linda;

	public DistributedImageFitness(GPConfig conf) {
		super(conf);
		// linda = new LindaServer();
		imfit = new ImageFitness(conf);
		gen = 0;
		workers = new Worker[numThreads];
	}

	public void initFitness() {
		imfit.initFitness();
		eval = new CyclicBarrier(numThreads + 1);
		main = new CyclicBarrier(numThreads + 1);

		// start threads
		for (int i = 0; i < numThreads; i++) {
			workers[i] = new Worker(eval, main);
			workers[i].start();
		}
	}

	/*
	 * public void assignFitness(List<GeneticProgram> pop, int popSize) {
	 * 
	 * linda.setJobs(pop, popSize); //imfit->assignFitness(pop,popSize); //cout <<"generation finished :)" <<endl;
	 * if(gen %10==0){ int best = 0; double bestFit = pop[0]->getFitness(); for(int i=1;i<popSize;i++){ if(bestFit >
	 * pop[i]->getFitness()){ bestFit = pop[i]->getFitness(); best = i; } } char buff[37];
	 * sprintf(buff,"/local/scratch/roma/out%10d.pnm",gen); imfit->outputResults(pop[best], buff); //cout <<"Finished
	 * generation " <<gen <<endl; }
	 * 
	 * gen++; }
	 */

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
		/*if (gen % 100 == 0) {
			int best = 0;
			double bestFit = pop.get(0).getFitness();
			for (int i = 1; i < popSize; i++) {
				if (bestFit > pop.get(i).getFitness()) {
					bestFit = pop.get(i).getFitness();
					best = i;
				}
			}

			imfit.outputResults(pop.get(best), String.format("out%10d.pnm", gen));
			// cout <<"Finished generation " <<gen <<endl;
		}*/

		gen++;
	}

	// Needs to be fixed
	public void outputResults(GeneticProgram program, String filename) {
		imfit.outputResults(program, filename);
	}

	// This has been fixed
	// There is no possible solution worthy of an early end point.
	// May need to be reinabled, but in principle, this is human guided not controlled
	// So it probably shouldn't terminate eary
	public boolean solutionFound(List<GeneticProgram> pop, int popSize) {
		return imfit.solutionFound(pop, popSize);
	}

	public boolean isBetter(GeneticProgram gp1, GeneticProgram gp2) {
		return imfit.isBetter(gp1, gp2);
	}

	public boolean isWorse(GeneticProgram gp1, GeneticProgram gp2) {
		return imfit.isWorse(gp1, gp2);
	}

	public boolean isEqual(GeneticProgram gp1, GeneticProgram gp2) {
		return imfit.isEqual(gp1, gp2);
	}

	public double best() {
		return imfit.best();
	}

	public double worst() {
		return imfit.worst();
	}

	// I realise, I am intentionally making square images.
	public void getResult(GeneticProgram gp, int size) {
		imfit.getResult(gp, size);

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

	private class Worker extends Thread {

		private List<GeneticProgram> l;
		private int min, max;
		private boolean done;
		private CyclicBarrier eval, main;

		public Worker(CyclicBarrier eval, CyclicBarrier main) {
			this.eval = eval;
			this.main = main;

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
				imfit.assignFitness(l.subList(min, max), max - min);

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

package library;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

public class ParallelFitness<T extends Fitness> extends Fitness {
	public final T fitness;
	private int gen;
	private final int numThreads;
	private Worker<T>[] workers;
	private Queue<Job> jobs;
	private CyclicBarrier end, start;
	private int stepSize;

	public ParallelFitness(GPConfig conf, T fitness) {
		this(conf, fitness, 4, 10);
	}

	@SuppressWarnings("unchecked")
	public ParallelFitness(GPConfig conf, T fitness, int numThreads, int stepSize) {
		super(conf);
		this.stepSize = 10;
		this.jobs = new ConcurrentLinkedQueue<Job>();
		this.numThreads = numThreads;
		this.fitness = fitness;
		this.start = new CyclicBarrier(numThreads + 1);
		this.end = new CyclicBarrier(numThreads + 1);
		gen = 0;
		workers = new Worker[numThreads];
	}

	public void initFitness() {
		fitness.initFitness();

		// start threads
		for (int i = 0; i < numThreads; i++) {
			workers[i] = new Worker<T>(start, end, jobs, fitness);
			workers[i].start();
		}
	}

	public void assignFitness(List<GeneticProgram> pop) {
		int min = 0, max = stepSize;
		while (max < pop.size()) {
			jobs.offer(new Job(min, max, pop));
			max = Math.min(max + stepSize, pop.size());
			min += stepSize;
		}

		try {
			start.await(); // wake up all threads
			end.await(); // go to sleep yourself
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
	public boolean solutionFound(List<GeneticProgram> pop) {
		return fitness.solutionFound(pop);
	}

	public double best() {
		return fitness.best();
	}

	public double worst() {
		return fitness.worst();
	}

	public void finish() {
		for (int i = 0; i < workers.length; i++) {
			jobs.offer(new Job(-1, -1, null));
		}
		try {
			start.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	private static class Worker<F extends Fitness> extends Thread {

		private Queue<Job> jobs;
		private F fitness;
		private CyclicBarrier start, end;

		public Worker(CyclicBarrier start, CyclicBarrier end, Queue<Job> jobs, F fit) {
			this.jobs = jobs;
			this.fitness = fit;
			this.start = start;
			this.end = end;

		}

		public void run() {

			while (true) {
				try {
					start.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				while (true) {
					Job j;
					j = jobs.poll();
					if (j == null) {
						break;
					}
					if (j.min == -1) {
						return;
					}
					fitness.assignFitness(j.pop.subList(j.min, j.max));
				}
				try {
					end.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class Job {
		public final int min;
		public final int max;
		public final List<GeneticProgram> pop;

		public Job(int mn, int mx, List<GeneticProgram> list) {
			this.min = mn;
			this.max = mx;
			this.pop = list;
		}
	}

	@Override
	public int compare(GeneticProgram p0, GeneticProgram p1) {
		return fitness.compare(p0, p1);
	}
}

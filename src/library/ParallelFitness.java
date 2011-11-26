package library;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

/**
 * This parallelises Fitness computations on the CPU using multiple threads. It generic, so can use any Fitness function
 * that is defined by a user. The fitness object must allow for multiple calls to assign fitness at the same time
 * 
 * @author Roma
 * 
 * @param <T>
 */
public class ParallelFitness<T extends Fitness> implements Fitness {
	public final T fitness;
	private int gen;
	private final int numThreads;
	private Worker<T>[] workers;
	private Queue<Job> jobs;
	private CyclicBarrier end, start;
	private int stepSize;

	/**
	 * Create a new Parallel fitness using the specified fitness function. It will use 4 threads, and group the jobs up
	 * into blocks of size 10
	 * 
	 * @param fitness
	 *            the fitness function to use
	 */
	public ParallelFitness(T fitness) {
		this(fitness, 4, 10);
	}

	/**
	 * Create a new parallel fitness
	 * 
	 * @param fitness
	 *            The fitness function to use
	 * @param numThreads
	 *            The number of CPU threads to use
	 * @param stepSize
	 *            Block size of jobs handed to each thread
	 */
	@SuppressWarnings("unchecked")
	public ParallelFitness(T fitness, int numThreads, int stepSize) {
		this.stepSize = 10;
		this.jobs = new ConcurrentLinkedQueue<Job>();
		this.numThreads = numThreads;
		this.fitness = fitness;
		this.start = new CyclicBarrier(numThreads + 1);
		this.end = new CyclicBarrier(numThreads + 1);
		gen = 0;
		workers = new Worker[numThreads];
	}

	/**
	 * Initialises the underlying fitness function and starts up all the workers
	 */
	public void initFitness() {
		fitness.initFitness();

		// start threads
		for (int i = 0; i < numThreads; i++) {
			workers[i] = new Worker<T>(start, end, jobs, fitness);
			workers[i].start();
		}
	}

	public void assignFitness(List<GeneticProgram> pop, GPConfig conf) {
		int min = 0, max = stepSize;
		while (max < pop.size()) {
			jobs.offer(new Job(min, max, pop, conf));
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

	public boolean solutionFound(List<GeneticProgram> pop) {
		return fitness.solutionFound(pop);
	}

	public double best() {
		return fitness.best();
	}

	public double worst() {
		return fitness.worst();
	}

	@Override
	public int compare(GeneticProgram p0, GeneticProgram p1) {
		return fitness.compare(p0, p1);
	}

	public void finish() {
		fitness.finish();
		for (int i = 0; i < workers.length; i++) {
			jobs.offer(new Job(-1, -1, null, null));
		}
		try {
			start.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Worker class that does actual jobs.
	 * A job with a start of -1 is considered a terminate job
	 * 
	 * @author Roma
	 *
	 * @param <F> The fitness function used for evaluation
	 */
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
					fitness.assignFitness(j.pop.subList(j.min, j.max), j.conf);
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

	/**
	 * Job class to designate how much work a thread is too do.
	 * 
	 * @author Roma
	 *
	 */
	private static class Job {
		public final int min;
		public final int max;
		public final List<GeneticProgram> pop;
		public final GPConfig conf;

		public Job(int mn, int mx, List<GeneticProgram> list, GPConfig conf) {
			this.min = mn;
			this.max = mx;
			this.pop = list;
			this.conf = conf;
		}
	}

}

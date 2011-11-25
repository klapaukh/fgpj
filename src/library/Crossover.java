package library;

public class Crossover {
	public void crossover(GeneticProgram gp1, GeneticProgram gp2,
			int numRetries, GPConfig config) {
		// point1 and point2 are the points in the trees of gp1 and gp2
		// where we will perform crossover
		Node point1 = null, point2 = null;

		// parent1 and parent2 are the parent nodes in the trees of point1
		// and point2, they will be NULL if point1 or point2 are the root of
		// the tree
		Function parent1 = null, parent2 = null;

		boolean valid = false;

		int pos1 = -1, pos2 = -1;

		int depth1, depth2;
		int count = 0;

		int par1 = (int) Math.abs(config.randomNumGenerator.nextLong() % config
				.getNumParts());
		int par2 = (int) Math.abs(config.randomNumGenerator.nextLong() % config
				.getNumParts());
		// The following loop will continually try to find two valid crossover
		// points by selecting a node from each of the trees. It will try up
		// to {numRetries} times before giving up which will leave the
		// trees unchanged.
		while (!valid) {
			count++;
			point1 = point2 = null;

			if (count >= numRetries)
				return;

			// Get a random point in the tree of gp1
			point1 = gp1.getRandomNode(par1);

			if (point1 == null) // Can't find valid node
				continue;

			parent1 = (Function) point1.getParent();

			// Get a random point in the tree which has the same return
			// type as point1
			point2 = gp2.getRandomNode(point1.getReturnType(), par2);

			// If there is no such node with the same return type then stop
			// and try again.
			if (point2 == null) // Can't find valid node
				continue;

			parent2 = (Function) point2.getParent();

			depth1 = point1.getDepth() - 1;
			depth2 = point2.getDepth() - 1;

			// Here we check that the crossover will produce trees which
			// do not violate the maximum depth
			if (((depth1 + point2.computeDepth(0)) <= config.maxDepth)
					&& ((depth2 + point1.computeDepth(0)) <= config.maxDepth)) {
				valid = true;
			} else {
				gp1.computeSizeAndDepth();
				gp2.computeSizeAndDepth();
			}
		}

		// If we get to this point we have a valid pair of nodes to perform
		// crossover with

		// Here we find which child position point1 is in it's
		// parent node.
		if (parent1 != null) {
			for (pos1 = 0; pos1 < parent1.getMaxArgs(); pos1++) {
				if (point1 == parent1.getArgN(pos1))
					break;
			}
		}

		// Here we find which child position point2 is in it's
		// parent node.
		if (parent2 != null) {
			for (pos2 = 0; pos2 < parent2.getMaxArgs(); pos2++) {
				if (point2 == parent2.getArgN(pos2))
					break;
			}
		}

		if (pos1 >= 0) {
			// If point1 was not at the root of the tree
			// replace point1 with point2
			// update point2s parent node
			parent1.setArgN(pos1, point2);
			point2.setParent(parent1);
		} else {
			// If point1 was at the root then make point2
			// the new root of gp1
			// TODO fix
			// int place =
			// config->randomNumGenerator->randNum()%config->numParts;
			gp1.setRoot(point2, par1);
			point2.setParent(null);
		}

		if (pos2 >= 0) {
			// If point1 was not at the root of the tree
			// replace point1 with point2
			// update point2s parent node
			parent2.setArgN(pos2, point1);
			point1.setParent(parent2);
		} else {
			// If point1 was at the root then make point2
			// the new root of gp1
			// TODO fix
			// int place =
			// config->randomNumGenerator->randNum()%config->numParts;
			gp2.setRoot(point1, par2);
			point1.setParent(null);
		}

		// Force the updated programs to update their size and depth
		// information
		gp1.computeSizeAndDepth();
		gp2.computeSizeAndDepth();
	}
}

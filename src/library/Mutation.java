package library;

public class Mutation {
	public void mutate(GeneticProgram gp, GPConfig config)
	{
		Node tmp=null, newTree=null;
		Function parent=null;

		//Get a random node and the parent of that node
		//need to get parent index here as well
		int p = (int) Math.abs(config.randomNumGenerator.randNum() % config
				.getNumParts());
		tmp = gp.getRandomNode(p);
		if(tmp == null)
			return;

		parent = (Function)(tmp.getParent());

		//Generate a new tree to replace the old one
		newTree = config.programGenerator.createGrowProgram(tmp.getDepth()-1,
				config.maxDepth-1,
				tmp.getReturnType());

		//If parent is not NULL then newTree is not at the root of
		//the tree
		if (parent != null)
		{
			int i;
			for (i=0; i<parent.getMaxArgs(); i++)
			{
				if (tmp == parent.getArgN(i))
					break;
			}

			parent.setArgN(i, newTree);
			newTree.setParent(parent);
		}
		else
		{
			//TODO make this kinda clever?
			//Need to use parent index here
			//int place = config->randomNumGenerator->randNum()%config->numParts;
			gp.setRoot(newTree,p);
			newTree.setParent(null);
		}

		//Delete the old tree
		gp.deleteTree(tmp);
		//Recompute the size and depth
		gp.computeSizeAndDepth();
	}

}

package library;

import java.util.List;

public class Selection {
	public int select(List<GeneticProgram> pop, int popSize, GPConfig config)
	{
	   int i;
	   double totalFitness=0;
	   double randValue;
	   double cumulFitness;

	   for(i=0; i<popSize; i++)
	   {
	      totalFitness += pop.get(i).getFitness();
	   } 

	   double tmpRand = config.randomNumGenerator.randReal();
	   randValue = totalFitness * tmpRand; 

	   cumulFitness = totalFitness;
	   for(i=0; i<popSize; i++)
	   {
	      cumulFitness -= pop.get(i).getFitness();

	      if(cumulFitness <= randValue) 
	         break;   
	   }

	   if (i >= popSize)
	   {
	      i = popSize-1;
	   }

	   return i;
	}

}

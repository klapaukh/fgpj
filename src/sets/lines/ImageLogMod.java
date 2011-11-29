package sets.lines;

import java.util.List;

import library.ConfigModifier;
import library.GPConfig;
import library.GeneticProgram;
import library.ParallelFitness;
import library.Population;

public class ImageLogMod implements ConfigModifier{

	private int freq;
	
	public ImageLogMod(int freq){
		this.freq = freq;
	}
	@Override
	public void ModifyConfig(GPConfig g, Population pop) {
		if(pop.getGenerationNumber() % freq == 0){
			List<GeneticProgram> progs= pop.getUnderlyingPopulation();
			((ParallelFitness<ImageFitness>)(g.fitnessObject)).fitness.getResult(progs.get(progs.size()-1), ImageFitness.SIZE,String.format("/local/tmp/roma/sample_%d.pnm", pop.getGenerationNumber()));
		}
		
	}

}

package nz.ac.vuw.ecs.fgpj.examples.symbolicRegression.image;
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
import java.util.List;

import nz.ac.vuw.ecs.fgpj.core.ConfigModifier;
import nz.ac.vuw.ecs.fgpj.core.GPConfig;
import nz.ac.vuw.ecs.fgpj.core.GeneticProgram;
import nz.ac.vuw.ecs.fgpj.core.ParallelFitness;
import nz.ac.vuw.ecs.fgpj.core.Population;


/**
 * Records the best image every fixed number of generations and saves it to a given directory
 * @author roma
 *
 */
public class ImageLoggerModifier implements ConfigModifier{

	/**
	 * Number of generations to save after
	 */
	private int freq;
	/**
	 * Directory to save to
	 */
	private final String dir;
	
	/**
	 * Make an new ImageLoggerModifier
	 * @param freq How many generations between images
	 * @param dir Directory to save to
	 */
	public ImageLoggerModifier(int freq, String dir){
		this.freq = freq;
		this.dir = dir;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void ModifyConfig(GPConfig g, Population pop) {
		//Check if it is the right generation
		if(pop.getGenerationNumber() % freq == 0){
			//save the image
			List<GeneticProgram> progs= pop.getUnderlyingPopulation();
			((ParallelFitness<MathImageFitness>)(g.fitnessObject)).fitness.getResult(progs.get(progs.size()-1), 100, 100, dir + "/sample_"+pop.getGenerationNumber() + ".pnm",g);
		}
		
	}

}

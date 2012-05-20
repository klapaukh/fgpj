package nz.ac.vuw.ecs.fgpj.examples.lines;

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
 * This is a ConfigModifier that lets you save the best intermediate images
 * every fixed number of generations. It assumes that you are using the
 * ParallelFitness function
 * 
 * @author roma
 * 
 */
public class ImageLogMod implements ConfigModifier {

	/**
	 * The frequency with which to save images
	 */
	private int freq;
	/**
	 * Where to save the images
	 */
	private final String dir;

	/**
	 * Create a new ImageLogMod. It will save the best image in the population
	 * every freq generations to the dir folder
	 * 
	 * @param freq
	 *            How often to save the image (in generations)
	 * @param dir
	 *            Directory to save images to
	 */
	public ImageLogMod(int freq, String dir) {
		this.freq = freq;
		this.dir = dir;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ModifyConfig(GPConfig g, Population pop) {
		// Check if it a generation in which to save
		if (pop.getGenerationNumber() % freq == 0) {

			// Get the population of programs
			List<GeneticProgram> progs = pop.getUnderlyingPopulation();

			// Get the ImageFitness out of the ParallelFitness and use that to
			// write the best image (which is the last one in the population) to
			// a file in the given directory
			((ParallelFitness<ImageFitness>) (g.fitnessObject)).fitness
					.getResult(progs.get(progs.size() - 1), dir + "/sample_"
							+ pop.getGenerationNumber() + ".pnm");
		}

	}

}

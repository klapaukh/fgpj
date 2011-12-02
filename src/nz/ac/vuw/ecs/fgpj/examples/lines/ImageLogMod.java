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


public class ImageLogMod implements ConfigModifier{

	private int freq;
	private final String dir;
	
	public ImageLogMod(int freq, String dir){
		this.freq = freq;
		this.dir = dir;
	}
	@Override
	public void ModifyConfig(GPConfig g, Population pop) {
		if(pop.getGenerationNumber() % freq == 0){
			List<GeneticProgram> progs= pop.getUnderlyingPopulation();
			((ParallelFitness<ImageFitness>)(g.fitnessObject)).fitness.getResult(progs.get(progs.size()-1),  dir + "/sample_"+pop.getGenerationNumber() +".pnm");
		}
		
	}

}

package nz.ac.vuw.ecs.fgpj.core;
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
/**
 * An example config modifier that impelements decimation as in the standard rmitgp library Decimation is the removal of
 * individuals from the bottom of the population The population is gradually decreased from its initial value ot the
 * final number as it reachs the needed generation. Not properly gradual as int division error accumulates until the
 * last gen when it causes a jump
 * 
 * @author Roman Klapaukh
 * 
 */
public class DecimationModifier implements ConfigModifier {

	private final int finalNumIndividuals;
	private final int numGenerationBeforeDecimation;
	private final int fallPerGeneration;

	public DecimationModifier(int popSize, int finalNumIndividuals, int numGenBeforeDec) {
		this.finalNumIndividuals = finalNumIndividuals;
		numGenerationBeforeDecimation = numGenBeforeDec;
		fallPerGeneration = (popSize - finalNumIndividuals) / (numGenerationBeforeDecimation + 1);
	}

	@Override
	public void ModifyConfig(GPConfig g, Population pop) {
		/*
		 * Decimation is the removal of individuals from the bottom of the population
		 */
		/*
		 * If the numGenerationBeforeDecimation set to a value greater than 0 progressive decimation is performed
		 */
		if (pop.getGenerationNumber() < numGenerationBeforeDecimation) {
			pop.setNumIndividuals(pop.getNumIndividuals() - fallPerGeneration);
		} else if (pop.getGenerationNumber() == numGenerationBeforeDecimation) {
			// Decimation is completed in this step.
			pop.setNumIndividuals(finalNumIndividuals);
		}
	}

}

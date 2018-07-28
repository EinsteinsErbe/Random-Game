package ch.major94.random_game.evolution;

import java.util.ArrayList;

public class MappingChromosome extends Chromosome<String> {
	
	public MappingChromosome() {
		super();
		
		type = ChromosomeType.MAPPING;
		STDDEV = 0;
		MEAN = N_FIELDS;
	}

	@Override
	public MappingChromosome clone() {
		MappingChromosome c = new MappingChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}
	
	@Override
	public ArrayList<String> build() {
		ArrayList<String> s = new ArrayList<String>();
		s.add("  "+type.getName());
		for(int i=0; i<N_FIELDS; i++) {
			s.add("    "+i+genes.get(i));
		}
		s.add("");
		return s;
	}

	@Override
	protected String newGene() {
		return " > " + getRandomSprite() + " " + optional(getRandomSprite(), 0.3, "");
	}
}

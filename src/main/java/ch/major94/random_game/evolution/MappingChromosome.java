package ch.major94.random_game.evolution;

public class MappingChromosome extends Chromosome<MappingChromosome, String> {
	
	public MappingChromosome() {
		super();
		
		type = ChromosomeType.MAPPING;
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newInstance() {
		// TODO Auto-generated method stub
		genes.add("10");
		genes.add("6");
		genes.add("3");
		genes.add("1");
	}

	@Override
	public MappingChromosome clone() {
		MappingChromosome c = new MappingChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}
}

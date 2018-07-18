package ch.major94.random_game.evolution;

public class MappingChromosome extends Chromosome<String> {
	
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
		genes.clear();
		// TODO Auto-generated method stub
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
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
	protected String newGene() {					//#################################TODO evtl fixed list (array of 10) for each mapping entry
		return getRandomField() + " > " + getRandomSprite() + " " + optional(getRandomSprite(), 0.3, "");
	}
}

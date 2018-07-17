package ch.major94.random_game.evolution;

public class LevelChromosome extends Chromosome<LevelChromosome, String> {
	
	public LevelChromosome() {
		super();
		
		type = ChromosomeType.LEVEL;
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newInstance() {
		// TODO Auto-generated method stub
		genes.add("i");
	}

	@Override
	public LevelChromosome clone() {
		LevelChromosome c = new LevelChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}
}

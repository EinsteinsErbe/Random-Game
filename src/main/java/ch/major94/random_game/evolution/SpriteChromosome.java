package ch.major94.random_game.evolution;

public class SpriteChromosome extends Chromosome<SpriteChromosome, String>{
	
	public SpriteChromosome() {
		super();
		
		type = ChromosomeType.SPRITE;
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
	public SpriteChromosome clone() {
		SpriteChromosome c = new SpriteChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}
}

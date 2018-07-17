package ch.major94.random_game.evolution;

public class InteractionChromosome extends Chromosome<InteractionChromosome, String> {
	
	public InteractionChromosome() {
		super();
		
		type = ChromosomeType.INTERACTION;
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
	public InteractionChromosome clone() {
		InteractionChromosome c = new InteractionChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}
}

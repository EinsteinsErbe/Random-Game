package ch.major94.random_game.evolution;

public class TerminationChromosome extends Chromosome<TerminationChromosome, String> {
	
	public TerminationChromosome() {
		super();
		
		type = ChromosomeType.TERMINATION;
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
	public TerminationChromosome clone() {
		TerminationChromosome c = new TerminationChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}
}

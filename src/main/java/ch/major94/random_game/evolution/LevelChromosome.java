package ch.major94.random_game.evolution;

import java.util.Arrays;

public class LevelChromosome extends Chromosome<String> {
	
	private final static int SIZE = 5;
	
	public LevelChromosome() {
		super();
		
		type = ChromosomeType.LEVEL;
	}

//	@Override
//	public void mutate() {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void newInstance() {
		genes.clear();
		// TODO Auto-generated method stub
		for(int i=0; i<SIZE; i++) {
			genes.add(newGene());
		}
	}

	@Override
	public LevelChromosome clone() {
		LevelChromosome c = new LevelChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}

	public String[] buildLevel() {
		String[] level = new String[SIZE];
		for(int i=0; i<SIZE; i++) {
			level[i] = getGene(i);
		}
		//Arrays.parallelSetAll(level, g -> getRandomGene()); 		//TODO nicht zufällig verteilen?
		return level;
	}

	@Override
	protected String newGene() {
		char[] cs = new char[SIZE];
		for(int i=0; i<SIZE; i++) {
			cs[i] = getRandomField();
		}
		return new String(cs);
	}
}

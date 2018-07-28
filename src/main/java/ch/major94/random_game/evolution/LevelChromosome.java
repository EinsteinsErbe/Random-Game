package ch.major94.random_game.evolution;

public class LevelChromosome extends Chromosome<String> {

	public LevelChromosome() {
		super();

		type = ChromosomeType.LEVEL;
		STDDEV = 2;
		MEAN = 6;
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
		String[] level = new String[genes.size()];
		for(int i=0; i<level.length; i++) {
			level[i] = getGene(i);
		}
		//Arrays.parallelSetAll(level, g -> getRandomGene()); 		//TODO nicht zufällig verteilen?
		return level;
	}

	@Override
	protected String newGene() {
		char[] cs = new char[MEAN];
		for(int i=0; i<cs.length; i++) {
			cs[i] = getRandomField();
		}
		return new String(cs);
	}
}

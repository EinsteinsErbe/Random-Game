package ch.major94.random_game.evolution;

import java.util.Arrays;

public class Genotype extends RandomElement implements Evolvable<Genotype>, Comparable<Genotype>{

	private static final int NUM_CHROMOSOMES = 5;

	private SpriteChromosome sprites;
	private InteractionChromosome interactions;
	private TerminationChromosome terminations;
	private MappingChromosome mapping;
	private LevelChromosome level;
	
	private double fitness;

	private Chromosome<?,?>[] chromosomes;

	public Genotype() {
		super();

		sprites = new SpriteChromosome();
		interactions = new InteractionChromosome();
		terminations = new TerminationChromosome();
		mapping = new MappingChromosome();
		level = new LevelChromosome();

		chromosomes = new Chromosome[NUM_CHROMOSOMES];
		chromosomes[0] = sprites;
		chromosomes[1] = interactions;
		chromosomes[2] =  terminations;
		chromosomes[3] = mapping;
		chromosomes[4] = level;
		
		fitness = 0;
		
		newInstance();
	}

	@Override
	public void mutate() {
		chromosomes[random.nextInt(NUM_CHROMOSOMES)].mutate();
	}

	@Override
	public Genotype crossover(Genotype genotype) {
		int i = random.nextInt(NUM_CHROMOSOMES);
		switch(i) {
		case 0: getSprites().crossover(genotype.getSprites()); break;
		case 1: getInteractions().crossover(genotype.getInteractions()); break;
		case 2: getTerminations().crossover(genotype.getTerminations()); break;
		case 3: getMapping().crossover(genotype.getMapping()); break;
		case 4: getLevel().crossover(genotype.getLevel()); break;
		default: break;
		}
		//TODO
		System.out.println(this+" + "+genotype);
		Genotype g = new Genotype();
		g.calcFitness(0);
		return g;
	}

	@Override
	public void newInstance() {
		Arrays.stream(chromosomes).forEach(c -> c.newInstance());
	}
	
	public void calcFitness(long seed) {
		//spiel parsen
		//eval funktion mit mcts (parallel)
		fitness += random.nextInt(100);
	}

	public Chromosome<?, ?> getChromosome(int i){
		return chromosomes[i];
	}

	public SpriteChromosome getSprites() {
		return sprites;
	}

	public InteractionChromosome getInteractions() {
		return interactions;
	}

	public TerminationChromosome getTerminations() {
		return terminations;
	}

	public MappingChromosome getMapping() {
		return mapping;
	}

	public LevelChromosome getLevel() {
		return level;
	}

	@Override
	public int compareTo(Genotype other) {
		return Double.compare(fitness, other.getFitness());
	}

	public double getFitness() {
		return fitness;
	}
}

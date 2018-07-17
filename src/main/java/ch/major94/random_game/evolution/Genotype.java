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

	public Genotype(Genotype g) {
		sprites = g.getSprites().clone();
		interactions = g.getInteractions().clone();
		terminations = g.getTerminations().clone();
		mapping = g.getMapping().clone();
		level = g.getLevel().clone();

		chromosomes = new Chromosome[NUM_CHROMOSOMES];
		chromosomes[0] = sprites;
		chromosomes[1] = interactions;
		chromosomes[2] =  terminations;
		chromosomes[3] = mapping;
		chromosomes[4] = level;

		this.fitness = g.getFitness();
	}

	@Override
	public void mutate() {
		chromosomes[random.nextInt(NUM_CHROMOSOMES)].mutate();
	}

	@Override
	public Genotype crossover(Genotype genotype) {
		/* AVOID INZEST?
		if(this.equals(genotype)) {
			System.out.println("INZEST");
			return this;
		}
		*/
		Genotype g = new Genotype(this);

		int i = random.nextInt(NUM_CHROMOSOMES);
		switch(i) {
		case 0: g.getSprites().crossover(genotype.getSprites()); break;
		case 1: g.getInteractions().crossover(genotype.getInteractions()); break;
		case 2: g.getTerminations().crossover(genotype.getTerminations()); break;
		case 3: g.getMapping().crossover(genotype.getMapping()); break;
		case 4: g.getLevel().crossover(genotype.getLevel()); break;
		default: break;
		}

		return g;
	}

	@Override
	public void newInstance() {
		Arrays.stream(chromosomes).forEach(c -> c.newInstance());
	}

	public void calcFitness(long seed) {
		//TODO
		//spiel parsen
		//eval funktion mit mcts (parallel)
		fitness = 0;
		for (String s : mapping.genes) {
			fitness += Integer.parseInt(s);
		}
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

	public Genotype clone() {
		return new Genotype(this);
	}

	public void showDetails() {
		System.out.println("======================================================");
		System.out.println(this);
		System.out.println("Fitness: "+fitness);
		for (Chromosome<?, ?> c : chromosomes) {
			System.out.println();
			c.showDetails();
		}
		System.out.println("======================================================");
	}
}

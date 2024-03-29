package ch.major94.random_game.evolution;

import java.util.ArrayList;
import java.util.Arrays;

import ch.major94.random_game.LaunchEval;

public class Genotype extends RandomElement implements Evolvable<Genotype>, Comparable<Genotype>{

	private static final int NUM_CHROMOSOMES = 5;
	private static final double STRONG_MUTATION_RATE = 0.1;
	private static final double VERY_STRONG_MUTATION_RATE = 0.01;

	private SpriteChromosome sprites;
	private InteractionChromosome interactions;
	private TerminationChromosome terminations;
	private MappingChromosome mapping;
	private LevelChromosome level;
	
	public String ID = "   ";	//oldest ancestor
	public String ID2 = "   ";	//direct ancestor
	public String ID3 = "   ";	//personal id

	private double fitness;

	private Chromosome<?>[] chromosomes;

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

	private void createID() {
		ID = buildID();
		ID2 = ID;
		ID3 = ID;
	}
	
	private String buildID() {
		return String.format("%1$3s", Integer.toHexString(hashCode()%4096).toUpperCase());
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
		
		this.ID = g.ID;
		this.ID2 = g.ID3;
		this.ID3 = buildID();
	}

	@Override
	public void mutate() {
		if(random.nextDouble()<VERY_STRONG_MUTATION_RATE) {
			newInstance();
			return;
		}
		
		Chromosome<?> c = chromosomes[random.nextInt(NUM_CHROMOSOMES)];
		if(random.nextDouble()<STRONG_MUTATION_RATE) {
			c.newInstance();
		}
		else {
			c.mutate();
		}
	}

	@Override
	public Genotype crossover(Genotype genotype) {
		Genotype g = new Genotype(this);
		
		// AVOID INZEST
		if(this.equals(genotype)) {
			g.mutate();
			return g;
		}

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
		createID();
	}

	public String[] buildGame() {
		ArrayList<String> game = new ArrayList<>();
		game.add("BasicGame");
		game.addAll(sprites.build());
		game.addAll(mapping.build());
		game.addAll(interactions.build());
		game.addAll(terminations.build());
		return game.toArray(new String[0]);
	}

	public String[] buildLevel() {
		return level.buildLevel();
	}

	public void calcFitness(long seed) {
		System.out.print(ID+" / "+ID2+" / "+ID3+"\t");
		try {
			fitness = LaunchEval.eval(buildGame(), buildLevel());
		} catch (Exception e) {
			System.out.println("INVALID");
			fitness = -1;
		}
	}

	public Chromosome<?> getChromosome(int i){
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
		System.out.println(ID+" / "+ID2+" / "+ID3);
		System.out.println("Fitness: "+fitness);
		for(String s : buildGame()) {
			System.out.println(s);
		}
		for(String s : buildLevel()) {
			System.out.println(s);
		}
		System.out.println("======================================================");
	}
}

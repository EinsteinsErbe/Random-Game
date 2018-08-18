package ch.major94.random_game.evolution;

import java.util.ArrayList;

public abstract class Chromosome<G> extends RandomElement implements Evolvable<Chromosome<G>>{

	protected final static int N_SPRITES = 5;
	protected final static int N_FIELDS = 5;
	protected static final String SPRITE = "sprite";
	protected static final double GENE_COUNT_MOD_RATE = 0.3;
	
	protected double STDDEV = 0;
	protected int MEAN = 4;

	protected static String[] sprites;
	protected static char[] fields;

	protected ArrayList<G> genes;

	protected ChromosomeType type = ChromosomeType.NONE;

	public Chromosome(){
		super();

		genes = new ArrayList<>();
	}

	public G getGene(int i) {
		return genes.get(i);
	}

	public ArrayList<G> getGenes() {
		return genes;
	}

	public G replaceGene(G gene, int i) {
		G gene2 = genes.remove(i);
		genes.add(i, gene);
		return gene2;
	}

	public G replaceRandomGene(G gene) {
		int i = getRandonGeneI();
		return replaceGene(gene, i);
	}

	protected int getRandonGeneI() {
		return random.nextInt(genes.size());
	}

	public G getRandomGene() {
		return getGene(getRandonGeneI());
	}

	@Override
	public Chromosome<G> crossover(Chromosome<G> chromosome) {		//TODO change to single point crossover?
		/*replace on random gene with random gene
		replaceRandomGene(chromosome.getRandomGene());
		return this;*/

		/*replace on random gene
		int i = getRandonGeneI();
		replaceGene(chromosome.getGene(i), i);
		return this;*/

		//2-Point Crossover
		int n = Math.min(genes.size(), chromosome.getGenes().size());
		int first = random.nextInt(n);
		int last = random.nextInt(n);

		if(first > last) {
			int t = first;
			first = last;
			last = t;
		}

		for(int i=first; i<=last; i++) {
			replaceGene(chromosome.getGene(i), i);
		}

		return this;
	}
	
	@Override
	public void newInstance() {
		genes.clear();

		for(int i=0; i<MEAN; i++) {
			genes.add(newGene());
		}
	}

	@Override
	public void mutate() {
		if(random.nextDouble() < GENE_COUNT_MOD_RATE) {
			modifyGeneCount();
		}
		else {
			replaceRandomGene(newGene());
		}
	}

	//	public void showDetails() {
	//		System.out.println(type);
	//		for (G g : genes) {
	//			System.out.println(g.toString());
	//		}
	//	}

	public ArrayList<String> build() {
		ArrayList<String> s = new ArrayList<String>();
		s.add("  "+type.getName());
		for (G g : genes) {
			s.add("    "+g.toString());
		}
		s.add("");
		return s;
	}

	protected void modifyGeneCount() {
		double change = random.nextGaussian()*STDDEV + MEAN - genes.size();

		if(change > 0) {
			genes.add(newGene());
		}
		if(change < 0 && genes.size()>1) {
			genes.remove(getRandonGeneI());
		}	
	}

	protected abstract G newGene();

	public abstract Chromosome<G> clone();

	public static void setupLists() {
		sprites = new String[N_SPRITES];

		for(int i=0; i<N_SPRITES; i++) {
			sprites[i] = SPRITE+i;
		}

		fields = new char[N_FIELDS];

		for(int i=0; i<N_FIELDS; i++) {
			fields[i] = Character.forDigit(i, 10);
		}
	}

	protected String getRandomSprite() {
		return sprites[random.nextInt(N_SPRITES)];
	}

	protected char getRandomField() {
		return fields[random.nextInt(N_FIELDS)];
	}

	protected <T> T pickRandom(T[] array) {
		return array[random.nextInt(array.length)];
	}

	protected String optional(String s, double prob, String elseS) {
		return random.nextDouble()<prob ? s : elseS;
	}
}

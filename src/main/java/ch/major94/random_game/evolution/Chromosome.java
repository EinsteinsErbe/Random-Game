package ch.major94.random_game.evolution;

import java.util.ArrayList;

public abstract class Chromosome<T extends Chromosome<T,G>, G> extends RandomElement implements Evolvable<Chromosome<T,G>>{
	
	protected ArrayList<G> genes;
	
	protected ChromosomeType type = ChromosomeType.NONE;
	
	public Chromosome(){
		super();
		
		genes = new ArrayList<>();
		
		//newInstance();
	}
	
	public G getGene(int i) {
		return genes.get(i);
	}
	
	public ArrayList<G> getGenes() {
		return genes;
	}
	
	public G replaceRandomGene(G gene) {
		int i = getRandonGeneI();
		G gene2 = genes.remove(i);
		genes.add(i, gene);
		return gene2;
	}
	
	private int getRandonGeneI() {
		return random.nextInt(genes.size());
	}
	
	public G getRandomGene() {
		return getGene(getRandonGeneI());
	}
	
	@Override
	public Chromosome<T, G> crossover(Chromosome<T,G> chromosome) {
		replaceRandomGene(chromosome.getRandomGene());
		return this;
	}
	
	public void showDetails() {
		System.out.println(type);
		for (G g : genes) {
			System.out.println(g.toString());
		}
	}
	
	public abstract T clone();
	
//	@Override
//	public void newInstance() {
//		//genes.stream().forEach(g -> g.);
//	}
}

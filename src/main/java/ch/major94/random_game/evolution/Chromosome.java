package ch.major94.random_game.evolution;

import java.util.ArrayList;

public abstract class Chromosome<T extends Chromosome<T,G>, G> extends RandomElement implements Evolvable<Chromosome<T,G>>{
	
	protected ArrayList<G> genes;
	
	public Chromosome(){
		super();
		
		genes = new ArrayList<>();
		
		newInstance();
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
	
	@Override
	public Chromosome<T, G> crossover(Chromosome<T,G> chromosome) {
		int i = getRandonGeneI();
		G gene = chromosome.replaceRandomGene(genes.remove(i));
		genes.add(i, gene);
		//TODO
		return chromosome;
	}
	
//	@Override
//	public void newInstance() {
//		//genes.stream().forEach(g -> g.);
//	}
}

package ch.major94.random_game.jenetic;

import io.jenetics.AbstractChromosome;
import io.jenetics.Chromosome;
import io.jenetics.util.ISeq;

public class VGDLChromosome extends AbstractChromosome<VGDLGene> {

	protected VGDLChromosome(ISeq<? extends VGDLGene> genes) {
		super(genes);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Chromosome<VGDLGene> newInstance(ISeq<VGDLGene> genes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chromosome<VGDLGene> newInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}

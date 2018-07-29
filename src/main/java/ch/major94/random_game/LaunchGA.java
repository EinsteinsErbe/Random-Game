package ch.major94.random_game;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import core.game.StateObservation;
import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.Mutator;
import io.jenetics.SinglePointCrossover;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
import tools.IO;

public class LaunchGA {
	
	static String[] game = new IO().readFile("game.txt");
	static String[] level = new IO().readFile("level.txt");
	static StateObservation state;

	static int fitness(Genotype<IntegerGene> program) {
	
		
		System.out.println("start "+program);
		
		LaunchEval.eval(game, level);
		System.out.println("end "+program);
		return program.getChromosome(0).stream().mapToInt(g -> g.intValue()).sum() - 
				program.getChromosome(1).stream().mapToInt(g -> g.intValue()).sum();
	}

 //	static final Codec<IntegerGene, IntegerGene>
//	CODEC = Codec.of(
//		Genotype.of(IntegerChromosome.of(0, 10, 5)
//		),
//		Genotype::getGene
//	);
	
	static Factory<Genotype<IntegerGene>> gtf =
            Genotype.of(IntegerChromosome.of(0, 10, 5),IntegerChromosome.of(0, 5, 5));

	public static void main(final String[] args) {
		
		ExecutorService exe = Executors.newSingleThreadExecutor();
		
		long start = System.nanoTime();
		
		final Engine<IntegerGene, Integer> engine = Engine
			.builder(LaunchGA::fitness, gtf)
			.executor(exe)
			.maximizing()
			.populationSize(2)
			.alterers(
				new SinglePointCrossover<>(0.3),
				//new MultiPointCrossover<>(0.5, 1),
				new Mutator<>(0.2))
			.build();

		final Genotype<IntegerGene> program = engine.stream()
			.limit(1)
			.collect(EvolutionResult.toBestGenotype());

		System.out.println(program+" -> "+fitness(program));
		
		long end = System.nanoTime();
		double delta = (end-start)/1000000000.0;

		System.out.println("Total time: "+delta+"s");
		exe.shutdown();
	}

}
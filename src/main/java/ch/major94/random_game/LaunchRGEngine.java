package ch.major94.random_game;

import ch.major94.random_game.evolution.Chromosome;
import ch.major94.random_game.evolution.Genotype;
import core.logging.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class LaunchRGEngine {

	private static final int POP_SIZE = 10;
	private static final int N_GENERATIONS = 10;
	private static final double uniformRate = 0.5;
	private static final double mutationRate = 0.1;
	private static final int tournamentSize = 2;
	private static final boolean elitism = true;

	private static Genotype[] pop;

	public static void main(String[] args) {

		Chromosome.setupLists();
		Logger.getInstance().active = false; //Disable parse errors

		Genotype best = new Genotype();
		//geno.showDetails();
		//System.exit(0);


		//ArrayList<Genotype> population = new ArrayList<Genotype>(100);
		pop = new Genotype[POP_SIZE];
		Arrays.parallelSetAll(pop, g -> new Genotype());

		System.out.println("initialized");
		//printPop();

		for (int i = 0; i <= N_GENERATIONS; i++) {
			if(i>0) {
				pop = evolve(pop);
			}

			Arrays.stream(pop).forEachOrdered(g -> g.calcFitness(0));
			System.out.println("calulated");
			Arrays.parallelSort(pop);
			best = pop[POP_SIZE-1];
			best.showDetails();
			//TODO write game files?
			writeFiles(best, i);
			System.out.println("GENERATION "+(i)+": best fitness: "+best.getFitness());
		}
	}

	private static Genotype[] evolve(Genotype[] pop) {
		Genotype[] newPop = new Genotype[POP_SIZE];

		Arrays.parallelSort(pop);

		// Keep our best individual
		Genotype best = pop[POP_SIZE-1];
		if (elitism) {
			newPop[0] = pop[POP_SIZE-1];
		}

		// Loop over the population size and create new individuals with
		// crossover
		//TODO parallelSetAll?
		Arrays.setAll(newPop, g -> tournamentSelection(pop).crossover(tournamentSelection(pop)));
		
		if (elitism) {
			newPop[0] = best;
		}

		// Mutate population
		Arrays.stream(newPop).parallel().forEach(g -> {
			if(Math.random()<mutationRate) g.mutate();
		});

		return newPop;
	}

	// Select individuals for crossover
	private static Genotype tournamentSelection(Genotype[] pop) {
		// Create a tournament population
		Genotype[] tournament = new Genotype[tournamentSize];
		// For each place in the tournament get a random individual
		Arrays.parallelSetAll(tournament, (i) -> pop[new Random().nextInt(POP_SIZE)]);
		// Get the fittest
		Arrays.parallelSort(tournament);
		return tournament[tournamentSize - 1];
	}

	public static void printPop() {
		for (Genotype g : pop) {
			g.showDetails();
		}
	}

	public static void writeFiles(Genotype g, int id) {
		writeFile("generated/"+id+"game1.txt", g.buildGame());
		writeFile("generated/"+id+"level1.txt", g.buildLevel());
	}   

	private static void writeFile(String name, String[] content) {
		try {
			BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(name), false));
			for (String s : content) {
				bwr.write(s);
				bwr.write("\n");
			}
			bwr.flush();
			bwr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

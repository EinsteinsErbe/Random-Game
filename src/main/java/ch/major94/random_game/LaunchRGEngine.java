package ch.major94.random_game;

import ch.major94.random_game.evolution.Genotype;

import java.util.Arrays;
import java.util.Random;

public class LaunchRGEngine {

    private static final int POP_SIZE = 10;
    private static final int N_GENERATIONS = 100;
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    public static void main(String[] args) {

        //ArrayList<Genotype> population = new ArrayList<Genotype>(100);
        Genotype[] pop = new Genotype[POP_SIZE];
        Arrays.parallelSetAll(pop, g -> new Genotype());

        for (int i = 0; i < N_GENERATIONS; i++) {
            System.out.print("GENERATION " + (i + 1));
            Arrays.stream(pop).forEachOrdered(g -> g.calcFitness(0));
            System.out.println(": Fitness:");
            pop = evolve(pop);
        }

        //Arrays.stream(pop).parallel().forEach(g -> g = new Genotype());
        Arrays.stream(pop).forEach(g -> g.calcFitness(0));
        Arrays.parallelSort(pop);
        Arrays.stream(pop).forEachOrdered(g -> System.out.println(g.getFitness()));
    }

    private static Genotype[] evolve(Genotype[] pop) {
        Genotype[] newPop = new Genotype[POP_SIZE];

        Arrays.parallelSort(pop);

//		// Keep our best individual
//		if (elitism) {
//			newPop[0] = pop[POP_SIZE-1];
//		}
//
//		// Crossover population
//		int elitismOffset;
//		if (elitism) {
//			elitismOffset = 1;
//		} else {
//			elitismOffset = 0;
//		}

        // Loop over the population size and create new individuals with
        // crossover
        //TODO parallelSetAll
        Arrays.setAll(newPop, g -> tournamentSelection(pop).crossover(tournamentSelection(pop)));

        // Mutate population
        Arrays.stream(newPop).parallel().forEach(g -> g.mutate());

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
}

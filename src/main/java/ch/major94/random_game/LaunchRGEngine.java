package ch.major94.random_game;

import ch.major94.random_game.evolution.Genotype;

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

        //ArrayList<Genotype> population = new ArrayList<Genotype>(100);
        pop = new Genotype[POP_SIZE];
        Arrays.parallelSetAll(pop, g -> new Genotype());
        
        //printPop();

        for (int i = 0; i <= N_GENERATIONS; i++) {
        	if(i>0) {
        		pop = evolve(pop);
        	}
            
            Arrays.stream(pop).forEachOrdered(g -> g.calcFitness(0));
            Arrays.parallelSort(pop);
            //pop[POP_SIZE-1].showDetails();
            System.out.println("GENERATION "+(i)+": best fitness: "+pop[POP_SIZE-1].getFitness());
        }

        Arrays.parallelSort(pop);
        //Arrays.stream(pop).forEachOrdered(g -> System.out.println(g.getFitness()));
        pop[POP_SIZE-1].showDetails();
        //printPop();
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
        //TODO parallelSetAll?
        Arrays.setAll(newPop, g -> tournamentSelection(pop).crossover(tournamentSelection(pop)));

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
}

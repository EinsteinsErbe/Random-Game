package ch.major94.random_game;

import ch.major94.random_game.display.LineChart;
import ch.major94.random_game.display.StackedAreaChart;
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

	private static int POP_SIZE = 20;
	private static int N_GENERATIONS = 30;
	private static double mutationRate = 0.3;
	private static int tournamentSize = 5;
	private static final boolean elitism = true;

	private static Genotype[] pop;

	public static void main(String[] args) {

		try {
			POP_SIZE = Integer.parseInt(args[0]);
			N_GENERATIONS = Integer.parseInt(args[1]);
			mutationRate = Double.parseDouble(args[2]);
			tournamentSize  = Integer.parseInt(args[3]);
			LaunchEval.AGENT_TIME = Integer.parseInt(args[4]);
			LaunchEval.N_AGENTS = Integer.parseInt(args[5]);
			System.out.println("parameters accepted");
		} catch (Exception e) {
			// TODO: handle exception
		}

		LineChart fitnessChart = new LineChart(
				"Random Game Evolution" ,
				"Verlauf der Fitness",
				"Generation",
				"Best Fitness");
		fitnessChart.pack();
		fitnessChart.setVisible(true);

		StackedAreaChart speciesChart = new StackedAreaChart(
				"Random Game Evolution",
				"Spezies",
				"Generation",
				"Anzahl");
		speciesChart.pack();
		speciesChart.setVisible(true);

		Chromosome.setupLists();
		Logger.getInstance().active = false; //Disable parse errors

		Genotype best = new Genotype();

		//ArrayList<Genotype> population = new ArrayList<Genotype>(100);
		pop = new Genotype[POP_SIZE];
		Arrays.parallelSetAll(pop, g -> new Genotype());

		for (int i = 0; i <= N_GENERATIONS; i++) {

			System.out.println("######################################################");
			System.out.println("evaluating GENERATION "+i);

			if(i>0) {
				pop = evolve(pop);
			}

			Arrays.stream(pop).forEachOrdered(g -> g.calcFitness(0));
			Arrays.parallelSort(pop);
			best = pop[POP_SIZE-1];
			best.showDetails();

			writeFiles(best, i);
			System.out.println("GENERATION "+(i)+": best fitness: "+best.getFitness());

			fitnessChart.addData(i, best.getFitness());
			speciesChart.addData(Arrays.stream(pop).map(g -> g.ID).toArray(String[]::new), i);
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

		// Mutate population
		Arrays.stream(newPop).parallel().forEach(g -> {
			if(Math.random()<mutationRate) g.mutate();
		});

		if (elitism) {
			newPop[0] = best;
		}

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

	public static void writeFile(String name, String[] content) {
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

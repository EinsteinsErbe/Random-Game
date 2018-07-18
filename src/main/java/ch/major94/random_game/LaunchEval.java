package ch.major94.random_game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import core.game.Game;
import core.game.StateObservation;
import core.vgdl.VGDLFactory;
import core.vgdl.VGDLParser;
import core.vgdl.VGDLRegistry;
import tools.IO;
import tracks.ruleGeneration.geneticRuleGenerator.SharedData;
import tracks.singlePlayer.advanced.olets.SingleMCTSPlayer;

public class LaunchEval {

	static int AGENT_TIME = 10;
	static int N_AGENTS = 8;
	static StringBuffer sb = new StringBuffer();
	
	static String sampleRandomController = "tracks.singlePlayer.simple.sampleRandom.Agent";
	static String doNothingController = "tracks.singlePlayer.simple.doNothing.Agent";
	static String sampleOneStepController = "tracks.singlePlayer.simple.sampleonesteplookahead.Agent";
	static String sampleFlatMCTSController = "tracks.singlePlayer.simple.greedyTreeSearch.Agent";

	static String sampleMCTSController = "tracks.singlePlayer.advanced.sampleMCTS.Agent";
	static String sampleRSController = "tracks.singlePlayer.advanced.sampleRS.Agent";
	static String sampleRHEAController = "tracks.singlePlayer.advanced.sampleRHEA.Agent";
	static String sampleOLETSController = "tracks.singlePlayer.advanced.olets.Agent";

	public static void main(String[] args) {

		String[] game = new IO().readFile("game.txt");
		String[] level = new IO().readFile("level.txt");

		println("A_TIME: \tMIN_ITER: \tFRAMES   \tPOINTS   \tWIN_RATE \tWIN_FRAMES \tTIME");

		for(int i=0; i<=100; i+=1) {
			//for(int j=1; j<=10; j+=5) {
			for(int k=0; k<5; k++) {
				eval(game, level, i, 1);
			}
			//}
		}
	}

	public static void eval(String[] game, String[] level, int a_time, int min_iter) {
		long start = System.nanoTime();
		AGENT_TIME = a_time;
		SingleMCTSPlayer.MIN_ITER = min_iter;
		print(a_time+"\t\t"+min_iter+"\t\t");
		double score = eval(game, level);
		long end = System.nanoTime();
		double delta = (end-start)/1000000000.0;
		println(delta+"\t SCORE: "+score);
	}

	public static double eval(String[] game, String[] level) {

		final int seed = new Random().nextInt();

		VGDLFactory.GetInstance().init();
		VGDLRegistry.GetInstance().init();

		Game toPlay = new VGDLParser().parseGame(game);
		toPlay.buildStringLevel(level, seed);
		return eval(toPlay.getObservation(), seed);
	}

	public static double eval(StateObservation state, int seed) {

		//********************************
		//entweder fitness function parallel aufrufen (evtl. problem wenn mehrere spiele gleichzeitig erstellt werden)
		//oder fitness funktion gut parallelisieren

		//auf welcher ebene daten austauschen? als string[] ist am einfachsten, als listen mit jeweiligem content schneller

		//Liste mit Agents, Anzahl und gewichte definieren
		//alle in ein Array oder Liste (ohne parallelSetAll)
		//		final int parallelism = 8;
		System.out.print("A ");
		double pos = evalBestAgent(sampleOLETSController, N_AGENTS, 1.0, seed, state, 500);
		System.out.print("B ");
		double neg = evalAvrgAgent(SharedData.RANDOM_AGENT_NAME, 10*N_AGENTS, -0.99, seed, state, 2000);

		System.out.print(" Positve result: "+pos);
		System.out.println("\t Negative result: "+-neg);

		return pos + neg;
	}

	private static double evalBestAgent(String agentName, int n_agents, double weight, int seed, StateObservation state, int max) {

		GameEval[] result = new GameEval[n_agents];
		
		AtomicBoolean winnerFound = new AtomicBoolean(false);

		Arrays.parallelSetAll(result, (i)->new GameEval(i, winnerFound));

//		Arrays.stream(result).parallel().forEach((GameEval ge) -> {
//			ge.setState(state);
//			ge.simulate(2000, agentName, AGENT_TIME, new Random().nextInt(), false);
//		});
//		
//		int frames = Arrays.stream(result).parallel().mapToInt(GameEval::getSteps).sum()/n_agents;
//		double score = Arrays.stream(result).parallel().mapToDouble(GameEval::getScore).sum()/n_agents;	
//		int winner = Arrays.stream(result).parallel().mapToInt(GameEval::getWin).sum();
//		double win_rate = winner/(double)n_agents;
//		int win_frames = winner>0 ? Arrays.stream(result).parallel().filter(ge -> ge.isWin()).mapToInt(GameEval::getSteps).sum()/winner : 0;
//
//		print(frames+"\t\t"+score+"\t\t"+win_rate+"\t\t"+win_frames+"\t\t");
//
//		return Arrays.stream(result).parallel().mapToDouble(GameEval::getScore).sum()/n_agents*weight;
		
//****************Kind of Best first search*********************	
		
		Arrays.stream(result).parallel().forEach((GameEval ge) -> {
			ge.setState(state);
			if(ge.simulate(max, agentName, AGENT_TIME, new Random().nextInt(), false)) {
				winnerFound.set(true);
			}
		});
		
		Optional<GameEval> first = Arrays.stream(result).parallel().filter(ge -> ge.isWin()).findFirst();
	
//************************************
//		Arrays.stream(result).parallel().forEach((GameEval ge) -> ge.setState(state));
//		
//		Optional<GameEval> first = Arrays.stream(result).parallel().filter(ge -> ge.simulate(2000, agentName, AGENT_TIME, new Random().nextInt(), false)).findFirst();
//
		if(first.isPresent()) {
			print(first.get().getSteps()+"\t\t"+first.get().getScore()+"\t\t"+first.get().getWin()+"\t\t"+first.get().getSteps()+"\t\t");
			return first.get().getScore()*weight;
		}
		else {
			return Arrays.stream(result).parallel().mapToDouble(GameEval::getScore).max().orElse(0)*weight;
		}
	}
	
	private static double evalAvrgAgent(String agentName, int n_agents, double weight, int seed, StateObservation state, int max) {

		GameEval[] result = new GameEval[n_agents];
		
		AtomicBoolean winnerFound = new AtomicBoolean(false);

		Arrays.parallelSetAll(result, (i)->new GameEval(i, winnerFound));

		Arrays.stream(result).parallel().forEach((GameEval ge) -> {
			ge.setState(state);
			ge.simulate(max, agentName, AGENT_TIME, new Random().nextInt(), false);
			System.out.print("X");
		});

		return Arrays.stream(result).parallel().mapToDouble(GameEval::getScore).sum()/n_agents*weight;
		
	}

	private static void println(String s) {
		print(s+"\n");
	}

	private static void print(String s) {
		sb.append(s.replaceAll("\t\t", "\t"));
		try {
			BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("log.txt"), true));
			bwr.write(s.replaceAll("\t\t", "\t"));
			bwr.flush();
			bwr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.print(s);
	}
}
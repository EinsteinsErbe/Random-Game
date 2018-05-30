package ch.major94.random_game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
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
	static int N_AGENTS = 100;
	static StringBuffer sb = new StringBuffer();

	public static void main(String[] args) {

		String[] game = new IO().readFile("game.txt");
		String[] level = new IO().readFile("level.txt");

		println("A_TIME: \tMIN_ITER: \tFRAMES   \tPOINTS   \tWIN_RATE \tWIN_FRAMES \tTIME");

		for(int i=40; i<=200; i+=5) {
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
		eval(game, level);
		long end = System.nanoTime();
		double delta = (end-start)/1000000000.0;
		println(delta+"");
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
		//oder fitness funtion gut parallelisieren

		//auf welcher ebene daten austauschen? als string[] ist am einfachsten, als listen mit jeweiligem content schneller

		//Liste mit Agents, Anzahl und gewichte definieren
		//alle in ein Array oder Liste (ohne parallelSetAll)
		//		final int parallelism = 8;

		double pos = evalAgent(SharedData.BEST_AGENT_NAME, N_AGENTS, 1.0, seed, state);
		double neg = 0;//evalAgent(SharedData.RANDOM_AGENT_NAME, 10*N_AGENTS, -2.0, seed, state);

		//System.out.println("Positve result: "+pos);
		//System.out.println("Negative result: "+-neg);

		return pos + neg;
	}

	private static double evalAgent(String agentName, int n_agents, double weight, int seed, StateObservation state) {

		GameEval[] result = new GameEval[n_agents];

		Arrays.parallelSetAll(result, (i)->new GameEval(i));

		Arrays.stream(result).parallel().forEach((GameEval ge) -> {
			ge.setState(state);
			ge.simulate(2000, agentName, AGENT_TIME, new Random().nextInt(), false);
		});

		int frames = Arrays.stream(result).parallel().mapToInt(GameEval::getSteps).sum()/n_agents;
		double score = Arrays.stream(result).parallel().mapToDouble(GameEval::getScore).sum()/n_agents;	
		int winner = Arrays.stream(result).parallel().mapToInt(GameEval::getWin).sum();
		double win_rate = winner/(double)n_agents;
		int win_frames = winner>0 ? Arrays.stream(result).parallel().filter(ge -> ge.isWin()).mapToInt(GameEval::getSteps).sum()/winner : 0;

		print(frames+"\t\t"+score+"\t\t"+win_rate+"\t\t"+win_frames+"\t\t");

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
		System.out.print(s);
	}
}
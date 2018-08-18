package ch.major94.random_game;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import core.game.Game;
import core.game.StateObservation;
import core.vgdl.VGDLFactory;
import core.vgdl.VGDLParser;
import core.vgdl.VGDLRegistry;
import tools.IO;
import tools.Utils;
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
	
	private static final int MAX_FRAMES = 1000;
	private static final String BEST_AGENT = sampleOLETSController;

	public static void main(String[] args) {

		//Test fitness function

		//Load available games
		String spGamesCollection =  "examples/all_games_sp.csv";
		String[][] games = Utils.readGames(spGamesCollection);

		for(int i=0; i<games.length; i++) {
			String gameName = games[i][1];
			String gameFile = games[i][0];
			String[] game = new IO().readFile(gameFile);
			
			sb.append(gameName);

			System.out.println(gameName);
			for(int k=0; k<5; k++) {
				String[] level = new IO().readFile(gameFile.replace(gameName, gameName + "_lvl" + k));
				double res = 0;
				try {
					res = eval(game, level);
				} catch (Exception e) {
					
				}
				
				sb.append("\t");
				sb.append(res);
			}
			sb.append("\n");
		}
		
		LaunchRGEngine.writeFile("fitnessTest_RS_"+AGENT_TIME+".txt", new String[]{sb.toString()});
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
		//final int parallelism = 8;
		System.out.print("Good Player: ");
		GameEval[] result1 = evalBestAgent(BEST_AGENT, N_AGENTS, seed, state, MAX_FRAMES);
		double max = Arrays.stream(result1).parallel().mapToDouble(GameEval::getScore).max().orElse(0);
		double win = Arrays.stream(result1).parallel().anyMatch(ge -> ge.isWin()) ? 1 : 0;
		int frames = Arrays.stream(result1).parallel().mapToInt(GameEval::getSteps).min().orElse(0);
		System.out.print(" Result: "+max+"\t"+win);
		if(Arrays.stream(result1).parallel().anyMatch(ge -> ge.isTimeOut())) {
			System.out.println("\t TIMEOUT");
			return 0;
		}

		System.out.print("\tBad Player: ");
		GameEval[] result2 = evalAvrgAgent(SharedData.RANDOM_AGENT_NAME, N_AGENTS, seed, state, MAX_FRAMES);
		double avrg = Arrays.stream(result2).parallel().mapToDouble(GameEval::getScore).average().orElse(0);
		double avrgWin = Arrays.stream(result2).parallel().filter(ge -> ge.isWin()).count()/(double)N_AGENTS;
		System.out.print(" Result: "+avrg+"   \t"+avrgWin);
		if(Arrays.stream(result1).parallel().anyMatch(ge -> ge.isTimeOut())) {
			System.out.println("\t TIMEOUT");
			return 0;
		}

		System.out.print("\tIdle Player: ");
		GameEval[] result3 = evalAvrgAgent(SharedData.DO_NOTHING_AGENT_NAME, N_AGENTS, seed, state, MAX_FRAMES);
		double avrg2 = Arrays.stream(result3).parallel().mapToDouble(GameEval::getScore).average().orElse(0);
		double avrgWin2 = Arrays.stream(result3).parallel().filter(ge -> ge.isWin()).count()/(double)N_AGENTS;
		System.out.print(" Result: "+avrg2+"   \t"+avrgWin2);
		if(Arrays.stream(result1).parallel().anyMatch(ge -> ge.isTimeOut())) {
			System.out.println("\t TIMEOUT");
			return 0;
		}

		avrg = Math.max(avrg, avrg2);
		avrgWin = Math.max(avrgWin, avrgWin2);

		double fitness = 0;

		//Anyone has won
		//fitness += win+avrgWin>0 ? 1 : 0;

		//better player wins
		fitness += win-avrgWin>0 ? win-avrgWin+1 : 0;

		//Bonus for getting more points than the random agents
		if(max > 0 && max>avrg) {
			fitness += Math.min((max-avrg)/max, 1);
		}

		//not instant game over
		fitness += frames>5 ? 1 : 0;

		System.out.println("\t fitness: "+fitness);

		return fitness;
	}

	private static GameEval[] evalBestAgent(String agentName, int n_agents, int seed, StateObservation state, int max) {

		GameEval[] result = new GameEval[n_agents];

		AtomicBoolean stop = new AtomicBoolean(false);

		Arrays.parallelSetAll(result, (i)->new GameEval(i, stop));	

		Arrays.stream(result).parallel().forEach((GameEval ge) -> {
			ge.setState(state);
			if(ge.simulate(max, agentName, AGENT_TIME, new Random().nextInt(), false)) {
				stop.set(true);
			}
			System.out.print(ge.finishChar());
		});

		return result;
	}

	private static GameEval[] evalAvrgAgent(String agentName, int n_agents, int seed, StateObservation state, int max) {

		GameEval[] result = new GameEval[n_agents];

		AtomicBoolean stop = new AtomicBoolean(false);

		Arrays.parallelSetAll(result, (i)->new GameEval(i, stop));

		Arrays.stream(result).parallel().forEach((GameEval ge) -> {
			ge.setState(state);
			ge.simulate(max, agentName, AGENT_TIME, new Random().nextInt(), false);
			System.out.print(ge.finishChar());
		});

		return result;
	}
}
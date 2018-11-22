package ch.major94.random_game;

import java.util.Random;

import core.competition.CompetitionParameters;
import tracks.ArcadeMachine;

public class LaunchRandomGame {
	
	static boolean visuals = true;

	static String sampleOLETSController = "tracks.singlePlayer.advanced.olets.Agent";
	
	static int run = 1;
	static int gen = 24;

	public static void main(String[] args) {
		
		String run = "";
		
		try {
			run = "run"+Integer.parseInt(args[0])+"/";
			gen = Integer.parseInt(args[1]);
			
			System.out.println("parameters accepted");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//Use custom Game
		String game = run+"generated/"+gen+"game1.txt";
		String level1 = run+"generated/"+gen+"level1.txt";
		String recordActionsFile = null;//"actions0.txt";
		
		//CompetitionParameters.GAMETICKS = 100;
		CompetitionParameters.ACTION_TIME = 40;

		//ArcadeMachine.runOneGame(game, level1, visuals, sampleOLETSController, recordActionsFile, new Random().nextInt(), 0);
		//ArcadeMachine.replayGame(game, level1, visuals, recordActionsFile);
		ArcadeMachine.playOneGame(game, level1, null, new Random().nextInt());
	}
}
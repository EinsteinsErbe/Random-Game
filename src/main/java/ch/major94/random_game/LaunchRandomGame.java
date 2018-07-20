package ch.major94.random_game;

import core.competition.CompetitionParameters;
import tracks.ArcadeMachine;

public class LaunchRandomGame {

	public static void main(String[] args) {

		final boolean visuals = true;
		
		CompetitionParameters.GAMETICKS = 0;
		
		final int gen = 10;

		//Use custom Game
		final String game = "generated/"+gen+"game1.txt";
		final String level1 = "generated/"+gen+"level1.txt";
		final String readActionsFile = "actions0.txt";

//		ArcadeMachine.runOneGame(game, level1, visuals, SharedData.NAIVE_AGENT_NAME, recordActionsFile, seed, 0);
		//ArcadeMachine.replayGame(game, level1, visuals, readActionsFile);
		ArcadeMachine.playOneGame(game, level1, null, 0);
	}
}
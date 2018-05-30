package ch.major94.random_game;

import core.competition.CompetitionParameters;
import tracks.ArcadeMachine;

public class LaunchRandomGame {

	public static void main(String[] args) {

		final boolean visuals = true;
		
		CompetitionParameters.GAMETICKS = 0;

		//Use custom Game
		final String game = "game.txt";
		final String level1 = "level.txt";
		final String readActionsFile = "actions0.txt";

//		ArcadeMachine.runOneGame(game, level1, visuals, SharedData.NAIVE_AGENT_NAME, recordActionsFile, seed, 0);
		ArcadeMachine.replayGame(game, level1, visuals, readActionsFile);
	}
}
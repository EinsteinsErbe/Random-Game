package ch.major94.random_game;

import core.competition.CompetitionParameters;
import tracks.ArcadeMachine;

public class LaunchRandomGame {

	public static void main(String[] args) {

		final boolean visuals = true;

		String sampleOLETSController = "tracks.singlePlayer.advanced.olets.Agent";

		CompetitionParameters.GAMETICKS = 0;

		final int gen = 20;

		//Use custom Game
		final String game = "generated/"+gen+"game1.txt";
		final String level1 = "generated/"+gen+"level1.txt";
		final String recordActionsFile = "actions0.txt";

		ArcadeMachine.runOneGame(game, level1, visuals, sampleOLETSController, recordActionsFile, 0, 0);
		//ArcadeMachine.replayGame(game, level1, visuals, recordActionsFile);
		//ArcadeMachine.playOneGame(game, level1, null, 0);
	}
}
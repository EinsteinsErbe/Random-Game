package ch.major94.random_game;

import java.lang.reflect.Constructor;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class GameEval {
	
	//final static double WIN_BONUS = 1000.0;

	private StateObservation state;
	private double score = 0;;
	private boolean win = false;
	private int steps = 0;
	private AtomicBoolean stop;
	private boolean timeOut = false;
	
	private int id;

	public GameEval(int id, AtomicBoolean stop) {
		this.id = id;
		this.stop = stop;
	}

	public boolean simulate(int maxFrames, String agentName, int agentTime, int seed, boolean saveActions) {
		//System.out.println("start agent "+id);
		AbstractPlayer a;
		
		win = false;
		try {
			state.setNewSeed(seed);
			a = createAgent(agentName, state);
			a.setup("actions"+id+".txt", seed, false);
			int res = getAgentResult(state, maxFrames, agentTime, a);
			if(saveActions) {
				a.teardown(state);
			}
			win = state.getGameWinner().equals(Types.WINNER.PLAYER_WINS);
			score = state.getGameScore();// + (win ? WIN_BONUS : 0);
			steps = res;
			if(timeOut) {
				stop.set(true);
			}
			//System.out.println("Agent "+id+": "+res+" frames | "+state.getGameScore()+" points, "+state.getGameWinner()+" => "+score);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return win;
	}
	
	private int getAgentResult(StateObservation stateObs, int steps, int agentTime, AbstractPlayer agent){
		int k = 0;
		int i;
		for(i=0;i<steps;i++){

			ElapsedCpuTimer timer = new ElapsedCpuTimer();
			timer.setMaxTimeMillis(agentTime);

			Types.ACTIONS bestAction = agent.act(stateObs, timer);

			stateObs.advance(bestAction);
			agent.logAction(bestAction);
			//k += checkIfOffScreen(stateObs);
			
			timeOut = timer.elapsedMillis() > 2*agentTime + 100;

			if(stateObs.isGameOver() || stop.get() || timeOut){
				break;
			}
		}
		if(k > 0) {
			// add k to global var keeping track of this
			//this.badFrames += k;
		}
		return i;
	}
	
	private static AbstractPlayer createAgent(String name, StateObservation state) throws Exception{

		Class<?> agentClass = Class.forName(name);
		Constructor<?> agentConst = agentClass.getConstructor(new Class[]{StateObservation.class, ElapsedCpuTimer.class});
		AbstractPlayer a = (AbstractPlayer)agentConst.newInstance(state, null);

		if(a instanceof tracks.singlePlayer.advanced.olets.Agent) {
			cleanOpenloopAgents(a);
		}

		return a;
	}

	private static void cleanOpenloopAgents(AbstractPlayer a) {
		((tracks.singlePlayer.advanced.olets.Agent)a).mctsPlayer = 
				new tracks.singlePlayer.advanced.olets.SingleMCTSPlayer(new Random(), 
						(tracks.singlePlayer.advanced.olets.Agent) a);
	}

	public void setState(StateObservation state) {
		this.state = state.copy();
	}

	public double getScore() {
		return score;
	}

	public boolean isWin() {
		return win;
	}
	
	public int getWin() {
		return win ? 1 : 0;
	}

	public int getId() {
		return id;
	}
	
	public int getSteps() {
		return steps;
	}
	
	public boolean isTimeOut() {
		return timeOut;
	}

	public char finishChar() {
		return timeOut ? 'T' : (win ? 'X' : '*');
	}
}

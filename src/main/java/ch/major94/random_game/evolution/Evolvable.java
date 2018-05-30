package ch.major94.random_game.evolution;

public interface Evolvable<T>{

	public void mutate();
	
	public T crossover(T cross);
	
	public void newInstance();
}

package ch.major94.random_game.evolution;

public enum ChromosomeType {
	LEVEL("Level"), INTERACTION("InteractionSet"), MAPPING("LevelMapping"), SPRITE("SpriteSet"), TERMINATION("TerminationSet"), NONE("none");
	
	private String name;
	
	ChromosomeType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

package ch.major94.random_game.evolution;

public class SpriteChromosome extends Chromosome<String>{
	
	private boolean hasAvatar;

	private String[] sprites = {
			"Conveyor", "Flicker", "Immovable", "OrientedFlicker", "Passive", /*"Resource",*/ "Spreader",
			/*"ErraticMissile",*/ "Missile", "RandomMissile", "Walker", "WalkerJumper",
			/*"ResourcePack",*/ "Chaser", "PathChaser", "Fleeing", "RandomInertial",
			"RandomNPC", /*"AlternateChaser", "RandomAltChaser", "PathAltChaser", "RandomPathAltChaser",*/
			"Bomber", "RandomBomber", "Portal", "SpawnPoint", "SpriteProducer", "Door",
			"MissileAvatar", "SpawnPointMultiSprite", "LOSChaser"
	};

	private String[] avatars = {
			"FlakAvatar", "HorizontalAvatar", "VerticalAvatar", "MovingAvatar","MissileAvatar",
			"OrientedAvatar","ShootAvatar", "OngoingAvatar", "OngoingTurningAvatar",
			"OngoingShootAvatar", "NullAvatar", "AimedAvatar", "PlatformerAvatar", "BirdAvatar",
			"SpaceshipAvatar", "CarAvatar", "WizardAvatar", "LanderAvatar", "ShootOnlyAvatar"
	};

	private String[] colors = {
			"GREEN", "BLUE", "RED","GRAY", "WHITE", "BROWN", "BLACK", "ORANGE", "YELLOW", "PINK", "GOLD",
			"LIGHTRED", "LIGHTORANGE", "LIGHTBLUE", "LIGHTGREEN", "LIGHTYELLOW", "LIGHTGRAY", "DARKGRAY", "DARKBLUE"
	};

	public SpriteChromosome() {
		super();

		type = ChromosomeType.SPRITE;
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void newInstance() {
		genes.clear();
		hasAvatar = false;
		// TODO Auto-generated method stub
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
	}

	@Override
	public SpriteChromosome clone() {
		SpriteChromosome c = new SpriteChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}

	@Override
	protected String newGene() {
		String[] pick = hasAvatar ? sprites : avatars;
		
		hasAvatar = true;
		
		return getRandomSprite() + " > " + pickRandom(pick) + " stype=" + getRandomSprite() + " color=" + pickRandom(colors) +
				" cooldown=" + random.nextInt(30) + " cons=" + random.nextInt(10) + " prob=0.01" +
				" speed=0.8" + optional(" singleton=True", 0.1, "");
	}
}

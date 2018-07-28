package ch.major94.random_game.evolution;

import java.util.ArrayList;

public class SpriteChromosome extends Chromosome<String>{

	private boolean hasAvatar;

	private String[] nonAvatars = {
			"Conveyor", "Flicker", "Immovable", "OrientedFlicker", "Passive", /*"Resource",*/ "Spreader",
			/*"ErraticMissile",*/ "Missile", "RandomMissile", "Walker", "WalkerJumper",
			/*"ResourcePack",*/ "Chaser", "PathChaser", "Fleeing", "RandomInertial",
			"RandomNPC", /*"AlternateChaser", "RandomAltChaser", "PathAltChaser", "RandomPathAltChaser",*/
			"Bomber", "RandomBomber", "Portal", "SpawnPoint", "SpriteProducer", "Door",
			"SpawnPointMultiSprite", "LOSChaser"
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
		replaceRandomGene(newGene());
		int i = getRandonGeneI();
		if(i == 0) {
			hasAvatar = false;
		}
		replaceGene(newGene(), i);
	}

//	@Override
//	public Chromosome<String> crossover(Chromosome<String> chromosome) {
//		int i = getRandonGeneI();
//		replaceGene(chromosome.getGene(i), i);
//		return this;
//	}

	@Override
	public void newInstance() {
		genes.clear();
		hasAvatar = false;
		
		for(int i=0; i<N_SPRITES; i++) {
			genes.add(newGene());
		}

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
	public ArrayList<String> build() {
		ArrayList<String> s = new ArrayList<String>();
		s.add("  "+type.getName());
		for(int i=0; i<N_SPRITES; i++) {
			s.add("    "+SPRITE+i+genes.get(i)+(i+1));
		}
		s.add("");
		return s;
	}

	@Override
	protected String newGene() {
		String[] pick = hasAvatar ? nonAvatars : avatars;

		hasAvatar = true;

		return " > " + pickRandom(pick) + " stype=" + getRandomSprite() + " color=" + pickRandom(colors) +
				" cooldown=" + random.nextInt(30) + " cons=" + random.nextInt(10) + " prob=0.01" +
				" speed=0.8" + " frameRate=8" + optional(" singleton=True", 0.1, "") + " randomtiling=0.9" + " img=debug/";
	}
}

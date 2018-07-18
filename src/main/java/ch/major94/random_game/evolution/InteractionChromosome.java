package ch.major94.random_game.evolution;

public class InteractionChromosome extends Chromosome<String> {

	private String[] effects = {
			"stepBack", "turnAround", "killSprite", "killBoth", "killAll", "transformTo stype=$SPRITE", /*"transformToSingleton", "transformIfCount",*/
			"wrapAround", /*"changeResource", "killIfHasLess", "killIfHasMore",*/ "cloneSprite",
			"flipDirection", "reverseDirection", /*"shieldFrom",*/ "undoAll", "spawn stype=$SPRITE", /*"spawnIfHasMore", "spawnIfHasLess",*/
			"pullWithIt", /*"wallStop", "collectResource", "collectResourceIfHeld", "killIfOtherHasMore",*/ "killIfFromAbove",
			/*"teleportToExit",*/ "bounceForward", "attractGaze", "align", /*"subtractHealthPoints", "addHealthPoints",
			"transformToAll", "addTimer",*/ "killIfFrontal", "killIfNotFrontal", "spawnBehind stype=$SPRITE",
			/*"updateSpawnType", "removeScore", "increaseSpeedToAll", "decreaseSpeedToAll", "setSpeedForAll", "transformToRandomChild",
			"addHealthPointsToMax", "spawnIfCounterSubTypes", "bounceDirection", "wallBounce", "killIfSlow", "killIfAlive",
			"waterPhysics", "halfSpeed",*/ "killIfNotUpright", /*"killIfFast", "wallReverse",*/ "spawnAbove stype=$SPRITE",
			/*"spawnLeft stype=$SPRITE", "spawnRightstype=$SPRITE", "spawnBelow stype=$SPRITE"*/
	};

	public InteractionChromosome() {
		super();

		type = ChromosomeType.INTERACTION;
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void newInstance() {
		genes.clear();
		// TODO Auto-generated method stub
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
		genes.add(newGene());
	}

	@Override
	public InteractionChromosome clone() {
		InteractionChromosome c = new InteractionChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}

	@Override
	protected String newGene() {
		String effect = pickRandom(effects).replace("$SPRITE", getRandomSprite());
		String sprite = getRandomSprite() + " " + getRandomSprite() + " > " + effect + optional(" scoreChange=1", 0.2, "");
		return sprite;
	}
}

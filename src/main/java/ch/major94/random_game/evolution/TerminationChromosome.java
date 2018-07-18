package ch.major94.random_game.evolution;

public class TerminationChromosome extends Chromosome<String> {

	private String[] templates = {
			"SpriteCounter stype=$SPRITE limit=$NUM1 win=$BOOL",
			"SpriteCounterMore stype=$SPRITE limit=$NUM1 win=$BOOL",
			"MultiSpriteCounter stype1=$SPRITE stype2=$OPSPRITE1 stype3=$OPSPRITE2 limit=$NUM1 win=$BOOL",
			//"StopCounter stype1=$SPRITE stype2=$OPSPRITE stype3=$OPSPRITE limit=$NUM win=$BOOL",
			"Timeout limit=$NUM2 win=$BOOL"
	};

	public TerminationChromosome() {
		super();

		type = ChromosomeType.TERMINATION;
	}

//	@Override
//	public void mutate() {
//		
//	}

	@Override
	public void newInstance() {
		genes.clear();
		// TODO Auto-generated method stub
		genes.add(newGene());
		genes.add(newGene());
	}

	@Override
	public TerminationChromosome clone() {
		TerminationChromosome c = new TerminationChromosome();
		for(int i=0; i<genes.size(); i++) {
			c.getGenes().add(getGene(i));
		}
		return c;
	}

	@Override
	protected String newGene() {						//#################### TODO für jedes eine Klasse erstellen? so könnte bei crossover
		String gene = pickRandom(templates);            //#################### TODO oder mutation auch einzelne parameter verändert werden

		gene = gene.replace("$SPRITE", getRandomSprite());
		gene = gene.replace("$OPSPRITE1", optional(getRandomSprite(), 0.8, "null"));
		gene = gene.replace("$OPSPRITE2", optional(getRandomSprite(), 0.8, "null"));
		gene = gene.replace("$BOOL", Boolean.toString(random.nextBoolean()));
		gene = gene.replace("$NUM1", Integer.toString(random.nextInt(10)));
		gene = gene.replace("$NUM2", Integer.toString(random.nextInt(2000)));
		
		return gene;
	}
}

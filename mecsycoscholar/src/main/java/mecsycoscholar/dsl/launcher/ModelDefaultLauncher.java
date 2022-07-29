package mecsycoscholar.dsl.launcher;

import mecsyco.description.ModelDescription;
import mecsyco.description.ModelDescriptionFactory;

/**
 * Class to launch a co-simulation of a multimodel with the default parameter
 * values.
 *
 */
public final class ModelDefaultLauncher {

	/**
	 * Path to the model/multimodel description to simulate. You can find candidates
	 * in 'Library/Basic', 'Library/NetLogo' and 'Library/Multimodel'.
	 */
	
	//private static String modelPath = "Library/Multimodel/HybridHighway.xml";
	private static String modelPath = "mm2xml/pedestrian/PedestrianExample.mm2xml";
	/**
	 * Note that no observing tools are used.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		ModelDescriptionFactory factory = new ModelDescriptionFactory();

		ModelDescription md = factory.createModelDescription(modelPath);

		md.launchDefaultSimulation();

	}

	private ModelDefaultLauncher() {

	}

}

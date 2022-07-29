package mecsycoscholar.dsl.launcher;

import mecsyco.description.ModelDescription;
import mecsyco.description.ModelDescriptionFactory;

/**
 * Class to launch a co-simulation of a multimodel with the default values of
 * parameters and a simple graphic for observation purpose.
 *
 */
public final class ModelDefaultLauncherWithObs {

	/**
	 * Path to the model/multimodel description to simulate. You can find candidates
	 * in 'Library/Basic', 'Library/NetLogo' and 'Library/Multimodel'.
	 */
	private static String modelPath = "Library/Multimodel/LorenzMultimodel.xml";

	/**
	 * Tab of the ports to observe, they must send numbers (double).
	 */
	private static String[] portsToObs = new String[] {"x", "y", "z"};

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		ModelDescriptionFactory factory = new ModelDescriptionFactory();

		ModelDescription md = factory.createModelDescription(modelPath);

		md.launchDefaultSimulationAndObs(portsToObs);

	}

	private ModelDefaultLauncherWithObs() {

	}

}

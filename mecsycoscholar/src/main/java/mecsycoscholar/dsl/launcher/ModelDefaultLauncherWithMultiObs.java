package mecsycoscholar.dsl.launcher;

import mecsyco.description.ModelDescription;
import mecsyco.description.ModelDescriptionFactory;

/**
 * Class to launch a co-simulation of a multimodel with the default values of
 * parameters and several graphics for observation purpose.
 *
 */
public final class ModelDefaultLauncherWithMultiObs {

	/**
	 * Path to the model/multimodel description to simulate. You can find candidates
	 * in 'Library/Basic', 'Library/NetLogo' and 'Library/Multimodel'.
	 */
	//private static String modelPath = "Library/Multimodel/LorenzMultimodel.xml";
	private static String modelPath = "Library/Multimodel/LorenzCoSimulation.xml";

	/**
	 * Tab of the ports to observe, they must send numbers
	 */
	private static String[][] portsToObs = new String[][] {
	};

	/**
	 * Note that here several simple graph will be displayed.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		ModelDescriptionFactory factory = new ModelDescriptionFactory();

		ModelDescription md = factory.createModelDescription(modelPath);

		md.launchDefaultSimulationAndMultiObs(portsToObs);

	}

	private ModelDefaultLauncherWithMultiObs() {

	}
}

package mecsycoscholar.dsl.launcher;

import mecsyco.description.CoSimDescription;

/**
 * Class to launch the experiment described in an xml file (generated with
 * cs2xml). The XML is generated using the MECSYCO DSL plugins, if you don't
 * have that look at
 * <a href="https://gitlab.inria.fr/Simbiot/mecsyco/mecsycodsl">MECSYCO DSL</a>.
 *
 */
public final class ModelExperimentLauncher {

	/**
	 * Path to experiment description to run.
	 */
	private static String experimentPath = "Experiments/ComparisonHouses3.xml";
	//private static String experimentPath = "Experiments/PedestrianExperiment.xml";

	/**
	 * Run experiment.
	 *
	 * @param args Useless.
	 */
	public static void main(String[] args) {
		CoSimDescription csd = new CoSimDescription(experimentPath);
		csd.launchExperiment();		
	}

	/**
	 * Prevent instantiation.
	 */
	private ModelExperimentLauncher() {

	}

}

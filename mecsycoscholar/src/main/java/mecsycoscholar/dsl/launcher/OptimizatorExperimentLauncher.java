package mecsycoscholar.dsl.launcher;

import mecsyco.description.CoSimDescription;
import mecsyco.observing.base.logging.SingleFileLoggingArtifact;
import mecsycoscholar.observing.base.logging.PostMortemCsvGraph;
import mecsycoscholar.observing.base.logging.PostMortemMultiObjectiveWeightGraph;
import mecsycoscholar.observing.base.logging.PostMortemPareto;

/**
 * Class to launch the experiment described in an xml file (generated with
 * cs2xml). The XML is generated using the MECSYCO DSL plugins, if you don't
 * have that look at
 * <a href="https://gitlab.inria.fr/Simbiot/mecsyco/mecsycodsl">MECSYCO DSL</a>.
 *
 */
public final class OptimizatorExperimentLauncher {

	/**
	 * Path to experiment description to run.
	 */
	private static String experimentPath = "Experiments/ComparisonHouses3.xml";

	/**
	 * Run experiment.
	 *
	 * @param args Useless.
	 */
	public static void main(String[] args) {
		CoSimDescription csd = new CoSimDescription(experimentPath);
		double newMaxTemp = (double) csd.getModelParameters().get("maxTemp").getValue();
		double newMinTemp = (double) csd.getModelParameters().get("minTemp").getValue();	
		int runs = 10;
		for (int i=0; i<runs; i++) {
			System.out.println("Caso "+Integer.toString(i+1) + ": MinTemp="+ Double.toString(newMinTemp) + ", MaxTemp="+ Double.toString(newMaxTemp));
			csd.getModelParameters().get("maxTemp").setValue(newMaxTemp);	
			csd.getModelParameters().get("minTemp").setValue(newMinTemp);
			csd.launchExperiment();
			csd = new CoSimDescription(experimentPath);
			newMaxTemp += 0.1;
			newMinTemp -= 0.1;	
		}
		
		String[] indicators = new String[]{"Comfort", "Consum"};
		
		PostMortemCsvGraph graphs = new PostMortemCsvGraph(runs, "C:/Users/Diego/Documents/outputsMecsyco/results", 0, indicators);
		PostMortemCsvGraph graphs1 = new PostMortemCsvGraph(runs, "C:/Users/Diego/Documents/outputsMecsyco/results", 1, indicators);
		graphs.setVisible(true);
		graphs1.setVisible(true);
		double[] weigths = new double[] {180.0,1.0};
		PostMortemMultiObjectiveWeightGraph graphs2 = new PostMortemMultiObjectiveWeightGraph(runs, "C:/Users/Diego/Documents/outputsMecsyco/results", weigths);
		graphs2.setVisible(true);
		PostMortemPareto graphs3 = new PostMortemPareto(runs, "C:/Users/Diego/Documents/outputsMecsyco/results");
		graphs3.setVisible(true);

	}

	/**
	 * Prevent instantiation.
	 */
	private OptimizatorExperimentLauncher() {

	}

}

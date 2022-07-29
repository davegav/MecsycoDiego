package mecsycoscholar.application.gettingstarted2.launcher;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.observing.base.comparator.DataComparator;
import mecsyco.observing.base.logging.EventLoggingArtifact;
import mecsyco.observing.base.logging.SingleFileLoggingArtifact;
import mecsyco.observing.base.r.RLoggingArtifact;
import mecsyco.observing.jfreechart.bar.PostMortemBarGraphic;
import mecsyco.observing.jfreechart.event.PostMortemEventGraphic;
import mecsyco.observing.jfreechart.pie.PostMortemPieGraphic;
import mecsyco.observing.jfreechart.xy.PostMortemTXGraphic;
import mecsyco.observing.jfreechart.xy.PostMortemXYGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.jzy3d.graphic.PostMortem3DGraphic;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsycoscholar.application.gettingstarted2.models.EquationModelArtifact;
import mecsycoscholar.application.gettingstarted2.models.LorenzX;
import mecsycoscholar.application.gettingstarted2.models.LorenzY;
import mecsycoscholar.application.gettingstarted2.models.LorenzZ;

/**
 * Second launcher, run a simulation with three {@link EquationModelArtifact}
 * that represent the three equations of the Lorenz system. Add charts.
 *
 */
public final class Launcher2 {

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		final double maxSimulationTime = 1;
		final double initX = 1;
		final double initY = 1;
		final double initZ = 4;
		final double a = 10.;
		final double b = 28.;
		final double c = 2.67;
		final double timeStep = 0.01;

		EventMAgent xAgent = new EventMAgent("agentX", maxSimulationTime);
		EventMAgent yAgent = new EventMAgent("agentY", maxSimulationTime);
		EventMAgent zAgent = new EventMAgent("agentZ", maxSimulationTime);

		LorenzX xSimulator = new LorenzX(initX, a, timeStep);
		LorenzY ySimulator = new LorenzY(initY, b, timeStep);
		LorenzZ zSimulator = new LorenzZ(initZ, c, timeStep);

		EquationModelArtifact xModelArtifact = new EquationModelArtifact("Xmodel", xSimulator);
		xAgent.setModelArtifact(xModelArtifact);
		EquationModelArtifact yModelArtifact = new EquationModelArtifact("Ymodel", ySimulator);
		yAgent.setModelArtifact(yModelArtifact);
		EquationModelArtifact zModelArtifact = new EquationModelArtifact("Zmodel", zSimulator);
		zAgent.setModelArtifact(zModelArtifact);

		CentralizedEventCouplingArtifact xOutputToZ = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToZ, "X");
		zAgent.addInputCouplingArtifact(xOutputToZ, "X");
		CentralizedEventCouplingArtifact xOutputToY = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToY, "X");
		yAgent.addInputCouplingArtifact(xOutputToY, "X");
		CentralizedEventCouplingArtifact yOutputToX = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToX, "Y");
		xAgent.addInputCouplingArtifact(yOutputToX, "Y");
		CentralizedEventCouplingArtifact yOutputToZ = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToZ, "Y");
		zAgent.addInputCouplingArtifact(yOutputToZ, "Y");
		CentralizedEventCouplingArtifact zOutput = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutput, "Z");
		yAgent.addInputCouplingArtifact(zOutput, "Z");

		ObservingMAgent obs = new ObservingMAgent("obs", maxSimulationTime);
		SwingDispatcherArtifact dispatcher = new SwingDispatcherArtifact();
		obs.setDispatcherArtifact(dispatcher);

		CentralizedEventCouplingArtifact xOutputToObs = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToObs, "X");
		obs.addInputCouplingArtifact(xOutputToObs, "X");
		CentralizedEventCouplingArtifact yOutputToObs = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToObs, "Y");
		obs.addInputCouplingArtifact(yOutputToObs, "Y");
		CentralizedEventCouplingArtifact zOutputToObs = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutputToObs, "Z");
		obs.addInputCouplingArtifact(zOutputToObs, "Z");
		CentralizedEventCouplingArtifact xOutputTuple2ToObs = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputTuple2ToObs, "obs2D");
		obs.addInputCouplingArtifact(xOutputTuple2ToObs, "XY");
		CentralizedEventCouplingArtifact zOutputTuple3ToObs = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutputTuple3ToObs, "obs3d");
		obs.addInputCouplingArtifact(zOutputTuple3ToObs, "XYZ");
		CentralizedEventCouplingArtifact zOutputVectorToObs = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutputVectorToObs, "obsVector");
		obs.addInputCouplingArtifact(zOutputVectorToObs, "XYZVector");

		dispatcher.addObservingArtifact(new PostMortemTXGraphic("Lorenz temporal", "Value", Renderer.Line,
				new String[] {"SeriesX", "SeriesY", "SeriesZ"}, new String[] {"X", "Y", "Z"}));

		dispatcher.addObservingArtifact(new PostMortemXYGraphic("Lorenz phase", "X", "Y", Renderer.Line, "XY", "XY"));

		dispatcher.addObservingArtifact(
				new PostMortemBarGraphic("Lorenz bar", "Bars", "Value", new String[] {"X", "Y", "Z"}, "XYZVector"));

		dispatcher.addObservingArtifact(
				new PostMortemPieGraphic("Lorenz Pie", new String[] {"X", "Y", "Z"}, "XYZVector"));

		dispatcher.addObservingArtifact(new PostMortemEventGraphic("Lorenz factual", "time", "X", "X", "X"));

		dispatcher.addObservingArtifact(new PostMortem3DGraphic("Lorenz 3D", "X", "Y", "Z", "XYZ"));

		dispatcher.addObservingArtifact(new DataComparator(new String[] {"./data_log/LorenzMFiles_X.csv",
				"./data_log/LorenzMFiles_Y.csv", "./data_log/LorenzMFiles_Z.csv"}, new String[] {"X", "Y", "Z"}));

		String path = "./data_log/";
		String rpath = System.getenv("R_HOME") + "/bin/Rscript.exe";
		String rscriptpath = "./src/main/java/mecsycoscholar/application/gettingstarted2/DemoR.R";

		dispatcher.addObservingArtifact(
				new SingleFileLoggingArtifact("Lorenz1File", new String[] {"X", "Y", "Z"}, path + "Result.csv"));

		// Launch either the log of multiple files, either the data comparator
		// Dispatcher.addObservingArtifact (new MultipleFileLoggingArtifact
		// ("LorenzMFiles", new String [] {"X", "Y", "Z"}));

		dispatcher.addObservingArtifact(new EventLoggingArtifact("LorenzEvent", new String[] {"X", "Y", "Z"}));

		dispatcher.addObservingArtifact(new RLoggingArtifact("Lorenz R Artifact", new String[] {"X", "Y", "Z"},
				path + "LorenzR.csv", rpath, rscriptpath));

		xAgent.startModelSoftware();
		yAgent.startModelSoftware();
		zAgent.startModelSoftware();
		obs.startModelSoftware();

		xAgent.setModelParameters();
		yAgent.setModelParameters();
		zAgent.setModelParameters();

		xAgent.coInitialize();
		yAgent.coInitialize();
		zAgent.coInitialize();

		xAgent.start();
		yAgent.start();
		zAgent.start();
		obs.start();

	}

	private Launcher2() {

	}

}

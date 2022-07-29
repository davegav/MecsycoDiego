package mecsycoscholar.application.gettingstarted2.launcher;

import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactReceiver;
import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactSender;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.type.Tuple2;
import mecsyco.observing.jzy3d.graphic.Live3DGraphic;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsycoscholar.application.gettingstarted2.models.EquationModelArtifact;
import mecsycoscholar.application.gettingstarted2.models.LorenzZ;

/**
 * Exemplify DDS use, run the Z part of the Lorenz system.
 *
 */
public final class LorenzZLauncher {

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		final double maxSimulationTime = 100;
		final double initZ = 4;
		final double c = 2.67;
		final double timeStep = 0.01;

		EventMAgent zAgent = new EventMAgent("agentZ", maxSimulationTime);
		LorenzZ zSimulator = new LorenzZ(initZ, c, timeStep);
		EquationModelArtifact zModelArtifact = new EquationModelArtifact("Zmodel", zSimulator);
		zAgent.setModelArtifact(zModelArtifact);

		// output
		DDSEventCouplingArtifactSender zOutput = new DDSEventCouplingArtifactSender("Z");
		zAgent.addOutputCouplingArtifact(zOutput, "Z");

		// Input
		DDSEventCouplingArtifactReceiver xOutputToZ = new DDSEventCouplingArtifactReceiver("XtoZ",
				Tuple2.of(Double.class, String.class));
		zAgent.addInputCouplingArtifact(xOutputToZ, "X");
		DDSEventCouplingArtifactReceiver yOutputToZ = new DDSEventCouplingArtifactReceiver("YtoZ",
				Tuple2.of(Double.class, String.class));
		zAgent.addInputCouplingArtifact(yOutputToZ, "Y");

		// Observing
		ObservingMAgent obs = new ObservingMAgent("obs", maxSimulationTime);
		SwingDispatcherArtifact dispatcher = new SwingDispatcherArtifact();
		obs.setDispatcherArtifact(dispatcher);

		CentralizedEventCouplingArtifact zOutputTuple3ToObs = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutputTuple3ToObs, "obs3d");
		obs.addInputCouplingArtifact(zOutputTuple3ToObs, "XYZ");

		dispatcher.addObservingArtifact(new Live3DGraphic("Lorenz 3D", "X", "Y", "Z", "XYZ"));

		zAgent.startModelSoftware();
		obs.startModelSoftware();

		zAgent.coInitialize();

		zAgent.start();
		obs.start();
	}

	private LorenzZLauncher() {

	}

}

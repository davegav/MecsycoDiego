package mecsycoscholar.application.gettingstarted2.launcher;

import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactReceiver;
import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactSender;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.type.Tuple2;
import mecsycoscholar.application.gettingstarted2.models.EquationModelArtifact;
import mecsycoscholar.application.gettingstarted2.models.LorenzY;

/**
 * Exemplify DDS use, run the Y part of the Lorenz system.
 *
 */
public final class LorenzYLauncher {

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		final double maxSimulationTime = 100;
		final double initY = 1;
		final double b = 28;
		final double timeStep = 0.01;

		EventMAgent yAgent = new EventMAgent("agentY", maxSimulationTime);
		LorenzY ySimulator = new LorenzY(initY, b, timeStep);
		EquationModelArtifact yModelArtifact = new EquationModelArtifact("Ymodel", ySimulator);
		yAgent.setModelArtifact(yModelArtifact);

		// output
		DDSEventCouplingArtifactSender yOutputToX = new DDSEventCouplingArtifactSender("YtoX");
		yAgent.addOutputCouplingArtifact(yOutputToX, "Y");
		DDSEventCouplingArtifactSender yOutputToZ = new DDSEventCouplingArtifactSender("YtoZ");
		yAgent.addOutputCouplingArtifact(yOutputToZ, "Y");

		// Input
		DDSEventCouplingArtifactReceiver xOutputToY = new DDSEventCouplingArtifactReceiver("XtoY",
				Tuple2.of(Double.class, String.class));
		yAgent.addInputCouplingArtifact(xOutputToY, "X");
		DDSEventCouplingArtifactReceiver wOutput = new DDSEventCouplingArtifactReceiver("Z",
				Tuple2.of(Double.class, String.class));
		yAgent.addInputCouplingArtifact(wOutput, "Z");

		yAgent.startModelSoftware();

		yAgent.coInitialize();

		yAgent.start();
	}

	private LorenzYLauncher() {

	}

}

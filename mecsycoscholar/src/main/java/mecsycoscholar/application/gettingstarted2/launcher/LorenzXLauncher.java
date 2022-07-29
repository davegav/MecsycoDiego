package mecsycoscholar.application.gettingstarted2.launcher;

import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactReceiver;
import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactSender;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.type.Tuple2;
import mecsycoscholar.application.gettingstarted2.models.EquationModelArtifact;
import mecsycoscholar.application.gettingstarted2.models.LorenzX;

/**
 * Exemplify DDS use, run the X part of the Lorenz system.
 *
 */
public final class LorenzXLauncher {

	/**
	 * Main method.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		final double maxSimulationTime = 100;
		final double initX = 1;
		final double a = 10;
		final double timeStep = 0.01;

		EventMAgent xAgent = new EventMAgent("agentX", maxSimulationTime);
		LorenzX xSimulator = new LorenzX(initX, a, timeStep);
		EquationModelArtifact xModelArtifact = new EquationModelArtifact("Xmodel", xSimulator);
		xAgent.setModelArtifact(xModelArtifact);

		// output
		DDSEventCouplingArtifactSender xOutputToZ = new DDSEventCouplingArtifactSender("XtoZ");
		xAgent.addOutputCouplingArtifact(xOutputToZ, "X");
		DDSEventCouplingArtifactSender xOutputToY = new DDSEventCouplingArtifactSender("XtoY");
		xAgent.addOutputCouplingArtifact(xOutputToY, "X");

		// Input
		DDSEventCouplingArtifactReceiver yOutputToX = new DDSEventCouplingArtifactReceiver("YtoX",
				Tuple2.of(Double.class, String.class));
		xAgent.addInputCouplingArtifact(yOutputToX, "Y");

		xAgent.startModelSoftware();

		xAgent.coInitialize();

		xAgent.start();
	}

	private LorenzXLauncher() {

	}

}

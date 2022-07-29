package mecsycoscholar.application.gettingstarted2.launcher;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsycoscholar.application.gettingstarted2.models.EquationModelArtifact;
import mecsycoscholar.application.gettingstarted2.models.LorenzX;
import mecsycoscholar.application.gettingstarted2.models.LorenzY;
import mecsycoscholar.application.gettingstarted2.models.LorenzZ;

/**
 * First launcher, run a simulation with three {@link EquationModelArtifact}
 * that represent the three equations of the Lorenz system. Results are just
 * printed in the console.
 *
 */
public final class Launcher1 {

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
		/*
		 * Use three different time step (just to show that synchronization still
		 * works).
		 */
		final double timeStepX = 0.01;
		final double timeStepY = 0.02;
		final double timeStepZ = 0.07;

		EventMAgent xAgent = new EventMAgent("agentX", maxSimulationTime);
		EventMAgent yAgent = new EventMAgent("agentY", maxSimulationTime);
		EventMAgent zAgent = new EventMAgent("agentZ", maxSimulationTime);

		LorenzX xSimulator = new LorenzX(initX, a, timeStepX);
		LorenzY ySimulator = new LorenzY(initY, b, timeStepY);
		LorenzZ zSimulator = new LorenzZ(initZ, c, timeStepZ);

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

		xAgent.startModelSoftware();
		yAgent.startModelSoftware();
		zAgent.startModelSoftware();

		xAgent.setModelParameters();
		yAgent.setModelParameters();
		zAgent.setModelParameters();

		xAgent.coInitialize();
		yAgent.coInitialize();
		zAgent.coInitialize();

		xAgent.start();
		yAgent.start();
		zAgent.start();

	}

	private Launcher1() {

	}

}

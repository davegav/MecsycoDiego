package mecsycoscholar.application.ode.LoktaVolterra;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JFrame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.operation.event.EventOperation;
import mecsyco.observing.base.logging.SingleFileLoggingArtifact;
import mecsyco.observing.base.logging.Utils;
import mecsyco.observing.swing.util.InitJFrame;
import mecsycoscholar.application.ode.model.NetLogoModelArtifact;
import mecsycoscholar.application.ode.operation.ToTuple1;

class TestLoktaVolterra {
	/**
	 * set the simulation time
	 */
	public static final double MAXSIMULATIONTIME = 1;

	/**
	 * set the step size of the prey model (between {0.01,0.001,0.0001})
	 */
	public static final double TIME_DISCRETIZATION_PREY = 0.01;

	/**
	 * set the step size of the prey model (between {0.01,0.001,0.0001})
	 */
	public static final double TIME_DISCRETIZATION_PREDATOR = 0.01;

	/**
	 * WAITING TIME.
	 */
	public static final long WAITING_TIME = 15000;

	/**
	 * Set the log level to error to avoid useless display.
	 */
	@BeforeAll
	static void initLogLevel() {
		// Activate logging, doesn't prevent log test success
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(Level.ERROR);
	}

	/**
	 * Set the default close operation of InitJFrame to avoid VM shutdown when they
	 * close.
	 */
	@BeforeAll
	static void initJFrameCloseOperation() {
		InitJFrame.setDefaultCloseOperationJFrame(JFrame.DISPOSE_ON_CLOSE);
	}

	@Test
	void testLoktaVolterraLauncher() {
		// create the model agent for the prey model
		EventMAgent preyAgent = new EventMAgent("agent_prey", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact preyModelArtifact = new NetLogoModelArtifact("My Models/netlogo/prey.nlogo", "Prey Model");
		preyModelArtifact.setAutoGuiStop(true);
		preyAgent.setModelArtifact(preyModelArtifact);

		// create the model agent for the predator model
		EventMAgent predatorAgent = new EventMAgent("agent_predator", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact predatorModelArtifact = new NetLogoModelArtifact("My Models/netlogo/predator.nlogo",
				"Predator Model");
		predatorModelArtifact.setAutoGuiStop(true);
		predatorAgent.setModelArtifact(predatorModelArtifact);

		// create the coupling artifacts
		CentralizedEventCouplingArtifact couplingFromPredatorToPrey = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPreyToPredator = new CentralizedEventCouplingArtifact();

		// link the coupling artifacts with the m-agents
		preyAgent.addInputCouplingArtifact(couplingFromPredatorToPrey, "Y");
		preyAgent.addOutputCouplingArtifact(couplingFromPreyToPredator, "X");

		predatorAgent.addOutputCouplingArtifact(couplingFromPredatorToPrey, "Y");
		predatorAgent.addInputCouplingArtifact(couplingFromPreyToPredator, "X");

		// initialize the model
		preyAgent.startModelSoftware();
		predatorAgent.startModelSoftware();

		// set model parameters
		String[] argPrey = {String.valueOf(TIME_DISCRETIZATION_PREY)};
		String[] argPredator = {String.valueOf(TIME_DISCRETIZATION_PREDATOR)};
		preyAgent.setModelParameters(argPrey);
		predatorAgent.setModelParameters(argPredator);

		predatorAgent.coInitialize();
		preyAgent.coInitialize();

		preyAgent.start();
		predatorAgent.start();
		try {
			// Wait for the simulation to end.
			preyAgent.join(WAITING_TIME);
			predatorAgent.join(WAITING_TIME);
			if (preyAgent.isAlive() || predatorAgent.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}
	}

	@Test
	void testLoktaVolterraLauncherCompare() {
		// create the model agent for the prey model
		EventMAgent preyAgent = new EventMAgent("agent_prey", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact preyModelArtifact = new NetLogoModelArtifact("My Models/netlogo/prey.nlogo", "Prey Model");
		preyModelArtifact.setAutoGuiStop(true);
		preyAgent.setModelArtifact(preyModelArtifact);

		// create the model agent for the predator model
		EventMAgent predatorAgent = new EventMAgent("agent_predator", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact predatorModelArtifact = new NetLogoModelArtifact("My Models/netlogo/predator.nlogo",
				"Predator Model");
		predatorModelArtifact.setAutoGuiStop(true);
		predatorAgent.setModelArtifact(predatorModelArtifact);

		// Agent d'observation
		ObservingMAgent obs = new ObservingMAgent("Obs", MAXSIMULATIONTIME);
		final String refFile = "./src/test/resources/refLotkaVolterra.csv";
		final String pathFile = "./src/test/resources/LotkaVolterra.csv";
		SingleFileLoggingArtifact logArtifact = new SingleFileLoggingArtifact("Log", new String[] {"x", "y"},
				pathFile);
		obs.setDispatcherArtifact(logArtifact);

		// create the coupling artifacts
		CentralizedEventCouplingArtifact couplingFromPredatorToPrey = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPreyToPredator = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPredatorToObs = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingFromPreyToObs = new CentralizedEventCouplingArtifact();

		// link the coupling artifacts with the m-agents
		preyAgent.addInputCouplingArtifact(couplingFromPredatorToPrey, "Y");
		preyAgent.addOutputCouplingArtifact(couplingFromPreyToPredator, "X");
		preyAgent.addOutputCouplingArtifact(couplingFromPreyToObs, "X");

		predatorAgent.addOutputCouplingArtifact(couplingFromPredatorToPrey, "Y");
		predatorAgent.addInputCouplingArtifact(couplingFromPreyToPredator, "X");
		predatorAgent.addOutputCouplingArtifact(couplingFromPredatorToObs, "Y");

		EventOperation op1 = new ToTuple1();
		couplingFromPreyToObs.addEventOperation(op1);
		EventOperation op2 = new ToTuple1();
		couplingFromPredatorToObs.addEventOperation(op2);
		obs.addInputCouplingArtifact(couplingFromPreyToObs, "x");
		obs.addInputCouplingArtifact(couplingFromPredatorToObs, "y");

		// initialize the model
		preyAgent.startModelSoftware();
		predatorAgent.startModelSoftware();
		obs.startModelSoftware();

		// set model parameters
		String[] argPrey = {String.valueOf(TIME_DISCRETIZATION_PREY)};
		String[] argPredator = {String.valueOf(TIME_DISCRETIZATION_PREDATOR)};
		preyAgent.setModelParameters(argPrey);
		predatorAgent.setModelParameters(argPredator);

		predatorAgent.coInitialize();
		preyAgent.coInitialize();

		obs.start();
		preyAgent.start();
		predatorAgent.start();
		try {
			// Wait for the simulation to end.
			preyAgent.join(WAITING_TIME);
			predatorAgent.join(WAITING_TIME);
			obs.join(WAITING_TIME);

			assertTrue(Utils.compare(refFile, pathFile));

		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}

	}

}

package mecsycoscholar.application.ode.lorenz;

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

class TestLorenz {
	/**
	 * set the global simulation time
	 */
	public static final double MAXSIMULATIONTIME = 0.1;

	/**
	 * set the step size of the X model (between {0.01,0.001,0.0001})
	 */
	public static final double TIME_DISCRETIZATION_X = 0.01;

	/**
	 * set the step size of the Y model (between {0.01,0.001,0.0001})
	 */
	public static final double TIME_DISCRETIZATION_Y = 0.01;

	/**
	 * set the step size of the Z model (between {0.01,0.001,0.0001})
	 */
	public static final double TIME_DISCRETIZATION_Z = 0.01;
	/**
	 * WAITING TIME.
	 */
	public static final long WAITING_TIME = 5000;

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
	void testCentralizedLorenzLaunch() {
		// create the model agent for the X model
		EventMAgent xAgent = new EventMAgent("agent_x", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact xModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-X.nlogo",
				"Evolution of the intensity of the convection motion");
		xModelArtifact.setAutoGuiStop(true);
		xAgent.setModelArtifact(xModelArtifact);

		// create the model agent for the Y model
		EventMAgent yAgent = new EventMAgent("agent_y", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact yModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-Y.nlogo",
				"Evolution of the temperature difference between the ascending and descending currents");
		yModelArtifact.setAutoGuiStop(true);
		yAgent.setModelArtifact(yModelArtifact);

		// create the model agent for the Z model
		EventMAgent zAgent = new EventMAgent("agent_z", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact zModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-Z.nlogo",
				"Evolution of the distortion of the vertical temperature from linearity");
		zModelArtifact.setAutoGuiStop(true);
		zAgent.setModelArtifact(zModelArtifact);

		// create the coupling artifacts
		CentralizedEventCouplingArtifact xOutputToZ = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact xOutputToY = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact yOutputToX = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact yOutputToZ = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact zOutput = new CentralizedEventCouplingArtifact();

		// set the output link
		xAgent.addOutputCouplingArtifact(xOutputToZ, "X");
		xAgent.addOutputCouplingArtifact(xOutputToY, "X");

		yAgent.addOutputCouplingArtifact(yOutputToX, "Y");
		yAgent.addOutputCouplingArtifact(yOutputToZ, "Y");

		zAgent.addOutputCouplingArtifact(zOutput, "Z");

		// set the input link
		xAgent.addInputCouplingArtifact(yOutputToX, "Y");

		yAgent.addInputCouplingArtifact(xOutputToY, "X");
		yAgent.addInputCouplingArtifact(zOutput, "Z");

		zAgent.addInputCouplingArtifact(xOutputToZ, "X");
		zAgent.addInputCouplingArtifact(yOutputToZ, "Y");

		// initialize the model
		xAgent.startModelSoftware();
		yAgent.startModelSoftware();
		zAgent.startModelSoftware();

		// set model parameters
		String[] argX = {String.valueOf(TIME_DISCRETIZATION_X)};
		String[] argY = {String.valueOf(TIME_DISCRETIZATION_Y)};
		String[] argZ = {String.valueOf(TIME_DISCRETIZATION_Z)};
		xAgent.setModelParameters(argX);
		zAgent.setModelParameters(argZ);
		yAgent.setModelParameters(argY);

		// send the initial value of each models
		xAgent.coInitialize();
		yAgent.coInitialize();
		zAgent.coInitialize();

		xAgent.start();
		yAgent.start();
		zAgent.start();

		try {
			// Wait for the simulation to end.
			xAgent.join(WAITING_TIME);
			yAgent.join(WAITING_TIME);
			zAgent.join(WAITING_TIME);
			if (xAgent.isAlive() || yAgent.isAlive() || zAgent.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}

	}

	@Test
	void testCentralizedLorenzCompare() {
		// create the model agent for the X model
		EventMAgent xAgent = new EventMAgent("agent_x", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact xModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-X.nlogo",
				"Evolution of the intensity of the convection motion");
		xModelArtifact.setAutoGuiStop(true);
		xAgent.setModelArtifact(xModelArtifact);

		// create the model agent for the Y model
		EventMAgent yAgent = new EventMAgent("agent_y", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact yModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-Y.nlogo",
				"Evolution of the temperature difference between the ascending and descending currents");
		yModelArtifact.setAutoGuiStop(true);
		yAgent.setModelArtifact(yModelArtifact);

		// create the model agent for the Z model
		EventMAgent zAgent = new EventMAgent("agent_z", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact zModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-Z.nlogo",
				"Evolution of the distortion of the vertical temperature from linearity");
		zModelArtifact.setAutoGuiStop(true);
		zAgent.setModelArtifact(zModelArtifact);

		// Agent d'observation
		ObservingMAgent obs = new ObservingMAgent("Obs", MAXSIMULATIONTIME);
		final String refFile = "./src/test/resources/refLorenz.csv";
		final String pathFile = "./src/test/resources/Lorenz.csv";
		SingleFileLoggingArtifact logArtifact = new SingleFileLoggingArtifact("Log", new String[] {"X", "Y", "Z"},
				pathFile);
		obs.setDispatcherArtifact(logArtifact);

		// create the coupling artifacts
		CentralizedEventCouplingArtifact xOutputToZ = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact xOutputToY = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact yOutputToX = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact yOutputToZ = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact zOutput = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact xOutputToObs = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact yOutputToObs = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact zOutputToObs = new CentralizedEventCouplingArtifact();

		// set the output link
		xAgent.addOutputCouplingArtifact(xOutputToZ, "X");
		xAgent.addOutputCouplingArtifact(xOutputToY, "X");
		xAgent.addOutputCouplingArtifact(xOutputToObs, "X");

		yAgent.addOutputCouplingArtifact(yOutputToX, "Y");
		yAgent.addOutputCouplingArtifact(yOutputToZ, "Y");
		yAgent.addOutputCouplingArtifact(yOutputToObs, "Y");

		zAgent.addOutputCouplingArtifact(zOutput, "Z");
		zAgent.addOutputCouplingArtifact(zOutputToObs, "Z");

		// set the input link
		xAgent.addInputCouplingArtifact(yOutputToX, "Y");

		yAgent.addInputCouplingArtifact(xOutputToY, "X");
		yAgent.addInputCouplingArtifact(zOutput, "Z");

		zAgent.addInputCouplingArtifact(xOutputToZ, "X");
		zAgent.addInputCouplingArtifact(yOutputToZ, "Y");

		EventOperation op1 = new ToTuple1();
		xOutputToObs.addEventOperation(op1);
		EventOperation op2 = new ToTuple1();
		yOutputToObs.addEventOperation(op2);
		EventOperation op3 = new ToTuple1();
		zOutputToObs.addEventOperation(op3);
		obs.addInputCouplingArtifact(xOutputToObs, "X");
		obs.addInputCouplingArtifact(yOutputToObs, "Y");
		obs.addInputCouplingArtifact(zOutputToObs, "Z");

		// initialize the model
		xAgent.startModelSoftware();
		yAgent.startModelSoftware();
		zAgent.startModelSoftware();
		obs.startModelSoftware();

		// set model parameters
		String[] argX = {String.valueOf(TIME_DISCRETIZATION_X)};
		String[] argY = {String.valueOf(TIME_DISCRETIZATION_Y)};
		String[] argZ = {String.valueOf(TIME_DISCRETIZATION_Z)};
		xAgent.setModelParameters(argX);
		zAgent.setModelParameters(argZ);
		yAgent.setModelParameters(argY);

		// send the initial value of each models
		xAgent.coInitialize();
		yAgent.coInitialize();
		zAgent.coInitialize();

		obs.start();
		xAgent.start();
		yAgent.start();
		zAgent.start();

		try {
			// Wait for the simulation to end.
			xAgent.join(WAITING_TIME);
			yAgent.join(WAITING_TIME);
			zAgent.join(WAITING_TIME);
			obs.join(WAITING_TIME);
			if (xAgent.isAlive() || yAgent.isAlive() || obs.isAlive() || zAgent.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}
			assertTrue(Utils.compare(refFile, pathFile));

		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}

	}

}

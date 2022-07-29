package mecsycoscholar.application.trafficgrid;

import static org.junit.Assert.fail;

import javax.swing.JFrame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.observing.swing.util.InitJFrame;
import mecsycoscholar.application.trafficgrid.model.GridModelArtifact;

class TestTrafficGrid {
	// Model settings
	/**
	 * Maximum simulation time.
	 */
	public static final double MAXSIMULATIONTIME = 10;
	/**
	 * WAITING TIME.
	 */
	public static final long WAITING_TIME = 10000;

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
	void testTrafficGridLauncher() {
		EventMAgent gridAgent1 = new EventMAgent("agent_grid_1", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		GridModelArtifact microModelArt1 = new GridModelArtifact("My Models/netlogo/Traffic-Grid.nlogo", "Grid 1");
		microModelArt1.setAutoGuiStop(true);
		gridAgent1.setModelArtifact(microModelArt1);

		// create the model agent for the X model
		EventMAgent gridAgent2 = new EventMAgent("agent_grid_2", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		GridModelArtifact microModelArt2 = new GridModelArtifact("My Models/netlogo/Traffic-Grid.nlogo", "Grid 2");
		microModelArt2.setAutoGuiStop(true);
		gridAgent2.setModelArtifact(microModelArt2);

		// create the model agent for the X model
		EventMAgent gridAgent3 = new EventMAgent("agent_grid_3", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		GridModelArtifact microModelArt3 = new GridModelArtifact("My Models/netlogo/Traffic-Grid.nlogo", "Grid 3");
		microModelArt3.setAutoGuiStop(true);
		gridAgent3.setModelArtifact(microModelArt3);

		// create the model agent for the X model
		EventMAgent gridAgent4 = new EventMAgent("agent_grid_4", MAXSIMULATIONTIME);
		// create the model artifact and link it with the agent
		GridModelArtifact microModelArt4 = new GridModelArtifact("My Models/netlogo/Traffic-Grid.nlogo", "Grid 4");
		microModelArt4.setAutoGuiStop(true);
		gridAgent4.setModelArtifact(microModelArt4);

		CentralizedEventCouplingArtifact artFromR1ToL2 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFromB1ToU4 = new CentralizedEventCouplingArtifact();

		CentralizedEventCouplingArtifact artFromR2ToL1 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFromB2ToU3 = new CentralizedEventCouplingArtifact();

		CentralizedEventCouplingArtifact artFromR3ToL4 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFromB3ToU2 = new CentralizedEventCouplingArtifact();

		CentralizedEventCouplingArtifact artFromR4ToL3 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact artFromB4ToU1 = new CentralizedEventCouplingArtifact();

		// set GridAgent 1 output coupling artifacts
		gridAgent1.addOutputCouplingArtifact(artFromR1ToL2, "right_out");
		gridAgent2.addInputCouplingArtifact(artFromR1ToL2, "left_in");

		gridAgent1.addOutputCouplingArtifact(artFromB1ToU4, "down_out");
		gridAgent4.addInputCouplingArtifact(artFromB1ToU4, "up_in");

		// Set GridAgent 2 output coupling artifacts
		gridAgent2.addOutputCouplingArtifact(artFromR2ToL1, "right_out");
		gridAgent1.addInputCouplingArtifact(artFromR2ToL1, "left_in");

		gridAgent2.addOutputCouplingArtifact(artFromB2ToU3, "down_out");
		gridAgent3.addInputCouplingArtifact(artFromB2ToU3, "up_in");

		// Set GridAgent 3 output coupling artifacts
		gridAgent3.addOutputCouplingArtifact(artFromR3ToL4, "right_out");
		gridAgent4.addInputCouplingArtifact(artFromR3ToL4, "left_in");

		gridAgent3.addOutputCouplingArtifact(artFromB3ToU2, "down_out");
		gridAgent2.addInputCouplingArtifact(artFromB3ToU2, "up_in");

		// Set GridAgent 4 output coupling artifacts
		gridAgent4.addOutputCouplingArtifact(artFromR4ToL3, "right_out");
		gridAgent3.addInputCouplingArtifact(artFromR4ToL3, "left_in");

		gridAgent4.addOutputCouplingArtifact(artFromB4ToU1, "down_out");
		gridAgent1.addInputCouplingArtifact(artFromB4ToU1, "up_in");

		gridAgent1.startModelSoftware();
		gridAgent2.startModelSoftware();
		gridAgent4.startModelSoftware();
		gridAgent3.startModelSoftware();

		gridAgent1.setModelParameters();
		gridAgent2.setModelParameters();
		gridAgent4.setModelParameters();
		gridAgent3.setModelParameters();

		gridAgent1.start();
		gridAgent2.start();
		gridAgent3.start();
		gridAgent4.start();
		try {
			// Wait for the simulation to end.
			gridAgent1.join(WAITING_TIME);
			gridAgent2.join(WAITING_TIME);
			gridAgent3.join(WAITING_TIME);
			gridAgent4.join(WAITING_TIME);
			if (gridAgent1.isAlive() || gridAgent2.isAlive() || gridAgent3.isAlive() || gridAgent4.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}
	}
}

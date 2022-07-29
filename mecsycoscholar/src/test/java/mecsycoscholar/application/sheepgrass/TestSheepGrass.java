package mecsycoscholar.application.sheepgrass;

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
import mecsycoscholar.application.sheepgrass.model.GrassModelArtifact;
import mecsycoscholar.application.sheepgrass.model.SheepModelArtifact;

class TestSheepGrass {
	/**
	 * WAITING TIME.
	 */
	public static final long WAITING_TIME = 10000;
	// Display settings
	/**
	 * First world name.
	 */
	public static final String FIRSTWORLDNAME = "grass-world";

	/**
	 * Second world name.
	 */
	public static final String SECONDWORLDNAME = "sheep-world";

	/**
	 * Frame width.
	 */
	public static final int WIDTH = 650;

	/**
	 * Frame height.
	 */
	public static final int HEIGHT = 690;

	// Model settings
	/**
	 * Maximum simulation time.
	 */
	public static final double MAXSIMULATIONTIME = 5;

	/**
	 * Path to the NetLogo model.
	 */
	public static final String FIRSTMODELPATH = "My Models/netlogo/sheep_grass_weeds.nlogo";

	/**
	 * Path to the NetLogo model.
	 */
	public static final String SECONDMODELPATH = "My Models/netlogo/sheep_shepherds.nlogo";

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
	void testSheepGrassLauncher() {
		EventMAgent agent1 = new EventMAgent("agent_grass", MAXSIMULATIONTIME);
		GrassModelArtifact model1 = new GrassModelArtifact(FIRSTMODELPATH, FIRSTWORLDNAME, WIDTH, HEIGHT);
		model1.setAutoGuiStop(true);
		agent1.setModelArtifact(model1);

		EventMAgent agent2 = new EventMAgent("agent_sheeps", MAXSIMULATIONTIME);
		SheepModelArtifact model2 = new SheepModelArtifact(SECONDMODELPATH, SECONDWORLDNAME, WIDTH, HEIGHT);
		model2.setAutoGuiStop(true);
		agent2.setModelArtifact(model2);

		CentralizedEventCouplingArtifact connection1 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact connection2 = new CentralizedEventCouplingArtifact();
		agent1.addOutputCouplingArtifact(connection1, "out");
		agent2.addInputCouplingArtifact(connection1, "in");
		agent2.addOutputCouplingArtifact(connection2, "out");
		agent1.addInputCouplingArtifact(connection2, "in");

		agent1.startModelSoftware();
		agent2.startModelSoftware();

		agent1.setModelParameters();
		agent2.setModelParameters();

		agent1.start();
		agent2.start();

		try {
			agent1.join(WAITING_TIME);
			agent2.join(WAITING_TIME);
			if (agent1.isAlive() || agent2.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}
	}

}

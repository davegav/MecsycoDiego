package mecsycoscholar.application.pedestrian;

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
import mecsyco.world.netlogo.type.NetLogoColorConstants;
import mecsycoscholar.application.pedestrian.model.NetLogoTurtlePedestrianShape;
import mecsycoscholar.application.pedestrian.model.PedestrianModelArtifact;

class TestPedestrian {
	// Display Settings
	/**
	 * First world name.
	 */
	public static final String FIRSTWORLDNAME = "World 1";

	/**
	 * Second world name.
	 */
	public static final String SECONDWORLDNAME = "World 2";

	/**
	 * Third world name.
	 */
	public static final String THIRDWORLDNAME = "World 3";

	// Model settings
	/**
	 * Maximum simulation time.
	 */
	public static final double MAXSIMULATIONTIME = 5;
	/**
	 * WAITING TIME.
	 */
	public static final long WAITING_TIME = 2000;

	/**
	 * Path to the first NetLogo model.
	 */
	public static final String FIRSTMODELPATH = "My Models/netlogo/pedestrian.nlogo";

	/**
	 * Path to the second NetLogo model.
	 */
	public static final String SECONDMODELPATH = "My Models/netlogo/pedestrian.nlogo";

	/**
	 * Path to the third NetLogo model.
	 */
	public static final String THIRDMODELPATH = "My Models/netlogo/pedestrian.nlogo";

	/**
	 * Set the log level to error to avoid useless display.
	 */
	@BeforeAll
	static void initLogLevel() {
		// Activate logging, doesn't prevent log test success.
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

	/**
	 * Test that the pedestrian simulation run without errors on few steps, and is
	 * not blocked (test that the simulation is ended after a while).
	 */
	@Test
	void testPedestrainLauncher() {
		EventMAgent agent1 = new EventMAgent("agent_1", MAXSIMULATIONTIME);
		PedestrianModelArtifact modelArtefact1 = new PedestrianModelArtifact(FIRSTMODELPATH, FIRSTWORLDNAME);
		modelArtefact1.setAutoGuiStop(true);
		agent1.setModelArtifact(modelArtefact1);
		CentralizedEventCouplingArtifact couplingArtifact1 = new CentralizedEventCouplingArtifact();

		EventMAgent agent2 = new EventMAgent("agent_2", MAXSIMULATIONTIME);
		PedestrianModelArtifact modelArtefact2 = new PedestrianModelArtifact(SECONDMODELPATH, SECONDWORLDNAME,
				NetLogoColorConstants.BLUE, NetLogoTurtlePedestrianShape.butterfly);
		modelArtefact2.setAutoGuiStop(true);
		agent2.setModelArtifact(modelArtefact2);
		CentralizedEventCouplingArtifact couplingArtifact2 = new CentralizedEventCouplingArtifact();

		EventMAgent agent3 = new EventMAgent("agent_3", MAXSIMULATIONTIME);
		PedestrianModelArtifact modelArtefact3 = new PedestrianModelArtifact(THIRDMODELPATH, THIRDWORLDNAME,
				NetLogoColorConstants.YELLOW, NetLogoTurtlePedestrianShape.turtle);
		modelArtefact3.setAutoGuiStop(true);
		agent3.setModelArtifact(modelArtefact3);
		CentralizedEventCouplingArtifact couplingArtifact3 = new CentralizedEventCouplingArtifact();

		agent1.addInputCouplingArtifact(couplingArtifact3, "left_in");
		agent1.addOutputCouplingArtifact(couplingArtifact1, "right_out");
		agent2.addInputCouplingArtifact(couplingArtifact1, "left_in");
		agent2.addOutputCouplingArtifact(couplingArtifact2, "right_out");
		agent3.addInputCouplingArtifact(couplingArtifact2, "left_in");
		agent3.addOutputCouplingArtifact(couplingArtifact3, "right_out");

		agent1.startModelSoftware();
		agent2.startModelSoftware();
		agent3.startModelSoftware();

		agent1.setModelParameters();
		agent2.setModelParameters();
		agent3.setModelParameters();

		agent1.start();
		agent2.start();
		agent3.start();
		try {
			// Wait for the simulation to end.
			agent1.join(WAITING_TIME);
			agent2.join(WAITING_TIME);
			agent3.join(WAITING_TIME);
			if (agent1.isAlive() || agent2.isAlive() || agent3.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}
	}

	/**
	 * Test that the pedestrian simulation run without errors on few steps, and that
	 * if the windows is not automatically closed at the end of the simulation, we
	 * detect this mistake.
	 */
	@Test
	void testPedestrainLauncherIsStillAlive() {
		EventMAgent agent1 = new EventMAgent("agent_1", MAXSIMULATIONTIME);
		PedestrianModelArtifact modelArtefact1 = new PedestrianModelArtifact(FIRSTMODELPATH, FIRSTWORLDNAME);
		agent1.setModelArtifact(modelArtefact1);
		CentralizedEventCouplingArtifact couplingArtifact1 = new CentralizedEventCouplingArtifact();

		EventMAgent agent2 = new EventMAgent("agent_2", MAXSIMULATIONTIME);
		PedestrianModelArtifact modelArtefact2 = new PedestrianModelArtifact(SECONDMODELPATH, SECONDWORLDNAME,
				NetLogoColorConstants.BLUE, NetLogoTurtlePedestrianShape.butterfly);
		agent2.setModelArtifact(modelArtefact2);
		CentralizedEventCouplingArtifact couplingArtifact2 = new CentralizedEventCouplingArtifact();

		EventMAgent agent3 = new EventMAgent("agent_3", MAXSIMULATIONTIME);
		PedestrianModelArtifact modelArtefact3 = new PedestrianModelArtifact(THIRDMODELPATH, THIRDWORLDNAME,
				NetLogoColorConstants.YELLOW, NetLogoTurtlePedestrianShape.turtle);
		agent3.setModelArtifact(modelArtefact3);
		CentralizedEventCouplingArtifact couplingArtifact3 = new CentralizedEventCouplingArtifact();

		agent1.addInputCouplingArtifact(couplingArtifact3, "left_in");
		agent1.addOutputCouplingArtifact(couplingArtifact1, "right_out");
		agent2.addInputCouplingArtifact(couplingArtifact1, "left_in");
		agent2.addOutputCouplingArtifact(couplingArtifact2, "right_out");
		agent3.addInputCouplingArtifact(couplingArtifact2, "left_in");
		agent3.addOutputCouplingArtifact(couplingArtifact3, "right_out");

		agent1.startModelSoftware();
		agent2.startModelSoftware();
		agent3.startModelSoftware();

		agent1.setModelParameters();
		agent2.setModelParameters();
		agent3.setModelParameters();

		agent1.start();
		agent2.start();
		agent3.start();
		try {
			// Wait for the simulation to end.
			agent1.join(WAITING_TIME);
			agent2.join(WAITING_TIME);
			agent3.join(WAITING_TIME);
			if (agent1.isAlive() || agent2.isAlive() || agent3.isAlive()) {
				// Check that the default behavior doesn't close the windows.
				modelArtefact1.closeWindows();
				modelArtefact2.closeWindows();
				modelArtefact3.closeWindows();
			} else {
				// The test fail if the threads are dead while the default behavio is to keep
				// them alive for their GUI.
				fail("After " + WAITING_TIME + "ms, the threads should not have been stopped in this configuration!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}
	}

}

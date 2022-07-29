package mecsycoscholar.application.gettingstarted;

import static org.junit.Assert.fail;

import javax.swing.JFrame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.operation.event.EventOperation;
import mecsyco.observing.swing.util.InitJFrame;
import mecsyco.world.netlogo.type.NetLogoColorConstants;
import mecsycoscholar.application.gettingstarted.model.SimpleRandomWalkModelArtifact;
import mecsycoscholar.application.gettingstarted.operation.TurtleColorSetter;
import mecsycoscholar.application.gettingstarted.operation.TurtleSizeSetter;

public class TestGettingStartedLaunchers {

	/**
	 * SImulation name.
	 */
	public static final String WORLD_NAME = "world";

	/**
	 * Frame width.
	 */
	public static final int WIDTH = 460;

	/**
	 * Frame height.
	 */
	public static final int HEIGHT = 520;

	/**
	 * Turtles color
	 */
	public static final double SIZE = 2;
	/**
	 * Turtles Size
	 */
	public static final double SIZE_TURTLE = 8.;

	// Model settings
	/**
	 * Maximum simulation time.
	 */
	public static final double MAX_SIMULATION_TIME = 10;

	/**
	 * Path to the NetLogo model.
	 */
	public static final String MODEL_PATH = "My Models/netlogo/random_walk.nlogo";

	/**
	 * Turtles color.
	 */
	public static final double GREEN = 55;

	/**
	 * Turtles color.
	 */
	public static final double RED = 15;

	/**
	 * Simulations shouldn't last more than 10 seconds.
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

	/**
	 * Test the minimal example.
	 */
	@Test
	public void testMinimalExample() {
		EventMAgent agent1 = new EventMAgent(WORLD_NAME + 1, MAX_SIMULATION_TIME);

		SimpleRandomWalkModelArtifact modelArtifact = new SimpleRandomWalkModelArtifact(MODEL_PATH, WORLD_NAME + 1,
				WIDTH, HEIGHT, SIZE, GREEN);
		modelArtifact.setAutoGuiStop(true);
		agent1.setModelArtifact(modelArtifact);

		// Start NetLogo
		agent1.startModelSoftware();

		// Model initialization
		agent1.setModelParameters();

		// Start the simulation
		agent1.start();

		try {
			agent1.join(WAITING_TIME);
			if (agent1.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}

	}

	/**
	 * Test the interacting example.
	 */
	@Test
	public void testInteractingExample() {
		// Model 1
		EventMAgent agent1 = new EventMAgent(WORLD_NAME + 1, MAX_SIMULATION_TIME);
		SimpleRandomWalkModelArtifact modelArtifact1 = new SimpleRandomWalkModelArtifact(MODEL_PATH, WORLD_NAME + 1,
				WIDTH, HEIGHT, SIZE, GREEN);
		modelArtifact1.setAutoGuiStop(true);
		agent1.setModelArtifact(modelArtifact1);
		CentralizedEventCouplingArtifact couplingArtifact1 = new CentralizedEventCouplingArtifact();

		// Model 2
		EventMAgent agent2 = new EventMAgent(WORLD_NAME + 2, MAX_SIMULATION_TIME);
		SimpleRandomWalkModelArtifact modelArtifact2 = new SimpleRandomWalkModelArtifact(MODEL_PATH, WORLD_NAME + 2,
				WIDTH, HEIGHT, SIZE, RED);
		modelArtifact2.setAutoGuiStop(true);
		agent2.setModelArtifact(modelArtifact2);
		CentralizedEventCouplingArtifact couplingArtifact2 = new CentralizedEventCouplingArtifact();

		// Connections
		agent1.addInputCouplingArtifact(couplingArtifact1, "in");
		agent1.addOutputCouplingArtifact(couplingArtifact2, "out");
		agent2.addInputCouplingArtifact(couplingArtifact2, "in");
		agent2.addOutputCouplingArtifact(couplingArtifact1, "out");

		// Start NetLogo
		agent1.startModelSoftware();
		agent2.startModelSoftware();

		// Model initializations
		agent1.setModelParameters();
		agent2.setModelParameters();

		// Simulation launching
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

	/**
	 * Test the Discovering example.
	 */
	@Test
	public void testDiscoveringExample() {
		// Model 1
		EventMAgent agent1 = new EventMAgent(WORLD_NAME + 1, MAX_SIMULATION_TIME);
		SimpleRandomWalkModelArtifact modelArtifact1 = new SimpleRandomWalkModelArtifact(MODEL_PATH, WORLD_NAME + 1,
				WIDTH, HEIGHT, SIZE, GREEN);
		modelArtifact1.setAutoGuiStop(true);
		agent1.setModelArtifact(modelArtifact1);
		CentralizedEventCouplingArtifact couplingArtifact1 = new CentralizedEventCouplingArtifact();

		// Model 2
		EventMAgent agent2 = new EventMAgent(WORLD_NAME + 2, MAX_SIMULATION_TIME);
		SimpleRandomWalkModelArtifact modelArtifact2 = new SimpleRandomWalkModelArtifact(MODEL_PATH, WORLD_NAME + 2,
				WIDTH, HEIGHT, SIZE, RED);
		modelArtifact2.setAutoGuiStop(true);
		agent2.setModelArtifact(modelArtifact2);
		CentralizedEventCouplingArtifact couplingArtifact2 = new CentralizedEventCouplingArtifact();

		EventOperation colorSetter = new TurtleColorSetter((double) NetLogoColorConstants.BLUE);
		couplingArtifact1.addEventOperation(colorSetter);

		// Connections
		agent1.addInputCouplingArtifact(couplingArtifact1, "in");
		agent1.addOutputCouplingArtifact(couplingArtifact2, "out");
		agent2.addInputCouplingArtifact(couplingArtifact2, "in");
		agent2.addOutputCouplingArtifact(couplingArtifact1, "out");

		// Start NetLogo
		agent1.startModelSoftware();
		agent2.startModelSoftware();

		// Model initializations
		agent1.setModelParameters();
		agent2.setModelParameters();

		// Simulation launching
		agent1.start();
		agent2.start();
		try {
			// Wait for the simulation to end.
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

	/**
	 * Test the Operation example.
	 */
	@Test
	public void testOperationExample() {
		// Model 1
		EventMAgent agent1 = new EventMAgent(WORLD_NAME + 1, MAX_SIMULATION_TIME);
		SimpleRandomWalkModelArtifact modelArtifact1 = new SimpleRandomWalkModelArtifact(MODEL_PATH, WORLD_NAME + 1,
				WIDTH, HEIGHT, SIZE, GREEN);
		agent1.setModelArtifact(modelArtifact1);
		modelArtifact1.setAutoGuiStop(true);
		CentralizedEventCouplingArtifact couplingArtifact1 = new CentralizedEventCouplingArtifact();

		// Model 2
		EventMAgent agent2 = new EventMAgent(WORLD_NAME + 2, MAX_SIMULATION_TIME);
		SimpleRandomWalkModelArtifact modelArtifact2 = new SimpleRandomWalkModelArtifact(MODEL_PATH, WORLD_NAME + 2,
				WIDTH, HEIGHT, SIZE, RED);
		modelArtifact2.setAutoGuiStop(true);
		agent2.setModelArtifact(modelArtifact2);
		CentralizedEventCouplingArtifact couplingArtifact2 = new CentralizedEventCouplingArtifact();

		EventOperation colorSetter = new TurtleColorSetter((double) NetLogoColorConstants.BLUE);
		couplingArtifact1.addEventOperation(colorSetter);

		EventOperation sizeSetter = new TurtleSizeSetter(SIZE_TURTLE);
		couplingArtifact2.addEventOperation(sizeSetter);

		// Connections
		agent1.addInputCouplingArtifact(couplingArtifact1, "in");
		agent1.addOutputCouplingArtifact(couplingArtifact2, "out");
		agent2.addInputCouplingArtifact(couplingArtifact2, "in");
		agent2.addOutputCouplingArtifact(couplingArtifact1, "out");

		// Start NetLogo
		agent1.startModelSoftware();
		agent2.startModelSoftware();

		// Model initializations
		agent1.setModelParameters();
		agent2.setModelParameters();

		// Simulation launching
		agent1.start();
		agent2.start();
		try {
			// Wait for the simulation to end.
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

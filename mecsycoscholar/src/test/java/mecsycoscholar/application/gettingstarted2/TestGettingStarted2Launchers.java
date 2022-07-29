package mecsycoscholar.application.gettingstarted2;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.coupling.multiplexer.AggregationMultiplexer;
import mecsyco.core.operation.aggregation.VectorStateAggregationOperation;
import mecsyco.observing.base.logging.SingleFileLoggingArtifact;
import mecsyco.observing.base.logging.Utils;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsyco.world.fmi.model.FMIModelArtifact;
import mecsycoscholar.application.gettingstarted2.models.EquationModelArtifact;
import mecsycoscholar.application.gettingstarted2.models.LorenzX;
import mecsycoscholar.application.gettingstarted2.models.LorenzY;
import mecsycoscholar.application.gettingstarted2.models.LorenzZ;

class TestGettingStarted2Launchers {

	/**
	 * Max simulation time for tests.
	 */
	private static final double MAX_SIMULATION_TIME = 0.1;

	/**
	 * Initial values for X.
	 */
	private static final double INIT_X = 1.;
	/**
	 * Initial values for Y.
	 */
	private static final double INIT_Y = 1.;
	/**
	 * Initial values for Z.
	 */
	private static final double INIT_Z = 4.;

	/**
	 * Parameter A for the Lorenz system.
	 */
	private static final double A = 10;
	/**
	 * Parameters B for the Lorenz system.
	 */
	private static final double B = 28;
	/**
	 * Parameters C for the Lorenz system.
	 */
	private static final double C = 2.67;

	/**
	 * Time steps used to solve the equations in X.
	 */
	private static final double TIME_STEP_X = 0.01;
	/**
	 * Time steps used to solve the equations in Y.
	 */
	private static final double TIME_STEP_Y = 0.02;
	/**
	 * Time steps used to solve the equations in Z.
	 */
	private static final double TIME_STEP_Z = 0.07;
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
	 * Test launcher 1 works. Doesn't check the results.
	 */
	@Test
	void testLauncher1() {

		EventMAgent xAgent = new EventMAgent("agentX", MAX_SIMULATION_TIME);
		EventMAgent yAgent = new EventMAgent("agentY", MAX_SIMULATION_TIME);
		EventMAgent zAgent = new EventMAgent("agentZ", MAX_SIMULATION_TIME);

		LorenzX xSimulator = new LorenzX(INIT_X, A, TIME_STEP_X);
		LorenzY ySimulator = new LorenzY(INIT_Y, B, TIME_STEP_Y);
		LorenzZ zSimulator = new LorenzZ(INIT_Z, C, TIME_STEP_Z);

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

		try {
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

	// Does not check launcher 2 because it relies mostly on observing tools which
	// will be checked in observing package.

	/**
	 * Test launcher 3 without observing tools.
	 */
	@Test
	void testLauncher3() {
		Map<String, Object> initVarsAgentX = new HashMap<>();
		initVarsAgentX.put("x", INIT_X); // Init value
		initVarsAgentX.put("y", INIT_Y); // Init value
		initVarsAgentX.put("a", A);

		Map<String, Object> initVarsAgentY = new HashMap<>();
		initVarsAgentY.put("x", INIT_X); // Init value
		initVarsAgentY.put("y", INIT_Y); // Init value
		initVarsAgentY.put("z", INIT_Z); // Init value
		initVarsAgentY.put("b", B);

		Map<String, Object> initVarsAgentZ = new HashMap<>();
		initVarsAgentZ.put("y", INIT_Y); // Init value
		initVarsAgentZ.put("z", INIT_Z); // Init value
		initVarsAgentZ.put("c", C);

		EventMAgent xAgent = new EventMAgent("agentX", MAX_SIMULATION_TIME);
		EventMAgent yAgent = new EventMAgent("agentY", MAX_SIMULATION_TIME);
		EventMAgent zAgent = new EventMAgent("agentZ", MAX_SIMULATION_TIME);

		FMIModelArtifact xModelArtifact = new FMIModelArtifact("Xmodel", "./My Models/fmu/LorenzX_OM_Cs2_x64.fmu",
				MAX_SIMULATION_TIME, TIME_STEP_X, 0.0, initVarsAgentX);
		xAgent.setModelArtifact(xModelArtifact);
		FMIModelArtifact yModelArtifact = new FMIModelArtifact("Ymodel", "./My Models/fmu/LorenzY_OM_Cs2_x64.fmu",
				MAX_SIMULATION_TIME, TIME_STEP_X, 0.0, initVarsAgentY);
		yAgent.setModelArtifact(yModelArtifact);
		FMIModelArtifact zModelArtifact = new FMIModelArtifact("Zmodel", "./My Models/fmu/LorenzZ_OM_Cs2_x64.fmu",
				MAX_SIMULATION_TIME, TIME_STEP_X, 0.0, initVarsAgentZ);
		zAgent.setModelArtifact(zModelArtifact);

		ObservingMAgent obs = new ObservingMAgent("obs", MAX_SIMULATION_TIME);
		SwingDispatcherArtifact dispatcher = new SwingDispatcherArtifact();
		obs.setDispatcherArtifact(dispatcher);

		CentralizedEventCouplingArtifact xOutputToZ = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToZ, "x");
		zAgent.addInputCouplingArtifact(xOutputToZ, "x");
		CentralizedEventCouplingArtifact xOutputToY = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToY, "x");
		yAgent.addInputCouplingArtifact(xOutputToY, "x");
		CentralizedEventCouplingArtifact yOutputToX = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToX, "y");
		xAgent.addInputCouplingArtifact(yOutputToX, "y");
		CentralizedEventCouplingArtifact yOutputToZ = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToZ, "y");
		zAgent.addInputCouplingArtifact(yOutputToZ, "y");
		CentralizedEventCouplingArtifact zOutput = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutput, "z");
		yAgent.addInputCouplingArtifact(zOutput, "z");

		VectorStateAggregationOperation ope = new VectorStateAggregationOperation(new String[] {"X", "Y", "Z"});
		AggregationMultiplexer mult = new AggregationMultiplexer("Mult", ope);

		CentralizedEventCouplingArtifact xOutputToMult = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToMult, "x");
		mult.addInput(xOutputToMult, "X");
		CentralizedEventCouplingArtifact yOutputToMult = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToMult, "y");
		mult.addInput(yOutputToMult, "Y");
		CentralizedEventCouplingArtifact zOutputToMult = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutputToMult, "z");
		mult.addInput(zOutputToMult, "Z");
		CentralizedEventCouplingArtifact multToObs = new CentralizedEventCouplingArtifact();
		mult.addOutput(multToObs, "XYZVector");
		obs.addInputCouplingArtifact(multToObs, "XYZVector");

		xAgent.startModelSoftware();
		yAgent.startModelSoftware();
		zAgent.startModelSoftware();
		obs.startModelSoftware();

		xAgent.setModelParameters();
		yAgent.setModelParameters();
		zAgent.setModelParameters();

		xAgent.coInitialize();
		yAgent.coInitialize();
		zAgent.coInitialize();

		xAgent.start();
		yAgent.start();
		zAgent.start();
		obs.start();
		mult.start();

		try {
			// Wait for the simulation to end.
			xAgent.join(WAITING_TIME);
			yAgent.join(WAITING_TIME);
			obs.join(WAITING_TIME);
			mult.join(WAITING_TIME);
			if (xAgent.isAlive() || yAgent.isAlive() || obs.isAlive() || mult.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}
	}

	@Test
	void testLauncher1Compare() {
		EventMAgent xAgent = new EventMAgent("agentX", MAX_SIMULATION_TIME);
		EventMAgent yAgent = new EventMAgent("agentY", MAX_SIMULATION_TIME);
		EventMAgent zAgent = new EventMAgent("agentZ", MAX_SIMULATION_TIME);

		LorenzX xSimulator = new LorenzX(INIT_X, A, TIME_STEP_X);
		LorenzY ySimulator = new LorenzY(INIT_Y, B, TIME_STEP_Y);
		LorenzZ zSimulator = new LorenzZ(INIT_Z, C, TIME_STEP_Z);

		EquationModelArtifact xModelArtifact = new EquationModelArtifact("Xmodel", xSimulator);
		xAgent.setModelArtifact(xModelArtifact);
		EquationModelArtifact yModelArtifact = new EquationModelArtifact("Ymodel", ySimulator);
		yAgent.setModelArtifact(yModelArtifact);
		EquationModelArtifact zModelArtifact = new EquationModelArtifact("Zmodel", zSimulator);
		zAgent.setModelArtifact(zModelArtifact);

		ObservingMAgent obs = new ObservingMAgent("obs", MAX_SIMULATION_TIME);
		SwingDispatcherArtifact dispatcher = new SwingDispatcherArtifact();
		obs.setDispatcherArtifact(dispatcher);

		final String refFile = "./src/test/resources/gettingstarted2/refLauncher1.csv";
		final String pathFile = "./src/test/resources/gettingstarted2/Launcher1.csv";
		SingleFileLoggingArtifact logArtifact = new SingleFileLoggingArtifact("Log", new String[] {"X", "Y", "Z"},
				pathFile);
		dispatcher.addObservingArtifact(logArtifact);

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
		CentralizedEventCouplingArtifact xtoObs = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xtoObs, "X");
		CentralizedEventCouplingArtifact ytoObs = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(ytoObs, "Y");
		CentralizedEventCouplingArtifact ztoObs = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(ztoObs, "Z");

		obs.addInputCouplingArtifact(xtoObs, "X");
		obs.addInputCouplingArtifact(ytoObs, "Y");
		obs.addInputCouplingArtifact(ztoObs, "Z");

		xAgent.startModelSoftware();
		yAgent.startModelSoftware();
		zAgent.startModelSoftware();
		obs.startModelSoftware();

		xAgent.setModelParameters();
		yAgent.setModelParameters();
		zAgent.setModelParameters();

		xAgent.coInitialize();
		yAgent.coInitialize();
		zAgent.coInitialize();

		obs.start();
		xAgent.start();
		yAgent.start();
		zAgent.start();

		try {
			xAgent.join(WAITING_TIME);
			yAgent.join(WAITING_TIME);
			obs.join(WAITING_TIME);
			zAgent.join(WAITING_TIME);
			if (xAgent.isAlive() || yAgent.isAlive() || obs.isAlive() || zAgent.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}
			assertTrue(Utils.compare(refFile, pathFile));
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}
	}

	@Test
	void testLauncher3Compare() {
		Map<String, Object> initVarsAgentX = new HashMap<>();
		initVarsAgentX.put("x", INIT_X); // Init value
		initVarsAgentX.put("y", INIT_Y); // Init value
		initVarsAgentX.put("a", A);

		Map<String, Object> initVarsAgentY = new HashMap<>();
		initVarsAgentY.put("x", INIT_X); // Init value
		initVarsAgentY.put("y", INIT_Y); // Init value
		initVarsAgentY.put("z", INIT_Z); // Init value
		initVarsAgentY.put("b", B);

		Map<String, Object> initVarsAgentZ = new HashMap<>();
		initVarsAgentZ.put("y", INIT_Y); // Init value
		initVarsAgentZ.put("z", INIT_Z); // Init value
		initVarsAgentZ.put("c", C);

		EventMAgent xAgent = new EventMAgent("agentX", MAX_SIMULATION_TIME);
		EventMAgent yAgent = new EventMAgent("agentY", MAX_SIMULATION_TIME);
		EventMAgent zAgent = new EventMAgent("agentZ", MAX_SIMULATION_TIME);

		FMIModelArtifact xModelArtifact = new FMIModelArtifact("Xmodel", "./My Models/fmu/LorenzX_OM_Cs2_x64.fmu",
				MAX_SIMULATION_TIME, TIME_STEP_X, 0.0, initVarsAgentX);
		xAgent.setModelArtifact(xModelArtifact);
		FMIModelArtifact yModelArtifact = new FMIModelArtifact("Ymodel", "./My Models/fmu/LorenzY_OM_Cs2_x64.fmu",
				MAX_SIMULATION_TIME, TIME_STEP_X, 0.0, initVarsAgentY);
		yAgent.setModelArtifact(yModelArtifact);
		FMIModelArtifact zModelArtifact = new FMIModelArtifact("Zmodel", "./My Models/fmu/LorenzZ_OM_Cs2_x64.fmu",
				MAX_SIMULATION_TIME, TIME_STEP_X, 0.0, initVarsAgentZ);
		zAgent.setModelArtifact(zModelArtifact);

		ObservingMAgent obs = new ObservingMAgent("obs", MAX_SIMULATION_TIME);
		SwingDispatcherArtifact dispatcher = new SwingDispatcherArtifact();
		obs.setDispatcherArtifact(dispatcher);

		final String refFile = "./src/test/resources/gettingstarted2/refLauncher3.csv";
		final String pathFile = "./src/test/resources/gettingstarted2/Launcher3.csv";
		SingleFileLoggingArtifact logArtifact = new SingleFileLoggingArtifact("Log", new String[] {"x", "y", "z"},
				pathFile);
		dispatcher.addObservingArtifact(logArtifact);

		CentralizedEventCouplingArtifact xOutputToZ = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToZ, "x");
		zAgent.addInputCouplingArtifact(xOutputToZ, "x");
		CentralizedEventCouplingArtifact xOutputToY = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToY, "x");
		yAgent.addInputCouplingArtifact(xOutputToY, "x");
		CentralizedEventCouplingArtifact yOutputToX = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToX, "y");
		xAgent.addInputCouplingArtifact(yOutputToX, "y");
		CentralizedEventCouplingArtifact yOutputToZ = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToZ, "y");
		zAgent.addInputCouplingArtifact(yOutputToZ, "y");
		CentralizedEventCouplingArtifact zOutput = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutput, "z");
		yAgent.addInputCouplingArtifact(zOutput, "z");
		CentralizedEventCouplingArtifact xtoObs = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xtoObs, "x");
		CentralizedEventCouplingArtifact ytoObs = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(ytoObs, "y");
		CentralizedEventCouplingArtifact ztoObs = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(ztoObs, "z");

		VectorStateAggregationOperation ope = new VectorStateAggregationOperation(new String[] {"X", "Y", "Z"});
		AggregationMultiplexer mult = new AggregationMultiplexer("Mult", ope);

		CentralizedEventCouplingArtifact xOutputToMult = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToMult, "x");
		mult.addInput(xOutputToMult, "X");
		CentralizedEventCouplingArtifact yOutputToMult = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToMult, "y");
		mult.addInput(yOutputToMult, "Y");
		CentralizedEventCouplingArtifact zOutputToMult = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutputToMult, "z");
		mult.addInput(zOutputToMult, "Z");
		CentralizedEventCouplingArtifact multToObs = new CentralizedEventCouplingArtifact();
		mult.addOutput(multToObs, "XYZVector");
		obs.addInputCouplingArtifact(multToObs, "XYZVector");

		obs.addInputCouplingArtifact(xtoObs, "x");
		obs.addInputCouplingArtifact(ytoObs, "y");
		obs.addInputCouplingArtifact(ztoObs, "z");

		xAgent.startModelSoftware();
		yAgent.startModelSoftware();
		zAgent.startModelSoftware();
		obs.startModelSoftware();

		xAgent.setModelParameters();
		yAgent.setModelParameters();
		zAgent.setModelParameters();

		xAgent.coInitialize();
		yAgent.coInitialize();
		zAgent.coInitialize();

		obs.start();
		xAgent.start();
		yAgent.start();
		zAgent.start();
		mult.start();

		try {
			// Wait for the simulation to end.
			xAgent.join(WAITING_TIME);
			yAgent.join(WAITING_TIME);
			obs.join(WAITING_TIME);
			mult.join(WAITING_TIME);
			if (xAgent.isAlive() || yAgent.isAlive() || obs.isAlive() || mult.isAlive()) {
				fail("After " + WAITING_TIME + "ms, the threads should have stopped!");
			}
			assertTrue(Utils.compare(refFile, pathFile));

		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("No exception should occur! Maybe due to a time limit.");
		}
	}

}

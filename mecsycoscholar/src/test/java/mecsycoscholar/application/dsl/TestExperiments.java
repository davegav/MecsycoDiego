package mecsycoscholar.application.dsl;

import static org.junit.jupiter.api.Assertions.fail;

import javax.swing.JFrame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import mecsyco.description.CoSimDescription;
import mecsyco.observing.swing.util.InitJFrame;
import mecsyco.world.netlogo.NetLogoModelArtifact;

/**
 * Contain short run of several experiments to check that they still work.
 */
class TestExperiments {

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
		NetLogoModelArtifact.setdefaultCloseOperationJFrame(JFrame.DISPOSE_ON_CLOSE);
		NetLogoModelArtifact.setDefaultAutoGuiStop(true);
	}

	@Test
	void testComparisonHouses() {
		String experimentPath = "Experiments/TestComparisonHouses.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testWeatherDataExperiment() {
		String experimentPath = "Experiments/TestWeatherDataExperiment.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testRandomWalkExperiment() {
		String experimentPath = "Experiments/TestRandomWalkExperiment.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testRandomWalkExperimentData() {
		String experimentPath = "Experiments/TestRandomWalkExperimentData.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testRandomWalkExperimentTime() {
		String experimentPath = "Experiments/TestRandomWalkExperimentTime.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testExperimentHybridHighwayMicroEvent() {
		String experimentPath = "Experiments/TestExperimentHybridHighwayMicroEvent.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testExperimentHybridHighway() {
		String experimentPath = "Experiments/TestExperimentHybridHighway.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testLorenzCoSimulationHybrid() {
		String experimentPath = "Experiments/TestLorenzCoSimulationHybrid.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testPedestrianExperiment() {
		String experimentPath = "Experiments/TestPedestrianExperiment.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testOneFloorRealOutdoorTemperaturex64() {
		String experimentPath = "Experiments/TestOneFloorRealOutdoorTemperature_x64.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testOneHeatedRoomRealOutdoorTemperaturex64() {
		String experimentPath = "Experiments/TestOneHeatedRoomRealOutdoorTemperature_x64.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testOneRoomSinusoidOutdoorTemperaturex64() {
		String experimentPath = "Experiments/TestOneRoomSinusoidOutdoorTemperature_x64.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testTrafficGridExperiment() {
		String experimentPath = "Experiments/TestTrafficGridExperiment.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testWolfSheepPredationWithEventsExperiment() {
		String experimentPath = "Experiments/TestWolfSheepPredationWithEventsExperiment.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

	@Test
	void testWolfSheepPredationWithHeavenExperiment() {
		String experimentPath = "Experiments/TestWolfSheepPredationWithHeavenExperiment.xml";
		CoSimDescription csd = new CoSimDescription(experimentPath);
		try {
			csd.launchExperiment().join();
		} catch (Error | Exception e) {
			e.printStackTrace();
			fail("No exception should occur!");
		}
	}

}

package mecsycoscholar.application.thermic.model;

import java.util.Map;

import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;

/**
 * An example of "smart" heater used to control the temperature in a room.
 * Receive the wanted temperature as parameter. Receive the temperature of the
 * room as inputs. Send a heat flow as output, and the power consumed.
 *
 * @author tparis
 *
 */
public class SmartConditionerModelArtifact extends ModelArtifact {

	/**
	 * Constant use for logging
	 */
	private static final String loggerConceptId = "smartheater";

	/**
	 * Time of the next internal event (i.e. the next evaluation point)
	 */
	private double nextEventTime;

	/**
	 * Current simulation time
	 */
	private double time;

	/**
	 * Time step of the controller.
	 */
	private double timeStep;

	// Parameters
	/**
	 * Parameters, the wanted temperature.
	 */
	private double wantedT;

	/**
	 * Parameters, the tolerance on the temperature.
	 */
	private double tolerance;

	/**
	 * Parameters, the maximal command of the controller.
	 */
	private double maxHeatflow;

	/**
	 * Input port name, which receives the actual temperature of the room.
	 */
	private final String roomTPort = "Troom";
	/**
	 * Actual temperature of the room.
	 */
	private double roomT;

	/**
	 * Output ports name, heat flow to heat the room.
	 */
	private final String roomQPort = "Qroom";

	/**
	 * Output ports name, the power consumed.
	 */
	private final String powerConsumedPort = "PowerConsumed";

	/**
	 * Heat flow for the room.
	 */
	private double roomQ;

	/**
	 * Power consumed to heat.
	 */
	private double powerConsumed;

	/**
	 *
	 * @param aModelId
	 * @param parameters
	 */
	public SmartConditionerModelArtifact(String aModelId, Map<String, Object> parameters) {
		super(loggerConceptId + "." + aModelId);
		wantedT = parameters.get("Twanted") != null ? (double) parameters.get("Twanted") : 293.15;
		tolerance = parameters.get("tolerance") != null ? (double) parameters.get("tolerance") : 1.;
		roomT = parameters.get(roomTPort + "_init") != null ? (double) parameters.get(roomTPort + "_init") : 293.15;
		maxHeatflow = parameters.get("maxHeatflow") != null ? (double) parameters.get("maxHeatflow") : 200.;
		timeStep = parameters.get("timeStep") != null ? (double) parameters.get("timeStep") : 1.;
	}

	@Override
	public void processInternalEvent(double aTime) {
		time = aTime;
		nextEventTime = aTime + timeStep;
		if (roomT < wantedT - tolerance) {
			// Start the heater
			roomQ = maxHeatflow;
			powerConsumed = roomQ;
		} else if (roomT <= wantedT - tolerance / 2 && roomQ < 0) {
			// Stop the clim
			roomQ = 0;
			powerConsumed = roomQ;
		} else if (roomT >= wantedT + tolerance / 2 && roomQ > 0) {
			// Stop the heater
			roomQ = 0;
			powerConsumed = -roomQ;
		} else if (roomT > wantedT + tolerance) {
			// Start the clim
			roomQ = -maxHeatflow;
			powerConsumed = -roomQ;
		}

	}

	@Override
	public double getNextInternalEventTime() {
		return nextEventTime;
	}

	@Override
	public double getLastEventTime() {
		return time;
	}

	@Override
	public void processExternalInputEvent(SimulEvent aEvent, String aPort) {
		if (roomTPort.equals(aPort)) {
			roomT = (Double) ((Tuple1<?>) aEvent.getData()).getItem1();
		} else {
			getLogger().error("Unknown port! " + aPort);
		}
	}

	@Override
	public SimulEvent getExternalOutputEvent(String aPort) {
		if (roomQPort.equals(aPort)) {
			return new SimulEvent(new Tuple1<Double>(roomQ), time);
		} else if (powerConsumedPort.equals(aPort)) {
			return new SimulEvent(new Tuple1<Double>(powerConsumed), time);
		}
		getLogger().error("Unknown output port! " + aPort);
		return null;
	}

	@Override
	public void initialize() {
		// Nothing to do
	}

	@Override
	public void finishSimulation() {
		getLogger().info("End of simulation.");

	}

	@Override
	public void setInitialParameters(String[] args) {
		// Nothing to do
	}

}

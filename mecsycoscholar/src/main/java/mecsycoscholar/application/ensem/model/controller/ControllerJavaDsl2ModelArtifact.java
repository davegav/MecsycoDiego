package mecsycoscholar.application.ensem.model.controller;

import java.util.Map;

import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;

/**
 *
 * Model-artifact to use with a {@link FlowController}.
 *
 */
public class ControllerJavaDsl2ModelArtifact extends ModelArtifact {

	/**
	 * Time of the next internal event (i.e. the next evaluation point)
	 */
	private double nextEventTime;

	/**
	 * current simulation time
	 */
	private double time;

	/**
	 * Controller model
	 */
	private ControllerJava2 controller;

	public ControllerJavaDsl2ModelArtifact(String aModelId, ControllerJava2 controller) {
		super(aModelId);
		this.controller = controller;
	}

	/**
	 * Constructor to be used with the MECSYCO DSL, it imposes to set the name of
	 * the inputs, outputs and parameters.
	 *
	 * @param aModelId   Name of the model
	 * @param parameters Map of the parameters of the model, the names used refer to
	 *                   the names used in the description file
	 */
	public ControllerJavaDsl2ModelArtifact(String aModelId,
			Map<String, Object> parameters) {
		super(aModelId);
		// We set the name of the inputs, outputs and parameters
		String[] inputPortNames = new String[] {"Temp1", "Temp2", "Temp3"};
		String[] outputPortNames = new String[] {"Pow1_out", "Pow2_out", "Pow3_out"};
		double period = ((Number) parameters.get("step")).doubleValue();
		double[] minTemp = new double[] {((Number) parameters.get("minTemp1")).doubleValue(),
				((Number) parameters.get("minTemp2")).doubleValue(),
				((Number) parameters.get("minTemp3")).doubleValue()};
		double[] maxTemp = new double[] {((Number) parameters.get("maxTemp1")).doubleValue(),
				((Number) parameters.get("maxTemp2")).doubleValue(),
				((Number) parameters.get("maxTemp3")).doubleValue()};
		double[] temp = new double[] {((Number) parameters.get("Temp1")).doubleValue(),
				((Number) parameters.get("Temp2")).doubleValue(),
				((Number) parameters.get("Temp3")).doubleValue()};
		double[] powMax = new double[] {((Number) parameters.get("maxPow1")).doubleValue(),
				((Number) parameters.get("maxPow2")).doubleValue(),
				((Number) parameters.get("maxPow3")).doubleValue()};
		double[] pow = new double[] {((Number) parameters.get("Pow1")).doubleValue(),
				((Number) parameters.get("Pow2")).doubleValue(),
				((Number) parameters.get("Pow3")).doubleValue()};
		this.controller = new ControllerJava2(inputPortNames, outputPortNames, period, minTemp, maxTemp, temp, powMax,
				pow);
	}

	/*
	 * (non-Javadoc) an internal event correspond here to an evaluation point (i.e.
	 * the controller takes the decisions and send order to the heaters). Thus, this
	 * method is equivalent to a doStep/takeDecision function
	 *
	 * @see mecsyco.core.model.GenericModelArtifact#processInternalEvent(double)
	 */
	@Override
	public void processInternalEvent(double aTime) {
		time = aTime;

		// Controller compute the orders for each heater.
		controller.computeOrders(time);
		// Time of the next event, the next computation
		nextEventTime = time + controller.getPeriod();
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
		time = aEvent.getTime();
		controller.updateValue(aPort, ((Tuple1<Double>) aEvent.getData()).getItem1());
	}

	@Override
	public SimulEvent getExternalOutputEvent(String aPort) {
		double data = controller.getCurrentOrder(aPort);
		// If the command is !=0 then we send it
		if (data != 0) {
			return new SimulEvent(new Tuple1<Double>(data), time);
		}
		// Else the port must be incorrect so we return null
		return new SimulEvent(new Tuple1<Double>(0.), time);

	}

	@Override
	public void initialize() {
		// useless in our case
	}

	@Override
	public void finishSimulation() {
		getLogger().info("Controller finish simulation at time " + time);
	}

	@Override
	public void setInitialParameters(String[] args) {
		// useless in our case
	}

}

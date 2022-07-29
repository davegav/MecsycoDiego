package mecsycoscholar.application.ensem.model.controller;

import java.util.Arrays;
import java.util.Map;

import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;

/**
 *
 * Model-artifact to use with a {@link FlowController}.
 *
 */
public class BehaviorControllerModelArtifact extends ModelArtifact {

	/**
	 * Time of the next internal event (i.e. the next evaluation point)
	 */
	private double nextEventTime;

	/**
	 * current simulation time
	 */
	private double time;
	
	private double step;
	
	private String[] inputPortNames;
	private String[] outputPortNames;
	private double[] Outputs;
	private double[] inputs;
	private double OptTemp;
	

	/**
	 * Controller model
	 */

	public BehaviorControllerModelArtifact(String aModelId) {
		super(aModelId);
	}

	/**
	 * Constructor to be used with the MECSYCO DSL, it imposes to set the name of
	 * the inputs, outputs and parameters.
	 *
	 * @param aModelId   Name of the model
	 * @param parameters Map of the parameters of the model, the names used refer to
	 *                   the names used in the description file
	 */
	public BehaviorControllerModelArtifact(String aModelId,
			Map<String, Object> parameters) {
		super(aModelId);
		
		// We set the name of the inputs, outputs and parameters
		this.inputPortNames = new String[] {"Temp1", "Temp2", "Temp3", "Pow1_out", "Pow2_out", "Pow3_out", "Occup"};
		this.outputPortNames = new String[] {"Comfort", "Consum"};
		this.step = ((Number) parameters.get("step")).doubleValue();
		
		
		this.Outputs = new double[] {((Number) parameters.get("Comfort")).doubleValue(),
				((Number) parameters.get("Consum")).doubleValue()};
		this.inputs = new double[] {((Number) parameters.get("Temp1")).doubleValue(),
				((Number) parameters.get("Temp2")).doubleValue(),
				((Number) parameters.get("Temp3")).doubleValue(),
				((Number) parameters.get("Pow1_out")).doubleValue(),
				((Number) parameters.get("Pow2_out")).doubleValue(),
				((Number) parameters.get("Pow3_out")).doubleValue(),
				((Number) parameters.get("Occup")).doubleValue()};
		
		this.OptTemp = ((Number) parameters.get("OptTemp")).doubleValue();
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
		this.time = aTime;

		// Controller compute the orders for each heater.
		
		// Comfort = (comfort1-> (abs(OptTemp - temp1) + comfort2 + comfort3) * ocupp    
		this.Outputs[0] += (Math.abs(OptTemp- this.inputs[0]) + Math.abs(OptTemp- this.inputs[1]) + Math.abs(OptTemp- this.inputs[2]))* this.inputs[6]; 

		// Consum = Pow1_out + Pow2_out + Pow3_out
		this.Outputs[1] += Math.abs(this.inputs[3]) + Math.abs(this.inputs[4]) + Math.abs(this.inputs[5]);
		
		// Time of the next event, the next computation
		this.nextEventTime = this.time + this.step;
	}

	@Override
	public double getNextInternalEventTime() {
		return this.nextEventTime;
	}

	@Override
	public double getLastEventTime() {
		return this.time;
	}
	
	/**
	 *
	 * @param inputPortName Name of the input port whose value must be updated
	 * @param value         Value for the update (a new temperature)
	 */
	public void updateValue(String inputPortName, double value) {
		int i = 0;
		boolean valueUpdated = false;
		while (i < this.inputPortNames.length && !valueUpdated) {
			if (inputPortName.equals(this.inputPortNames[i])) {
				this.inputs[i] = value;
				valueUpdated = true;
			}
			i++;
		}
		if (!valueUpdated) {   //TODO temporal fix, do I need to update this here?
			System.out.println("The input port " + inputPortName + " does not exist!");
		}
	}

	@Override
	public void processExternalInputEvent(SimulEvent aEvent, String aPort) {
		this.time = aEvent.getTime();
		updateValue(aPort, ((Tuple1<Double>) aEvent.getData()).getItem1());
	}
	
	/**
	 *
	 * @param outputPortName
	 * @return The value of the power command of the specified port.
	 */
	public double getCurrentOutput(String outputPortName) {
		int i = 0;
		while (i < outputPortNames.length) {
			if (outputPortName.equals(outputPortNames[i])) {
				return this.Outputs[i];
			}
			i++;
		}
		System.out.println("The output port " + outputPortName + " does not exist!");
		return -1;
	}
	
	
	

	@Override
	public SimulEvent getExternalOutputEvent(String aPort) {
		double data = getCurrentOutput(aPort);
		// If the command is !=0 then we send it
		if (data != 0) {
			return new SimulEvent(new Tuple1<Double>(data), this.time);
		}
		// Else the port must be incorrect so we return null
		return new SimulEvent(new Tuple1<Double>(0.), this.time);

	}

	@Override
	public void initialize() {
		// useless in our case
	}

	@Override
	public void finishSimulation() {
		getLogger().info("Controller finish simulation at time " + this.time);
	}

	@Override
	public void setInitialParameters(String[] args) {
		// useless in our case
	}

}

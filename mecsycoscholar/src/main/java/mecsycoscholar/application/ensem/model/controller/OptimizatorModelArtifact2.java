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
public class OptimizatorModelArtifact2 extends ModelArtifact {

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
	private int iteration;
	private int runs;

	/**
	 * Controller model
	 */

	public OptimizatorModelArtifact2(String aModelId) {
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
	public OptimizatorModelArtifact2(String aModelId,
			Map<String, Object> parameters) {
		super(aModelId);
		
		// We set the name of the inputs, outputs and parameters
		this.inputPortNames = new String[] {"Comfort", "Consum", "TempMinIn", "TempMaxIn"};
		this.outputPortNames = new String[] {"TempMinOut", "TempMaxOut", "Iteration"};
		
		this.step = ((Number) parameters.get("step")).doubleValue();
	
		this.inputs = new double[] {((Number) parameters.get("Comfort")).doubleValue(),
				((Number) parameters.get("Consum")).doubleValue(),
				((Number) parameters.get("TempMin")).doubleValue(),
				((Number) parameters.get("TempMax")).doubleValue()};
		this.Outputs = new double[] {((Number) parameters.get("TempMin")).doubleValue(),
				((Number) parameters.get("TempMax")).doubleValue(),
				((Number) parameters.get("Iteration")).doubleValue()};
		this.OptTemp = ((Number) parameters.get("OptTemp")).doubleValue();
		this.iteration = 0;
		this.runs = ((Number) parameters.get("runs")).intValue();
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
		// Time of the next event, the next computation
		this.nextEventTime = this.time + this.step;
		if (this.nextEventTime == 604800.0) {
			this.Outputs[2] += 1;
		}
		if (this.time == 604800.0 && this.iteration < this.runs) {
			this.iteration += 1;  //Still necessary?
			
			this.Outputs[0] = this.Outputs[0] - 0.5;
			this.Outputs[1] = this.Outputs[1] + 0.5;
			
			//logs
			
			System.out.println("Iteration "+ Integer.toString(this.iteration));
			System.out.println(this.Outputs[0]);
			System.out.println(this.Outputs[1]);
			System.out.println(this.inputs[0]);
			System.out.println(this.inputs[1]);
			
			// reset indicators
			
			this.inputs[0] = 0.0;
			this.inputs[1] = 0.0;
			
			
		}else if (this.time == 604800.0) {
			this.iteration += 1;
		}
		

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
			if (inputPortName.equals(inputPortNames[i])) {
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
		while (i < this.outputPortNames.length) {
			if (outputPortName.equals(this.outputPortNames[i])) {
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
		if (this.iteration < this.runs) {
			this.nextEventTime = this.step;
			this.time = 0.0;
		}else {
			getLogger().info("Controller finish simulation at time " + this.time);
		}
	}

	@Override
	public void setInitialParameters(String[] args) {
		// useless in our case
	}

}

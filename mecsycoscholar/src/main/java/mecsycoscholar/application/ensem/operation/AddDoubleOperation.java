package mecsycoscholar.application.ensem.operation;

import java.util.Map;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;

/**
 * Operation that just add a double to the data (assumed to be a {@link Tuple1}
 * of double).
 */
public class AddDoubleOperation extends EventOperation {

	/**
	 * Real to be added.
	 */
	private double arg;

	/**
	 *
	 * @param operand
	 */
	public AddDoubleOperation(double operand) {
		// Get one element of the parameters map (normally, there is only one).
		arg = operand;
	}

	/**
	 *
	 * @param aParameterMap
	 */
	public AddDoubleOperation(Map<String, Object> aParameterMap) {
		// Get one element of the parameters map (normally, there is only one).
		arg = (double) aParameterMap.get(aParameterMap.keySet().iterator().next());
	}

	@Override
	public SimulData apply(SimulEvent aEvent) {
		return new Tuple1<Double>(((Double) ((Tuple1<?>) aEvent.getData()).getItem1()) + arg);
	}

}

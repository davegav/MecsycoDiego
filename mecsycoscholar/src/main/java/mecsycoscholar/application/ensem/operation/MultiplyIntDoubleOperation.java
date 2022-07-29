package mecsycoscholar.application.ensem.operation;

import java.util.Map;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;

/**
 * Operation that just multiply by a double the received data (assumed to be a
 * {@link Tuple1} of double).
 */
public class MultiplyIntDoubleOperation extends EventOperation {

	/**
	 * Operand.
	 */
	private double arg;

	/**
	 *
	 * @param operand
	 */
	public MultiplyIntDoubleOperation(double operand) {
		// Get one element of the parameters map (normally, there is only one).
		arg = operand;
	}

	/**
	 *
	 * @param aParameterMap
	 */
	public MultiplyIntDoubleOperation(Map<String, Object> aParameterMap) {
		// Get one element of the parameters map (normally, there is only one).
		arg = Double.valueOf( (Integer) aParameterMap.get(aParameterMap.keySet().iterator().next()));
		
	}

	@Override
	public SimulData apply(SimulEvent aEvent) {
		return new Tuple1<Double>((Double) (((Tuple1<?>) aEvent.getData()).getItem1()) * arg);
	}

}

package mecsycoscholar.application.ode.operation;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;
import mecsyco.core.type.Tuple2;

/**
 * Transform a Tuple&lt;Double, ?...&gt; into a Tuple1&lt;Double&gt;.
 *
 */
public class ToTuple1 extends EventOperation {

	@Override
	public SimulData apply(SimulEvent aEvent) {
		Tuple2<?, ?> received = (Tuple2<?, ?>) aEvent.getData();
		Tuple1<Double> changed = new Tuple1<Double>((Double) received.getItem1());
		return changed;
	}

}

package mecsycoscholar.application.highway.operation;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.SimulVector;
import mecsyco.core.type.Tuple1;

public class Turtle2SimulDataDouble extends EventOperation {

	@Override
	public SimulData apply (SimulEvent event) {
		final SimulVector<?> eT = (SimulVector<?>) event.getData();

		return new Tuple1<Double>((double) eT.count());
	}

}

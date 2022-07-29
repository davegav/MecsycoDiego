package mecsycoscholar.application.highway.operation;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;
import mecsyco.core.type.Tuple3;

public class TurtlesToGroup extends EventOperation{

	@Override
	public SimulData apply (SimulEvent aEvent) {
		SimulData ret = null;
		if(aEvent.getData()!=null) {
			ArrayedSimulVector<ArrayedSimulVector<String>> turtleList = (ArrayedSimulVector<ArrayedSimulVector<String>>) aEvent.getData();
			int nb = turtleList.count();
			if(nb!=0){
				double speed = Double.parseDouble(((turtleList.item(0).item(0))));
				ret = new Tuple3<Double, Integer, String>(speed,nb, "BLUE");
			}
		}
		
		return ret;
	}
	
}

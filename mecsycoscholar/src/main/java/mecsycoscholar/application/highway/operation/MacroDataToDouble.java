package mecsycoscholar.application.highway.operation;

import java.util.Map;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple1;
import mecsyco.core.type.Tuple3;

public class MacroDataToDouble  extends EventOperation {

	private String type;
	
	public MacroDataToDouble(String type){
		this.type=type;
	}
	
	public MacroDataToDouble (Map<String, Object> parameters) {
		type=parameters.get("type")!=null?(String)parameters.get("type"):"flow";
	}
	
	@Override
	public SimulData apply(SimulEvent aEvent) {
		Tuple3<Double, Double, Double> data = (Tuple3<Double, Double, Double>) aEvent.getData();
		// System.out.println(density + " " + macro_length + " " + density * macro_length * nbVoie);
		double ret=0;
		switch(type){
			case "speed":
				ret=data.getItem2();
				break;
			case "density":
				ret=data.getItem3();
				break;
			case "flow":
				ret=data.getItem1();
		}
		return new Tuple1<Double>(ret);
	}
	
	

}


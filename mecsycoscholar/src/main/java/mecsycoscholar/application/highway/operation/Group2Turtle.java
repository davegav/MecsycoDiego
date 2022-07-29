package mecsycoscholar.application.highway.operation;

import java.util.Map;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.SimulVector;
import mecsyco.core.type.Tuple3;

public class Group2Turtle extends EventOperation{

	// Speed of the micro model (km/h) 
	private double microMaxSpeed;
	
	public Group2Turtle(){
		microMaxSpeed=-1;
	}
	
	public Group2Turtle(double maxSpeed){
		microMaxSpeed=maxSpeed;
	}
	
	public Group2Turtle(Map<String, Object> parameters){
		microMaxSpeed=parameters.get("microMaxSpeed")!=null?(Double)parameters.get("microMaxSpeed"):-1;
	}
	
	//@JsonProperty("x") double myXpos, @JsonProperty("y") double myYpos, @JsonProperty("id") long myId, @JsonProperty("heading") double myHeading, @JsonProperty("color") double myColor, @JsonProperty("speed") double mySpeed
	@Override
	public SimulData apply(SimulEvent event) {
		Tuple3<Double, Integer, String> group = (Tuple3<Double, Integer, String>)(event.getData());
		SimulVector<SimulVector<String>> turtlesToExchange = new ArrayedSimulVector<SimulVector<String>>();
		for (int i = 0; i < group.getItem2(); i++) {
			if(microMaxSpeed<0){
				turtlesToExchange = turtlesToExchange.appended(
						new ArrayedSimulVector<String>()
//						.appended("0")
//						.appended("0")
//						.appended(""+i)
						.appended("90")
//						.appended("126")
						.appended("1.")); //Default speed 1
			}
			else{
				double speed = group.getItem1()/microMaxSpeed;
				if(speed>1)speed=1;
				turtlesToExchange = turtlesToExchange.appended(
						new ArrayedSimulVector<String>()
//						.appended("0")
//						.appended("0")
//						.appended(""+i)
						.appended("90")
//						.appended("126")
						.appended(""+speed));
			}
		}
		return turtlesToExchange;
	}

}


package mecsycoscholar.application.highway.operation;

import java.util.Map;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple3;

public class Turtles2Flow extends EventOperation {

	// length of a macro cells (km)
	private double macro_length;

	// length of a patch in the micro road (km)
	private double patch_length;

	// length of the micro road (km)
	private double micro_length;

	// maximum speed of the micro road (km)
	private double micro_max_speed;

	public Turtles2Flow (double macro_length, double micro_length, double patch_length, double micro_max_speed, int macro_nb_voie) {
		this.macro_length = macro_length;
		this.micro_length = micro_length;
		this.patch_length = patch_length;
		this.micro_max_speed = micro_max_speed;
	}
	
	public Turtles2Flow (Map<String, Object> parameters) {
		this.macro_length = (double)parameters.get("macro_length");
		this.micro_length = (double)parameters.get("micro_length");
		this.patch_length = (double)parameters.get("patch_length");
		this.micro_max_speed = (double)parameters.get("micro_max_speed");
	}

	@Override
	public SimulData apply (SimulEvent event) {
		ArrayedSimulVector<ArrayedSimulVector<String>> turtleList = (ArrayedSimulVector<ArrayedSimulVector<String>>) event.getData();
		double density = 0;
		double flow_rate;
		int nbTurtles = 0;
		double avg_speed =0;
		for (ArrayedSimulVector<String> turtleInfos : turtleList) {
			//Data stored as String if the order xcor, heading, speed
			if (Double.parseDouble(turtleInfos.item(0)) * patch_length >= micro_length - macro_length) {
				nbTurtles++;
				avg_speed+=Double.parseDouble(turtleInfos.item(3));
			}
		}

		density = (nbTurtles * (1 / macro_length));
		if(nbTurtles!=0)avg_speed = avg_speed * micro_max_speed/nbTurtles;
		flow_rate = density * avg_speed;
		return new Tuple3<Double, Double, Double>(flow_rate, avg_speed, 0.);
	}


}

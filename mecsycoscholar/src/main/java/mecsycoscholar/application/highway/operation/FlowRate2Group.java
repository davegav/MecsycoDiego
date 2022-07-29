package mecsycoscholar.application.highway.operation;

import java.util.Map;

import mecsyco.core.operation.event.EventOperation;
import mecsyco.core.type.SimulData;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.Tuple3;

public class FlowRate2Group extends EventOperation {

	// Maximum speed in the macro model (km/h)
	// length of a macro time step (h)
	private double macro_time_step;

	private double rounding_error;

	public FlowRate2Group (double macro_time_step) {
		this.macro_time_step = macro_time_step;
		rounding_error = 0;
	}
	
	public FlowRate2Group (Map<String,Object> parameters) {
		this.macro_time_step = parameters.get("macro_time_step")!=null?(Double)parameters.get("macro_time_step"):0.0001;
		rounding_error = 0;
	}

	@Override
	public SimulData apply (SimulEvent aEvent) {
		double flux = (Double)((Tuple3<?, ?, ?>) aEvent.getData()).getItem1();
		double speed = (Double)((Tuple3<?, ?, ?>) aEvent.getData()).getItem2();

		double input_flux = flux * macro_time_step;
		int integer_flux = (int) (input_flux);
		rounding_error += (input_flux) - integer_flux;
		// double rounding_error_before = rounding_error;

		integer_flux += (int) (rounding_error);
		rounding_error -= (int) (rounding_error);
		//System.out.println("output flux "+flux+" macro time step: "+macro_time_step+ " input flux"+ input_flux +" integer flux "+integer_flux);

		SimulData ret = null;
		if(integer_flux!=0){
			ret = new Tuple3<Double, Integer, String>(speed,integer_flux, "BLUE");
		}
		return ret;
	}
}


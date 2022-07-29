/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta, M. Urbanski Copyright
(c) Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package mecsycoscholar.application.highway.model.event;

import java.util.Map;
import java.util.TreeMap;

import mecsyco.core.model.ModelArtifact;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.SimulEvent;
import mecsyco.core.type.SimulVector;
import mecsyco.core.type.Tuple1;
import mecsyco.core.type.Tuple3;
import mecsyco.core.type.Tuple4;
import mecsycoscholar.application.highway.type.VehiclesGroup;

/**
 * Event model in the hybrid highway example. Model of a highway where cars are
 * modeled as events.
 *
 */
public class EventModelArtifact extends ModelArtifact {

	/**
	 * Model.
	 */
	private TrafficModel model;

	/**
	 * Automate the closure of the GUI at the end of the simulation.
	 */
	private boolean autoCloseGui;

	/**
	 *
	 * @param model_name
	 * @param road_length
	 * @param max_speed
	 * @param min_speed
	 * @param number_output_port
	 * @param initial_car_number
	 */
	public EventModelArtifact(String model_name, double road_length, double max_speed, double min_speed,
			int number_output_port, int initial_car_number) {
		super(model_name);
		model = new TrafficModel(model_name, road_length, max_speed, min_speed, number_output_port, initial_car_number,
				false);
		autoCloseGui = false;
	}

	/**
	 *
	 * @param model_name
	 * @param road_length
	 * @param max_speed
	 * @param min_speed
	 * @param number_output_port
	 * @param initial_car_number
	 * @param headless
	 */
	public EventModelArtifact(String model_name, double road_length, double max_speed, double min_speed,
			int number_output_port, int initial_car_number, boolean headless) {
		super(model_name);
		model = new TrafficModel(model_name, road_length, max_speed, min_speed, number_output_port, initial_car_number,
				headless);
		autoCloseGui = false;
	}

	/**
	 *
	 * @param m
	 */
	public EventModelArtifact(TrafficModel m) {
		super(m.getName());
		model = m;
		autoCloseGui = false;
	}

	/**
	 * Constructor for use with DSL.
	 *
	 * @param aModelId
	 * @param parameters
	 */
	public EventModelArtifact(String aModelId, Map<String, Object> parameters) {
		super(aModelId);
		model = new TrafficModel(aModelId,
				parameters.get("road_length") != null ? (Double) parameters.get("road_length") : 2.,
				parameters.get("max_speed") != null ? (Double) parameters.get("max_speed") : 90.,
				parameters.get("min_speed") != null ? (Double) parameters.get("min_speed") : 50.,
				parameters.get("number_output_port") != null ? (Integer) parameters.get("number_output_port") : 1,
				parameters.get("initial_car_number") != null ? (Integer) parameters.get("initial_car_number") : 20,
				parameters.get("headless") != null ? (Boolean) parameters.get("headless") : false);
		autoCloseGui = parameters.get("autoCloseGui") != null ? (Boolean) parameters.get("autoCloseGui") : false;
	}

	@Override
	public void processInternalEvent(double time) {
		model.internalTransition();
	}

	@Override
	public double getNextInternalEventTime() {
		return model.ta();
	}

	@Override
	public double getLastEventTime() {
		return model.getLast_Event_Time();
	}

	@Override
	public void processExternalInputEvent(SimulEvent event, String port) {
		VehiclesGroup group = new VehiclesGroup(
				((Tuple3<Double, Integer, String>) event.getData()).getItem1(),
				((Tuple3<Double, Integer, String>) event.getData()).getItem2());
		model.externalTransition(event.getTime(), group);
	}

	@Override
	public SimulEvent getExternalOutputEvent(String port) {
		SimulEvent ret = null;
		double time = model.getLast_Event_Time();
		if (port.equals("observation")) {
			ret = new SimulEvent(new Tuple1<Double>(model.totalNumberOfCars()), time);
		} else if (port.equals("state")) {
			TreeMap<Double, VehiclesGroup> data = model.getS();
			// We have to convert the data in a "standard" compliant (one already present in
			// MECSYCO)
			SimulVector<Tuple4<Double, Double, Integer, String>> dataConverted = new ArrayedSimulVector<Tuple4<Double, Double, Integer, String>>();
			for (Double d : data.keySet()) {
				dataConverted = dataConverted.appended(new Tuple4<Double, Double, Integer, String>(d,
						data.get(d).getSpeed(), data.get(d).getCount(), data.get(d).getColor().toString()));
			}

			ret = new SimulEvent(dataConverted, time);
		} else if (port.startsWith("out")) {
			// Retrieve the number to use for model.output.
			VehiclesGroup group = model.output(Integer.parseInt(port.substring(3, port.length())));
			if (group != null) {
				// Average speed, Number of vehicles, Color (color not used)
				Tuple3<Double, Integer, String> data = new Tuple3<Double, Integer, String>(group.getSpeed(),
						group.getCount(), group.getColor().getRGB() + "");
				ret = new SimulEvent(data, time);
			}
		}
		return ret;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void finishSimulation() {
		if (autoCloseGui) {
			model.closeWindows();
		}
		model.close();
	}

	@Override
	public void setInitialParameters(String[] args) {

	}

}

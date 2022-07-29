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

import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import mecsycoscholar.application.highway.model.event.gui.EventTrafficModelGUI;
import mecsycoscholar.application.highway.type.VehiclesGroup;

/**
 * Model of a highway where cars are represented as events.
 *
 */
public class TrafficModel {

	protected TreeMap<Double, VehiclesGroup> S;

	protected EventTrafficModelGUI gui;

	protected double length;

	protected double max_speed;

	protected double min_speed;

	protected VehiclesGroup[] output_port;

	protected double last_event_time;

	protected int number_output_ports;

	protected String name;

	protected boolean headless;

	public TrafficModel(String n, double l, double max, double min, int number_output, int car_number,
			boolean headless) {
		S = new TreeMap<Double, VehiclesGroup>();
		name = n;
		length = l;
		max_speed = max;
		min_speed = min;
		output_port = null;
		last_event_time = 0;
		this.headless = headless;
		if (!headless) {
			gui = new EventTrafficModelGUI(this);
		}
		number_output_ports = number_output;
		output_port = new VehiclesGroup[number_output];

		for (double i = 0; i < car_number; i++) {
			double avg_speed = (Math.random() * (max_speed - min_speed)) + min_speed;
			double arrival_time = last_event_time + (length / avg_speed);

			if (S.get(arrival_time) != null) {
				final VehiclesGroup temp = S.get(arrival_time);
				S.put(arrival_time, temp.withCount(temp.getCount() + 1));
			} else {
				S.put(arrival_time, new VehiclesGroup(avg_speed, 1));
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNumber_output_ports() {
		return number_output_ports;
	}

	public TrafficModel(double l, double max, double min) {
		this("Event-Based Traffic Model", l, max, min, 1, 0, false);
	}

	// get the time of the next internal event
	public double ta() {
		if (!headless && gui.isDisplayable()) {
			double ret = Double.MAX_VALUE;
			if (S.size() != 0) {
				ret = S.firstKey();
			}
			return ret;
		} else {
			return Double.MAX_VALUE;
		}

	}

	// add existing group of cars in the model
	public void externalTransition(Double time, VehiclesGroup group) {
		last_event_time = time;
		for (int i = 0; i < group.getCount(); i++) {

			double avg_speed = (Math.random() * (max_speed - min_speed)) + min_speed;
			double arrival_time = time + (length / avg_speed);

			synchronized (S) {
				if (S.get(arrival_time) != null) {
					final VehiclesGroup temp = S.get(arrival_time);
					S.put(arrival_time, temp.withCount(temp.getCount() + 1));
				} else {
					S.put(arrival_time, new VehiclesGroup(avg_speed, 1));
				}
			}
		}
		// System.out.println("====External Transition====");
		// printState();
		for (int i = 0; i < output_port.length; i++) {
			output_port[i] = null;
		}

		if (!headless) {
			gui.refreshModel();
		}

	}

	// remove the first entered cars from the model and store them in the output
	// port
	public void internalTransition() {
		if (S.size() != 0) {
			synchronized (S) {
				Map.Entry<Double, VehiclesGroup> event = S.pollFirstEntry();
				last_event_time = event.getKey();
				int index_output = new Random().nextInt(output_port.length);
				for (int i = 0; i < output_port.length; i++) {
					if (i == index_output) {
						output_port[i] = event.getValue();
					} else {
						output_port[i] = null;
					}
				}
			}
		}

		if (!headless) {
			gui.refreshModel();
		}
	}

	public double getLast_Event_Time() {
		return last_event_time;
	}

	public VehiclesGroup output(int port) {
		return output_port[port];
	}

	public void printState(String transition) {
		System.out.println("==========" + transition + "==========");
		System.out.println("Time of the next Event : " + ta());
		System.out.println("Time of the last Event : " + last_event_time);

		for (int i = 0; i < output_port.length; i++) {
			System.out.println("output on port " + i + ": " + output_port[i]);
		}
	}

	public double totalNumberOfCars() {
		double ret = 0;
		synchronized (S) {
			Iterator<VehiclesGroup> ite = S.values().iterator();
			while (ite.hasNext()) {
				ret += ite.next().getCount();
			}
		}
		return ret;
	}

	public TreeMap<Double, VehiclesGroup> getS() {
		return S;
	}

	public void setS(TreeMap<Double, VehiclesGroup> s) {
		S = s;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getMax_speed() {
		return max_speed;
	}

	public void setMax_speed(double max_speed) {
		this.max_speed = max_speed;
	}

	public double getMin_speed() {
		return min_speed;
	}

	public void setMin_speed(double min_speed) {
		this.min_speed = min_speed;
	}

	public void setLast_event_time(double last_event_time) {
		this.last_event_time = last_event_time;
	}

	public void close() {
		System.out.println("Event traffic model closed");
	}

	/**
	 * Method to close a frame, it is used to end JUnit test of this model artifact.
	 * Send a window closing event to the frame.
	 */
	public void closeWindows() {
		gui.dispatchEvent(new WindowEvent(gui, WindowEvent.WINDOW_CLOSING));
	}
}

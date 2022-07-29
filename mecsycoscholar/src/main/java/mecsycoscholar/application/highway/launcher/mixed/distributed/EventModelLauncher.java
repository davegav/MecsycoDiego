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

package mecsycoscholar.application.highway.launcher.mixed.distributed;

import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactReceiver;
import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactSender;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.type.Tuple3;
import mecsycoscholar.application.highway.model.event.EventModelArtifact;
import mecsycoscholar.application.highway.operation.FlowRate2Group;


public class EventModelLauncher {

	
	public static double maxSimulationTime = 4;
	
	/*
	 * MACRO PARAMETERS
	 */
	
	// h
	public static double time_step = 0.0001;

	
	/*
	 * MICRO PARAMETERS
	 */

	// maximum speed in the micro model (km/h)
	public static double microMaxSpeed = 100;

	// length of a micro time step (h)
	public static double micro_timestep = 0.00004;
	
	/*
	 * EVENT PARAMETERS
	 */
	
	// length of the road (km)
	public static double event_road_length = 2.0;
	
	// maximum speed (km/h)
	public static double event_max_speed = 90;
	
	// minimum speed (km/h)
	public static double event_min_speed = 50;
	
	// initial number of cars
	public static int event_initial_cars = 20;

	public static void main (String[] args) {
		
		// Event model
		
		EventMAgent eventAgent = new EventMAgent("agent_event",event_road_length/event_max_speed,maxSimulationTime);
		EventModelArtifact eventModel = new EventModelArtifact("Road", event_road_length, event_max_speed, event_min_speed, 1, event_initial_cars);
		eventAgent.setModelArtifact(eventModel);

		
		// coupling artifacts
		DDSEventCouplingArtifactReceiver macroDownstreamArt = new DDSEventCouplingArtifactReceiver("macroDownstreamArt", Tuple3.of(Double.class, Double.class, Double.class));
		macroDownstreamArt.addEventOperation(new FlowRate2Group(time_step));
		
		DDSEventCouplingArtifactSender eventDownstreamArt = new DDSEventCouplingArtifactSender("eventDownstreamArt");
		
		// upstream coupling
		eventAgent.addInputCouplingArtifact(macroDownstreamArt,"in0");
		eventAgent.addOutputCouplingArtifact(eventDownstreamArt,"out0");

		/*
		 * OBSERVATION
		 */

		DDSEventCouplingArtifactSender event2Obs = new DDSEventCouplingArtifactSender("event2Obs");

		eventAgent.addOutputCouplingArtifact(event2Obs,"observation");
				
		eventAgent.startModelSoftware();
		
		eventAgent.setModelParameters();
		
		eventAgent.start();

	}
}

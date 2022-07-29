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
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.operation.time.MultiplicationTimeOperation;
import mecsyco.core.type.ArrayedSimulVector;
import mecsyco.core.type.Tuple1;
import mecsyco.core.type.Tuple3;
import mecsyco.observing.jfreechart.xy.LiveTXGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsycoscholar.application.highway.operation.Density2Cars;
import mecsycoscholar.application.highway.operation.MacroDataToDouble;
import mecsycoscholar.application.highway.operation.Turtle2SimulDataDouble;
import mecsycoscholar.application.highway.operation.Turtles2Flow;


public class ObservationLauncher {

	
	public static double maxSimulationTime = 4;
	
	/*
	 * MACRO PARAMETERS
	 */
	
	// length of a cell (km)
	public static double length = 0.2;
	
	// number of lane in the macro model
	public static int nb_voie = 3;

	// critical density (veh/km/lane)
	public static double critical_density = 45;

	// km/h
	public static double free_speed = 100;

	// h
	public static double time_step = 0.0001;

	// density in veh/km/voie
	public static double initial_density = 30;

	// speed km/h
	public static double initial_speed = 70;

	public static int nbCells = 5;

	public static double capacity = 1800;

	public static double tau = 1;

	public static double kappa = 1;

	public static double nu = 1;
	
	/*
	 * MICRO PARAMETERS
	 */

	// maximum speed in the micro model (km/h)
	public static double microMaxSpeed = 100;

	// length of a micro time step (h)
	public static double micro_timestep = 0.00004;

	// Length of the micro model (km)
	public static double micro_length = 0.4;

	// size of a patch in the micro model (km)
	public static double patch_length = 0.004;
	
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


		// Observing agent
		ObservingMAgent ObservingAgent = new ObservingMAgent("observing agent", maxSimulationTime);
		SwingDispatcherArtifact ObservingAgentModelArtifact = new SwingDispatcherArtifact();
		ObservingAgent.setDispatcherArtifact(ObservingAgentModelArtifact);

		
		// Number of cars observing
		ObservingAgentModelArtifact.addObservingArtifact(new LiveTXGraphic("Evolution of the number of vehicles", "Number of vehicles",
				Renderer.Line, new String[] { "Cell1", "Micro","Event" }, new String[] { "Cell1", "Micro","Event" }));

		// Observation coupling
		DDSEventCouplingArtifactReceiver c1ObservationArt = new DDSEventCouplingArtifactReceiver("c1ObservationArt",Tuple1.of(Double.class));
		c1ObservationArt.addEventOperation(new Density2Cars(length, nb_voie,nbCells));

		DDSEventCouplingArtifactReceiver microObservationArt = new DDSEventCouplingArtifactReceiver("microObservationArt",ArrayedSimulVector.of(ArrayedSimulVector.of(String.class)));
		microObservationArt.addEventOperation(new Turtle2SimulDataDouble());
		microObservationArt.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));
		
		DDSEventCouplingArtifactReceiver event2Obs = new DDSEventCouplingArtifactReceiver("event2Obs",Tuple1.of(Double.class));

		ObservingAgent.addInputCouplingArtifact(c1ObservationArt, "Cell1");
		ObservingAgent.addInputCouplingArtifact(microObservationArt, "Micro");
		ObservingAgent.addInputCouplingArtifact(event2Obs,"Event");
		
		// Interactions observation
		
		DDSEventCouplingArtifactReceiver micro2obsFlowRate= new DDSEventCouplingArtifactReceiver("micro2obsFlowRate",ArrayedSimulVector.of(ArrayedSimulVector.of(String.class)));
		micro2obsFlowRate.addEventOperation(new Turtles2Flow(length,micro_length,patch_length,microMaxSpeed,nb_voie));
		micro2obsFlowRate.addEventOperation(new MacroDataToDouble("flow"));
		micro2obsFlowRate.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));
		
		DDSEventCouplingArtifactReceiver macro2obsFlowRate= new DDSEventCouplingArtifactReceiver("macro2obsFlowRate",Tuple3.of(Double.class, Double.class, Double.class));
		macro2obsFlowRate.addEventOperation(new MacroDataToDouble("flow"));
		
		
		ObservingAgentModelArtifact.addObservingArtifact(new LiveTXGraphic("Output flowrate", "veh/h",
				Renderer.Line, new String[] { "Cell1", "Micro"}, new String[] { "Cell1Flow", "MicroFlow"}));
		
		ObservingAgent.addInputCouplingArtifact(micro2obsFlowRate,"MicroFlow");
		ObservingAgent.addInputCouplingArtifact(macro2obsFlowRate,"Cell1Flow");
				
		ObservingAgent.startModelSoftware();			
		ObservingAgent.start();
	}
}

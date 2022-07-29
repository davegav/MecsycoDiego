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

package mecsycoscholar.application.highway.launcher.mixed;

import java.io.IOException;

import org.jdom2.JDOMException;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.operation.time.DivisionTimeOperation;
import mecsyco.core.operation.time.MultiplicationTimeOperation;
import mecsyco.description.world.NetLogoModelDescription;
import mecsyco.observing.jfreechart.xy.LiveTXGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsyco.observing.swing.util.InitJFrame;
import mecsyco.world.netlogo.NetLogoModelArtifact;
import mecsycoscholar.application.highway.model.equation.CellModelArtifact;
import mecsycoscholar.application.highway.model.event.EventModelArtifact;
import mecsycoscholar.application.highway.model.mas.MicroModelArtifact;
import mecsycoscholar.application.highway.operation.Density2Cars;
import mecsycoscholar.application.highway.operation.FlowRate2Group;
import mecsycoscholar.application.highway.operation.Group2Turtle;
import mecsycoscholar.application.highway.operation.MacroDataToDouble;
import mecsycoscholar.application.highway.operation.Turtle2SimulDataDouble;
import mecsycoscholar.application.highway.operation.Turtles2Flow;

public class HybridHighwayTrafficLauncher {

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

	public static void main(String[] args) {
		launchHybridHighWayOld();

	}

	/**
	 * Example with the new NetLogo model artifact
	 */
	public static void launchHybridHighWay() {
		NetLogoModelArtifact.setdefaultCloseOperationJFrame(InitJFrame.getDefaultCloseOperationJFrame());
		// First Cell
		EventMAgent cellAgent1 = new EventMAgent("agent_cell_1", maxSimulationTime);
		CellModelArtifact CellModel1 = new CellModelArtifact("tronçon 1", length, time_step, initial_density,
				initial_speed, free_speed, capacity, critical_density, nb_voie, tau, kappa, nu, nbCells);
		cellAgent1.setModelArtifact(CellModel1);

		// Multi-agent model
		EventMAgent microAgent = new EventMAgent("agent_micro", maxSimulationTime / micro_timestep);
		NetLogoModelDescription md;
		try {
			md = new NetLogoModelDescription("Library\\NetLogo\\Micro.xml");
			NetLogoModelArtifact microModel = (NetLogoModelArtifact) md.getModelArtifact();
			microAgent.setModelArtifact(microModel);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}

		// Event model

		EventMAgent eventAgent = new EventMAgent("agent_event", event_road_length / event_max_speed, maxSimulationTime);
		EventModelArtifact eventModel = new EventModelArtifact("Road", event_road_length, event_max_speed,
				event_min_speed, 1, event_initial_cars);
		eventAgent.setModelArtifact(eventModel);

		// coupling artifacts
		CentralizedEventCouplingArtifact macroDownstreamArt = new CentralizedEventCouplingArtifact();
		macroDownstreamArt.addEventOperation(new FlowRate2Group(time_step));

		CentralizedEventCouplingArtifact microDownstreamArt = new CentralizedEventCouplingArtifact();
		microDownstreamArt
				.addEventOperation(new Turtles2Flow(length, micro_length, patch_length, microMaxSpeed, nb_voie));
		microDownstreamArt.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));

		CentralizedEventCouplingArtifact eventDownstreamArt = new CentralizedEventCouplingArtifact();
		eventDownstreamArt.addEventOperation(new Group2Turtle(microMaxSpeed));
		eventDownstreamArt.addTimeOperation(new DivisionTimeOperation(micro_timestep));

		// CentralizedEventCouplingArtifact eventUpstreamArt = new
		// CentralizedEventCouplingArtifact();
		// eventUpstreamArt.setEventOperation(new
		// Group2Density(length,event_road_length));

		// simulation building
		// downstream coupling
		// eventAgent.addOutputCouplingArtifact(eventUpstreamArt,"state");
		// cellAgent1.addInputCouplingArtifact(eventUpstreamArt,"downstream");

		// upstream coupling
		cellAgent1.addInputCouplingArtifact(microDownstreamArt, "upstream");
		cellAgent1.addOutputCouplingArtifact(macroDownstreamArt, "downstream");
		eventAgent.addInputCouplingArtifact(macroDownstreamArt, "in0");
		eventAgent.addOutputCouplingArtifact(eventDownstreamArt, "out0");
		microAgent.addInputCouplingArtifact(eventDownstreamArt, "in");
		microAgent.addOutputCouplingArtifact(microDownstreamArt, "out");

		/*
		 * OBSERVATION
		 */

		// Observing agent
		ObservingMAgent ObservingAgent = new ObservingMAgent("observing agent", maxSimulationTime);
		SwingDispatcherArtifact ObservingAgentModelArtifact = new SwingDispatcherArtifact();
		ObservingAgent.setDispatcherArtifact(ObservingAgentModelArtifact);

		// Number of cars observing
		ObservingAgentModelArtifact.addObservingArtifact(
				new LiveTXGraphic("Evolution of the number of vehicles", "Number of vehicles", Renderer.Line,
						new String[] {"Cell1", "Micro", "Event"}, new String[] {"Cell1", "Micro", "Event"}));

		// Observation coupling
		CentralizedEventCouplingArtifact c1ObservationArt = new CentralizedEventCouplingArtifact();
		c1ObservationArt.addEventOperation(new Density2Cars(length, nb_voie, nbCells));

		CentralizedEventCouplingArtifact microObservationArt = new CentralizedEventCouplingArtifact();
		microObservationArt.addEventOperation(new Turtle2SimulDataDouble());
		microObservationArt.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));

		CentralizedEventCouplingArtifact event2Obs = new CentralizedEventCouplingArtifact();

		cellAgent1.addOutputCouplingArtifact(c1ObservationArt, "observation");
		microAgent.addOutputCouplingArtifact(microObservationArt, "out");
		eventAgent.addOutputCouplingArtifact(event2Obs, "observation");

		ObservingAgent.addInputCouplingArtifact(c1ObservationArt, "Cell1");
		ObservingAgent.addInputCouplingArtifact(microObservationArt, "Micro");
		ObservingAgent.addInputCouplingArtifact(event2Obs, "Event");

		// Interactions observation

		CentralizedEventCouplingArtifact micro2obsFlowRate = new CentralizedEventCouplingArtifact();
		micro2obsFlowRate
				.addEventOperation(new Turtles2Flow(length, micro_length, patch_length, microMaxSpeed, nb_voie));
		micro2obsFlowRate.addEventOperation(new MacroDataToDouble("flow"));
		micro2obsFlowRate.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));

		CentralizedEventCouplingArtifact macro2obsFlowRate = new CentralizedEventCouplingArtifact();
		macro2obsFlowRate.addEventOperation(new MacroDataToDouble("flow"));

		ObservingAgentModelArtifact.addObservingArtifact(new LiveTXGraphic("Output flowrate", "veh/h", Renderer.Line,
				new String[] {"Cell1", "Micro"}, new String[] {"Cell1Flow", "MicroFlow"}));

		cellAgent1.addOutputCouplingArtifact(macro2obsFlowRate, "downstream");
		microAgent.addOutputCouplingArtifact(micro2obsFlowRate, "out");

		ObservingAgent.addInputCouplingArtifact(micro2obsFlowRate, "MicroFlow");
		ObservingAgent.addInputCouplingArtifact(macro2obsFlowRate, "Cell1Flow");

		cellAgent1.startModelSoftware();
		microAgent.startModelSoftware();
		eventAgent.startModelSoftware();
		ObservingAgent.startModelSoftware();

		cellAgent1.setModelParameters();
		microAgent.setModelParameters();
		eventAgent.setModelParameters();

		ObservingAgent.start();
		cellAgent1.start();
		microAgent.start();
		eventAgent.start();
	}

	/**
	 * Old example with the updated Micro model artifact
	 */
	public static void launchHybridHighWayOld() {
		// First Cell
		EventMAgent cellAgent1 = new EventMAgent("agent_cell_1", maxSimulationTime);
		CellModelArtifact CellModel1 = new CellModelArtifact("tronçon 1", length, time_step, initial_density,
				initial_speed, free_speed, capacity, critical_density, nb_voie, tau, kappa, nu, nbCells);
		cellAgent1.setModelArtifact(CellModel1);

		// Multi-agent model
		EventMAgent microAgent = new EventMAgent("agent_micro", maxSimulationTime / micro_timestep);
		MicroModelArtifact microModel = new MicroModelArtifact("My Models/netlogo/Traffic-2Roads-100.nlogo", "Road1",
				230, 1250);
		microAgent.setModelArtifact(microModel);

		// Event model

		EventMAgent eventAgent = new EventMAgent("agent_event", event_road_length / event_max_speed, maxSimulationTime);
		// Note that there is only one output (fixed) as the vehicles can only go in one
		// direction.
		// Number of outputs isn't link to the number of lanes in this event model.
		EventModelArtifact eventModel = new EventModelArtifact("Road", event_road_length, event_max_speed,
				event_min_speed, 1, event_initial_cars);
		eventAgent.setModelArtifact(eventModel);

		// coupling artifacts
		CentralizedEventCouplingArtifact macroDownstreamArt = new CentralizedEventCouplingArtifact();
		macroDownstreamArt.addEventOperation(new FlowRate2Group(time_step));

		CentralizedEventCouplingArtifact microDownstreamArt = new CentralizedEventCouplingArtifact();
		microDownstreamArt
				.addEventOperation(new Turtles2Flow(length, micro_length, patch_length, microMaxSpeed, nb_voie));
		microDownstreamArt.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));

		CentralizedEventCouplingArtifact eventDownstreamArt = new CentralizedEventCouplingArtifact();
		eventDownstreamArt.addEventOperation(new Group2Turtle(microMaxSpeed));
		eventDownstreamArt.addTimeOperation(new DivisionTimeOperation(micro_timestep));

		// upstream coupling
		cellAgent1.addInputCouplingArtifact(microDownstreamArt, "upstream");
		cellAgent1.addOutputCouplingArtifact(macroDownstreamArt, "downstream");
		eventAgent.addInputCouplingArtifact(macroDownstreamArt, "in0");
		eventAgent.addOutputCouplingArtifact(eventDownstreamArt, "out0");
		microAgent.addInputCouplingArtifact(eventDownstreamArt, "in");
		microAgent.addOutputCouplingArtifact(microDownstreamArt, "out");

		/*
		 * OBSERVATION
		 */

		// Observing agent
		ObservingMAgent ObservingAgent = new ObservingMAgent("observing agent", maxSimulationTime);
		SwingDispatcherArtifact ObservingAgentModelArtifact = new SwingDispatcherArtifact();
		ObservingAgent.setDispatcherArtifact(ObservingAgentModelArtifact);

		// Number of cars observing
		ObservingAgentModelArtifact.addObservingArtifact(
				new LiveTXGraphic("Evolution of the number of vehicles", "Number of vehicles", Renderer.Line,
						new String[] {"Cell1", "Micro", "Event"}, new String[] {"Cell1", "Micro", "Event"}));

		// Observation coupling
		CentralizedEventCouplingArtifact c1ObservationArt = new CentralizedEventCouplingArtifact();
		c1ObservationArt.addEventOperation(new Density2Cars(length, nb_voie, nbCells));

		CentralizedEventCouplingArtifact microObservationArt = new CentralizedEventCouplingArtifact();
		microObservationArt.addEventOperation(new Turtle2SimulDataDouble());
		microObservationArt.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));

		CentralizedEventCouplingArtifact event2Obs = new CentralizedEventCouplingArtifact();

		cellAgent1.addOutputCouplingArtifact(c1ObservationArt, "observation");
		microAgent.addOutputCouplingArtifact(microObservationArt, "out");
		eventAgent.addOutputCouplingArtifact(event2Obs, "observation");

		ObservingAgent.addInputCouplingArtifact(c1ObservationArt, "Cell1");
		ObservingAgent.addInputCouplingArtifact(microObservationArt, "Micro");
		ObservingAgent.addInputCouplingArtifact(event2Obs, "Event");

		// Interactions observation

		CentralizedEventCouplingArtifact micro2obsFlowRate = new CentralizedEventCouplingArtifact();
		micro2obsFlowRate
				.addEventOperation(new Turtles2Flow(length, micro_length, patch_length, microMaxSpeed, nb_voie));
		micro2obsFlowRate.addEventOperation(new MacroDataToDouble("flow"));
		micro2obsFlowRate.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));

		CentralizedEventCouplingArtifact macro2obsFlowRate = new CentralizedEventCouplingArtifact();
		macro2obsFlowRate.addEventOperation(new MacroDataToDouble("flow"));

		ObservingAgentModelArtifact.addObservingArtifact(new LiveTXGraphic("Output flowrate", "veh/h", Renderer.Line,
				new String[] {"Cell1", "Micro"}, new String[] {"Cell1Flow", "MicroFlow"}));

		cellAgent1.addOutputCouplingArtifact(macro2obsFlowRate, "downstream");
		microAgent.addOutputCouplingArtifact(micro2obsFlowRate, "out");

		ObservingAgent.addInputCouplingArtifact(micro2obsFlowRate, "MicroFlow");
		ObservingAgent.addInputCouplingArtifact(macro2obsFlowRate, "Cell1Flow");

		cellAgent1.startModelSoftware();
		microAgent.startModelSoftware();
		eventAgent.startModelSoftware();
		ObservingAgent.startModelSoftware();

		cellAgent1.setModelParameters();
		microAgent.setModelParameters();
		eventAgent.setModelParameters();

		// Obsolete
//		eventAgent.coInitialize();
//		cellAgent1.coInitialize();
//		microAgent.coInitialize();

		ObservingAgent.start();
		cellAgent1.start();
		microAgent.start();
		eventAgent.start();
	}
}

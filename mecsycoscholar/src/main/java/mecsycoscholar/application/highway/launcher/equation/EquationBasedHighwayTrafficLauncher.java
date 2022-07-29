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

package mecsycoscholar.application.highway.launcher.equation;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.observing.jfreechart.xy.LiveTXGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsycoscholar.application.highway.model.equation.CellModelArtifact;

public class EquationBasedHighwayTrafficLauncher {

	public static double maxSimulationTime = 4;

	public static double length = 0.5;

	public static double nb_voie = 3;

	public static double critical_density = 45;

	public static double free_speed = 100;

	public static double time_step = 0.003;

	public static double initial_density = 40;

	public static double initial_speed = 70;

	public static int nbCells = 10;

	public static double capacity = 1800;

	public static double tau = 1;

	public static double kappa = 1;

	public static double nu = 1;

	public static int delay=50; // Slow down factor for human readability

	public static void main (String[] args) {
		// First Cell
		EventMAgent cellAgent1 = new EventMAgent("cell_1", maxSimulationTime);
		CellModelArtifact CellModel1 = new CellModelArtifact("tronçon 1", length, time_step, initial_density,
				initial_speed, free_speed, capacity, critical_density, nb_voie, tau, kappa, nu, nbCells,delay);
		cellAgent1.setModelArtifact(CellModel1);

		// Second Cell
		EventMAgent cellAgent2 = new EventMAgent("cell_2", maxSimulationTime);
		CellModelArtifact CellModel2 = new CellModelArtifact("tronçon 2", length, time_step, 0, 0, free_speed,
				capacity, critical_density, nb_voie, tau, kappa, nu, nbCells,delay);
		cellAgent2.setModelArtifact(CellModel2);

		// Simulation coupling
		CentralizedEventCouplingArtifact c1UpstreamArt = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact c1DownstreamArt = new CentralizedEventCouplingArtifact();

		CentralizedEventCouplingArtifact c2UpstreamArt = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact c2DownstreamArt = new CentralizedEventCouplingArtifact();

		cellAgent1.addOutputCouplingArtifact(c1UpstreamArt, "upstream");
		cellAgent1.addOutputCouplingArtifact(c1DownstreamArt, "downstream");

		cellAgent1.addInputCouplingArtifact(c2UpstreamArt, "downstream");
		cellAgent1.addInputCouplingArtifact(c2DownstreamArt, "upstream");

		cellAgent2.addOutputCouplingArtifact(c2UpstreamArt, "upstream");
		cellAgent2.addOutputCouplingArtifact(c2DownstreamArt, "downstream");

		cellAgent2.addInputCouplingArtifact(c1UpstreamArt, "downstream");
		cellAgent2.addInputCouplingArtifact(c1DownstreamArt, "upstream");

		// Observing agent
		ObservingMAgent ObservingAgent = new ObservingMAgent("agent_obs", maxSimulationTime);
		SwingDispatcherArtifact ObservingAgentModelArtifact = new SwingDispatcherArtifact();
		ObservingAgent.setDispatcherArtifact(ObservingAgentModelArtifact);

		ObservingAgentModelArtifact.addObservingArtifact(new LiveTXGraphic("Evolution of the number of vehicles", "Number of vehicles",
				Renderer.Line, new String[] { "Cell1", "Cell2" }, new String[] { "Cell1", "Cell2" }));

		// Observation coupling
		CentralizedEventCouplingArtifact c1ObservationArt = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact c2ObservationArt = new CentralizedEventCouplingArtifact();

		cellAgent1.addOutputCouplingArtifact(c1ObservationArt, "observation");
		cellAgent2.addOutputCouplingArtifact(c2ObservationArt, "observation");

		ObservingAgent.addInputCouplingArtifact(c1ObservationArt, "Cell1");
		ObservingAgent.addInputCouplingArtifact(c2ObservationArt, "Cell2");

		ObservingAgent.startModelSoftware();
		cellAgent1.startModelSoftware();
		cellAgent2.startModelSoftware();

		cellAgent1.coInitialize();
		cellAgent2.coInitialize();

		ObservingAgent.start();
		cellAgent1.start();
		cellAgent2.start();

	}
}

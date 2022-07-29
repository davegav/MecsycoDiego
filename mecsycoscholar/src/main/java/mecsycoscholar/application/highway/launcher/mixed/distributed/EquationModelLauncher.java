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
import mecsyco.core.operation.time.MultiplicationTimeOperation;
import mecsyco.core.type.ArrayedSimulVector;
import mecsycoscholar.application.highway.model.equation.CellModelArtifact;
import mecsycoscholar.application.highway.operation.Turtles2Flow;


public class EquationModelLauncher {


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

	public static void main (String[] args) {
		// First Cell
		EventMAgent cellAgent1 = new EventMAgent("agent_cell_1", maxSimulationTime);
		CellModelArtifact CellModel1 = new CellModelArtifact("tronçon 1", length, time_step, initial_density,
				initial_speed, free_speed, capacity, critical_density, nb_voie, tau, kappa, nu, nbCells);
		cellAgent1.setModelArtifact(CellModel1);

		// coupling artifacts
		DDSEventCouplingArtifactSender macroDownstreamArt = new DDSEventCouplingArtifactSender("macroDownstreamArt");

		DDSEventCouplingArtifactReceiver microDownstreamArt = new DDSEventCouplingArtifactReceiver("microDownstreamArt", ArrayedSimulVector.of(ArrayedSimulVector.of(String.class)));
		microDownstreamArt.addEventOperation(new Turtles2Flow(length,micro_length,patch_length,microMaxSpeed,nb_voie));
		microDownstreamArt.addTimeOperation(new MultiplicationTimeOperation(micro_timestep));


		// upstream coupling
		cellAgent1.addInputCouplingArtifact(microDownstreamArt,"upstream");
		cellAgent1.addOutputCouplingArtifact(macroDownstreamArt, "downstream");

		/*
		 * OBSERVATION
		 */

		// Observation coupling
		DDSEventCouplingArtifactSender c1ObservationArt = new DDSEventCouplingArtifactSender("c1ObservationArt");

		cellAgent1.addOutputCouplingArtifact(c1ObservationArt, "observation");


		// Interactions observation
		DDSEventCouplingArtifactSender macro2obsFlowRate= new DDSEventCouplingArtifactSender("macro2obsFlowRate");
		cellAgent1.addOutputCouplingArtifact(macro2obsFlowRate, "downstream");

		cellAgent1.startModelSoftware();
		
		cellAgent1.setModelParameters();
		
		cellAgent1.start();

	}
}

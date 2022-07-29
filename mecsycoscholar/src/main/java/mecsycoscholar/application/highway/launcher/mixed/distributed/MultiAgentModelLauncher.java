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

import java.io.IOException;

import org.jdom2.JDOMException;

import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactReceiver;
import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactSender;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.operation.time.DivisionTimeOperation;
import mecsyco.core.type.Tuple3;
import mecsyco.description.world.NetLogoModelDescription;
import mecsyco.world.netlogo.NetLogoModelArtifact;
import mecsycoscholar.application.highway.operation.Group2Turtle;

public class MultiAgentModelLauncher {

	public static double maxSimulationTime = 4;

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

	public static void main(String[] args) {
		launch();
	}

	public static void launch() {
		// Multi-agent model
		EventMAgent microAgent = new EventMAgent("agent_micro", maxSimulationTime / micro_timestep);
		NetLogoModelDescription md;
		try {
			md = new NetLogoModelDescription("Library\\NetLogo\\Micro.xml");
			NetLogoModelArtifact microModel = (NetLogoModelArtifact) md.getModelArtifact();
			microAgent.setModelArtifact(microModel);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// coupling artifacts
		DDSEventCouplingArtifactSender microDownstreamArt = new DDSEventCouplingArtifactSender("microDownstreamArt");

		DDSEventCouplingArtifactReceiver eventDownstreamArt = new DDSEventCouplingArtifactReceiver("eventDownstreamArt",
				Tuple3.of(Double.class, Integer.class, String.class));
		eventDownstreamArt.addEventOperation(new Group2Turtle(microMaxSpeed));
		eventDownstreamArt.addTimeOperation(new DivisionTimeOperation(micro_timestep));

		// upstream coupling
		microAgent.addInputCouplingArtifact(eventDownstreamArt, "in");
		microAgent.addOutputCouplingArtifact(microDownstreamArt, "out");

		/*
		 * OBSERVATION
		 */

		// Observation coupling
		DDSEventCouplingArtifactSender microObservationArt = new DDSEventCouplingArtifactSender("microObservationArt");

		microAgent.addOutputCouplingArtifact(microObservationArt, "out");

		// Interactions observation

		DDSEventCouplingArtifactSender micro2obsFlowRate = new DDSEventCouplingArtifactSender("micro2obsFlowRate");

		microAgent.addOutputCouplingArtifact(micro2obsFlowRate, "out");

		microAgent.startModelSoftware();
		microAgent.setModelParameters();

		microAgent.start();
	}

	public static void launchOld() {

	}
}

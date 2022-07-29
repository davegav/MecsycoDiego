/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta Copyright(c) 
Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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

package mecsycoscholar.application.ode.launcher.lotkavolterra.distributed;

import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactReceiver;
import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactSender;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.type.Tuple1;
import mecsycoscholar.application.ode.model.NetLogoModelArtifact;

/**
 * Launch a distributed simulation of a Lotka-Volterra system : Prey population: dx/dt = a*x-b*x*y
 * Predator population: dy/dt = c*x*y-d*y
 *
 * Each ODE is simulated by one NetLogo instance: Prey population: prey.nlogo Predator population:
 * predator.nlogo
 * 
 * A monolithic Netlogo simulation of the system can be found in the file prey-predator.nlogo
 *
 * 
 *
 */

public class LotkaVolterraPreyLauncher {

	// set the simulation time
	public final static double maxSimulationTime = 40;

	// set the step size of the prey model (between {0.01,0.001,0.0001})
	public final static double time_discretization_prey = 0.01;

	public static void main (String args[]) {

		// create the model agent for the prey model
		EventMAgent preyAgent = new EventMAgent("agent_prey", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact preyModelArtifact = new NetLogoModelArtifact("My Models/netlogo/prey.nlogo","Prey Model");
		preyAgent.setModelArtifact(preyModelArtifact);

		// create the coupling artifacts
		DDSEventCouplingArtifactReceiver couplingFromPredatorToPreyReceiver = new DDSEventCouplingArtifactReceiver(
				"PredatorToPrey", Tuple1.of(Double.class));
		DDSEventCouplingArtifactSender couplingFromPreyToPredatorSender = new DDSEventCouplingArtifactSender(
				"PreyToPredator");

		// link the coupling artifacts with the m-agents
		preyAgent.addInputCouplingArtifact(couplingFromPredatorToPreyReceiver, "Y");
		preyAgent.addOutputCouplingArtifact(couplingFromPreyToPredatorSender, "X");

		// initialize the model
		preyAgent.startModelSoftware();

		// set model parameters
		String arg_prey[] = { String.valueOf(time_discretization_prey) };
		preyAgent.setModelParameters(arg_prey);

		// first simulation step
		preyAgent.coInitialize();

		preyAgent.start();

	}
}

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

package mecsycoscholar.application.ode.launcher.lotkavolterra;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.observing.base.logging.SingleFileLoggingArtifact;
import mecsyco.observing.jfreechart.xy.LiveTXGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
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

public class PreyLauncher {

	// set the simulation time
	public final static double maxSimulationTime = 40;

	// set the step size of the prey model (between {0.01,0.001,0.0001})
	public final static double time_discretization_1 = 0.2;

	public final static double time_discretization_10 = 0.1;

	public final static double time_discretization_100 = 0.01;

	public static void main (String args[]) {

		// create the model agent for the prey model
		EventMAgent preyAgent1 = new EventMAgent("agent_prey_1", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact preyModelArtifact1 = new NetLogoModelArtifact("My Models/netlogo/prey.nlogo", "Prey Model");
		preyAgent1.setModelArtifact(preyModelArtifact1);

		// create the model agent for the prey model
		EventMAgent preyAgent10 = new EventMAgent("agent_predator_10", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact preyModelArtifact10 = new NetLogoModelArtifact("My Models/netlogo/prey.nlogo", "Prey Model");
		preyAgent10.setModelArtifact(preyModelArtifact10);

		// create the model agent for the prey model
		EventMAgent preyAgent100 = new EventMAgent("agent_predator_100", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact preyModelArtifact100 = new NetLogoModelArtifact("My Models/netlogo/prey.nlogo","Prey Model");
		preyAgent100.setModelArtifact(preyModelArtifact100);

		// create the model agent for the observing model
		ObservingMAgent obsAgent = new ObservingMAgent("agent_obs", maxSimulationTime);
		// create the observing model artifact and link it with the agent
		SwingDispatcherArtifact ObsModelArtifact = new SwingDispatcherArtifact();
		obsAgent.setDispatcherArtifact(ObsModelArtifact);

		// Create graphics
		ObsModelArtifact.addObservingArtifact(new LiveTXGraphic("title", "y", Renderer.Step, new String[] { "X", "Y", "Z" },
				new String[] { "X", "Y", "Z" }));

		ObsModelArtifact.addObservingArtifact(new SingleFileLoggingArtifact ("prey.csv", new String[] { "X", "Y", "Z" }));

		obsAgent.setDispatcherArtifact(ObsModelArtifact);

		// create the coupling artifacts
		CentralizedEventCouplingArtifact couplingPrey1 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingPrey10 = new CentralizedEventCouplingArtifact();
		CentralizedEventCouplingArtifact couplingPrey100 = new CentralizedEventCouplingArtifact();

		// link the coupling artifacts with the m-agents
		preyAgent1.addOutputCouplingArtifact(couplingPrey1, "X");
		preyAgent10.addOutputCouplingArtifact(couplingPrey10, "X");
		preyAgent100.addOutputCouplingArtifact(couplingPrey100, "X");

		obsAgent.addInputCouplingArtifact(couplingPrey1, "X");
		obsAgent.addInputCouplingArtifact(couplingPrey10, "Y");
		obsAgent.addInputCouplingArtifact(couplingPrey100, "Z");

		// initialize the model
		preyAgent1.startModelSoftware();
		preyAgent10.startModelSoftware();
		preyAgent100.startModelSoftware();

		obsAgent.startModelSoftware();

		// set model parameters
		String arg_prey1[] = { String.valueOf(time_discretization_1) };
		String arg_prey10[] = { String.valueOf(time_discretization_10) };
		String arg_prey100[] = { String.valueOf(time_discretization_100) };

		preyAgent1.setModelParameters(arg_prey1);
		preyAgent10.setModelParameters(arg_prey10);
		preyAgent100.setModelParameters(arg_prey100);

		// first simulation step
		preyAgent1.coInitialize();
		preyAgent10.coInitialize();
		preyAgent100.coInitialize();

		preyAgent1.start();
		preyAgent10.start();
		preyAgent100.start();

		obsAgent.start();

	}
}

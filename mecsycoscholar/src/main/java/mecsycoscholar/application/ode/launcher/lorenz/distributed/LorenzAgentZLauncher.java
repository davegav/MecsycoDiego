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

package mecsycoscholar.application.ode.launcher.lorenz.distributed;

import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactReceiver;
import mecsyco.communication.dds.coupling.DDSEventCouplingArtifactSender;
import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.type.Tuple1;
import mecsyco.observing.jfreechart.xy.LiveTXGraphic;
import mecsyco.observing.jfreechart.xy.Renderer;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsycoscholar.application.ode.model.NetLogoModelArtifact;


/**
 * Launch a distributed simulation of a Lorenz system :
 * dx/dt = a*(y-x)
 * dy/dt = x*(b-z)
 * dz/dt = x*y - c*z
 * 
 * Each ODE is simulated by one NetLogo instance:
 *  dx/dt: Lorenz-X.nlogo
 *  dy/dt: Lorenz-Y.nlogo
 *  dz/dt: Lorenz-Z.nlogo
 *  
 *  A monolithic Netlogo simulation of the system can be found in the file Lorenz.nlogo
 *  
 *  
 */


public class LorenzAgentZLauncher {

	// set the global simulation time
	public final static double maxSimulationTime=80;

	// set the step size of the Z model (between {0.01,0.001,0.0001})
	public final static double time_discretization_Z=0.01;

	public static void main(String args[]){

		// create the model agent for the Z model
		EventMAgent ZAgent = new EventMAgent("agent_z", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact ZModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-Z.nlogo","Evolution of the distortion of the vertical temperature from linearity");
		ZAgent.setModelArtifact(ZModelArtifact);


		// create the coupling artifacts
		DDSEventCouplingArtifactReceiver XOutputToZReceiver=new DDSEventCouplingArtifactReceiver("XOutputToZ", Tuple1.of(Number.class));



		DDSEventCouplingArtifactReceiver YOutputToZReceiver=new DDSEventCouplingArtifactReceiver("YOutputToZ", Tuple1.of(Number.class));

		DDSEventCouplingArtifactSender ZOutputToYSender=new DDSEventCouplingArtifactSender("ZOutputToY");



		ObservingMAgent obsAgent = new ObservingMAgent("agent_obs", maxSimulationTime);
		// create the observing model artifact and link it with the agent
		SwingDispatcherArtifact ObsModelArtifact = new SwingDispatcherArtifact();
		obsAgent.setDispatcherArtifact(ObsModelArtifact);

		DDSEventCouplingArtifactReceiver XOutputToObs=new DDSEventCouplingArtifactReceiver("XOutputToObs", Tuple1.of(Number.class));
		DDSEventCouplingArtifactReceiver YOutputToZObs=new DDSEventCouplingArtifactReceiver("YOutputToObs", Tuple1.of(Number.class));
		CentralizedEventCouplingArtifact ZoutputToObs=new CentralizedEventCouplingArtifact();

		// set the output link 
		ZAgent.addOutputCouplingArtifact(ZoutputToObs,"Z");

		//set the input link			
		ZAgent.addInputCouplingArtifact(XOutputToZReceiver,"X");
		ZAgent.addInputCouplingArtifact(YOutputToZReceiver,"Y");

		ZAgent.addOutputCouplingArtifact(ZOutputToYSender,"Z");
		obsAgent.addInputCouplingArtifact(XOutputToObs,"XOutputToObs");
		obsAgent.addInputCouplingArtifact(YOutputToZObs,"YOutputToObs");
		obsAgent.addInputCouplingArtifact(ZoutputToObs,"ZoutputToObs");

		ObsModelArtifact.addObservingArtifact(new LiveTXGraphic("title", "y", Renderer.Line, new String[] { "Series1",
				"Serie2", "Serie3" },
				new String[] { "XOutputToObs", "YOutputToObs", "ZoutputToObs" }));

		// initialize the model
		ZAgent.startModelSoftware();
		obsAgent.startModelSoftware();

		// set model parameters
		String argZ[]={ String.valueOf(time_discretization_Z) };
		ZAgent.setModelParameters(argZ);



		// send the initial value of each models
		ZAgent.coInitialize();
		ZAgent.start();
		obsAgent.start();

	}
}

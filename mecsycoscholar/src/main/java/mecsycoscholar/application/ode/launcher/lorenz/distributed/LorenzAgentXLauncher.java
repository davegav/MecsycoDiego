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
import mecsyco.core.type.Tuple1;
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


public class LorenzAgentXLauncher {

	// set the global simulation time
	public final static double maxSimulationTime=80;

	// set the step size of the X model (between {0.01,0.001,0.0001})
	public final static double time_discretization_X=0.01;

	public static void main(String args[]){

		// create the model agent for the X model
		EventMAgent XAgent = new EventMAgent("agent_x", maxSimulationTime);
		// create the model artifact and link it with the agent
		NetLogoModelArtifact XModelArtifact = new NetLogoModelArtifact("My Models/netlogo/Lorenz-X.nlogo","Evolution of the intensity of the convection motion");
		XAgent.setModelArtifact(XModelArtifact);



		// create the coupling artifacts
		DDSEventCouplingArtifactSender XOutputToZSender=new DDSEventCouplingArtifactSender("XOutputToZ");
		DDSEventCouplingArtifactSender XOutputToObs=new DDSEventCouplingArtifactSender("XOutputToObs");


		DDSEventCouplingArtifactSender XOutputToYSender=new DDSEventCouplingArtifactSender("XOutputToY");

		DDSEventCouplingArtifactReceiver YOutputToXReceiver=new DDSEventCouplingArtifactReceiver("YOutputToX", Tuple1.of(Number.class));



		// set the output link 
		XAgent.addOutputCouplingArtifact(XOutputToZSender,"X");
		XAgent.addOutputCouplingArtifact(XOutputToYSender,"X");
		XAgent.addOutputCouplingArtifact(XOutputToObs,"X");


		//set the input link
		XAgent.addInputCouplingArtifact(YOutputToXReceiver,"Y");

		// initialize the model
		XAgent.startModelSoftware();

		// set model parameters
		String argX[]={ String.valueOf(time_discretization_X) };
		XAgent.setModelParameters(argX);


		// send the initial value of each models
		XAgent.coInitialize();

		XAgent.start();

	}
}

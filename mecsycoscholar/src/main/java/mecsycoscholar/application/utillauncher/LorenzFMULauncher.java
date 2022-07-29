package mecsycoscholar.application.utillauncher;

import java.util.HashMap;
import java.util.Map;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.multiplexer.AggregationMultiplexer;
import mecsyco.core.operation.aggregation.XYZStateAggregationOperation;
import mecsyco.observing.jzy3d.graphic.Live3DGraphic;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsyco.world.fmi.model.FMIModelArtifact;
import mecsycoscholar.helper.Utils;

public class LorenzFMULauncher {

	public static void main(String[] args) {
		double maxSimulationTime=50;
		double stepSize=0.01;
		double startTime=0.0;
		
		double x_init=1., y_init=1., z_init=4.;


		Map<String, Object> initVarsAgentX = new HashMap<>();
		initVarsAgentX.put("x",x_init);
		initVarsAgentX.put("a",10.);

		Map<String, Object> initVarsAgentY = new HashMap<>();
		initVarsAgentY.put("y",y_init);
		initVarsAgentY.put("b",28.);

		Map<String, Object> initVarsAgentZ = new HashMap<>();
		initVarsAgentZ.put("z",z_init);
		initVarsAgentZ.put("c",2.67);

		EventMAgent XAgent = new EventMAgent("agentX",maxSimulationTime);
		EventMAgent YAgent = new EventMAgent("agentY",maxSimulationTime);
		EventMAgent ZAgent = new EventMAgent("agentZ",maxSimulationTime);

		FMIModelArtifact XModelArtifact = new FMIModelArtifact ("Xmodel","./My Models/fmu/LorenzX_OM_Cs2_x64.fmu",
				maxSimulationTime,stepSize,startTime,initVarsAgentX);
		XAgent.setModelArtifact(XModelArtifact);
		FMIModelArtifact YModelArtifact = new FMIModelArtifact ("Ymodel","./My Models/fmu/LorenzY_OM_Cs2_x64.fmu",
				maxSimulationTime,stepSize,startTime,initVarsAgentY);
		YAgent.setModelArtifact(YModelArtifact);
		FMIModelArtifact ZModelArtifact = new FMIModelArtifact ("Zmodel","./My Models/fmu/LorenzZ_OM_Cs2_x64.fmu",
				maxSimulationTime,stepSize,startTime,initVarsAgentZ);
		ZAgent.setModelArtifact(ZModelArtifact);

		ObservingMAgent obs=new ObservingMAgent("obs", maxSimulationTime);
		SwingDispatcherArtifact Dispatcher=new SwingDispatcherArtifact();
		obs.setDispatcherArtifact(Dispatcher);

		//Links between agents
		Utils.connect(XAgent, ZAgent, "x");
		Utils.connect(XAgent, YAgent, "x");
		Utils.connect(YAgent, XAgent, "y");
		Utils.connect(YAgent, ZAgent, "y");
		Utils.connect(ZAgent, YAgent, "z");
		//links with multiplexer
		XYZStateAggregationOperation ope=new XYZStateAggregationOperation("x","y","z" );
		AggregationMultiplexer Mult = new AggregationMultiplexer("Mult", ope);

		Utils.connect(new EventMAgent[]{XAgent,YAgent,ZAgent}, new String[]{"x","y","z"}, Mult, obs, "XYZ");

		Dispatcher.addObservingArtifact (
				new Live3DGraphic("Lorenz 3D", "X", "Y", "Z", "XYZ"));






		XAgent.startModelSoftware();
		YAgent.startModelSoftware();
		ZAgent.startModelSoftware();
		obs.startModelSoftware();
		
		XAgent.setModelParameters();
		YAgent.setModelParameters();
		ZAgent.setModelParameters();

		XAgent.coInitialize();
		YAgent.coInitialize();
		ZAgent.coInitialize();


		XAgent.start();
		YAgent.start();
		ZAgent.start();
		obs.start();
		Mult.start();

	}

}

package mecsycoscholar.application.gettingstarted2.launcher;

import java.util.HashMap;
import java.util.Map;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.coupling.multiplexer.AggregationMultiplexer;
import mecsyco.core.operation.aggregation.VectorStateAggregationOperation;
import mecsyco.observing.jfreechart.bar.LiveBarGraphic;
import mecsyco.observing.jfreechart.pie.LivePieGraphic;
import mecsyco.observing.jzy3d.graphic.Live3DGraphic;
import mecsyco.observing.swing.dispatcher.SwingDispatcherArtifact;
import mecsyco.world.fmi.model.FMIModelArtifact;

/**
 * Third launcher, show the se of {@link FMIModelArtifact} on the same example
 * and add multipexer.
 *
 */
public final class Launcher3 {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		final double maxSimulationTime = 50;
		final double timeStep = 0.01;
		final double startTime = 0.0;

		final double initX = 1;
		final double initY = 1;
		final double initZ = 4;
		final double a = 10.;
		final double b = 28.;
		final double c = 2.67;

		Map<String, Object> initVarsAgentX = new HashMap<>();
		initVarsAgentX.put("x", initX); // Init value
		initVarsAgentX.put("y", initY); // Init value
		initVarsAgentX.put("a", a);

		Map<String, Object> initVarsAgentY = new HashMap<>();
		initVarsAgentY.put("x", initX); // Init value
		initVarsAgentY.put("y", initY); // Init value
		initVarsAgentY.put("z", initZ); // Init value
		initVarsAgentY.put("b", b);

		Map<String, Object> initVarsAgentZ = new HashMap<>();
		initVarsAgentZ.put("y", initY); // Init value
		initVarsAgentZ.put("z", initZ); // Init value
		initVarsAgentZ.put("c", c);

		EventMAgent xAgent = new EventMAgent("agentX", maxSimulationTime);
		EventMAgent yAgent = new EventMAgent("agentY", maxSimulationTime);
		EventMAgent zAgent = new EventMAgent("agentZ", maxSimulationTime);

		FMIModelArtifact xModelArtifact = new FMIModelArtifact("Xmodel", "./My Models/fmu/LorenzX_OM_Cs2_x64.fmu",
				maxSimulationTime, timeStep, startTime, initVarsAgentX);
		xAgent.setModelArtifact(xModelArtifact);
		FMIModelArtifact yModelArtifact = new FMIModelArtifact("Ymodel", "./My Models/fmu/LorenzY_OM_Cs2_x64.fmu",
				maxSimulationTime, timeStep, startTime, initVarsAgentY);
		yAgent.setModelArtifact(yModelArtifact);
		FMIModelArtifact zModelArtifact = new FMIModelArtifact("Zmodel", "./My Models/fmu/LorenzZ_OM_Cs2_x64.fmu",
				maxSimulationTime, timeStep, startTime, initVarsAgentZ);
		zAgent.setModelArtifact(zModelArtifact);

		ObservingMAgent obs = new ObservingMAgent("obs", maxSimulationTime);
		SwingDispatcherArtifact dispatcher = new SwingDispatcherArtifact();
		obs.setDispatcherArtifact(dispatcher);

		CentralizedEventCouplingArtifact xOutputToZ = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToZ, "x");
		zAgent.addInputCouplingArtifact(xOutputToZ, "x");
		CentralizedEventCouplingArtifact xOutputToY = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToY, "x");
		yAgent.addInputCouplingArtifact(xOutputToY, "x");
		CentralizedEventCouplingArtifact yOutputToX = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToX, "y");
		xAgent.addInputCouplingArtifact(yOutputToX, "y");
		CentralizedEventCouplingArtifact yOutputToZ = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToZ, "y");
		zAgent.addInputCouplingArtifact(yOutputToZ, "y");
		CentralizedEventCouplingArtifact zOutput = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutput, "z");
		yAgent.addInputCouplingArtifact(zOutput, "z");

		/* XY multiplexer */
//		XYStateAggregationOperation ope = new XYStateAggregationOperation("x", "y");
//		AggregationMultiplexer mult = new AggregationMultiplexer("Mult", ope);
//
//		CentralizedEventCouplingArtifact xOutputToMult = new CentralizedEventCouplingArtifact();
//		xAgent.addOutputCouplingArtifact(xOutputToMult, "x");
//		mult.addInput(xOutputToMult, "x");
//		CentralizedEventCouplingArtifact yOutputToMult = new CentralizedEventCouplingArtifact();
//		yAgent.addOutputCouplingArtifact(yOutputToMult, "y");
//		mult.addInput(yOutputToMult, "y");
//		CentralizedEventCouplingArtifact multToObs = new CentralizedEventCouplingArtifact();
//		mult.addOutput(multToObs, "XY");
//		obs.addInputCouplingArtifact(multToObs, "XY");
//
//		dispatcher.addObservingArtifact(new LiveXYGraphic("Lorenz phase", "X", "Y", Renderer.Line, "XY", "XY"));

		/* XYZ multiplexer */
//		XYZStateAggregationOperation ope = new XYZStateAggregationOperation("x", "y", "z");
//		AggregationMultiplexer mult = new AggregationMultiplexer("Mult", ope);
//
//		CentralizedEventCouplingArtifact xOutputToMult = new CentralizedEventCouplingArtifact();
//		xAgent.addOutputCouplingArtifact(xOutputToMult, "x");
//		mult.addInput(xOutputToMult, "x");
//		CentralizedEventCouplingArtifact yOutputToMult = new CentralizedEventCouplingArtifact();
//		yAgent.addOutputCouplingArtifact(yOutputToMult, "y");
//		mult.addInput(yOutputToMult, "y");
//		CentralizedEventCouplingArtifact zOutputToMult = new CentralizedEventCouplingArtifact();
//		zAgent.addOutputCouplingArtifact(zOutputToMult, "z");
//		mult.addInput(zOutputToMult, "z");
//		CentralizedEventCouplingArtifact multToObs = new CentralizedEventCouplingArtifact();
//		mult.addOutput(multToObs, "XYZ");
//		obs.addInputCouplingArtifact(multToObs, "XYZ");

		dispatcher.addObservingArtifact(new Live3DGraphic("Lorenz 3D", "X", "Y", "Z", "XYZ"));

		/* VECTOR multiplexer */
		VectorStateAggregationOperation ope = new VectorStateAggregationOperation(new String[] {"X", "Y", "Z"});
		AggregationMultiplexer mult = new AggregationMultiplexer("Mult", ope);

		CentralizedEventCouplingArtifact xOutputToMult = new CentralizedEventCouplingArtifact();
		xAgent.addOutputCouplingArtifact(xOutputToMult, "x");
		mult.addInput(xOutputToMult, "X");
		CentralizedEventCouplingArtifact yOutputToMult = new CentralizedEventCouplingArtifact();
		yAgent.addOutputCouplingArtifact(yOutputToMult, "y");
		mult.addInput(yOutputToMult, "Y");
		CentralizedEventCouplingArtifact zOutputToMult = new CentralizedEventCouplingArtifact();
		zAgent.addOutputCouplingArtifact(zOutputToMult, "z");
		mult.addInput(zOutputToMult, "Z");
		CentralizedEventCouplingArtifact multToObs = new CentralizedEventCouplingArtifact();
		mult.addOutput(multToObs, "XYZVector");
		obs.addInputCouplingArtifact(multToObs, "XYZVector");

		dispatcher.addObservingArtifact(
				new LiveBarGraphic("Lorenz bar", "Bars", "Value", new String[] {"X", "Y", "Z"}, "XYZVector"));
		dispatcher.addObservingArtifact(new LivePieGraphic("Lorenz Pie", new String[] {"X", "Y", "Z"}, "XYZVector"));

		xAgent.startModelSoftware();
		yAgent.startModelSoftware();
		zAgent.startModelSoftware();
		obs.startModelSoftware();

		xAgent.setModelParameters();
		yAgent.setModelParameters();
		zAgent.setModelParameters();

		xAgent.start();
		yAgent.start();
		zAgent.start();
		obs.start();
		mult.start();

	}

	private Launcher3() {

	}

}

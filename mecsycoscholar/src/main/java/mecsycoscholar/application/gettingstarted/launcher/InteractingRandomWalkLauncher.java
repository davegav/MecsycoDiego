package mecsycoscholar.application.gettingstarted.launcher;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.model.ModelArtifact;
import mecsycoscholar.application.gettingstarted.model.SimpleRandomWalkModelArtifact;

public final class InteractingRandomWalkLauncher {
	  public static final double RED = 15;
	  public static final double GREEN = 55;
	  public static final double BLUE = 105;
	  public static final double SIZE1 = 2;

	  public final static String firstWorldName = "World 1";

	  public final static String secondWorldName = "World 2";
	  
	  public final static String thirdWorldName = "World 3";

	  public final static int width = 460;

	  public final static int height = 520;

	  // Model settings
	  public final static double maxSimulationTime = 1000;

	  public final static String firstModelPath =
	    "My Models/netlogo/random_walk.nlogo";

	  public final static String secondModelPath =
	    "My Models/netlogo/random_walk.nlogo";
	  
	  public final static String thirdModelPath =
			    "My Models/netlogo/random_walk.nlogo";

	  // Entry point
	  public static void main(String... args) {
	    // Model 1
	    EventMAgent agent1 = new EventMAgent("world-1", maxSimulationTime);
	    ModelArtifact modelArtifact1 =
	      new SimpleRandomWalkModelArtifact(firstModelPath, firstWorldName, width, height, SIZE1, RED);
	    agent1.setModelArtifact(modelArtifact1);
	    CentralizedEventCouplingArtifact couplingArtifact1 =
	      new CentralizedEventCouplingArtifact();

	    // Model 2
	    EventMAgent agent2 = new EventMAgent("world-2", maxSimulationTime);
	    ModelArtifact modelArtifact2 =
	      new SimpleRandomWalkModelArtifact(secondModelPath, secondWorldName, width, height, SIZE1, GREEN);
	    agent2.setModelArtifact(modelArtifact2);
	    CentralizedEventCouplingArtifact couplingArtifact2 =
	      new CentralizedEventCouplingArtifact();
	    
	    // Model 3
	    EventMAgent agent3 = new EventMAgent("world-3", maxSimulationTime);
	    ModelArtifact modelArtifact3 =
	      new SimpleRandomWalkModelArtifact(thirdModelPath, thirdWorldName, width, height, SIZE1, BLUE);
	    agent3.setModelArtifact(modelArtifact3);
	    CentralizedEventCouplingArtifact couplingArtifact3 =
	      new CentralizedEventCouplingArtifact();

	    // Connections
	    agent1.addInputCouplingArtifact(couplingArtifact1, "in");
	    agent1.addOutputCouplingArtifact(couplingArtifact2, "out");
	    
	    agent2.addInputCouplingArtifact(couplingArtifact2, "in");
	    agent2.addOutputCouplingArtifact(couplingArtifact3, "out");
	    
	    agent3.addInputCouplingArtifact(couplingArtifact3, "in");
	    agent3.addOutputCouplingArtifact(couplingArtifact1, "out");

	    // Software initializations
	    agent1.startModelSoftware();
	    agent2.startModelSoftware();
	    agent3.startModelSoftware();

	    // Model initializations
	    agent1.setModelParameters();
	    agent2.setModelParameters();
	    agent3.setModelParameters();

	    // SImulation launching
	    agent1.start();
	    agent2.start();
	    agent3.start();
	  }
}

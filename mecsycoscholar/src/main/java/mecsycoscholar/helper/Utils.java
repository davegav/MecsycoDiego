package mecsycoscholar.helper;

import mecsyco.core.agent.EventMAgent;
import mecsyco.core.agent.ObservingMAgent;
import mecsyco.core.coupling.CentralizedEventCouplingArtifact;
import mecsyco.core.coupling.multiplexer.AggregationMultiplexer;

/**
 * Utility class with methods to connect agents.
 *
 */
public final class Utils {

	/* AGENT TO AGENT */

	/**
	 * Create a simple connection between two agents using the same port name for
	 * output and input
	 *
	 * @param source   : Source agent (sender)
	 * @param target   : Targeted agent (receiver)
	 * @param portName : Ports' name
	 */
	public static void connect(EventMAgent source, EventMAgent target, String portName) {
		CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
		source.addOutputCouplingArtifact(coupling, portName);
		target.addInputCouplingArtifact(coupling, portName);
	}

	/**
	 * Create a simple connection between two agents
	 *
	 * @param source         : Source agent (sender)
	 * @param target         : Targeted agent (receiver)
	 * @param sourcePortName : Output port's name
	 * @param targetPortName : Input port's name
	 *
	 */
	public static void connect(EventMAgent source, EventMAgent target, String sourcePortName, String targetPortName) {
		CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
		source.addOutputCouplingArtifact(coupling, sourcePortName);
		target.addInputCouplingArtifact(coupling, targetPortName);
	}

	/**
	 * Create multiple connection between two agents using the same port name for
	 * output and input
	 *
	 * @param source    : Source agent (sender)
	 * @param target    : Targeted agent (receiver)
	 * @param portNames : List of the ports connected
	 */
	public static void connect(EventMAgent source, EventMAgent target, String[] portNames) {
		for (String portName : portNames) {
			connect(source, target, portName);
		}
	}

	/**
	 * Create a simple connection between two agents
	 *
	 * @param source          : Source agent (sender)
	 * @param target          : Targeted agent (receiver)
	 * @param sourcePortNames : List of the output ports' name
	 * @param targetPortNames : List of the input ports' name
	 *
	 */
	public static void connect(EventMAgent source, EventMAgent target, String[] sourcePortNames,
			String[] targetPortNames) {
		if (sourcePortNames.length != targetPortNames.length) {
			Exception e = new Exception("You don't have the right number of ports(" + sourcePortNames.length
					+ " inputs for " + targetPortNames.length + " outputs) \n ");
			e.printStackTrace();

		} else {
			for (int i = 0; i < sourcePortNames.length; i++) {
				connect(source, target, sourcePortNames[i], targetPortNames[i]);
			}
		}
	}

	/* MULTIPLEXER */

	/**
	 * Create a complex connection using a multiplexer.
	 *
	 * @param sources         Table of source agents (senders)
	 * @param sourcePortNames Table of output ports' names of the senders
	 * @param multiplexer     Multiplexer with an aggregation operation
	 * @param target          Target agent (receiver)
	 * @param targetPortName  Input port's name of the target
	 */
	public static void connect(EventMAgent[] sources, String[] sourcePortNames, AggregationMultiplexer multiplexer,
			EventMAgent target, String targetPortName) {
		for (int i = 0; i < sources.length; i++) {
			CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
			sources[i].addOutputCouplingArtifact(coupling, sourcePortNames[i]);
			multiplexer.addInput(coupling, sourcePortNames[i]);
		}
		CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
		multiplexer.addOutput(coupling, targetPortName);
		target.addInputCouplingArtifact(coupling, targetPortName);
	}

	/**
	 * Create a complex connection using a multiplexer.
	 *
	 * @param sources              Table of source agents (senders)
	 * @param sourcePortNames      Table of output ports' names of the senders
	 * @param multiplexerPortNames Table of input ports' names of the multiplexer
	 * @param multiplexer          Multiplexer with an aggregation operation
	 * @param target               Target agent (receiver)
	 * @param targetPortName       Input port's name of the target
	 */
	public static void connect(EventMAgent[] sources, String[] sourcePortNames, String[] multiplexerPortNames,
			AggregationMultiplexer multiplexer, EventMAgent target, String targetPortName) {

		if (sourcePortNames.length != multiplexerPortNames.length) {
			Exception e = new Exception("You don't have the right number of ports(" + sourcePortNames.length
					+ " outputs for " + multiplexerPortNames.length + " inputs) \n ");
			e.printStackTrace();

		} else {
			for (int i = 0; i < sources.length; i++) {
				CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
				sources[i].addOutputCouplingArtifact(coupling, sourcePortNames[i]);
				multiplexer.addInput(coupling, multiplexerPortNames[i]);
			}
			CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
			multiplexer.addOutput(coupling, targetPortName);
			target.addInputCouplingArtifact(coupling, targetPortName);
		}
	}

	/**
	 * Create a complex connection using a multiplexer.
	 *
	 * @param sources         Table of source agents (senders)
	 * @param sourcePortNames Table of output ports' names of the senders
	 * @param multiplexer     Multiplexer with an aggregation operation
	 * @param targets         Table of target agents (receivers)
	 * @param targetPortNames Table of input ports' names of the targets
	 */
	public static void connect(EventMAgent[] sources, String[] sourcePortNames, AggregationMultiplexer multiplexer,
			EventMAgent[] targets, String[] targetPortNames) {
		for (int i = 0; i < sources.length; i++) {
			CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
			sources[i].addOutputCouplingArtifact(coupling, sourcePortNames[i]);
			multiplexer.addInput(coupling, sourcePortNames[i]);
		}
		for (int i = 0; i < targets.length; i++) {
			CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
			multiplexer.addOutput(coupling, targetPortNames[i]);
			targets[i].addInputCouplingArtifact(coupling, targetPortNames[i]);
		}

	}

	/**
	 * Create a complex connection to an observer using a multiplexer.
	 *
	 * @param sources         Table of source agents (senders)
	 * @param sourcePortNames Table of output ports' names of the senders
	 * @param multiplexer     Multiplexer with an aggregation operation
	 * @param target          Target observing agent (receiver)
	 * @param targetPortName  Input port's name of the target
	 */
	public static void connect(EventMAgent[] sources, String[] sourcePortNames, AggregationMultiplexer multiplexer,
			ObservingMAgent target, String targetPortName) {
		for (int i = 0; i < sources.length; i++) {
			CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
			sources[i].addOutputCouplingArtifact(coupling, sourcePortNames[i]);
			multiplexer.addInput(coupling, sourcePortNames[i]);
		}
		CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
		multiplexer.addOutput(coupling, targetPortName);
		target.addInputCouplingArtifact(coupling, targetPortName);
	}

	/**
	 * Create a complex connection using a multiplexer.
	 *
	 * @param sources              Table of source agents (senders)
	 * @param sourcePortNames      Table of output ports' names of the senders
	 * @param multiplexerPortNames Table of input ports' names of the multiplexer
	 * @param multiplexer          Multiplexer with an aggregation operation
	 * @param target               Target agent (receiver)
	 * @param targetPortName       Input port's name of the target
	 */
	public static void connect(EventMAgent[] sources, String[] sourcePortNames, String[] multiplexerPortNames,
			AggregationMultiplexer multiplexer, ObservingMAgent target, String targetPortName) {

		if (sourcePortNames.length != multiplexerPortNames.length) {
			Exception e = new Exception("You don't have the right number of ports(" + sourcePortNames.length
					+ " outputs for " + multiplexerPortNames.length + " inputs) \n ");
			e.printStackTrace();

		} else {
			for (int i = 0; i < sources.length; i++) {
				CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
				sources[i].addOutputCouplingArtifact(coupling, sourcePortNames[i]);
				multiplexer.addInput(coupling, multiplexerPortNames[i]);
			}
			CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
			multiplexer.addOutput(coupling, targetPortName);
			target.addInputCouplingArtifact(coupling, targetPortName);
		}
	}

	/* AGENT TO OBSERVING AGENT */

	/**
	 * Create a simple connection between an agent and an observing agent using the
	 * same port name for output and input
	 *
	 * @param source   : Source agent (sender)
	 * @param target   : Targeted observing agent (receiver)
	 * @param portName : Ports' name
	 */
	public static void connect(EventMAgent source, ObservingMAgent target, String portName) {
		CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
		source.addOutputCouplingArtifact(coupling, portName);
		target.addInputCouplingArtifact(coupling, portName);
	}

	/**
	 * Create a simple connection between an agent and an observing agent
	 *
	 * @param source         : Source agent (sender)
	 * @param target         : Targeted observing agent (receiver)
	 * @param sourcePortName : Output port's name
	 * @param targetPortName : Input port's name
	 *
	 */
	public static void connect(EventMAgent source, ObservingMAgent target, String sourcePortName,
			String targetPortName) {
		CentralizedEventCouplingArtifact coupling = new CentralizedEventCouplingArtifact();
		source.addOutputCouplingArtifact(coupling, sourcePortName);
		target.addInputCouplingArtifact(coupling, targetPortName);
	}

	/**
	 * Create multiple connection between an agent and an observing agent using the
	 * same port name for output and input
	 *
	 * @param source    : Source agent (sender)
	 * @param target    : Targeted observing agent (receiver)
	 * @param portNames : List of the ports connected
	 */
	public static void connect(EventMAgent source, ObservingMAgent target, String[] portNames) {
		for (String portName : portNames) {
			connect(source, target, portName);
		}
	}

	/**
	 * Create a simple connection between an agent and an observing agent
	 *
	 * @param source          : Source agent (sender)
	 * @param target          : Targeted Observing agent (receiver)
	 * @param sourcePortNames : List of the output ports' name
	 * @param targetPortNames : List of the input ports' name
	 *
	 */
	public static void connect(EventMAgent source, ObservingMAgent target, String[] sourcePortNames,
			String[] targetPortNames) {
		if (sourcePortNames.length != targetPortNames.length) {
			Exception e = new Exception("You don't have the right number of ports(" + sourcePortNames.length
					+ " inputs for " + targetPortNames.length + " outputs) \n ");
			e.printStackTrace();

		} else {
			for (int i = 0; i < sourcePortNames.length; i++) {
				connect(source, target, sourcePortNames[i], targetPortNames[i]);
			}
		}
	}

	/**
	 * Prevent instantiation.
	 */
	private Utils() {
	}

}

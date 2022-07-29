package mecsycoscholar.observing.base.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mecsyco.core.type.SimulEvent;
import mecsyco.observing.base.logging.LoggingArtifact;

/**
 * Use a single file for logging. Handle every kind of
 * {@link mecsyco.core.type.SimulData}, but only
 * {@link mecsyco.core.type.Tuple1} should be used if several ports are logged.
 */
public class SingleFileCustomLoggingArtifact extends LoggingArtifact {

	// Writer
	/**
	 * Output stream to write the file.
	 */
	private PrintWriter singleOut;

	/**
	 * Time of the last event. Used to determine when to write a line in the CSV
	 * file.
	 */
	private double lastEventTime;

	/**
	 * Map to store events occurring at the same time, keep them before writing them
	 * all.
	 */
	private Map<String, String> bagData;

	/**
	 * Complete path to the file to write (must be CSV)
	 */
	private String filePath;
	
	/**
	 * Run index to create different files for each run
	 */
	private static double run=0;

	// Implementation
	/**
	 * Show whether or not the first event has already passed.
	 */
	private boolean firstEvent;

	/**
	 * Default constructor, logger will save the data in the default path
	 * {@link #getFilePath()}.
	 *
	 * @param aObservingId Default name used for the CSV file name.
	 * @param aKnownPorts  Ports to log
	 */
	public SingleFileCustomLoggingArtifact(String aObservingId, List<String> aKnownPorts) {
		super(aObservingId, aKnownPorts);
		filePath = getDefaultFolderPath() + Double.toString(run) + ".csv";
	}

	/**
	 *
	 * @param aObservingId
	 * @param aKnownPorts
	 * @param parameters   Map of parameters, only "filePath" is allowed to set the
	 *                     path of the csv file and its name
	 */
	public SingleFileCustomLoggingArtifact(String aObservingId, List<String> aKnownPorts, Map<String, Object> parameters) {
		this(aObservingId, aKnownPorts);
		if (parameters.get("filePath") != null) {
			run++;
			filePath = ((String) parameters.get("filePath")) + Double.toString(run) + ".csv"; // I added this last part (DIEGO)
		} // Else the default one is kept
	}

	/**
	 *
	 * @param aObservingId
	 * @param aKnownPorts
	 * @param aFilePath    The folder where the file will be saved.
	 */
	public SingleFileCustomLoggingArtifact(String aObservingId, List<String> aKnownPorts, String aFilePath) {
		super(aObservingId, aKnownPorts);
		filePath = aFilePath  + aObservingId + ".csv"; // I added this last part (DIEGO)
	}

	/**
	 *
	 * @param aObservingId
	 * @param aKnownPorts
	 */
	public SingleFileCustomLoggingArtifact(String aObservingId, String[] aKnownPorts) {
		super(aObservingId, aKnownPorts);
		filePath = getDefaultFolderPath() + aObservingId + ".csv"; // I added this last part (DIEGO)
	}

	/**
	 *
	 * @param aObservingId
	 * @param aKnownPorts
	 * @param aFilePath    The folder where the file will be saved.
	 */
	public SingleFileCustomLoggingArtifact(String aObservingId, String[] aKnownPorts, String aFilePath) {
		super(aObservingId, aKnownPorts);
		filePath = aFilePath + aObservingId + ".csv"; // I added this last part (DIEGO)
	}

	@Override
	public void initialize() {
		bagData = new HashMap<String, String>();
		firstEvent = false;
		lastEventTime = Double.MIN_VALUE; // At the beginning there were no events.
		try {
			File file = new File(filePath);
			// Error if folder parents don't exist, so we create them if requested.
			File parent = new File(file.getParent());
			if (!parent.exists()) {
				getLogger().warn("The path where the log file must be created doesn't exist, "
						+ "try to create parents folders and files. "
						+ parent.getAbsolutePath());
				if (!parent.mkdirs()) {
					getLogger().error("Error creating parent folders! " + filePath);
				}
			}
			singleOut = new PrintWriter(new FileWriter(file));
			writeHeader();
		} catch (IOException e) {
			getLogger().error("Error when creating the file : +" + filePath, e);
		}
	}

	@Override
	public void processExternalInputEvent(SimulEvent aEvent, String aPort) {
		if (singleOut != null) {
			if (!firstEvent) {
				// first event we get, just store it and initialize (no need to print
				firstEvent = true;
			} else if (lastEventTime < aEvent.getTime()) {
				// If the time advance (and its not the first event), we got all the previous
				// simultaneous events, just have to write them.
				writeBagData();
			}
			lastEventTime = aEvent.getTime();
			// Store the event before writing it
			bagData.put(aPort, dataToString(aEvent.getData()));
		} else {
			getLogger().error("Not initialized!");
		}
	}

	@Override
	public void finishSimulation() {
		if (singleOut != null) {
			// Write the last events received
			writeBagData();
			singleOut.flush();
			singleOut.close();
		}
	}

	/**
	 * Write the header
	 */
	private void writeHeader() {
		String header = "time" + getSeparator();
		for (String portName : getKnownPorts()) {
			header += portName + getSeparator();
		}
		singleOut.print(header + "\n");
	}

	/**
	 * Write data from the bag to the file and clear the bag.
	 */
	private void writeBagData() {
		singleOut.print(doubleToString(lastEventTime) + getSeparator() + dataBagToString() + "\n");
		bagData.clear(); // Clear the bagData
	}

	/**
	 *
	 * @return String of all the data in the data bag
	 */
	private String dataBagToString() {
		// Get data from all ports, always in the same order.
		StringBuffer datas = new StringBuffer();
		for (String portName : getKnownPorts()) {
			if (bagData.get(portName) != null) {
				datas.append(bagData.get(portName)); // separator included
			} else {
				datas.append("null;"); // separator not included
			}
		}
		return datas.toString();
	}

	/**
	 * @return The path of the CSV file to write
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Enable to set the path and the name of the CSV file to write. Must be called
	 * before the initialization method.
	 *
	 * @param aFilePath
	 */
	public void setFilePath(String aFilePath) {
		this.filePath = aFilePath;
	}

}

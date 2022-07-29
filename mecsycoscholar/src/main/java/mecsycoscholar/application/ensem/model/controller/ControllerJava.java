package mecsycoscholar.application.ensem.model.controller;

/**
 *
 * Represents a flow controller which has to send power flow commands to heater
 * depending on a set of temperatures.
 *
 */
public class ControllerJava {

	/**
	 * Names of the output ports of the controller model
	 */
	private String[] outputPortNames;

	/**
	 * Names of the input ports of the controller model
	 */
	private String[] inputPortNames;

	/**
	 * The "current" (according to the last data received from the rooms probes)
	 * temperature of each rooms.
	 */
	private double[] temp;

	/**
	 * The "current" (according to the last data received from the rooms probes)
	 * power consumption of each rooms.
	 */
	private double[] pow;

	/**
	 * Minimal temperature authorized in rooms
	 */
	private double[] minTemp;

	/**
	 * Maxomal temperature authorized in rooms
	 */
	private double[] maxTemp;

	/**
	 * Maximum power of heaters in each rooms.
	 */
	private double[] maxPow;

	/**
	 * Period of time between each evaluation point.
	 */
	private double step;

	public ControllerJava(String[] inputPortNames, String[] outputPortNames, double period,
			double[] minTemp, double[] maxTemp, double[] temp,
			double[] powMax, double[] pow) {
		super();
		this.inputPortNames = inputPortNames;
		this.outputPortNames = outputPortNames;
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.maxPow = powMax;
		this.step = period;
		this.pow = pow;
		this.temp = temp;
	}

	/**
	 *
	 * @param inputPortName Name of the input port whose value must be updated
	 * @param value         Value for the update (a new temperature)
	 */
	public void updateValue(String inputPortName, double value) {
		int i = 0;
		boolean valueUpdated = false;
		while (i < inputPortNames.length && !valueUpdated) {
			if (inputPortName.equals(inputPortNames[i])) {
				temp[i] = value;
				valueUpdated = true;
			}
			i++;
		}
		if (!valueUpdated) {
			System.out.println("The input port " + inputPortName + " does not exist!");
		}
	}

	/**
	 *
	 * @param outputPortName
	 * @return The value of the power command of the specified port.
	 */
	public double getCurrentOrder(String outputPortName) {
		int i = 0;
		while (i < outputPortNames.length) {
			if (outputPortName.equals(outputPortNames[i])) {
				return pow[i];
			}
			i++;
		}
		System.out.println("The output port " + outputPortName + " does not exist!");
		return -1;
	}

	public void computeOrders(double time) {
		// Modify the table of pow in function of the temperature of each room.
		// example
		for (int i = 0; i < minTemp.length; i++) {
			if (temp[i] < minTemp[i] && pow[i] == 0) // We fall below the wanted temperature
			{
				pow[i] = -maxPow[i];  // we start to heat up
			} else if (temp[i] < minTemp[i] && pow[i] > 0) { // Conditioner is on and we fall below the wanted
																// temperature
				pow[i] = 0;  // we stop cooling
			} else if (temp[i] > maxTemp[i] && pow[i] == 0) {
				pow[i] = maxPow[i];  // we start to cool down
			} else if (temp[i] > maxTemp[i] && pow[i] < 0) { // Heater is on, and we are over the wanted temperature
				pow[i] = 0; // we stop heating
			} 
		}
	}

	/**
	 *
	 * @return The power consumed by all the rooms.
	 */
	public double totalPowerConsumption() {
		double totalPowerConsumption = 0;
		for (double powerByRoom : pow) {
			totalPowerConsumption += Math.abs(powerByRoom);
		}
		return totalPowerConsumption;
	}

	// Getters
	public String[] getOutputPortNames() {
		return outputPortNames;
	}

	public String[] getInputPortNames() {
		return inputPortNames;
	}

	public double[] getTemp() {
		return temp;
	}

	public double[] getPow() {
		return pow;
	}

	public double[] getMinTemp() {
		return minTemp;
	}

	public double[] getPowMax() {
		return maxPow;
	}

	public double getPeriod() {
		return step;
	}

}

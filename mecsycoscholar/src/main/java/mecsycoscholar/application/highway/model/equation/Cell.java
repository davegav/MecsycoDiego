/*
C. Bourjot, B. Camus, V. Chevrier, L. Ciarletta, M. Urbanski Copyright
(c) Université de Lorraine & INRIA, Affero GPL license v3, 2014-2015


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

package mecsycoscholar.application.highway.model.equation;

public class Cell {

	// longueur du tronçon
	private double L;

	// durée d'un pas de temps (heure)
	private double T;

	// density (veh/km/voie)
	private double rho;

	// flow (veh/h)
	private double q;

	// average speed (km/h)
	private double v;

	// maximum capacity (veh/h)
	private double capacity;

	// global parameters
	private double tau, kappa, nu;

	// number of voie
	private double lambda;

	// critical density (veh/h/voie)
	private double rho_cr;

	// free speed
	private double vf;

	// exponent
	private double psi;

	// input densité
	private double input_rho;

	// input flow_rate
	private double input_q;

	// input speed
	private double input_v;

	// time;
	private double time;

	private String name;

	public Cell (String name, double length, double time_step, double density, double speed, double free_speed,
			double capacity, double critical_density, double nb_voie, double tau, double kappa, double nu) {
		this.name = name;
		L = length;
		T = time_step;
		rho = density;
		v = speed;
		vf = free_speed;
		this.capacity = capacity;
		lambda = nb_voie;
		this.tau = tau;
		this.kappa = kappa;
		this.nu = nu;
		rho_cr = critical_density;
		input_rho = 0;
		input_q = 0;
		input_v = 0;
		time = 0;
		q = rho * v * lambda;
		psi = -(1 / (Math.log(capacity / (vf * rho_cr))));
	}

	private double V () {
		return vf * Math.exp((-1 / psi) * Math.pow((rho / rho_cr), psi));
	}

	public void doStep () {
		if(Double.isNaN(v)||Double.isNaN(rho)||Double.isNaN(q)||Double.isNaN(input_q)||Double.isNaN(input_rho)||Double.isNaN(input_v)){
			System.out.println(name+" v: " + v + " rho: " + rho + " q: " + q + " time: " + time +" input_v: " + input_v + " input_q " + input_q + " input_rho " + input_rho);
		}
		
		
		v = v + (T / tau) * (V() - v) + (T / L) * v * (input_v - v)
				- ((nu * T * (input_rho - rho)) / (tau * L * (rho + kappa)));
		

		rho = rho + (T / (L * lambda)) * (input_q - q);

		q = rho * v * lambda;

		time += T;
		

	}

	public double getRho () {
		return rho;
	}

	public double getCapacity () {
		return capacity;
	}
	
	public double getCriticalDensity(){
		return rho_cr;
	}

	public double getQ () {
		return q;
	}

	public double getV () {
		return v;
	}

	public double getTime () {
		return time;
	}

	public void setInput_rho (double input_rho) {
		this.input_rho = input_rho;
		//System.out.println("input_rho "+input_rho);
	}

	public void setInput_q (double input_q) {
		this.input_q = input_q;
	}

	public void setInput_v (double input_v) {
		this.input_v = input_v;
	}

	public String getName () {
		return name;
	}

	public double getFreeSpeed(){
		return vf;
	}
}

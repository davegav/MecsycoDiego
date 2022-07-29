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

package mecsycoscholar.application.ode.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class LorenzSystem {

	private double a, b, c, x, y, z, time_step;

	private double time;

	private double maxSimTime;

	public LorenzSystem (double A, double B, double C, double X, double Y, double Z, double max, double step) {
		a = A;
		b = B;
		c = C;
		x = X;
		y = Y;
		z = Z;
		time = 0;
		maxSimTime = max;
		time_step = step;
	}

	public void setTimeStep (double step) {
		time_step = step;
	}

	public double getX () {
		return x;
	}

	public void setX (double x) {
		this.x = x;
	}

	public double getY () {
		return y;
	}

	public void setY (double y) {
		this.y = y;
	}

	public double getZ () {
		return z;
	}

	public void setZ (double z) {
		this.z = z;
	}

	public void doStep () {
		double dx = (a * (y - x));
		double dy = (x * (b - z) - y);
		double dz = (z + (x * y - c * z));
		x += dx * time_step;
		y += dy * time_step;
		z += dz * time_step;
		time += time_step;
	}

	public void run () {
		PrintWriter out;
		try {
			out = new PrintWriter(new File("data_log/logCentralizedLorenz.csv"));
			out.print("time;x;y;z");
			while (time < maxSimTime) {
				doStep();
				out.print(String.valueOf(time)+ ";" + x+ ";"+y+"z"+"\n");
			}
			out.flush();
			out.close();
			System.out.println("Lorenz System Model Stop at time " + time);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main (String args[]) {
		LorenzSystem system = new LorenzSystem(10, 28, 2.67, 1, 1, 4, 100, 0.01);
		system.run();
	}
}

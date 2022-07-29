/*
V. Chevrier, L. Ciarletta, V. Elvinger, Copyright (c) Université de
Lorraine & INRIA, Affero GPL license v3, 2014-2015

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

package mecsycoscholar.common.type;

import java.io.Serializable;

import mecsyco.core.helper.ObjectHelpers;
import mecsyco.world.netlogo.type.NetLogoColorConstants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NetLogoCarTurtle implements Serializable {

// Version
	private static final long serialVersionUID = 1L;

// Cosntant
	public final static double DefaultSpeed = 1;

// Implementation
	protected final static String ToStringTemplate = "Car{\n\tid: %s\n\tx: %s\n\ty: %s\n\tcolor: %s\n\theading: %s\n\tspeed: %s\n}";

    /**
     * X coordinate of the netlogo turtle
     */
    private final double x;
    /**
     * Y coordinate of the netlogo turtle
     */
    private final double y;
    /**
     * id of the netlogo turtle
     */
    private final long id;
    /**
     * heading of the netlogo turtle
     */
    private final double heading;
    /**
     * color of the netlogo turtle
     */
    private final double color;
    /**
     * speed of the netlogo turtle
     */
    private final double speed;

    @JsonCreator
    public NetLogoCarTurtle(@JsonProperty("x") double myXpos, @JsonProperty("y") double myYpos, @JsonProperty("id") long myId, @JsonProperty("heading") double myHeading, @JsonProperty("color") double myColor, @JsonProperty("speed") double mySpeed) {
        x = myXpos;
        y = myYpos;
        id = myId;
        heading = myHeading;
        color = myColor;
        speed = mySpeed;
    }
    
    public NetLogoCarTurtle(double myXpos, double myYpos, long myId, double myHeading, double myColor) {
        this(myXpos, myYpos, myId, myHeading, myColor, DefaultSpeed);
    }

   // Access
    public double getHeading() {
        return heading;
    }

    public double getColor() {
        return color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public long getId() {
        return id;
    }

    public double getSpeed(){
    	return speed;
    }

	@Override
	public int hashCode () {
		return Long.valueOf(id).hashCode();
	}

 // Status
	@Override
	public boolean equals (Object aCandidate) {
		return ObjectHelpers.haveSameClass(this, aCandidate) && ((NetLogoCarTurtle) aCandidate).id == id;
	}

// Debugging
	@Override
	public String toString () {
		return String.format(ToStringTemplate, id, x, y,
				NetLogoColorConstants.asString(color), heading, speed);
	}

}


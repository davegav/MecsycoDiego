package TP_Thermique_ENSEM
  package PureFmu
    model Room
      parameter Modelica.SIunits.Volume V = 8 "Volume of the room";
      constant Modelica.SIunits.SpecificVolume air_rho = 1.293 "Specific volume";
      parameter Modelica.SIunits.SpecificHeatCapacity cp = 1006 "Air specific heat capacity";
      parameter Modelica.SIunits.Temperature Tinit = 294.15;
      Modelica.Thermal.HeatTransfer.Components.HeatCapacitor AirNode(C = V * air_rho * cp, T(displayUnit = "K", fixed = true, start = Tinit)) annotation(
        Placement(visible = true, transformation(origin = {0, 32}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Thermal.HeatTransfer.Sensors.TemperatureSensor temperatureSensor1 annotation(
        Placement(visible = true, transformation(origin = {46, 6}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Thermal.HeatTransfer.Sources.PrescribedHeatFlow prescribedHeatFlow1 annotation(
        Placement(visible = true, transformation(origin = {-64, 2}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput Qin annotation(
        Placement(visible = true, transformation(origin = {-98, 2}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-100, 0}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealOutput Tin annotation(
        Placement(visible = true, transformation(origin = {100, 4}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {102, 0}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
    equation
      connect(Qin, prescribedHeatFlow1.Q_flow) annotation(
        Line(points = {{-98, 2}, {-74, 2}, {-74, 2}, {-74, 2}}, color = {0, 0, 127}));
      connect(temperatureSensor1.T, Tin) annotation(
        Line(points = {{56, 6}, {92, 6}, {92, 4}, {100, 4}}, color = {0, 0, 127}));
      connect(prescribedHeatFlow1.port, AirNode.port) annotation(
        Line(points = {{-54, 2}, {0, 2}, {0, 22}, {0, 22}}, color = {191, 0, 0}));
      connect(AirNode.port, temperatureSensor1.port) annotation(
        Line(points = {{0, 22}, {0, 22}, {0, 6}, {36, 6}, {36, 6}}, color = {191, 0, 0}));
      annotation(
        uses(Modelica(version = "3.2.1")),
        Icon(graphics = {Ellipse(origin = {-1, 3}, fillColor = {170, 255, 255}, fillPattern = FillPattern.Solid, extent = {{-99, 97}, {101, -103}}, endAngle = 360), Text(origin = {-4, 5}, extent = {{-66, 33}, {66, -33}}, textString = "%name")}));
    end Room;
    
    model Room2inputs
    parameter Real V = 8 "Volume of the room";
    parameter Real cv = 1256 "Volumetric heat capacity";
    parameter Real Tinit = 293.15 "Initial temperature";
    Modelica.Thermal.HeatTransfer.Components.HeatCapacitor heatCapacitor1(C = V * cv, T(displayUnit = "K", fixed = true, start = Tinit)) annotation(
      Placement(visible = true, transformation(origin = {0, 32}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    Modelica.Thermal.HeatTransfer.Sensors.TemperatureSensor temperatureSensor1 annotation(
      Placement(visible = true, transformation(origin = {46, 6}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    Modelica.Thermal.HeatTransfer.Sources.PrescribedHeatFlow prescribedHeatFlow1 annotation(
      Placement(visible = true, transformation(origin = {-26, 4}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    Modelica.Blocks.Interfaces.RealInput Qext annotation(
      Placement(visible = true, transformation(origin = {-102, 30}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-100, 40}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
    Modelica.Blocks.Interfaces.RealOutput Tin annotation(
      Placement(visible = true, transformation(origin = {108, 6}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {102, 0}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
    Modelica.Blocks.Math.Add add1 annotation(
      Placement(visible = true, transformation(origin = {-64, 4}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    Modelica.Blocks.Interfaces.RealInput Qheater annotation(
      Placement(visible = true, transformation(origin = {-102, -32}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-100, -40}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
  equation
    connect(temperatureSensor1.T, Tin) annotation(
      Line(points = {{56, 6}, {106, 6}}, color = {0, 0, 127}));
    connect(Qheater, add1.u2) annotation(
      Line(points = {{-102, -32}, {-82, -32}, {-82, -2}, {-76, -2}, {-76, -2}}, color = {0, 0, 127}));
    connect(Qext, add1.u1) annotation(
      Line(points = {{-102, 30}, {-82, 30}, {-82, 10}, {-76, 10}, {-76, 10}}, color = {0, 0, 127}));
    connect(add1.y, prescribedHeatFlow1.Q_flow) annotation(
      Line(points = {{-52, 4}, {-38, 4}, {-38, 4}, {-36, 4}}, color = {0, 0, 127}));
    connect(prescribedHeatFlow1.port, heatCapacitor1.port) annotation(
      Line(points = {{-16, 4}, {0, 4}, {0, 22}}, color = {191, 0, 0}));
    connect(heatCapacitor1.port, temperatureSensor1.port) annotation(
      Line(points = {{0, 22}, {0, 22}, {0, 6}, {36, 6}, {36, 6}}, color = {191, 0, 0}));
    annotation(
      uses(Modelica(version = "3.2.1")),
      Icon(graphics = {Ellipse(origin = {-1, 3}, fillColor = {170, 255, 255}, fillPattern = FillPattern.Solid, extent = {{-99, 97}, {101, -103}}, endAngle = 360), Text(origin = {-4, 5}, extent = {{-66, 33}, {66, -33}}, textString = "%name")}, coordinateSystem(initialScale = 0.1)));
  end Room2inputs;

    model Wall
      // parametre
      parameter Modelica.SIunits.Area S = 1 "Surface of the wall without windows";
      parameter Modelica.SIunits.SurfaceCoefficientOfHeatTransfer lambda = 0.3 "Surfacic thermic conductance of the wall in W/(K.m^2)";
      // Composants
      Modelica.Thermal.HeatTransfer.Sources.PrescribedTemperature prescribedTemperature1 annotation(
        Placement(visible = true, transformation(origin = {48, -40}, extent = {{-10, -10}, {10, 10}}, rotation = 180)));
      Modelica.Thermal.HeatTransfer.Sensors.HeatFlowSensor heatFlowSensor annotation(
        Placement(visible = true, transformation(origin = {28, 0}, extent = {{10, -10}, {-10, 10}}, rotation = 180)));
      Modelica.Blocks.Interfaces.RealInput T_b "Input temperature" annotation(
        Placement(transformation(extent = {{-20, -20}, {20, 20}}, rotation = 180, origin = {100, -40}), iconTransformation(extent = {{-20, -20}, {20, 20}}, rotation = 180, origin = {80, -20})));
      Modelica.Blocks.Interfaces.RealOutput Q_b "Output heat flow" annotation(
        Placement(transformation(extent = {{90, 30}, {110, 50}}), iconTransformation(extent = {{60, 0}, {100, 40}})));
      Modelica.Thermal.HeatTransfer.Sources.PrescribedTemperature prescribedTemperature2 annotation(
        Placement(transformation(extent = {{-10, -10}, {10, 10}}, rotation = 0, origin = {-54, -42})));
      Modelica.Thermal.HeatTransfer.Sensors.HeatFlowSensor heatFlowSensor1 annotation(
        Placement(transformation(extent = {{-10, -10}, {10, 10}}, rotation = 180, origin = {-30, 0})));
      Modelica.Blocks.Interfaces.RealInput T_a "Input temperature" annotation(
        Placement(transformation(extent = {{-20, -20}, {20, 20}}, rotation = 0, origin = {-100, -42}), iconTransformation(extent = {{-20, -20}, {20, 20}}, rotation = 0, origin = {-80, -20})));
      Modelica.Blocks.Interfaces.RealOutput Q_a "Output heat flow" annotation(
        Placement(transformation(extent = {{-10, -10}, {10, 10}}, rotation = 180, origin = {-100, 40}), iconTransformation(extent = {{-20, -20}, {20, 20}}, rotation = 180, origin = {-80, 20})));
      Modelica.Thermal.HeatTransfer.Components.ThermalConductor thermalConductor1(G = S * lambda) annotation(
        Placement(visible = true, transformation(origin = {-2, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    equation
      connect(heatFlowSensor.port_b, prescribedTemperature1.port) annotation(
        Line(points = {{38, 0}, {38, -40}}, color = {191, 0, 0}));
      connect(prescribedTemperature1.T, T_b) annotation(
        Line(points = {{60, -40}, {100, -40}}, color = {0, 0, 127}));
      connect(thermalConductor1.port_b, heatFlowSensor.port_a) annotation(
        Line(points = {{8, 0}, {18, 0}, {18, 0}, {18, 0}}, color = {191, 0, 0}));
      connect(heatFlowSensor.Q_flow, Q_b) annotation(
        Line(points = {{28, 10}, {28, 40}, {100, 40}}, color = {0, 0, 127}));
      connect(heatFlowSensor1.port_a, thermalConductor1.port_a) annotation(
        Line(points = {{-20, 0}, {-12, 0}, {-12, 0}, {-12, 0}}, color = {191, 0, 0}));
      connect(heatFlowSensor1.Q_flow, Q_a) annotation(
        Line(points = {{-30, 10}, {-30, 40}, {-100, 40}}, color = {0, 0, 127}, smooth = Smooth.None));
      connect(prescribedTemperature2.T, T_a) annotation(
        Line(points = {{-66, -42}, {-100, -42}}, color = {0, 0, 127}, smooth = Smooth.None));
      connect(prescribedTemperature2.port, heatFlowSensor1.port_b) annotation(
        Line(points = {{-44, -42}, {-40, -42}, {-40, 0.00000000000000133227}}, color = {255, 0, 0}, smooth = Smooth.None));
      annotation(
        Diagram(coordinateSystem(preserveAspectRatio = false, extent = {{-100, -100}, {100, 100}}), graphics),
        Icon(coordinateSystem(preserveAspectRatio = false, initialScale = 0.1), graphics = {Rectangle(fillColor = {175, 175, 175}, fillPattern = FillPattern.Solid, extent = {{-20, 80}, {20, -80}}), Text(origin = {6, 14}, rotation = -90, extent = {{-42, 32}, {58, -40}}, textString = "%name")}),
        uses(Modelica(version = "3.2.1")));
    end Wall;

    model House
      parameter Modelica.SIunits.SpecificHeatCapacity cp = 1006;
      parameter Modelica.SIunits.SurfaceCoefficientOfHeatTransfer lambda = 0.3;
      Room room1(V = 4 * 10 * 2.5, cp = cp) annotation(
        Placement(visible = true, transformation(origin = {60, 60}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Room room2(V = 3 * 4 * 2.5, cp = cp) annotation(
        Placement(visible = true, transformation(origin = {60, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Room room3(V = 3 * 6 * 2.5, cp = cp) annotation(
        Placement(visible = true, transformation(origin = {60, -60}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput Pow1 annotation(
        Placement(visible = true, transformation(origin = {-108, 60}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-98, 60}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput Pow2 annotation(
        Placement(visible = true, transformation(origin = {-108, 0}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-100, 0}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput Pow3 annotation(
        Placement(visible = true, transformation(origin = {-108, -60}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-96, -60}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealOutput Temp1 annotation(
        Placement(visible = true, transformation(origin = {108, 60}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {102, 60}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealOutput Temp2 annotation(
        Placement(visible = true, transformation(origin = {108, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {100, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealOutput Temp3 annotation(
        Placement(visible = true, transformation(origin = {108, -60}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {100, -62}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput OutTemp annotation(
        Placement(visible = true, transformation(origin = {-40, 108}, extent = {{-20, -20}, {20, 20}}, rotation = -90), iconTransformation(origin = {-42, 98}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      Modelica.Blocks.Math.Sum sum1(nin = 4) annotation(
        Placement(visible = true, transformation(origin = {20, 60}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Math.Sum sum2(nin = 4) annotation(
        Placement(visible = true, transformation(origin = {20, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Math.Sum sum3(nin = 4) annotation(
        Placement(visible = true, transformation(origin = {20, -60}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Wall wall_outTo1(S = 18 * 2.5, lambda = lambda) annotation(
        Placement(visible = true, transformation(origin = {-20, 72}, extent = {{-10, 10}, {10, -10}}, rotation = 0)));
      Wall wall_outTo2(S = 7 * 2.5, lambda = lambda) annotation(
        Placement(visible = true, transformation(origin = {-20, 12}, extent = {{-10, 10}, {10, -10}}, rotation = 0)));
      Wall wall_outTo3(S = 9 * 2.5, lambda = lambda) annotation(
        Placement(visible = true, transformation(origin = {-20, -48}, extent = {{-10, 10}, {10, -10}}, rotation = 0)));
      Wall wall12(S = 4 * 2.5, lambda = lambda) annotation(
        Placement(visible = true, transformation(origin = {-10, 42}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Wall wall13(S = 6 * 2.5, lambda = lambda) annotation(
        Placement(visible = true, transformation(origin = {-10, -80}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Wall wall23(S = 3 * 2.5, lambda = lambda) annotation(
        Placement(visible = true, transformation(origin = {-10, -20}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    equation
      connect(room1.Tin, wall13.T_a) annotation(
        Line(points = {{70, 60}, {78, 60}, {78, 90}, {120, 90}, {120, -96}, {-34, -96}, {-34, -82}, {-18, -82}, {-18, -82}}, color = {0, 0, 127}));
      connect(room1.Tin, wall12.T_b) annotation(
        Line(points = {{70, 60}, {78, 60}, {78, 40}, {-2, 40}, {-2, 40}}, color = {0, 0, 127}));
      connect(room2.Tin, wall12.T_a) annotation(
        Line(points = {{70, 0}, {80, 0}, {80, 26}, {-30, 26}, {-30, 40}, {-18, 40}, {-18, 40}}, color = {0, 0, 127}));
      connect(room2.Tin, wall23.T_b) annotation(
        Line(points = {{70, 0}, {80, 0}, {80, -22}, {-2, -22}, {-2, -22}}, color = {0, 0, 127}));
      connect(room3.Tin, wall23.T_a) annotation(
        Line(points = {{70, -60}, {80, -60}, {80, -32}, {-30, -32}, {-30, -22}, {-18, -22}, {-18, -22}}, color = {0, 0, 127}));
      connect(room3.Tin, wall13.T_b) annotation(
        Line(points = {{70, -60}, {80, -60}, {80, -82}, {-2, -82}, {-2, -82}}, color = {0, 0, 127}));
      connect(room3.Tin, wall_outTo3.T_b) annotation(
        Line(points = {{70, -60}, {80, -60}, {80, -40}, {0, -40}, {0, -46}, {-12, -46}, {-12, -46}}, color = {0, 0, 127}));
      connect(room2.Tin, wall_outTo2.T_b) annotation(
        Line(points = {{70, 0}, {80, 0}, {80, 18}, {0, 18}, {0, 14}, {-12, 14}, {-12, 14}}, color = {0, 0, 127}));
      connect(room1.Tin, wall_outTo1.T_b) annotation(
        Line(points = {{70, 60}, {78, 60}, {78, 84}, {0, 84}, {0, 74}, {-12, 74}, {-12, 74}}, color = {0, 0, 127}));
      connect(wall_outTo3.Q_b, sum3.u[1]) annotation(
        Line(points = {{-12, -50}, {0, -50}, {0, -60}, {8, -60}}, color = {0, 0, 127}));
      connect(wall_outTo3.T_a, OutTemp) annotation(
        Line(points = {{-28, -46}, {-40, -46}, {-40, 108}}, color = {0, 0, 127}));
      connect(wall_outTo2.Q_b, sum2.u[1]) annotation(
        Line(points = {{-12, 10}, {0, 10}, {0, 0}, {8, 0}}, color = {0, 0, 127}));
      connect(wall_outTo2.T_a, OutTemp) annotation(
        Line(points = {{-28, 14}, {-40, 14}, {-40, 108}}, color = {0, 0, 127}));
      connect(wall_outTo1.Q_b, sum1.u[1]) annotation(
        Line(points = {{-12, 70}, {0, 70}, {0, 60}, {8, 60}}, color = {0, 0, 127}));
      connect(OutTemp, wall_outTo1.T_a) annotation(
        Line(points = {{-40, 108}, {-40, 74}, {-28, 74}}, color = {0, 0, 127}));
      connect(room1.Tin, Temp1) annotation(
        Line(points = {{70, 60}, {108, 60}}, color = {0, 0, 127}));
      connect(sum1.y, room1.Qin) annotation(
        Line(points = {{31, 60}, {50, 60}}, color = {0, 0, 127}));
      connect(Pow1, sum1.u[4]) annotation(
        Line(points = {{-108, 60}, {8, 60}}, color = {0, 0, 127}));
      connect(wall13.Q_a, sum1.u[3]) annotation(
        Line(points = {{-18, -78}, {-40, -78}, {-40, -102}, {126, -102}, {126, 96}, {8, 96}, {8, 60}}, color = {0, 0, 127}));
      connect(wall12.Q_b, sum1.u[2]) annotation(
        Line(points = {{-2, 44}, {8, 44}, {8, 60}}, color = {0, 0, 127}));
      connect(Pow2, sum2.u[4]) annotation(
        Line(points = {{-108, 0}, {8, 0}, {8, 0}, {8, 0}}, color = {0, 0, 127}));
      connect(Pow3, sum3.u[4]) annotation(
        Line(points = {{-108, -60}, {8, -60}, {8, -60}, {8, -60}}, color = {0, 0, 127}));
      connect(wall23.Q_a, sum3.u[3]) annotation(
        Line(points = {{-18, -18}, {-36, -18}, {-36, -36}, {8, -36}, {8, -58}, {8, -58}, {8, -60}}, color = {0, 0, 127}));
      connect(wall12.Q_a, sum2.u[3]) annotation(
        Line(points = {{-18, 44}, {-36, 44}, {-36, 22}, {8, 22}, {8, 0}, {8, 0}}, color = {0, 0, 127}));
      connect(wall13.Q_b, sum3.u[2]) annotation(
        Line(points = {{-2, -78}, {8, -78}, {8, -60}, {8, -60}}, color = {0, 0, 127}));
      connect(wall23.Q_b, sum2.u[2]) annotation(
        Line(points = {{-2, -18}, {8, -18}, {8, 0}, {8, 0}}, color = {0, 0, 127}));
      connect(room3.Tin, Temp3) annotation(
        Line(points = {{70, -60}, {108, -60}}, color = {0, 0, 127}));
      connect(room2.Tin, Temp2) annotation(
        Line(points = {{70, 0}, {100, 0}, {100, 0}, {108, 0}}, color = {0, 0, 127}));
      connect(sum3.y, room3.Qin) annotation(
        Line(points = {{32, -60}, {50, -60}}, color = {0, 0, 127}));
      connect(sum2.y, room2.Qin) annotation(
        Line(points = {{32, 0}, {50, 0}}, color = {0, 0, 127}));
      annotation(
        uses(Modelica(version = "3.2.2")));
    end House;

    model OutTemp
      parameter Real amplitude = 10 "Amplitude of sine wave";
      parameter Modelica.SIunits.Time period = 86400 "Period of sine wave";
      parameter Modelica.SIunits.Angle phase = 0 "Phase of sine wave";
      parameter Real offset = 283.15 "Offset of output signal";
      parameter Modelica.SIunits.Time startTime = 0 "Output = offset for time < startTime";
      Modelica.Blocks.Sources.Sine outTemp(amplitude = amplitude, freqHz = 1/period, offset = offset, phase = phase, startTime = startTime) annotation(
        Placement(visible = true, transformation(origin = {0, 2}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealOutput OutTemp annotation(
        Placement(visible = true, transformation(origin = {104, 2}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {100, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    equation
      connect(outTemp.y, OutTemp) annotation(
        Line(points = {{12, 2}, {104, 2}}, color = {0, 0, 127}));
      annotation(
        uses(Modelica(version = "3.2.2")),
        Icon(graphics = {Rectangle(origin = {5, 1}, fillColor = {0, 255, 255}, fillPattern = FillPattern.Solid, extent = {{-85, 81}, {85, -81}}), Text(origin = {3, 3}, extent = {{-49, 23}, {49, -23}}, textString = "%name")}));
    end OutTemp;

    model Test
      House house1(cp = 1006, lambda = 2.5) annotation(
        Placement(visible = true, transformation(origin = {0, 26}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      OutTemp outTemp1(amplitude = 5, offset = 278.15) annotation(
        Placement(visible = true, transformation(origin = {-56, 72}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Sources.Constant const(k = 750) annotation(
        Placement(visible = true, transformation(origin = {-74, -12}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Sources.Constant const1(k = 2000) annotation(
        Placement(visible = true, transformation(origin = {-72, 24}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    equation
      connect(const1.y, house1.Pow1) annotation(
        Line(points = {{-60, 24}, {-52, 24}, {-52, 32}, {-10, 32}, {-10, 32}}, color = {0, 0, 127}));
      connect(const.y, house1.Pow2) annotation(
        Line(points = {{-63, -12}, {-44, -12}, {-44, 26}, {-10, 26}}, color = {0, 0, 127}));
      connect(const.y, house1.Pow3) annotation(
        Line(points = {{-63, -12}, {-36.5, -12}, {-36.5, 20}, {-10, 20}}, color = {0, 0, 127}));
      connect(outTemp1.OutTemp, house1.OutTemp) annotation(
        Line(points = {{-46, 72}, {-4, 72}, {-4, 36}, {-4, 36}}, color = {0, 0, 127}));
      annotation(
        uses(Modelica(version = "3.2.1")),
        experiment(StartTime = 0, StopTime = 86400, Tolerance = 1e-6, Interval = 60));
    end Test;
  end PureFmu;

  package BuildSysProVersion
    model House
      //(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {BuildSysPro.Utilities.Data.Solids.Concrete(), BuildSysPro.Utilities.Data.Solids.ExpandedPolystyrene32(), BuildSysPro.Utilities.Data.Solids.PlasterBoard()}, positionIsolant = {0, 1, 0})
      //(n = 3, m = {1, 1, 1}, e = {0.01, 0.40, 0.01}, mat = {BuildSysPro.Utilities.Data.Solids.PlasterBoard(), BuildSysPro.Utilities.Data.Solids.MineralWool40(), BuildSysPro.Utilities.Data.Solids.PlasterBoard()}, positionIsolant = {0, 1, 0})
      //(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {beton, isolantPolystyrene, placo}, positionIsolant = {0, 1, 0})
      parameter BuildSysPro.Utilities.Types.InitCond initialization = initialization;
      parameter Modelica.SIunits.SurfaceCoefficientOfHeatTransfer hs_ext = 25.0 "Convective heat transfer coeficient outside.";
      parameter Modelica.SIunits.SurfaceCoefficientOfHeatTransfer hs_int = 7.69 "Convective heat transfer coeficient inside.";
      parameter Real alpha_ext = 0.6 "Absorption coefficient of the outer wall in the visible (around 0.3 for clear walls and 0.9 for dark shades)";
      parameter Real eps = 0.9 "Emissivity of the outer surface of the wall in LWR (concrete 0.9)";
      parameter Modelica.SIunits.Temperature Tinit = 293.15;
      parameter Modelica.SIunits.Temperature TextInit = 293.15;
      parameter Modelica.SIunits.Volume V1 = 4 * 10 * 2.5;
      parameter Modelica.SIunits.Volume V2 = 3 * 4 * 2.5;
      parameter Modelica.SIunits.Volume V3 = 6 * 3 * 2.5;
      parameter Modelica.SIunits.Area S1N = 2.5 * 4;
      parameter Modelica.SIunits.Area S1O = 2.5 * 10;
      parameter Modelica.SIunits.Area S1S = 2.5 * 4;
      parameter Modelica.SIunits.Area S2N = 2.5 * 3;
      parameter Modelica.SIunits.Area S2E = 2.5 * 4;
      parameter Modelica.SIunits.Area S3E = 2.5 * 6;
      parameter Modelica.SIunits.Area S3S = 2.5 * 3;
      parameter BuildSysPro.Utilities.Records.GenericSolid isolantPolystyrene(lambda = 0.032, rho = 35, c = 1450);
      parameter BuildSysPro.Utilities.Records.GenericSolid beton(lambda = 1.75, rho = 2450, c = 920);
      parameter BuildSysPro.Utilities.Records.GenericSolid laineVerre(lambda = 0.040, rho = 20, c = 1210);
      parameter BuildSysPro.Utilities.Records.GenericSolid placo(lambda = 0.25, rho = 900, c = 1000);
      //Param Wall
      //m = nombre de noeud de calcul par couche (défaut 1)
      //Mat c'est la liste des matériaux (lambda = conductivité en W/(m.K), rho=densité en kg/m3, c=capacité thermique en J/(kg.K))
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W1N(InitType = initialization, RadExterne = true, S = S1N, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {beton, isolantPolystyrene, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_ext, hs_int = hs_int, skyViewFactor = 0) annotation(
        Placement(visible = true, transformation(origin = {-58, 82}, extent = {{-10, -10}, {10, 10}}, rotation = -90)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W2E(InitType = initialization, RadExterne = true, S = S2E, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {beton, isolantPolystyrene, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_ext, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {74, 60}, extent = {{10, -10}, {-10, 10}}, rotation = 0)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W3E(InitType = initialization, RadExterne = true, S = S3E, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {beton, isolantPolystyrene, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_int, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {72, -44}, extent = {{10, -10}, {-10, 10}}, rotation = 0)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W12(InitType = initialization, S = S2E, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.01, 0.4, 0.01}, mat = {placo, laineVerre, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_ext, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {0, 62}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W13(InitType = initialization, S = S3E, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.01, 0.4, 0.01}, mat = {placo, laineVerre, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_ext, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {-20, -38}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W23(InitType = initialization, S = S2N, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.01, 0.4, 0.01}, mat = {placo, laineVerre, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_ext, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {36, 0}, extent = {{-10, -10}, {10, 10}}, rotation = -90)));
      BuildSysPro.Building.AirFlow.HeatTransfer.AirNode R1(Tair(displayUnit = "K") = Tinit, V = V1) annotation(
        Placement(visible = true, transformation(origin = {-60, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.Building.AirFlow.HeatTransfer.AirNode R2(Tair(displayUnit = "K") = Tinit, V = V2) annotation(
        Placement(visible = true, transformation(origin = {38, 60}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.Building.AirFlow.HeatTransfer.AirNode R3(Tair(displayUnit = "K") = Tinit, V = V3) annotation(
        Placement(visible = true, transformation(origin = {40, -42}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W1S(InitType = initialization, RadExterne = true, S = S1S, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {beton, isolantPolystyrene, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_ext, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {-58, -82}, extent = {{-10, -10}, {10, 10}}, rotation = 90)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W3S(InitType = initialization, RadExterne = true, S = S3S, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {beton, isolantPolystyrene, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_ext, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {40, -82}, extent = {{-10, -10}, {10, 10}}, rotation = 90)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W2N(InitType = initialization, RadExterne = true, S = S3E, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {beton, isolantPolystyrene, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_int, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {38, 82}, extent = {{-10, -10}, {10, 10}}, rotation = -90)));
      BuildSysPro.Building.BuildingEnvelope.HeatTransfer.SimpleWall W1O(GLOext = false, InitType = initialization, RadExterne = true, RadInterne = false, S = S1O, Tp(displayUnit = "K") = Tinit, alpha_ext = alpha_ext, caracParoi(n = 3, m = {1, 1, 1}, e = {0.2, 0.1, 0.01}, mat = {beton, isolantPolystyrene, placo}, positionIsolant = {0, 1, 0}), eps = eps, hs_ext = hs_ext, hs_int = hs_int) annotation(
        Placement(visible = true, transformation(origin = {-88, -2}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.BoundaryConditions.Solar.Interfaces.SolarFluxInput FO annotation(
        Placement(visible = true, transformation(origin = {-144, 2}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-108, 2}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      BuildSysPro.BoundaryConditions.Solar.Interfaces.SolarFluxInput FN annotation(
        Placement(visible = true, transformation(origin = {0, 144}, extent = {{-20, -20}, {20, 20}}, rotation = -90), iconTransformation(origin = {0, 116}, extent = {{-20, -20}, {20, 20}}, rotation = -90)));
      BuildSysPro.BoundaryConditions.Solar.Interfaces.SolarFluxInput FS annotation(
        Placement(visible = true, transformation(origin = {0, -134}, extent = {{-20, -20}, {20, 20}}, rotation = 90), iconTransformation(origin = {0, -118}, extent = {{-20, -20}, {20, 20}}, rotation = 90)));
      BuildSysPro.BoundaryConditions.Solar.Interfaces.SolarFluxInput FE annotation(
        Placement(visible = true, transformation(origin = {142, 0}, extent = {{20, -20}, {-20, 20}}, rotation = 0), iconTransformation(origin = {114, 0}, extent = {{20, -20}, {-20, 20}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Interfaces.HeatPort_a T_ext annotation(
        Placement(visible = true, transformation(origin = {-128, -44}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {-102, -96}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Interfaces.HeatPort_a Pow1 annotation(
        Placement(visible = true, transformation(origin = {-16, 2}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {-52, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Interfaces.HeatPort_a Pow2 annotation(
        Placement(visible = true, transformation(origin = {68, 30}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {50, 50}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Interfaces.HeatPort_a Pow3 annotation(
        Placement(visible = true, transformation(origin = {2, -66}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {50, -50}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    initial equation
      R1.VolAir.T = Tinit;
      R2.VolAir.T = Tinit;
      R3.VolAir.T = Tinit;
    equation
      connect(R1.port_a, W1S.T_int) annotation(
        Line(points = {{-60, -4}, {-60, -4}, {-60, -56}, {-54, -56}, {-54, -72}, {-54, -72}}, color = {191, 0, 0}));
      connect(T_ext, W1S.T_ext) annotation(
        Line(points = {{-128, -44}, {-104, -44}, {-104, -102}, {-56, -102}, {-56, -90}, {-54, -90}}, color = {191, 0, 0}));
      connect(T_ext, W1N.T_ext) annotation(
        Line(points = {{-128, -44}, {-112, -44}, {-112, 110}, {-60, 110}, {-60, 92}, {-60, 92}}, color = {191, 0, 0}));
      connect(R1.port_a, W1N.T_int) annotation(
        Line(points = {{-60, -4}, {-60, -4}, {-60, 74}, {-60, 74}}, color = {191, 0, 0}));
      connect(W1O.T_int, R1.port_a) annotation(
        Line(points = {{-78, -4}, {-60, -4}, {-60, -4}, {-60, -4}}, color = {191, 0, 0}));
      connect(R3.port_a, W3E.T_int) annotation(
        Line(points = {{40, -46}, {62, -46}, {62, -47}, {63, -47}}, color = {191, 0, 0}));
      connect(T_ext, W3E.T_ext) annotation(
        Line(points = {{-128, -44}, {-104, -44}, {-104, -102}, {116, -102}, {116, -48}, {81, -48}, {81, -47}}, color = {191, 0, 0}));
      connect(FE, W3E.FluxIncExt) annotation(
        Line(points = {{142, 0}, {102, 0}, {102, -40}, {74, -40}, {74, -39}, {75, -39}}, color = {255, 192, 1}));
      connect(FE, W2E.FluxIncExt) annotation(
        Line(points = {{142, 0}, {102, 0}, {102, 64}, {78, 64}, {78, 66}}, color = {255, 192, 1}));
      connect(FS, W3S.FluxIncExt) annotation(
        Line(points = {{0, -134}, {36, -134}, {36, -84}}, color = {255, 192, 1}));
      connect(FS, W1S.FluxIncExt) annotation(
        Line(points = {{0, -134}, {-64, -134}, {-64, -84}, {-62, -84}}, color = {255, 192, 1}));
      connect(FN, W2N.FluxIncExt) annotation(
        Line(points = {{0, 144}, {0, 144}, {0, 116}, {42, 116}, {42, 86}, {44, 86}}, color = {255, 192, 1}));
      connect(FN, W1N.FluxIncExt) annotation(
        Line(points = {{0, 144}, {0, 116}, {-52, 116}, {-52, 86}}, color = {255, 192, 1}));
      connect(Pow2, R2.port_a) annotation(
        Line(points = {{68, 30}, {38, 30}, {38, 56}, {38, 56}}, color = {191, 0, 0}));
      connect(Pow3, R3.port_a) annotation(
        Line(points = {{2, -66}, {18, -66}, {18, -46}, {40, -46}, {40, -46}}, color = {191, 0, 0}));
      connect(Pow1, R1.port_a) annotation(
        Line(points = {{-16, 2}, {-16, 2}, {-16, -4}, {-60, -4}, {-60, -4}}, color = {191, 0, 0}));
      connect(T_ext, W2E.T_sky) annotation(
        Line(points = {{-128, -44}, {-112, -44}, {-112, 110}, {98, 110}, {98, 50}, {84, 50}, {84, 52}}, color = {191, 0, 0}));
      connect(T_ext, W2N.T_ext) annotation(
        Line(points = {{-128, -44}, {-112, -44}, {-112, 110}, {36, 110}, {36, 92}, {36, 92}}, color = {191, 0, 0}));
      connect(T_ext, W3S.T_ext) annotation(
        Line(points = {{-128, -44}, {-112, -44}, {-112, -44}, {-104, -44}, {-104, -102}, {42, -102}, {42, -90}, {44, -90}}, color = {191, 0, 0}));
      connect(T_ext, W1O.T_ext) annotation(
        Line(points = {{-128, -44}, {-112, -44}, {-112, -6}, {-96, -6}, {-96, -4}}, color = {191, 0, 0}));
      connect(FO, W1O.FluxIncExt) annotation(
        Line(points = {{-144, 2}, {-96, 2}, {-96, 4}, {-90, 4}}, color = {255, 192, 1}));
      connect(R2.port_a, W2N.T_int) annotation(
        Line(points = {{38, 56}, {36, 56}, {36, 73}, {35, 73}}, color = {191, 0, 0}));
      connect(R2.port_a, W2E.T_int) annotation(
        Line(points = {{38, 56}, {72, 56}, {72, 57}, {65, 57}}, color = {191, 0, 0}));
      connect(R3.port_a, W13.T_int) annotation(
        Line(points = {{40, -46}, {4, -46}, {4, -42}, {-10, -42}, {-10, -40}}, color = {191, 0, 0}));
      connect(R3.port_a, W23.T_int) annotation(
        Line(points = {{40, -46}, {32, -46}, {32, -8}, {34, -8}}, color = {191, 0, 0}));
      connect(R2.port_a, W23.T_ext) annotation(
        Line(points = {{38, 56}, {38, 56}, {38, 32}, {34, 32}, {34, 10}, {34, 10}}, color = {191, 0, 0}));
      connect(W12.T_int, R2.port_a) annotation(
        Line(points = {{10, 60}, {22, 60}, {22, 58}, {38, 58}, {38, 56}}, color = {191, 0, 0}));
      connect(R1.port_a, W12.T_ext) annotation(
        Line(points = {{-60, -4}, {-34, -4}, {-34, 60}, {-8, 60}, {-8, 60}}, color = {191, 0, 0}));
      connect(R1.port_a, W13.T_ext) annotation(
        Line(points = {{-60, -4}, {-60, -4}, {-60, -40}, {-28, -40}, {-28, -40}}, color = {191, 0, 0}));
      connect(W3S.T_int, R3.port_a) annotation(
        Line(points = {{44, -72}, {42, -72}, {42, -56}, {40, -56}, {40, -46}, {40, -46}}, color = {191, 0, 0}));
      annotation(
        Icon(graphics = {Rectangle(origin = {-49, 1}, extent = {{-51, 99}, {49, -99}}), Rectangle(origin = {51, 60}, extent = {{-51, 40}, {49, -40}}), Rectangle(origin = {53, -49}, extent = {{47, -49}, {-53, 69}})}));
    end House;

    model HouseFmu
      parameter Modelica.SIunits.SurfaceCoefficientOfHeatTransfer hs_ext = 25.0 "Convective heat transfer coeficient outside.";
      parameter Modelica.SIunits.SurfaceCoefficientOfHeatTransfer hs_int = 7.69 "Convective heat transfer coeficient inside.";
      parameter Real alpha_ext = 0.6 "Absorption coefficient of the outer wall in the visible (around 0.3 for clear walls and 0.9 for dark shades)";
      parameter Real eps = 0.9 "Emissivity of the outer surface of the wall in LWR (concrete 0.9)";
      parameter Modelica.SIunits.Temperature Tinit = 293.15;
      parameter Modelica.SIunits.Temperature TextInit = 293.15;
      parameter Modelica.SIunits.Volume V1 = 4 * 10 * 2.5;
      parameter Modelica.SIunits.Volume V2 = 3 * 4 * 2.5;
      parameter Modelica.SIunits.Volume V3 = 6 * 3 * 2.5;
      parameter Modelica.SIunits.Area S1N = 2.5 * 4;
      parameter Modelica.SIunits.Area S1O = 2.5 * 10;
      parameter Modelica.SIunits.Area S1S = 2.5 * 4;
      parameter Modelica.SIunits.Area S2N = 2.5 * 3;
      parameter Modelica.SIunits.Area S2E = 2.5 * 4;
      parameter Modelica.SIunits.Area S3E = 2.5 * 6;
      parameter Modelica.SIunits.Area S3S = 2.5 * 3;
      TP_Thermique_ENSEM.BuildSysProVersion.House house(S1N = S1N, S1O = S1O, S1S = S1S, S2E = S2E, S2N = S2N, S3E = S3E, S3S = S3S, TextInit = TextInit, Tinit = Tinit, V1 = V1, V2 = V2, V3 = V3, alpha_ext = alpha_ext, eps = eps, hs_ext = hs_ext, hs_int = hs_int, initialization = BuildSysPro.Utilities.Types.InitCond.NoInit) annotation(
        Placement(visible = true, transformation(origin = {0, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.BoundaryConditions.Solar.Interfaces.SolarFluxInput FluxO annotation(
        Placement(visible = true, transformation(origin = {-100, 0}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-100, 0}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      BuildSysPro.BoundaryConditions.Solar.Interfaces.SolarFluxInput FluxS annotation(
        Placement(visible = true, transformation(origin = {0, -102}, extent = {{-20, -20}, {20, 20}}, rotation = 90), iconTransformation(origin = {0, -102}, extent = {{-20, -20}, {20, 20}}, rotation = 90)));
      BuildSysPro.BoundaryConditions.Solar.Interfaces.SolarFluxInput FluxN annotation(
        Placement(visible = true, transformation(origin = {0, 100}, extent = {{-20, -20}, {20, 20}}, rotation = -90), iconTransformation(origin = {0, 100}, extent = {{-20, -20}, {20, 20}}, rotation = -90)));
      BuildSysPro.BoundaryConditions.Solar.Interfaces.SolarFluxInput FluxE annotation(
        Placement(visible = true, transformation(origin = {100, 0}, extent = {{20, -20}, {-20, 20}}, rotation = 0), iconTransformation(origin = {100, 0}, extent = {{20, -20}, {-20, 20}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Sources.PrescribedTemperature prescribedTemperature1 annotation(
        Placement(visible = true, transformation(origin = {-56, -20}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput Text annotation(
        Placement(visible = true, transformation(origin = {-100, -60}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-100, -60}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput Pow1 annotation(
        Placement(visible = true, transformation(origin = {-100, 50}, extent = {{-20, -20}, {20, 20}}, rotation = 0), iconTransformation(origin = {-100, 50}, extent = {{-20, -20}, {20, 20}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput Pow2 annotation(
        Placement(visible = true, transformation(origin = {100, 80}, extent = {{20, -20}, {-20, 20}}, rotation = 0), iconTransformation(origin = {100, 80}, extent = {{20, -20}, {-20, 20}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealInput Pow3 annotation(
        Placement(visible = true, transformation(origin = {100, -80}, extent = {{20, -20}, {-20, 20}}, rotation = 0), iconTransformation(origin = {100, -80}, extent = {{20, -20}, {-20, 20}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Sources.PrescribedHeatFlow prescribedHeatFlow1 annotation(
        Placement(visible = true, transformation(origin = {-56, 34}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Sources.PrescribedHeatFlow prescribedHeatFlow2 annotation(
        Placement(visible = true, transformation(origin = {34, 36}, extent = {{10, -10}, {-10, 10}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Sources.PrescribedHeatFlow prescribedHeatFlow3 annotation(
        Placement(visible = true, transformation(origin = {58, -54}, extent = {{10, -10}, {-10, 10}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Sensors.TemperatureSensor temperatureSensor1 annotation(
        Placement(visible = true, transformation(origin = {-52, 76}, extent = {{10, -10}, {-10, 10}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Sensors.TemperatureSensor temperatureSensor2 annotation(
        Placement(visible = true, transformation(origin = {74, 34}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      BuildSysPro.BaseClasses.HeatTransfer.Sensors.TemperatureSensor temperatureSensor3 annotation(
        Placement(visible = true, transformation(origin = {38, -84}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealOutput T2 annotation(
        Placement(visible = true, transformation(origin = {108, 32}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {108, 32}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealOutput T3 annotation(
        Placement(visible = true, transformation(origin = {70, -98}, extent = {{-10, -10}, {10, 10}}, rotation = 0), iconTransformation(origin = {70, -98}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Interfaces.RealOutput T1 annotation(
        Placement(visible = true, transformation(origin = {-104, 88}, extent = {{10, -10}, {-10, 10}}, rotation = 0), iconTransformation(origin = {-104, 88}, extent = {{10, -10}, {-10, 10}}, rotation = 0)));
    equation
      connect(house.Pow2, temperatureSensor2.port) annotation(
        Line(points = {{6, 6}, {64, 6}, {64, 34}, {64, 34}}, color = {191, 0, 0}));
      connect(temperatureSensor2.T, T2) annotation(
        Line(points = {{84, 34}, {102, 34}, {102, 32}, {108, 32}}, color = {0, 0, 127}));
      connect(temperatureSensor3.T, T3) annotation(
        Line(points = {{48, -84}, {52, -84}, {52, -96}, {70, -96}, {70, -98}}, color = {0, 0, 127}));
      connect(house.Pow3, temperatureSensor3.port) annotation(
        Line(points = {{6, -4}, {6, -4}, {6, -84}, {28, -84}, {28, -84}}, color = {191, 0, 0}));
      connect(temperatureSensor1.port, house.Pow1) annotation(
        Line(points = {{-42, 76}, {-6, 76}, {-6, 0}, {-6, 0}}, color = {191, 0, 0}));
      connect(temperatureSensor1.T, T1) annotation(
        Line(points = {{-62, 76}, {-78, 76}, {-78, 88}, {-104, 88}, {-104, 88}}, color = {0, 0, 127}));
      connect(prescribedTemperature1.port, house.T_ext) annotation(
        Line(points = {{-46, -20}, {-28, -20}, {-28, -10}, {-10, -10}}, color = {191, 0, 0}));
      connect(Text, prescribedTemperature1.T) annotation(
        Line(points = {{-100, -60}, {-80, -60}, {-80, -20}, {-66, -20}}, color = {0, 0, 127}));
      connect(FluxO, house.FO) annotation(
        Line(points = {{-100, 0}, {-12, 0}, {-12, 0}, {-10, 0}}, color = {255, 192, 1}));
      connect(FluxN, house.FN) annotation(
        Line(points = {{0, 100}, {0, 100}, {0, 12}, {0, 12}}, color = {255, 192, 1}));
      connect(FluxE, house.FE) annotation(
        Line(points = {{100, 0}, {14, 0}, {14, 0}, {12, 0}}, color = {255, 192, 1}));
      connect(FluxS, house.FS) annotation(
        Line(points = {{0, -102}, {0, -102}, {0, -12}, {0, -12}}, color = {255, 192, 1}));
      connect(prescribedHeatFlow3.Q_flow, Pow3) annotation(
        Line(points = {{68, -54}, {74, -54}, {74, -80}, {100, -80}, {100, -80}}, color = {0, 0, 127}));
      connect(prescribedHeatFlow2.Q_flow, Pow2) annotation(
        Line(points = {{44, 36}, {54, 36}, {54, 80}, {100, 80}, {100, 80}}, color = {0, 0, 127}));
      connect(prescribedHeatFlow3.port, house.Pow3) annotation(
        Line(points = {{48, -54}, {32, -54}, {32, -8}, {6, -8}, {6, -6}, {6, -6}}, color = {191, 0, 0}));
      connect(prescribedHeatFlow2.port, house.Pow2) annotation(
        Line(points = {{24, 36}, {8, 36}, {8, 6}, {6, 6}, {6, 6}}, color = {191, 0, 0}));
      connect(prescribedHeatFlow1.port, house.Pow1) annotation(
        Line(points = {{-46, 34}, {-24, 34}, {-24, 6}, {-6, 6}, {-6, 0}, {-6, 0}}, color = {191, 0, 0}));
      connect(Pow1, prescribedHeatFlow1.Q_flow) annotation(
        Line(points = {{-100, 50}, {-78, 50}, {-78, 34}, {-66, 34}, {-66, 34}}, color = {0, 0, 127}));
    end HouseFmu;

    model Test
      HouseFmu houseFmu1(Tinit = 298.15) annotation(
        Placement(visible = true, transformation(origin = {-2, 0}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Sources.Sine sine1(amplitude = 5, freqHz = 1 / 86400, offset = 283.15) annotation(
        Placement(visible = true, transformation(origin = {-82, -20}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Sources.Constant const(k = 0) annotation(
        Placement(visible = true, transformation(origin = {-64, 52}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    equation
      connect(houseFmu1.FluxS, const.y) annotation(
        Line(points = {{-2, -10}, {-2, -10}, {-2, -28}, {76, -28}, {76, 52}, {-52, 52}, {-52, 52}}, color = {255, 192, 1}));
      connect(houseFmu1.Pow3, const.y) annotation(
        Line(points = {{8, -8}, {60, -8}, {60, 52}, {-54, 52}, {-54, 52}, {-52, 52}}, color = {0, 0, 127}));
      connect(houseFmu1.FluxE, const.y) annotation(
        Line(points = {{8, 0}, {38, 0}, {38, 52}, {-52, 52}, {-52, 52}}, color = {255, 192, 1}));
      connect(houseFmu1.Pow2, const.y) annotation(
        Line(points = {{8, 8}, {20, 8}, {20, 52}, {-52, 52}, {-52, 52}}, color = {0, 0, 127}));
      connect(houseFmu1.FluxN, const.y) annotation(
        Line(points = {{-2, 10}, {-2, 10}, {-2, 52}, {-52, 52}, {-52, 52}}, color = {255, 192, 1}));
      connect(houseFmu1.Pow1, const.y) annotation(
        Line(points = {{-12, 6}, {-28, 6}, {-28, 52}, {-52, 52}, {-52, 52}}, color = {0, 0, 127}));
      connect(const.y, houseFmu1.FluxO) annotation(
        Line(points = {{-52, 52}, {-34, 52}, {-34, 0}, {-12, 0}, {-12, 0}}, color = {0, 0, 127}));
      connect(sine1.y, houseFmu1.Text) annotation(
        Line(points = {{-70, -20}, {-38, -20}, {-38, -6}, {-12, -6}, {-12, -6}}, color = {0, 0, 127}));
      annotation(
        experiment(StartTime = 0, StopTime = 86400, Tolerance = 1e-06, Interval = 60));
    end Test;

    model TestFmu
      TP_Thermique_ENSEM.BuildSysProVersion.HouseFmu houseFmu1(TextInit = 289.15, Tinit = 295.15) annotation(
        Placement(visible = true, transformation(origin = {12, 14}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Sources.Sine sine1(amplitude = 5, freqHz = 1 / 86400, offset = 283.15) annotation(
        Placement(visible = true, transformation(origin = {-80, 20}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Sources.Constant Flux(k = 0) annotation(
        Placement(visible = true, transformation(origin = {-46, 74}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Modelica.Blocks.Sources.Constant const(k = 0) annotation(
        Placement(visible = true, transformation(origin = {-46, -60}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
    equation
      connect(const.y, houseFmu1.Pow1) annotation(
        Line(points = {{-34, -60}, {-8, -60}, {-8, 18}, {2, 18}, {2, 20}}, color = {0, 0, 127}));
      connect(const.y, houseFmu1.Pow2) annotation(
        Line(points = {{-34, -60}, {48, -60}, {48, 22}, {24, 22}, {24, 22}, {22, 22}}, color = {0, 0, 127}));
      connect(const.y, houseFmu1.Pow3) annotation(
        Line(points = {{-34, -60}, {48, -60}, {48, 6}, {22, 6}, {22, 6}}, color = {0, 0, 127}));
      connect(Flux.y, houseFmu1.FluxS) annotation(
        Line(points = {{-34, 74}, {56, 74}, {56, -24}, {12, -24}, {12, 4}, {12, 4}}, color = {0, 0, 127}));
      connect(Flux.y, houseFmu1.FluxE) annotation(
        Line(points = {{-34, 74}, {56, 74}, {56, 14}, {22, 14}, {22, 14}}, color = {0, 0, 127}));
      connect(Flux.y, houseFmu1.FluxN) annotation(
        Line(points = {{-34, 74}, {12, 74}, {12, 24}, {12, 24}}, color = {0, 0, 127}));
      connect(Flux.y, houseFmu1.FluxO) annotation(
        Line(points = {{-34, 74}, {-20, 74}, {-20, 14}, {2, 14}, {2, 14}}, color = {0, 0, 127}));
      connect(sine1.y, houseFmu1.Text) annotation(
        Line(points = {{-68, 20}, {-26, 20}, {-26, 6}, {2, 6}, {2, 8}}, color = {0, 0, 127}));
      annotation(
        experiment(StartTime = 0, StopTime = 432000, Tolerance = 1e-06, Interval = 120));
    end TestFmu;
  end BuildSysProVersion;
end TP_Thermique_ENSEM;

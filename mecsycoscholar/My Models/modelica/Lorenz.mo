package Lorenz
  package Model
    model CentralizedLorenz
      parameter Real a = 10;
      parameter Real b = 28;
      parameter Real c = 2.67;
      parameter Real initX = 1 "Initial value for x";
      parameter Real initY = 1 "Initial value for y";
      parameter Real initZ = 4 "Initial value for z";
      output Real x;
      output Real y;
      output Real z;
    initial equation
      x = initX;
      y = initY;
      z = initZ;
    equation
      der(x) = a * (y - x);
      der(y) = x * (b - z);
      der(z) = x * y - c * z;
    end CentralizedLorenz;


model LorenzX
  parameter Real a = 10;
  parameter Real initX = 1 "Initial value for x";
  Modelica.Blocks.Interfaces.RealOutput x annotation(Placement(visible = true, transformation(origin = {112, 40}, extent = {{-22, -22}, {22, 22}}, rotation = 0), iconTransformation(origin = {101, 39}, extent = {{-21, -21}, {21, 21}}, rotation = 0)));
  Modelica.Blocks.Interfaces.RealInput y annotation(Placement(visible = true, transformation(origin = {106, -40}, extent = {{-20, -20}, {20, 20}}, rotation = 180), iconTransformation(origin = {100, -40}, extent = {{-20, -20}, {20, 20}}, rotation = 180)));

initial equation
  x = initX;
equation
  der(x) = a * (y - x);
end LorenzX;

model LorenzY
  parameter Real b = 28;
  parameter Real initY = 1 "Initial value for y";
  Modelica.Blocks.Interfaces.RealInput x
  annotation(Placement(visible = true, transformation(origin = {109, 59}, extent = {{-25, -25}, {25, 25}}, rotation = 180), iconTransformation(origin = {101, 61}, extent = {{-21, -21}, {21, 21}}, rotation = 180)));
  Modelica.Blocks.Interfaces.RealOutput y annotation(Placement(visible = true, transformation(origin = {114, -60}, extent = {{-22, -22}, {22, 22}}, rotation = 0), iconTransformation(origin = {101, -61}, extent = {{-21, -21}, {21, 21}}, rotation = 0)));
  Modelica.Blocks.Interfaces.RealInput z annotation(Placement(visible = true, transformation(origin = {109, -1}, extent = {{-23, -23}, {23, 23}}, rotation = 180), iconTransformation(origin = {101, -1}, extent = {{-21, -21}, {21, 21}}, rotation = 180)));

initial equation
  y = initY;
equation
  der(y) = x * (b - z);
end LorenzY;

    model LorenzZ
      parameter Real c = 2.67;
      parameter Real initZ = 4 "Initial value for z";
      Modelica.Blocks.Interfaces.RealInput x
      annotation(Placement(visible = true, transformation(origin = {109, 59}, extent = {{-27, -27}, {27, 27}}, rotation = 180), iconTransformation(origin = {101, 61}, extent = {{-21, -21}, {21, 21}}, rotation = 180)));
      Modelica.Blocks.Interfaces.RealInput y annotation(Placement(visible = true, transformation(origin = {108, 0}, extent = {{-24, -24}, {24, 24}}, rotation = 180), iconTransformation(origin = {101, -1}, extent = {{-21, -21}, {21, 21}}, rotation = 180)));
      Modelica.Blocks.Interfaces.RealOutput z annotation(Placement(visible = true, transformation(origin = {115, -59}, extent = {{-25, -25}, {25, 25}}, rotation = 0), iconTransformation(origin = {101, -61}, extent = {{-21, -21}, {21, 21}}, rotation = 0)));
    initial equation
      z = initZ;
    equation
      der(z) = x * y - c * z;
    end LorenzZ;
  end Model;

  package Test
    model TestDecentralizedLorenz
      Model.LorenzY lorenzY1 annotation(
          Placement(visible = true, transformation(origin = {2, 64}, extent = {{-10, -10}, {10, 10}}, rotation = -90)));
      Model.LorenzX lorenzX1 annotation(
          Placement(visible = true, transformation(origin = {-46, 6}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
      Model.LorenzZ lorenzZ1 annotation(
          Placement(visible = true, transformation(origin = {46, -4}, extent = {{-10, -10}, {10, 10}}, rotation = 180)));
      equation
        connect(lorenzY1.y, lorenzX1.y) annotation(
          Line(points = {{-4, 54}, {-4, 54}, {-4, 2}, {-36, 2}, {-36, 2}}, color = {0, 0, 127}));
        connect(lorenzX1.x, lorenzY1.x) annotation(
          Line(points = {{-36, 10}, {6, 10}, {6, 54}, {8, 54}}, color = {0, 0, 127}));
        connect(lorenzZ1.z, lorenzY1.z) annotation(
          Line(points = {{36, 2}, {2, 2}, {2, 54}}, color = {0, 0, 127}));
        connect(lorenzX1.x, lorenzZ1.x) annotation(
          Line(points = {{-36, 10}, {6, 10}, {6, -10}, {36, -10}}, color = {0, 0, 127}));
        connect(lorenzY1.y, lorenzZ1.y) annotation(
          Line(points = {{-4, 54}, {-4, -4}, {36, -4}}, color = {0, 0, 127}));
      end TestDecentralizedLorenz;
  end Test;
end Lorenz;

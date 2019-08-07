/*
 * Project: Info-II-SS19
 *
 * Copyright (c) 2008-2019,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for computer sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.lab4inf.plotter;

import de.lab4inf.plotter.Functions.*;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.*;

import java.util.ArrayList;

/**
 * Basic skeleton for a Swing based GUI application.
 *
 * @author nwulff
 * @since 02.04.2013
 * @version $Id: Main.java,v 1.2 2019/04/02 12:58:54 nwulff Exp $
 */

public class Main {
	private JFrame frame;
	protected CanvasView canvas;
	private JPanel statusBar;
	protected JTextField status;
	protected JLabel a;
	protected JLabel b;
	private Color color;

	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;

	private boolean coordinates = true;

	private AffineTransform matrix;

	/**
	 * Public default constructor.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Start of the application.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Main app = new Main();
		app.start();
	}

	/**
	 * Initialize the application.
	 */
	protected void initialize() {
		frame = new JFrame(" -- plotter example -- ");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setBounds(50, 50, 50 + 640, 50 + 480);
		frame.setJMenuBar(createMenuBar());

		frame.getContentPane().add(createStatusBar(), BorderLayout.SOUTH);
		frame.getContentPane().add(createContent(), BorderLayout.CENTER);

		xMin = -10;
		xMax = 10;
		yMin = -10;
		yMax = 10;
	}

	/**
	 * Start the graphical user interface.
	 */
	public void start() {
		// show the GUI
		frame.setVisible(true);
		status.setText("Application started");
	}

	/**
	 * Internal helper to create the main content.
	 * 
	 * @return component with application content.
	 */
	protected JComponent createContent() {
		canvas = new CanvasView();
		canvas.addMouseListener(new Painter());
		canvas.addMouseMotionListener(new Pointer());
		canvas.setBackground(Color.GREEN);
		canvas.setForeground(Color.RED);
		canvas.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return canvas;
	}

	/**
	 * Internal helper to create the statusbar and -fields.
	 * 
	 * @return component with status/bar.
	 */
	protected JComponent createStatusBar() {
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setHgap(5);

		statusBar = new JPanel(layout);
		statusBar.add(new JLabel("Status: "));

		a = new JLabel("");
		statusBar.add(a);

		b = new JLabel("");
		statusBar.add(b);

		status = new JTextField();
		status.setPreferredSize(new Dimension(400, 0));
		status.setEditable(true);
		status.setBorder(new BevelBorder(BevelBorder.RAISED, Color.MAGENTA, Color.LIGHT_GRAY));
		status.getInsets().set(2, 10, 2, 10);
		statusBar.add(status);

		return statusBar;

//		Graphics gc = canvas.getGraphics();
//		gc.drawLine(, 0, end.x, canvas.getHeight());
	}

	/**
	 * Internal helper to create the frames menubar.
	 * 
	 * @return menu bar
	 */
	protected JMenuBar createMenuBar() {
		JMenuBar mb = new JMenuBar();
		JMenuItem item;
		JMenu menu;
		// Action menu
		menu = new JMenu("Actions");
		mb.add(menu);

		item = new JMenuItem("Draw RandomCircle");
		item.addActionListener(new RandomDrawerCircle());
		menu.add(item);

		item = new JMenuItem("Draw RandomRectangle");
		item.addActionListener(new RandomDrawerRectangle());
		menu.add(item);

		menu.addSeparator();
		item = new JMenuItem("Exit");
		item.addActionListener(new AppCloser());
		menu.add(item);

		// Color menu not used so far
		menu = new JMenu("Colors");
		mb.add(menu);
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem item2;

		item2 = new JRadioButtonMenuItem("Black", true);
		item2.addActionListener(new setColor(Color.BLACK));
		menu.add(item2);
		group.add(item2);

		item2 = new JRadioButtonMenuItem("Red");
		item2.addActionListener(new setColor(Color.RED));
		menu.add(item2);
		group.add(item2);

		item2 = new JRadioButtonMenuItem("Green");
		item2.addActionListener(new setColor(Color.GREEN));
		menu.add(item2);
		group.add(item2);

		item2 = new JRadioButtonMenuItem("Blue");
		item2.addActionListener(new setColor(Color.BLUE));
		menu.add(item2);
		group.add(item2);

		menu = new JMenu("Coordinates");
		mb.add(menu);

		item = new JMenuItem("Set Coordinates");
		item.addActionListener(new Window());
		menu.add(item);

		item = new JCheckBoxMenuItem("Show Coordinate System", true);
		item.addItemListener(new showCoordinates());
		menu.add(item);

		menu = new JMenu("Functions");
		mb.add(menu);

		item = new JMenuItem("Linear");
		item.addActionListener(new LinearFunctionDrawer());
		menu.add(item);

		item = new JMenuItem("Parabola");
		item.addActionListener(new functionDrawer(new Square()));
		menu.add(item);

		item = new JMenuItem("Cubic");
		item.addActionListener(new functionDrawer(new Cube()));
		menu.add(item);

		item = new JMenuItem("Sinus");
		item.addActionListener(new functionDrawer(new Sinus()));
		menu.add(item);

		item = new JMenuItem("SinusC");
		item.addActionListener(new functionDrawer(new SinusC()));
		menu.add(item);

		item = new JMenuItem("Cosinus");
		item.addActionListener(new functionDrawer(new Cosinus()));
		menu.add(item);

		item = new JMenuItem("Tangens");
		item.addActionListener(new functionDrawer(new Tangens()));
		menu.add(item);

		item = new JMenuItem("Exp2");
		item.addActionListener(new functionDrawer(new Exp()));
		menu.add(item);

		menu.addSeparator();
		item = new JMenuItem("Clear Shapes");
		item.addActionListener(new ClearShapes());
		menu.add(item);

		// Help menu not used so far
		menu = new JMenu("Help");
		mb.add(menu);

		return mb;
	}

	class AppCloser implements ActionListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("application finished, bye-bye... \n");
			frame.setVisible(false);
			frame.dispose();
			System.exit(0);
		}
	}

	class RandomDrawerCircle implements ActionListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			double width = xMax - xMin;
			double height = yMax - yMin;
			double x = Math.random();
			double y = Math.random();
			double r = Math.random();
			String msg = String.format("rnd draw x:%.3f y:%.3f r:%.3f", x, y, r);
			status.setText(msg);
			x = width * x + xMin;
			y = height * y + yMin;
			r = r * Math.sqrt(width * height);
			System.out.println(msg);
			ColorShape shape = new ColorShape(new Ellipse2D.Double(x, y, r * 2, r * 2), color);
			canvas.addShape(shape);
			canvas.repaint();
		}
	}

	class RandomDrawerRectangle implements ActionListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			double width = xMax - xMin;
			double height = yMax - yMin;
			double x = Math.random();
			double y = Math.random();
			double r = Math.random();
			String msg = String.format("rnd draw x:%.3f y:%.3f r:%.3f", x, y, r);
			status.setText(msg);
			x = width * x + xMin;
			y = height * y + yMin;
			r = r * Math.sqrt(width * height);
			System.out.println(msg);
			ColorShape teil = new ColorShape(new Rectangle2D.Double(x, y, r, r), color);
			canvas.addShape(teil);
			canvas.repaint();

		}
	}

	class functionDrawer implements ActionListener {
		private Function function;

		functionDrawer(Function func) {
			function = func;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			GeneralPath path = new GeneralPath();
			path.moveTo(xMin, function.f(xMin));
			for (double j = xMin; j < xMax; j += 0.001) {
				path.lineTo(j, function.f(j));
			}
			ColorShape shape = new ColorShape(path, color, function);
			canvas.addShape(shape);
			canvas.repaint();
		}
	}

	class LinearFunctionDrawer implements ActionListener{
		double m, n;


		@Override
		public void actionPerformed(ActionEvent e) {

			JTextField m_ = new JTextField();
			JTextField n_ = new JTextField();

			Object[] message = { "Slope", m_, "y-intersect", n_,};
			JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			pane.createDialog(null, "Create Linear Function").setVisible(true);
			m = Double.parseDouble(m_.getText());
			n = Double.parseDouble(n_.getText());
			Linear l = new Linear(m, n);

			GeneralPath path = new GeneralPath();
			path.moveTo(xMin, l.f(xMin));
			for (double j = xMin; j < xMax; j += 0.001) {
				path.lineTo(j, l.f(j));
			}
			ColorShape shape = new ColorShape(path, color, l);
			canvas.addShape(shape);
			canvas.repaint();
		}
	}

	class ClearShapes implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.shapes.clear();
			canvas.repaint();
		}
	}

	class showCoordinates implements ItemListener {


		@Override
		public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
			if(e.getStateChange() == ItemEvent.SELECTED){
				coordinates = true;
			} else{
				coordinates = false;
			}
			canvas.repaint();
		}
	}

	class setColor implements ActionListener {
			private Color c;
		public setColor(Color pC){
			c = pC;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			color = c;
		}
	}

	class Pointer extends MouseMotionAdapter {
		Point point;

		@Override
		public void mouseMoved(MouseEvent e) {
			double m00 = canvas.getWidth() / (xMax - xMin);
			double m11 = -(canvas.getHeight() / (yMax - yMin));
			double m02 = -((canvas.getWidth() / (xMax - xMin)) * xMin);
			double m12 = canvas.getHeight() + (canvas.getHeight() / (yMax - yMin) * yMin);
			matrix = new AffineTransform(m00, 0, 0, m11, m02, m12);

			point = e.getPoint();
			String msg = String.format("X: %f Y: %f ", point.getX(), point.getY());
			Point2D trans = new Point2D.Double();
			Point2D src = e.getPoint();
			try {
				trans = matrix.inverseTransform(src, trans);
			} catch (Exception q) {
				System.out.println("Funktioniert nich");
			}
			String msg2 = String.format("X: %f Y: %f", trans.getX(), trans.getY());
			a.setText(msg);
			b.setText(msg2);
		}
	}

	class Painter extends MouseAdapter {
		Point2D start, end;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			try {
				start = matrix.inverseTransform(e.getPoint(), start);
				String msg = String.format("Mouse start: %f, %f", start.getX(), start.getY());
				status.setText(msg);
				System.out.println(msg);
			} catch (Exception q) {
				System.out.println("Ihr könnt mich alle mal");

			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			try {
				end = matrix.inverseTransform(e.getPoint(), end);
				String msg = String.format("Mouse end: %f, %f", end.getX(), end.getY());
				status.setText(msg);
				System.out.println(msg);
				Graphics gc = canvas.getGraphics();
				gc.setColor(color);
				ColorShape ding = new ColorShape(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()),
						color);
				canvas.addShape(ding);
				canvas.repaint();
			} catch (Exception q) {
				System.out.println("Am arsch");
			}
		}
	}

	class CanvasView extends JPanel {
		private static final long serialVersionUID = -7968868629914748721L;
		public ArrayList<ColorShape> shapes;

		public CanvasView() {
			shapes = new ArrayList<ColorShape>();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			double m00 = canvas.getWidth() / (xMax - xMin);
			double m11 = -(canvas.getHeight() / (yMax - yMin));
			double m02 = -(canvas.getWidth() / (xMax - xMin)) * xMin;
			double m12 = canvas.getHeight() + (canvas.getHeight() / (yMax - yMin) * yMin);
			matrix = new AffineTransform(m00, 0, 0, m11, m02, m12);

			Graphics2D gc = (Graphics2D) g;
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			gc.setStroke(new BasicStroke(0));
			AffineTransform save = gc.getTransform();
			gc.transform(matrix);
			gc.setColor(Color.BLACK);
			Shape lineX = new Line2D.Double(xMin, 0, xMax, 0);
			gc.draw(lineX);
			Shape lineY = new Line2D.Double(0, yMin, 0, yMax);
			gc.draw(lineY);

			if(coordinates) {
				coordinates(g);
			}

			for (ColorShape shape : shapes) {

				if (shape.func != null) {
					GeneralPath path = new GeneralPath();
					path.moveTo(xMin, shape.func.f(xMin));
					for (double j = xMin; j < xMax; j += 0.001) {
						path.lineTo(j, shape.func.f(j));
					}

					shape.shape = path;
				}
				gc.setColor(shape.color);
				gc.draw(shape.shape);
			}
			gc.setTransform(save);
		}

		public void addShape(ColorShape s) {
			shapes.add(s);
		}

		public void coordinates(Graphics g) {
			
			double xLength = (xMax - xMin) / 10;
			double yLength = (yMax - yMin) / 10;

			double xHeight = (xMax - xMin) / 100;
			double yHeight = (yMax - yMin) / 100;
			
			double height = (xMax-xMin)/100;

			Graphics2D gc = (Graphics2D) g;
			Font font = gc.getFont();
			font = font.deriveFont((float)11);
			font = font.deriveFont(AffineTransform.getScaleInstance(1.0/matrix.getScaleX(), 1.0/matrix.getScaleY()));
			gc.setFont(font);

			for (double x = 0; x < xMax; x += xLength) {
				Line2D strich = new Line2D.Double(x, xHeight / 2, x, -(xHeight / 2));
				gc.draw(strich);
				gc.drawString("" + x, (float) x, (float) (-xHeight - height));
			}

			for (double x = -xLength; x > xMin; x -= xLength) {
				Line2D strich = new Line2D.Double(x, xHeight / 2, x, -(xHeight / 2));
				gc.draw(strich);
				gc.drawString("" + x, (float) x, (float)(-xHeight - height));
			}

			for (double y = yLength; y < yMax; y += yLength) {
				Line2D strich = new Line2D.Double(yHeight / 2, y, -(yHeight / 2), y);
				gc.draw(strich);
				gc.drawString("" + y, (float) -yHeight, (float) y);
			}

			for (double y = -yLength; y > yMin; y -= yLength) {
				Line2D strich = new Line2D.Double(yHeight / 2, y, -(yHeight / 2), y);
				gc.draw(strich);
				gc.drawString("" + y, (float) -yHeight, (float) y);
			}
		}
	}

	class ColorShape {
		public Shape shape;
		public Color color;
		public Function func;

		public ColorShape(Shape s, Color c) {
			shape = s;
			color = c;
			func = null;
		}

		public ColorShape(Shape s, Color c, Function f) {
			shape = s;
			color = c;
			func = f;
		}

	}

	class Window implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField xMin2 = new JTextField();
			JTextField xMax2 = new JTextField();
			JTextField yMin2 = new JTextField();
			JTextField yMax2 = new JTextField();

			Object[] message = { "X-Min", xMin2, "X-Max", xMax2, "Y-Min", yMin2, "Y-Max", yMax2 };

			JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			pane.createDialog(null, "Set Coordinates").setVisible(true);
			xMin = Double.parseDouble(xMin2.getText());
			xMax = Double.parseDouble(xMax2.getText());
			yMin = Double.parseDouble(yMin2.getText());
			yMax = Double.parseDouble(yMax2.getText());
			canvas.repaint();
		}
	}
}

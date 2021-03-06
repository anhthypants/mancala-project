import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayerPanel extends JFrame implements ChangeListener {

	private static final int PANEL_WIDTH = 1000;
	private static final int PANEL_HEIGHT = 400;
	private double width;
	private double height;
	private ArrayList<Double> pitNum;
	private Shape[] beads;
	private Shape[] shapes;
	private MancalaModel mancalaModel;
	private ViewStrategy viewStrategy;

	/**
	 * creates the playerpanel and initializes variables
	 * 
	 * @author anhthy
	 */

	private Point mousePoint;

	/**
	 * create MouseListeners
	 * 
	 * @author anhth
	 *
	 */
	private class MouseListeners extends MouseAdapter {
		/**
		 * updates data when mouse pressed
		 * 
		 * @param e
		 *            is the event when Mouse is pressed
		 */
		public void mousePressed(MouseEvent e) {
			mousePoint = e.getPoint();
			Shape contained = viewStrategy.createShape();
			int changePos = 0;
			for (int i = 0; i < shapes.length; i++) {
				Shape current = shapes[i];
				if (current.contains(mousePoint)) {
					contained = current;
					System.out.println(i + "Points: " + mousePoint.getX() + " " + mousePoint.getY());
					double numberOfStones = pitNum.get(i);

					mancalaModel.update(i, (double) 0);
					for (int j = i; j < numberOfStones + i; j++) {
						System.out.println((j + 1) % 14);
						mancalaModel.update((j + 1) % 14, (pitNum.get((j + 1) % 14) + 1));

					}

				}

			}

		}

		@Override
		/**
		 * allows mouse wheel to be moved
		 * 
		 * @param arg0
		 *            is where mouse wheel is moved
		 */
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			if (mousePoint == null) {
				return;
			}
			Point lastMousePoint = mousePoint;
			mousePoint = arg0.getPoint();
			double dx = mousePoint.getX() - lastMousePoint.getX();
			double dy = mousePoint.getY() - lastMousePoint.getY();

		}

	}

	public PlayerPanel(MancalaModel m) {
		mancalaModel = m;
		pitNum = mancalaModel.getData();
		
		//by default viewStrategy is Ellipse
		viewStrategy = new EllipseViewStrategy();
		
		shapes = new Shape[14];
		beads = new Shape[36];

		width = getWidth() / 8;
		height = getHeight() / 2;

		MouseListeners listeners = new MouseListeners();
		addMouseListener(listeners);
		addMouseMotionListener(listeners);

		Icon playerIcon = new Icon() {

			@Override
			public int getIconHeight() {
				// TODO Auto-generated method stub
				return PANEL_HEIGHT;
			}

			@Override
			public int getIconWidth() {
				// TODO Auto-generated method stub
				return PANEL_WIDTH;
			}

			/**
			 * paints the board with the pits and circles
			 * 
			 * @author anhthy
			 */
			public void paintIcon(Component c, Graphics g, int x, int y) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.PINK);

				double width = 900 / 8;
				double height = 400 / 2;
				double w = width;
				double h = 25;

				double dx = width - 10;
				double dy = width + 10;

				Shape circle = viewStrategy.createShape(w, h, dx, dy);
				Shape circle2 = viewStrategy.createShape(w * 2, h, dx, dy);
				Shape circle3 = viewStrategy.createShape(w * 3, h, dx, dy);
				Shape circle4 = viewStrategy.createShape(w * 4, h, dx, dy);
				Shape circle5 = viewStrategy.createShape(w * 5, h, dx, dy);
				Shape circle6 = viewStrategy.createShape(w * 6, h, dx, dy);
				Shape pitCircle = viewStrategy.createShape(5, h, width - 10, height * 2 - 100);
				shapes[5] = (circle);
				shapes[4] = (circle2);
				shapes[3] = (circle3);
				shapes[2] = (circle4);
				shapes[1] = (circle5);
				shapes[0] = (circle6);
				shapes[6] = (pitCircle);

				w = width;
				h = height + 15;

				Shape bcircle = viewStrategy.createShape(w, h, dx, dy);
				Shape bcircle2 = viewStrategy.createShape(w * 2, h, dx, dy);
				Shape bcircle3 = viewStrategy.createShape(w * 3, h, dx, dy);
				Shape bcircle4 = viewStrategy.createShape(w * 4, h, dx, dy);
				Shape bcircle5 = viewStrategy.createShape(w * 5, h, dx, dy);
				Shape bcircle6 = viewStrategy.createShape(w * 6, h, dx, dy);
				Shape bpitCircle = viewStrategy.createShape(5 + 7 * w, 25, width - 10, height * 2 - 100);
				shapes[7] = (bcircle);
				shapes[8] = (bcircle2);
				shapes[9] = (bcircle3);
				shapes[10] = (bcircle4);
				shapes[11] = (bcircle5);
				shapes[12] = (bcircle6);
				shapes[13] = (bpitCircle);

				int beadCount = 0;
				Shape bead;
				for (int i = 0; i < shapes.length; i++) {
					Shape drawn = shapes[i];

					for (int j = 0; j < pitNum.get(i); j++) {
						bead = new Bead(drawn, j).create();
						beads[beadCount] = bead;
						beadCount++;

					}

					g2.fill(drawn);
					g2.draw(drawn);

				}

				for (int i = 0; i < beads.length; i++) {
					Shape b = beads[i];
					g2.setPaint(Color.GREEN);
					g2.fill(b);
					g2.draw(b);
				}
			}

		};

		add(new JLabel(playerIcon));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public void setViewStrategy(ViewStrategy viewStrategy) 
	{	this.viewStrategy = viewStrategy;	}

	@Override
	public void stateChanged(ChangeEvent e) {
		pitNum = mancalaModel.getData();
		repaint();
	}
}

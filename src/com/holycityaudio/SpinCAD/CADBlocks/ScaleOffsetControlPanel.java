/* SpinCAD Designer - DSP Development Tool for the Spin FV-1 
 * Copyright (C)2013 - Gary Worsham 
 * Based on ElmGen by Andrew Kilpatrick 
 * 
 *   This program is free software: you can redistribute it and/or modify 
 *   it under the terms of the GNU General Public License as published by 
 *   the Free Software Foundation, either version 3 of the License, or 
 *   (at your option) any later version. 
 * 
 *   This program is distributed in the hope that it will be useful, 
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *   GNU General Public License for more details. 
 * 
 *   You should have received a copy of the GNU General Public License 
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 *     
 */ 

package com.holycityaudio.SpinCAD.CADBlocks;

import java.awt.Color;
import java.awt.Point;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



@SuppressWarnings("serial")
public class ScaleOffsetControlPanel extends JFrame implements ChangeListener{
	JSlider inLowSlider;
	JSlider inHighSlider;
	JSlider outLowSlider;
	JSlider outHighSlider;


	JLabel inLowLabel;
	JLabel inHighLabel;
	JLabel outLowLabel;
	JLabel outHighLabel;
	
	private ScaleOffsetControlCADBlock sof;
	
	public ScaleOffsetControlPanel(ScaleOffsetControlCADBlock scaleOffsetControlCADBlock) {
		this.sof = scaleOffsetControlCADBlock;
		this.setTitle("Scale Offset");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
		
	}

	private void createAndShowGUI() {
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		inLowSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		inLowSlider.addChangeListener(this);
		inHighSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		inHighSlider.addChangeListener(this);
		outLowSlider = new JSlider(JSlider.HORIZONTAL, -200, 100, 0);
		outLowSlider.addChangeListener(this);
		outHighSlider = new JSlider(JSlider.HORIZONTAL, -200, 100, 0);
		outHighSlider.addChangeListener(this);
		
		inLowLabel = new JLabel();
		inHighLabel = new JLabel();
		outLowLabel = new JLabel();
		outHighLabel = new JLabel();
		
		this.getContentPane().add(inLowLabel);
		this.getContentPane().add(inLowSlider);
		this.getContentPane().add(inHighLabel);
		this.getContentPane().add(inHighSlider);
		this.getContentPane().add(outLowLabel);
		this.getContentPane().add(outLowSlider);
		this.getContentPane().add(outHighLabel);
		this.getContentPane().add(outHighSlider);		
		
		inLowSlider.setValue((int)Math.round((sof.getInLow() * 100.0)));
		inHighSlider.setValue((int)Math.round((sof.getInHigh() * 100.0)));
		outLowSlider.setValue((int)Math.round((sof.getOutLow() * 100.0)));
		outHighSlider.setValue((int)Math.round((sof.getOutHigh() * 100.0)));
		inLowLabel.setText("Input Low " + String.format("%2.2f", sof.getInLow()));
		inHighLabel.setText("Input High " + String.format("%2.2f", sof.getInHigh()));
		outLowLabel.setText("Output Low " + String.format("%2.2f", sof.getOutLow()));
		outHighLabel.setText("Output High " + String.format("%2.2f", sof.getOutHigh()));

		this.setVisible(true);
		this.pack();
		this.setLocation(new Point(sof.getX() + 200, sof.getY() + 150));
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		
	}
	
	public void stateChanged(ChangeEvent ce) {
		if(ce.getSource() == inLowSlider) {
			sof.setInLow((double)inLowSlider.getValue() / 100.0);
			if(checkValuesInRange()) {
				inLowLabel.setForeground(Color.BLACK);
				inLowLabel.setOpaque(false);		
			} else  {
				inLowLabel.setBackground(Color.RED);
				inLowLabel.setForeground(Color.WHITE);
				inLowLabel.setOpaque(true);		
			} 
			inLowLabel.setText("Input Low " + String.format("%2.2f", sof.getInLow()));
		}
		else if(ce.getSource() == inHighSlider) {
			sof.setInHigh((double)inHighSlider.getValue() / 100.0);
			if(checkValuesInRange()) {
				inHighLabel.setForeground(Color.BLACK);
				inHighLabel.setOpaque(false);		
			} else  {
				inHighLabel.setBackground(Color.RED);
				inHighLabel.setForeground(Color.WHITE);
				inHighLabel.setOpaque(true);		
			} 
			inHighLabel.setText("Input High " + String.format("%2.2f", sof.getInHigh()));
		}
		else if(ce.getSource() == outLowSlider) {
			sof.setOutLow((double)outLowSlider.getValue() / 100.0);
			if(checkValuesInRange()) {
				outLowLabel.setForeground(Color.BLACK);
				outLowLabel.setOpaque(false);		
			} else  {
				outLowLabel.setBackground(Color.RED);
				outLowLabel.setForeground(Color.WHITE);
				outLowLabel.setOpaque(true);		
			} 
			outLowLabel.setText("Output Low " + String.format("%2.2f", sof.getOutLow()));
		}
		else if(ce.getSource() == outHighSlider) {
			sof.setOutHigh((double)outHighSlider.getValue() / 100.0);
			if(checkValuesInRange()) {
				outHighLabel.setForeground(Color.BLACK);
				outHighLabel.setOpaque(false);		
			} else  {
				outHighLabel.setBackground(Color.RED);
				outHighLabel.setForeground(Color.WHITE);
				outHighLabel.setOpaque(true);		
			} 
			outHighLabel.setText("Output High " + String.format("%2.2f", sof.getOutHigh()));
		}
	}

	public boolean checkValuesInRange() {
		double scale = (sof.getOutHigh() - sof.getOutLow())/(sof.getInHigh() - sof.getInLow());
		double offset = sof.getOutLow() - (sof.getInLow() * scale);
		if((scale < -2.0) || (scale > 1.99993896484) || (offset < -1.0) || (offset > 1.0)) {
			return false;			
		}
		return true;
	}
}
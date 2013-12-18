/* SpinCAD Designer - DSP Development Tool for the Spin FV-1
 * BPFControlPanel.java 
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

import java.awt.Point;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Mixer3_1ControlPanel {
	private JFrame frame;

	JSlider gain1Slider;
	JLabel gain1Label;
	JSlider gain2Slider;
	JLabel gain2Label;
	JSlider gain3Slider;
	JLabel gain3Label;

	private Mixer3_1CADBlock spbMix;

	public Mixer3_1ControlPanel(Mixer3_1CADBlock mixer3_1cadBlock) {

		spbMix = mixer3_1cadBlock;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				frame = new JFrame();
				frame.setTitle("Volume");
				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

				gain1Slider = new JSlider(JSlider.HORIZONTAL, 1, 50, (int) (spbMix.getGain1() * 100));
				gain1Slider.addChangeListener(new volumeSliderListener());
				gain1Label = new JLabel();
				
				gain2Slider = new JSlider(JSlider.HORIZONTAL, 1, 50, (int) (spbMix.getGain2() * 100));
				gain2Slider.addChangeListener(new volumeSliderListener());
				gain2Label = new JLabel();

				gain3Slider = new JSlider(JSlider.HORIZONTAL, 1, 50, (int) (spbMix.getGain3() * 100));
				gain3Slider.addChangeListener(new volumeSliderListener());
				gain3Label = new JLabel();
				
				frame.getContentPane().add(gain1Label);
				frame.getContentPane().add(gain1Slider);
				frame.getContentPane().add(gain2Label);
				frame.getContentPane().add(gain2Slider);
				frame.getContentPane().add(gain3Label);
				frame.getContentPane().add(gain3Slider);
				
				updateGainLabels();
				frame.setVisible(true);	
				frame.setResizable(false);
				frame.setLocation(new Point(spbMix.getX() + 200, spbMix.getY() + 150));
				frame.pack();
			}
		});
	}

	class volumeSliderListener implements ChangeListener { 
		public void stateChanged(ChangeEvent ce) {
			if(ce.getSource() == gain1Slider) {
				spbMix.setGain1((double) gain1Slider.getValue()/100.0);
				updateGainLabels();
			}
			else if(ce.getSource() == gain2Slider) {
				spbMix.setGain2((double) gain2Slider.getValue()/100.0);
				updateGainLabels();
			}
			else if(ce.getSource() == gain3Slider) {
				spbMix.setGain3((double) gain3Slider.getValue()/100.0);
				updateGainLabels();
			}
		}
	}

	public void updateGainLabels() {
		gain1Label.setText("Gain " + String.format("%4.2f", spbMix.getGain1()));		
		gain2Label.setText("Gain " + String.format("%4.2f", spbMix.getGain2()));		
		gain3Label.setText("Gain " + String.format("%4.2f", spbMix.getGain3()));		

	}
}

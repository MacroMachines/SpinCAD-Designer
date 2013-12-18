/* ElmGen - DSP Development Tool
 * Copyright (C)2011 - Andrew Kilpatrick
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
class RingModControlPanel extends JFrame implements ChangeListener, ActionListener {
	JSlider lfoSlider;
	JLabel lfoLabel;
	
	private RingModCADBlock outBlock;
	
	public RingModControlPanel(RingModCADBlock ringModCADBlock) {
		this.outBlock = ringModCADBlock;
		this.setTitle("Ring Mod");
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		lfoSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		lfoSlider.addChangeListener(this);
		
		lfoLabel = new JLabel();
		
		this.getContentPane().add(lfoLabel);
		this.getContentPane().add(lfoSlider);
		
		lfoSlider.setValue((int)Math.round(100.0 * outBlock.getLFO()));
		lfoLabel.setText("LFO "	+ String.format("%2.2f", outBlock.getLFO()));
		
		this.setVisible(true);
		this.pack();
		this.setLocation(outBlock.getX() + 200, outBlock.getY() + 150);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void stateChanged(ChangeEvent ce) {
		if(ce.getSource() == lfoSlider) {
			outBlock.setLFO((double) lfoSlider.getValue()/100.0);
			lfoLabel.setText("LFO "
					+ String.format("%2.2f", outBlock.getLFO()));
		}
	}
}
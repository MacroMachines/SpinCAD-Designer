/* SpinCAD Designer - DSP Development Tool for the Spin FV-1
 * HPF2PCADBlock.java
 * Copyright (C) 2013 - 2014 - Gary Worsham
 * Based on ElmGen by Andrew Kilpatrick.  Modified by Gary Worsham 2013 - 2014.  Look for GSW in code.
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

import com.holycityaudio.SpinCAD.SpinCADPin;
import com.holycityaudio.SpinCAD.SpinFXBlock;

public class HPF2PCADBlock extends FilterCADBlock{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5711126291575876825L;
	double f0 = 240;
	double kqh = 0.2;

	public HPF2PCADBlock(int x, int y) {
		super(x, y);
		addInputPin(this, "Audio Input");
		addOutputPin(this, "Audio Output");
		hasControlPanel = true;
		addControlInputPin(this);
		setName("High Pass 2P");	}

	public void editBlock(){
		new HPF2PControlPanel(this);
	}	

	public void generateCode(SpinFXBlock sfxb) {
		// coefficients

		int input = -1;

		SpinCADPin p = this.getPin("Audio Input").getPinConnection();

		if(p != null) {
			input = p.getRegister();

			int kfh = sfxb.allocateReg();
			int byp = sfxb.allocateReg();
			int hp1al = sfxb.allocateReg();
			int hp1bl = sfxb.allocateReg();
			int hpout = sfxb.allocateReg();
			sfxb.comment("2 pole high pass");

			sfxb.skip(RUN, 3);
			sfxb.clear();
			sfxb.writeRegister(hp1al,  0);
			sfxb.writeRegister(hp1bl,  0);

			//			;prepare pot2 for low pass frequency control:
			p = this.getPin("Control Input 1").getPinConnection();
			int control1 = -1;
			if(p != null) {
				control1 = p.getRegister();
				//				rdax	pot2,1		;get pot2
				sfxb.readRegister(control1,1);
				//				sof	0.5,-0.5	;ranges -0.5 to 0
				sfxb.scaleOffset(0.5,  -0.5);
				//				exp	1,0
				sfxb.exp(1, 0);
				//				wrax	kfl,0		;write to LP filter control
				sfxb.writeRegister(kfh, 0);
				//				;now derive filter bypass function (at open condition)

				//				rdax	pot2,1		;read pot2 (LP) again
				sfxb.readRegister(control1,1);
				//				sof	1,-0.999
				sfxb.scaleOffset(1,  -0.999);
				//				exp	1,0
				sfxb.exp(1, 0);
				//				wrax	lbyp,0
				sfxb.writeRegister(byp,  0);
			} else {
				sfxb.scaleOffset(0, 0.25);	// set dummy value
				sfxb.writeRegister(kfh,  0);
				sfxb.writeRegister(byp,  0);
			}

			// ------------- start of filter code
//			rdax	lp1al,1
			sfxb.readRegister(hp1al,1);
//			mulx	kfl
			sfxb.mulx(kfh);
//			rdax	lp1bl,1
			sfxb.readRegister(hp1bl,1);
//			wrax	lp1bl,-1
			sfxb.writeRegister(hp1bl, -1);
//			rdax	lp1al,kql
			sfxb.readRegister(hp1al,-kqh);
//			rdax	fol,1
			sfxb.readRegister(input,1);
			sfxb.writeRegister(hpout, 1);
//			mulx	kfl
			sfxb.mulx(kfh);
//			rdax	lp1al,1
			sfxb.readRegister(hp1al,1);
//			wrax	lp1al,0
			sfxb.writeRegister(hp1al, 0);


			this.getPin("Audio Output").setRegister(hpout);	
		}
		System.out.println("HPF 4 pole code gen!");
	}

	public double getFreq() {
		return f0;
	}

	public void setFreq(double f) {
		f0 = f;
	}

	public void setIs4Pole(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public boolean getIs4Pole() {
		// TODO Auto-generated method stub
		return false;
	}

	public double getQ() {
		return kqh/10.0;
	}

	public void setQ(double value) {
		kqh = 10/(value); 
	}

}

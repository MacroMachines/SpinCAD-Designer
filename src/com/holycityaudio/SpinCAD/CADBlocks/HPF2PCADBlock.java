package com.holycityaudio.SpinCAD.CADBlocks;

import com.holycityaudio.SpinCAD.SpinCADPin;
import com.holycityaudio.SpinCAD.SpinFXBlock;

public class HPF2PCADBlock extends FilterCADBlock{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5711126291575876825L;
	double f0 = 240;
	public HPF2PCADBlock(int x, int y) {
		super(x, y);
		addControlInputPin(this);
		setName("High Pass 2P");	}

	public void editBlock(){
		//		new LPF1PControlPanel(this);
	}	

	public void generateCode(SpinFXBlock sfxb) {
		// coefficients

		int input = -1;

		SpinCADPin p = this.getPin("Audio Input 1").getPinConnection();

		if(p != null) {
			input = p.getRegister();

			int kfh = sfxb.allocateReg();
			int byp = sfxb.allocateReg();
			int hp1al = sfxb.allocateReg();
			int hp1bl = sfxb.allocateReg();
			int hpout = sfxb.allocateReg();
			double kqh = -0.2;
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
			sfxb.readRegister(hp1al,kqh);
//			rdax	fol,1
			sfxb.readRegister(input,1);
			sfxb.writeRegister(hpout, 1);
//			mulx	kfl
			sfxb.mulx(kfh);
//			rdax	lp1al,1
			sfxb.readRegister(hp1al,1);
//			wrax	lp1al,0
			sfxb.writeRegister(hp1al, 0);


			this.getPin("Audio Output 1").setRegister(hpout);	
		}
		System.out.println("LPF 4 pole code gen!");
	}

	public double getFreq() {
		return f0;
	}

	public void setFreq(double f) {
		f0 = f;
	}
}

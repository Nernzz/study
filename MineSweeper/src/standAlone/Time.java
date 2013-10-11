package standAlone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;


class Time implements ActionListener{
	private static int time = 0;

	@Override
	public void actionPerformed(ActionEvent e) {
		JLabel label = Main.getLabel();
		int f = MapClick.getF();

		if(f == 1){
			label.setText("Time:"+ ++time);
			Main.setLabel(label);
		}else if(f == 2){
			Main.timer.stop();
		}
	}

	static int getTime(){
		return time;
	}

	static void setTime(int time){
		Time.time = time;
	}
}
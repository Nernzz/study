package standAlone;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

class Dialog extends JDialog implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = -6125220581096443687L;

	Dialog(Frame owner, String s){
		super(owner);
		getContentPane().setLayout(new FlowLayout());

		JButton btn = new JButton("終了");
		JButton btn2 = new JButton("もう一度プレイ");
		btn.addActionListener(this);
		btn2.addActionListener(this);
		getContentPane().add(btn);
		getContentPane().add(btn2);

		setTitle(s);
		setSize(200, 140);
		setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand() == "終了"){
			System.exit(0);
		}else if(e.getActionCommand() == "もう一度プレイ"){
			setVisible(false);
		}
		MapClick.mapInitialize();
	}
}
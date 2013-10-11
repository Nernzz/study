import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

class MineGUI extends JFrame implements ActionListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 8174425090961383164L;
	MineGUI() {
		JButton b[][];
		/*--- MenuBar ---*/
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem menuOpen = new JMenuItem("Open");
		JMenuItem menuExit = new JMenuItem("Exit");
		JMenu menuView = new JMenu("View");
		JCheckBoxMenuItem menuTool = new JCheckBoxMenuItem("Tool Bar");
		JMenu menuSize = new JMenu("Size");
		JMenuItem menuSizeLarge = new JMenuItem("Large");
		JMenuItem menuSizeSmall = new JMenuItem("Small");

		menuFile.setMnemonic('F');
		menuOpen.setMnemonic('O');
		menuExit.setMnemonic('x');
		menuView.setMnemonic('V');
		menuTool.setMnemonic('T');
		menuSize.setMnemonic('S');
		menuSizeLarge.setMnemonic('L');
		menuSizeSmall.setMnemonic('S');

		menuOpen.addActionListener(this);
		menuExit.addActionListener(this);
		menuTool.addActionListener(this);
		menuSizeLarge.addActionListener(this);
		menuSizeSmall.addActionListener(this);

		getRootPane().setJMenuBar(menuBar);
		menuBar.add(menuFile);
		menuFile.add(menuOpen);
		menuFile.add(menuExit);
		menuBar.add(menuView);
		menuView.add(menuTool);
		menuView.add(menuSize);
		menuSize.add(menuSizeLarge);
		menuSize.add(menuSizeSmall);

		/*--- Minefield ---*/
		b = new JButton[9][9];
		setLayout(new GridLayout(9, 9));
	    for(int j = 0; j < 9; j++){
			for(int i = 0; i < 9; i++){
		    	b[i][j] = new JButton("");
		    	b[i][j].addActionListener(this/*new MapOpen(i,j)*/);
		    	add(b[i][j]);
		    }
	    }

		/*--- Property ---*/
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MineSweeper");
		setSize(250, 300);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.print(e);
	}
}
/*
class MapOpen implements ActionListener{
	private int x,y,f = 0;
	public MapOpen(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(x +" "+ y);
		if(f == 0){ // 最初のクリック

			f++;
		}else{

		}
	}
}
*/
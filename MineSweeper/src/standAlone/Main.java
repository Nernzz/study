package standAlone;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;


/***
 *
 * @author s1023092
 */

class Main extends JFrame implements ActionListener{
	/**
	 *
	 */
	private static final long serialVersionUID = 1764198284798537056L;

	public static void main(String[] args){
		new Main();
	}

	Container container;
	JPanel panel;
	JPanel panel2;
	static JLabel label;
	static JButton b[][];
    static Timer timer = new Timer(1000, new Time());

	Main() {
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
		container = getContentPane();
		container.setLayout(null);
		container.setBounds(0, 0, 380, 440);
		panel = new JPanel(new GridLayout(9,9));
		panel.setBounds(0, 0, 380, 400);
	    panel2 = new JPanel();
		panel2.setBounds(0, 400, 380, 40);
		label = new JLabel("Time:0");
	    panel2.add(label);
	    b = new JButton[9][9];
	    for(int j = 0; j < 9; j++){
			for(int i = 0; i < 9; i++){
				b[i][j] = new JButton("");
		    	b[i][j].addActionListener(new MapClick(i,j));
		    	b[i][j].addMouseListener(new MapClick(i,j));
	    		panel.add(b[i][j]);
		    }
	    }
	    container.add(panel);
	    container.add(panel2);

		/*--- Property ---*/
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MineSweeper");
		setSize(388, 490);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e){ // メニューバー

	}

	static JLabel getLabel(){
		return label;
	}

	static void setLabel(JLabel label){
		Main.label = label;
	}

	static JButton[][] getButton(){
		return b;
	}

	static void setButton(JButton[][] b){
		Main.b = b;
	}
}
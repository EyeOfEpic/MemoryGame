import java.awt.EventQueue;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Main {
	private static final ExecutorService pool = Executors.newCachedThreadPool();
	private JFrame frame;
	private static int seite = 4; //4 oder 6
	public ArrayList<ImageIcon> Icons = new ArrayList<ImageIcon>();
	Random rnd = new Random();
	private static ImageIcon id;
	private static boolean zustand = false;
	private static int pairsLeft = (seite*seite)/2;
	private static JButton tempBut1;
	private static JButton tempBut2;
	private JLabel paare;


	/**
	 * Launch the application.
	 */
	public void befüllen() throws IOException
	{

		File f = new File("./bilder"); // current directory

		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".png") || lowercaseName.endsWith(".jpg")) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] files = f.listFiles(textFilter);
		for (File file : files) {
			ImageIcon i = new ImageIcon(file.getCanonicalPath());
			Icons.add(i);
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		paare = new JLabel("00");
		paare.setBounds(628, 11, 46, 14);
		frame.getContentPane().add(paare);

		JLabel lblPaarebrig = new JLabel("Paare \u00FCbrig");
		lblPaarebrig.setBounds(532, 11, 69, 14);
		frame.getContentPane().add(lblPaarebrig);

		try {
			befüllen();
		} catch (IOException e) {System.out.println("Irgendetwas ging beim laden der Bilder schief!");}

		ArrayList<ImageIcon> memBilder = new ArrayList<ImageIcon>();
		for (int i = 0; i < (seite*seite)/2;i++)	// alle bilder in ein Array, und dieses Array dann verdoppeln (von 1,2,3,4 zu 1,1,2,2,3,3,4,4)
		{
			ImageIcon temp = Icons.get(i);
			memBilder.add(temp);
			memBilder.add(temp);
		}
		for (int i =0; i<seite; i++)
		{
			for (int j =0; j<seite; j++)
			{
				JButton btn = new JButton("");
				btn.setBounds((30+(i*100)), (30+(j*100)), 90, 90);
				frame.getContentPane().add(btn);
				int temp = rnd.nextInt(memBilder.size()); 
				btn.setDisabledIcon(memBilder.get(temp));
				memBilder.remove(temp);
				btn.setName(Integer.toString(i)+Integer.toString(j));
				btn.addActionListener(new BtnDActionListener());
				//btn.setIcon(btn.getDisabledIcon());
			}
		}
		

	}
	private class BtnDActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			((AbstractButton) arg0.getSource()).setIcon(((AbstractButton) arg0.getSource()).getDisabledIcon());
			if (zustand == false)
			{
				tempBut1 = (JButton) arg0.getSource();
				id = (ImageIcon) tempBut1.getDisabledIcon();
				zustand = true;
			}
			else if (zustand == true)
			{
				tempBut2 = (JButton) arg0.getSource();
				if (id == tempBut2.getDisabledIcon() && tempBut1.getName() != tempBut2.getName())
				{
					tempBut1.setDisabledIcon(tempBut1.getIcon());
					tempBut1.setIcon(null);
					tempBut2.setDisabledIcon(tempBut2.getIcon());
					tempBut2.setIcon(null);
					tempBut1.setEnabled(false);
					tempBut2.setEnabled(false);		
					pairsLeft--;
					
					paare.setText(Integer.toString(pairsLeft));
					if (pairsLeft == 0)
					{
						paare.setText("0");
						JOptionPane pane = new JOptionPane();
						JOptionPane.showMessageDialog (null, "Gewonnen!", "Glückwunsch!", JOptionPane.INFORMATION_MESSAGE);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.exit(0                                                                                                                                 );
					
					}
					
					zustand = false;
				}else if (tempBut1.getName()!=tempBut2.getName())
				{
					tempBut2.setIcon(tempBut2.getDisabledIcon());
					pool.execute(() -> {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						tempBut1.setDisabledIcon(tempBut1.getIcon());
						tempBut1.setIcon(null);
						tempBut2.setDisabledIcon(tempBut2.getIcon());
						tempBut2.setIcon(null);
						zustand = false;
					}); 
				}
			}
		}
	}
}

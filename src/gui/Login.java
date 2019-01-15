package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
/**
 * This class represents the login window of the game
 * @author Shayke Shok and Omer Edut
 *
 */
public class Login {

	private JFrame frmLogin;
	private JTextField textid1;
	private JTextField textid2;
	private JTextField textid3;
	private String id1, id2, id3;
	private JLabel txtErr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 450, 300);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblLoginWindow = new JLabel("Login Window");
		lblLoginWindow.setBackground(Color.CYAN);
		lblLoginWindow.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginWindow.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnAutomaticGame = new JButton("automatic game");
		btnAutomaticGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (canContinue()) {
					MainWindow window = new MainWindow(id1,id2,id3,true);
					window.setVisible(true);
					window.setSize(1433, 632);
					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frmLogin.dispose();
				}else {
					txtErr.setVisible(true);
				}
			}
		});
		JButton btnUserGame = new JButton("user game");
		btnUserGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (canContinue()) {
					MainWindow window = new MainWindow(id1,id2,id3,false);
					window.setVisible(true);
					window.setSize(1433, 632);
					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frmLogin.dispose();
				}else {
					txtErr.setVisible(true);
				}
			}

		});
		

		textid1 = new JTextField();
		textid1.setColumns(10);

		textid2 = new JTextField();
		textid2.setColumns(10);

		textid3 = new JTextField();
		textid3.setColumns(10);

		JLabel lblid1 = new JLabel("ID1:");
		lblid1.setLabelFor(textid1);

		JLabel lblid2 = new JLabel("ID2:");
		lblid2.setLabelFor(textid2);

		JLabel lblid3 = new JLabel("ID3:");
		lblid3.setLabelFor(textid3);
		
		txtErr = new JLabel("*You must fill at least one of the id inputs");
		txtErr.setVisible(false);
		txtErr.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtErr.setForeground(Color.RED);
		GroupLayout groupLayout = new GroupLayout(frmLogin.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(lblLoginWindow, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(113)
					.addComponent(btnUserGame, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAutomaticGame))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(153)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblid2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(textid2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblid1)
							.addGap(18)
							.addComponent(textid1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblid3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(textid3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(52)
					.addComponent(txtErr))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblLoginWindow)
					.addGap(32)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textid1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblid1))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textid2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblid2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textid3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblid3))
					.addGap(22)
					.addComponent(txtErr)
					.addPreferredGap(ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAutomaticGame, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnUserGame, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		frmLogin.getContentPane().setLayout(groupLayout);
	}
	/**
	 * get the id from the frame inputs
	 */
	private void getIDfromInput() {
		try {
			id1 = textid1.getText();
			id2 = textid2.getText();
			id3 = textid3.getText();
		} catch (NullPointerException e) {
		}
	}
	
	/**
	 * check if least one of the id inputs is not null
	 * @return true if least one is not null
	 */
	private boolean canContinue() {
		getIDfromInput();
		if ((id1 == null && id2 == null && id3 == null)||(id1.isEmpty() && id2.isEmpty() && id3.isEmpty()))
			return false;
		return true;
	}
	/**
	 * get the id from the frame inputs
	 */
}

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VotingWindow extends JFrame {
	private JTextField textField;
	private JTextField textField_1;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	public VotingWindow(final VoteSystem voteSystem) {
		
		setResizable(false);
		final JFrame frame = new JFrame();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0,screen.width,screen.height - 30);
		setExtendedState(frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("E-Voting Machine");
		getContentPane().setLayout(null);
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, screen.width, screen.height -30);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Login", null, panel_3, null);
		tabbedPane.setEnabledAt(0, false);
		panel_3.setLayout(null);
		
		final JPanel panel = new JPanel();
		tabbedPane.addTab("Vote", null, panel, null);
		tabbedPane.setEnabledAt(1, false);
		panel.setLayout(null);
		
		final JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Review", null, panel_1, null);
		tabbedPane.setEnabledAt(2, false);
		panel_1.setLayout(null);
		
		final JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Confirmation", null, panel_2, null);
		tabbedPane.setEnabledAt(3, false);
		panel_2.setLayout(null);
		
		
		final JPanel panel_4 = new JPanel();
		tabbedPane.addTab("", null, panel_4, null);
		tabbedPane.setEnabledAt(4, false);
		panel_4.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Please selection from the following options:");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel.setBounds(455, 16, 406, 25);
		panel_4.add(lblNewLabel);
		
		JButton btnUnofficialTally = new JButton("Official Tally");
		btnUnofficialTally.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 JOptionPane.showMessageDialog(getContentPane(), voteSystem.voteDB.getTally());
			}
		});
		btnUnofficialTally.setBounds(554, 67, 147, 29);
		panel_4.add(btnUnofficialTally);
		
		JButton btnRecount = new JButton("Recount");
		btnRecount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), "Every vote is printed anonymously to a file 'Recount.txt' in "
						+ "\nthe home folder. Please check there for a manual recount.");
			}
		});
		btnRecount.setBounds(554, 108, 147, 29);
		panel_4.add(btnRecount);
		
		JButton btnClosePoll = new JButton("Reset Polls");
		btnClosePoll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), "Are you sure you want to erase all \nstored information, including results?");
				voteSystem.userDB.dropTable();
				voteSystem.voteDB.dropTable();
				try{
					File textFile = new File("Recount.txt");
					FileWriter fw = new FileWriter(textFile, true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write("Poll Closed! Votes about this line are from a separate election");
					bw.newLine();
					bw.close();
					fw.close();
		    	}catch(Exception x){
		    		x.printStackTrace();
		    	}
				dispose(); //Close window
				getContentPane().setVisible(false); //Hide window
				String[] args = new String[2];
				voteSystem.main(args);
			}
		});
		btnClosePoll.setBounds(554, 190, 147, 29);
		panel_4.add(btnClosePoll);
		
		JButton btnCertifyVotes = new JButton("Certify Votes");
		btnCertifyVotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String feedback = voteSystem.certifyVotes();
				JOptionPane.showMessageDialog(null, feedback);
			}
		});
		btnCertifyVotes.setBounds(554, 149, 147, 29);
		panel_4.add(btnCertifyVotes);
		
		JButton btnNewButton = new JButton("logout");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); //Close window
				getContentPane().setVisible(false); //Hide window
				VotingWindow open = new VotingWindow(voteSystem);
				open.setVisible(true);
			}
		});
		btnNewButton.setBounds(578, 268, 102, 29);
		panel_4.add(btnNewButton);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(501, 78, 62, 16);
		panel_3.add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(575, 72, 134, 28);
		panel_3.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(575, 112, 134, 28);
		panel_3.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblPassword = new JLabel("Voter ID#");
		lblPassword.setBounds(502, 118, 61, 16);
		panel_3.add(lblPassword);
		
		JButton btnLoginAsVoter = new JButton("Login as Voter");
		btnLoginAsVoter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				Integer id = Integer.valueOf(textField_1.getText());
				String role = "V";
				int status = voteSystem.login(username, id, role);
				if (status == 200)
					tabbedPane.setSelectedComponent(panel);
				else if (status == 250)
					JOptionPane.showMessageDialog(getContentPane(), "This user already voted.");
				else if (status == 100)
					JOptionPane.showMessageDialog(getContentPane(), "You are not a registered User!");
				else if (status == 150)
					JOptionPane.showMessageDialog(getContentPane(), "Incorrect Login Information. Please try again.");
				else if (status == 50)
					JOptionPane.showMessageDialog(getContentPane(), "Too many login attempts. Account is locked.");
			}
		});
		btnLoginAsVoter.setBounds(423, 168, 184, 29);
		panel_3.add(btnLoginAsVoter);
		
		JButton btnLoginAsElection = new JButton("Login as Poll Worker");
		btnLoginAsElection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				Integer id = Integer.valueOf(textField_1.getText());
				String role = "EO";
				int status = voteSystem.login(username, id, role);
				if (status == 200)
					tabbedPane.setSelectedComponent(panel_4);
				else if (status == 150)
					JOptionPane.showMessageDialog(getContentPane(), "Incorrect Login Information. Please try again.");
				else if (status == 50)
					JOptionPane.showMessageDialog(getContentPane(), "Too many login attempts. Account is locked.");
			}
		});
		btnLoginAsElection.setBounds(619, 168, 198, 29);
		panel_3.add(btnLoginAsElection);
		
		JLabel lblWelcomeToThe = new JLabel("Welcome to the E-voting System");
		lblWelcomeToThe.setBounds(501, 16, 265, 16);
		panel_3.add(lblWelcomeToThe);
		
		JLabel lblPresident = new JLabel("Presidential Candidates");
		lblPresident.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblPresident.setBounds(38, 17, 226, 43);
		panel.add(lblPresident);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 102, 153));
		separator.setBounds(6, 6, 1247, 12);
		panel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(0, 102, 153));
		separator_1.setBounds(6, 62, 1247, 12);
		panel.add(separator_1);
		
		JRadioButton rdbtnPikachu = new JRadioButton("Pikachu");
		rdbtnPikachu.setActionCommand("Pikachu");
		buttonGroup.add(rdbtnPikachu);
		rdbtnPikachu.setBounds(101, 84, 141, 23);
		panel.add(rdbtnPikachu);
		
		JRadioButton rdbtnSquirtle = new JRadioButton("Squirtle");
		rdbtnSquirtle.setActionCommand("Squirtle");
		buttonGroup.add(rdbtnSquirtle);
		rdbtnSquirtle.setBounds(101, 119, 141, 23);
		panel.add(rdbtnSquirtle);
		
		JRadioButton rdbtnBulbasaur = new JRadioButton("Bulbasaur");
		rdbtnBulbasaur.setActionCommand("Bulbasaur");
		buttonGroup.add(rdbtnBulbasaur);
		rdbtnBulbasaur.setBounds(101, 154, 141, 23);
		panel.add(rdbtnBulbasaur);
		
		JRadioButton rdbtnCharmander = new JRadioButton("Charmander");
		rdbtnCharmander.setActionCommand("Charmander");
		rdbtnCharmander.setBounds(101, 189, 141, 23);
		buttonGroup.add(rdbtnCharmander);
		panel.add(rdbtnCharmander);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(new Color(0, 102, 153));
		separator_2.setBounds(6, 238, 1247, 12);
		panel.add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(new Color(0, 102, 153));
		separator_3.setBounds(6, 300, 1247, 12);
		panel.add(separator_3);
		
		final JTextPane textPane = new JTextPane();
		textPane.setBounds(617, 88, 186, 16);
		panel_1.add(textPane);
		
		JButton btnSubmitVotes = new JButton("Submit Votes");
		btnSubmitVotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (buttonGroup.getSelection() != null && buttonGroup.getSelection() != null)
				{
					textPane.setText(buttonGroup.getSelection().getActionCommand());
					tabbedPane.setSelectedComponent(panel_1);
				}
				else
					JOptionPane.showMessageDialog(getContentPane(), "Please make a selection for each field.");
			}
		});
		btnSubmitVotes.setBounds(563, 566, 117, 29);
		panel.add(btnSubmitVotes);
		
		JLabel lblPleaseConfirmYour = new JLabel("Please Confirm Your Selections");
		lblPleaseConfirmYour.setBounds(489, 30, 269, 22);
		lblPleaseConfirmYour.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		panel_1.add(lblPleaseConfirmYour);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(6, 64, 1247, 12);
		separator_5.setForeground(new Color(0, 102, 153));
		panel_1.add(separator_5);
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voteSystem.submit(buttonGroup.getSelection().getActionCommand());
				tabbedPane.setSelectedComponent(panel_2);
			}
		});
		btnConfirm.setBounds(476, 201, 117, 29);
		panel_1.add(btnConfirm);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedComponent(panel);
			}
		});
		btnEdit.setBounds(653, 201, 117, 29);
		panel_1.add(btnEdit);
		
		JLabel lblPresidentialCandidate = new JLabel("Presidential Candidate:");
		lblPresidentialCandidate.setBounds(448, 88, 145, 16);
		panel_1.add(lblPresidentialCandidate);
		
		JLabel lblThankYouFor = new JLabel("Thank you for voting in the 2016 Election!");
		lblThankYouFor.setBounds(391, 126, 496, 30);
		lblThankYouFor.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		panel_2.add(lblThankYouFor);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); //Close window
				getContentPane().setVisible(false); //Hide window
				VotingWindow open = new VotingWindow(voteSystem);
				open.setVisible(true);
			}
		});
		btnLogout.setBounds(559, 196, 117, 29);
		panel_2.add(btnLogout);
		
		JLabel lblYourSelectionsHave = new JLabel("Your selections have been submitted.");
		lblYourSelectionsHave.setBounds(507, 18, 325, 36);
		panel_2.add(lblYourSelectionsHave);
		getContentPane().add(tabbedPane);
		
		getContentPane().add(tabbedPane);
	}
}

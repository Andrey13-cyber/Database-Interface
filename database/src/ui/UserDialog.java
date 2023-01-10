package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import core.DataClass;
import core.User;
import dao.UserADO;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class UserDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPasswordField passwordField;
	private final JPanel panel = new JPanel();
	private JComboBox userComboBox;
	
	private UserADO userADO;
	private User user;
	
	public void setUserADO(UserADO theUserADO) {
		userADO = theUserADO;
	}

	public void populateUsers(List<User> users) {
		List<String> userLogin = new ArrayList<>();
		 for (int i=0; i<users.size(); i++) {
		      userLogin.add(users.get(i).getLogin());
		    }
		
		userComboBox.setModel(new DefaultComboBoxModel(userLogin.toArray()));
		
	}
	
	public static void main(String[] args) {
		try {
			UserDialog dialog = new UserDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			
			StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    		System.out.println(passwordEncryptor.encryptPassword("54321"));
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @throws Exception 
	 */
	public UserDialog() throws Exception {
		setTitle("Вход");
		try {
			setUserADO(userADO = new UserADO());
			
		} catch(Exception exc){
			JOptionPane.showInputDialog(this, "Error: " + exc, JOptionPane.ERROR_MESSAGE);
			
		}
		setBounds(100, 100, 502, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						User selectedUser;
						User loggedUser;
			            try {
			            	selectedUser = userADO.convertNameToUser(userComboBox.getSelectedItem().toString());
			            	
			            	selectedUser.setLoggedIn(selectedUser.getId());
			            
			            	String plainTextPassword = passwordField.getText();
			            	
			            	boolean isValidPassword = userADO.authenticate(selectedUser, plainTextPassword);
			            	
			            	if (isValidPassword) {
			            		setVisible(false);
					            dispose();
					            ChooseDialog frame = new ChooseDialog(selectedUser);
					            //FlightDialog frame = new FlightDialog(selectedUser);
					            frame.setVisible(true);
			            	}
			            	else {
			            		JOptionPane.showMessageDialog(UserDialog.this, "Invalid password", "Invalid Password", JOptionPane.ERROR_MESSAGE);
			            	}
			              
			              //System.out.println(selectedUser.getId());
			            } catch (SQLException e1) {
			              // TODO Auto-generated catch block
			              e1.printStackTrace();
			            } catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			          
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UserDialog.this.dispose();
					}
				 });
				buttonPane.add(cancelButton);
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					FormSpecs.RELATED_GAP_COLSPEC,
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.RELATED_GAP_COLSPEC,
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.RELATED_GAP_COLSPEC,
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.RELATED_GAP_COLSPEC,
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.RELATED_GAP_COLSPEC,
					FormSpecs.DEFAULT_COLSPEC,
					FormSpecs.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,
					FormSpecs.RELATED_GAP_ROWSPEC,
					FormSpecs.DEFAULT_ROWSPEC,}));
			{
				JLabel loginText = new JLabel("Login");
				panel.add(loginText, "4, 4, center, default");
			}
			{
				userComboBox = new JComboBox();
				panel.add(userComboBox, "8, 4, fill, default");
				populateUsers(userADO.getAllUsers());
			}
			{
				JLabel passwordText = new JLabel("Password");
				panel.add(passwordText, "4, 8");
			}
			{
				passwordField = new JPasswordField();
				passwordField.setEchoChar('*');
				panel.add(passwordField, "8, 8, fill, default");
				passwordField.setColumns(10);
			}
		}
	}

}

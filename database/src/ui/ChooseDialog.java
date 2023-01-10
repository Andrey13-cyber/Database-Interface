package ui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import core.User;
import dao.AirplaneADO;
import dao.CheckAirplaneADO;
import dao.CheckHealthADO;
import dao.ClientADO;
import dao.FlightADO;
import dao.ManagerWeightADO;
import dao.WeightADO;
import dao.WorkerADO;

import com.jgoodies.forms.layout.FormSpecs;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChooseDialog extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	
	public ChooseDialog(User selectedUser) {
		setTitle("Выбор таблиц");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 287, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
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
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
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
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JButton btnFLight = new JButton("Показать рейсы");
		btnFLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTable frame;
				try {
					frame = new DialogTable(selectedUser, new FlightADO());
					frame.setVisible(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		contentPane.add(btnFLight, "6, 2");
		
		JButton btnWeight = new JButton("Показать грузы");
		btnWeight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTable frame;
				try {
					if (selectedUser.getIsAdmin()) {
						frame = new DialogTable(selectedUser, new WeightADO());
						frame.setVisible(true);
					}
					/*
					 * frame = new DialogTable(selectedUser, new WeightADO());
					 * frame.setVisible(true);
					 */
					else {
						frame = new DialogTable(selectedUser, new ManagerWeightADO(1));
						frame.setVisible(true);
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnWeight, "6, 4");
		
		JButton btnClient = new JButton("Показать клиентов");
		btnClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTable frame;
				try {
					frame = new DialogTable(selectedUser, new ClientADO());
					frame.setVisible(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnClient, "6, 6");
		
		JButton btnAero = new JButton("Показать самолеты");
		btnAero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTable frame;
				try {
					frame = new DialogTable(selectedUser, new AirplaneADO());
					frame.setVisible(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnAero, "6, 8");
		
		JButton btnWorker = new JButton("Показать сотрудников");
		btnWorker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTable frame;
				try {
					frame = new DialogTable(selectedUser, new WorkerADO());
					frame.setVisible(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnWorker, "6, 10");
		
		JButton btnAeroSost = new JButton("Проверка самолета");
		btnAeroSost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTable frame;
				try {
					frame = new DialogTable(selectedUser, new CheckAirplaneADO());
					frame.setVisible(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnAeroSost, "6, 12");
		
		JButton btnHealthy = new JButton("Проверка здоровья");
		btnHealthy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogTable frame;
				try {
					frame = new DialogTable(selectedUser, new CheckHealthADO());
					frame.setVisible(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnHealthy, "6, 14");
	}

}

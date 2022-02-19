package forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import comunication.JSCSerialSession;


public class ArduinoGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	JTextArea textArea_receiver;
	JScrollPane sp;
	JButton btnFechar;
	JButton btnEnviar;
	JButton btnAbrir;
	JProgressBar progressBar_statusBar;
	JLabel lblNewLabel_1_1_1;
	
	private final AtomicBoolean running = new AtomicBoolean(false);
    private final ConcurrentLinkedQueue<Runnable> outputQueue = new ConcurrentLinkedQueue<>();
    private JSCSerialSession session = null;
    private JTextField textField_sent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArduinoGUI frame = new ArduinoGUI();
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public ArduinoGUI() throws IOException {
		setTitle("Java e Arduino");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 754, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		btnFechar = new JButton("Fechar");
		textField_sent = new JTextField();
		textArea_receiver = new JTextArea();
		btnEnviar = new JButton("Enviar");
		btnAbrir = new JButton("Abrir");
		progressBar_statusBar = new JProgressBar();
		lblNewLabel_1_1_1 = new JLabel("DESCONTECTADO");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "OP\u00C7\u00D5ES", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 261, 359);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("COM PORT");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(10, 23, 66, 14);
		panel.add(lblNewLabel);
		
		JLabel lblBaudRate = new JLabel("BAUD RATE");
		lblBaudRate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBaudRate.setBounds(10, 53, 66, 14);
		panel.add(lblBaudRate);
		
		JLabel lblNewLabel_1_1 = new JLabel("STATUS");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1.setBounds(10, 78, 66, 14);
		panel.add(lblNewLabel_1_1);
		
		JComboBox comboBox_port = new JComboBox();
		comboBox_port.setModel(new DefaultComboBoxModel(new String[] {"COM3"}));
		comboBox_port.setBounds(86, 19, 165, 22);
		panel.add(comboBox_port);
		
		JComboBox comboBox_baudRate = new JComboBox();
		comboBox_baudRate.setModel(new DefaultComboBoxModel(new String[] {"9600", "38400", "115200"}));
		comboBox_baudRate.setBounds(86, 49, 165, 22);
		panel.add(comboBox_baudRate);
		
		
		progressBar_statusBar.setBounds(86, 82, 165, 14);
		panel.add(progressBar_statusBar);
		
		
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setBounds(79, 107, 152, 14);
		panel.add(lblNewLabel_1_1_1);
		
		
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int port = Integer.parseInt(comboBox_baudRate.getSelectedItem().toString());
					session = new JSCSerialSession(comboBox_port.getSelectedItem().toString(), 
							port);
					if(session.isReady()) {
						startSerialThread();
						
						Thread thread = new Thread(() ->
				        {
				        	try {
					        	for(int i=0; i<=100; i++) {
									
									Thread.sleep(5);
									progressBar_statusBar.setValue(i);
									if(i==100) {
										doSomm();
									}
					        	}
				        	}catch(Exception ed) {
				        		
				        	}
				        });
						thread.start();
							
						
					}
				} catch (IOException e1) {
			
				}
				
			}

			private void doSomm() {
				// TODO Auto-generated method stub
				lblNewLabel_1_1_1.setText("CONECTADO");
				lblNewLabel_1_1_1.setForeground(Color.green);
				textArea_receiver.setEnabled(true);
				btnFechar.setEnabled(true);
				btnEnviar.setEnabled(true);
				btnAbrir.setEnabled(false);
				textField_sent.setEnabled(true);
			}
		});
		btnAbrir.setBounds(35, 134, 97, 41);
		panel.add(btnAbrir);
		
		
		btnFechar.setEnabled(false);
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				lblNewLabel_1_1_1.setText("DESCONTECADO");
				lblNewLabel_1_1_1.setForeground(null);
				textArea_receiver.setEnabled(false);
				btnFechar.setEnabled(false);
				btnEnviar.setEnabled(false);
				btnAbrir.setEnabled(true);
				textField_sent.setEnabled(false);
				
				session.terminate();
			}
		});
		btnFechar.setBounds(142, 134, 89, 41);
		panel.add(btnFechar);
		
		
		textField_sent.setEnabled(false);
		textField_sent.setBounds(320, 32, 309, 20);
		contentPane.add(textField_sent);
		textField_sent.setColumns(10);
		
		
		textArea_receiver.setEnabled(false);
		sp = new JScrollPane(textArea_receiver);   // JTextArea is placed in a JScrollPane.
		sp.setSize(413, 295);
		sp.setLocation(315, 75);
		textArea_receiver.setBounds(318, 74, 405, 273);
		

		contentPane.add(sp);
		
		
		btnEnviar.setEnabled(false);
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				session.sendCommand(textField_sent.getText().toString() + "#");
			}
		});
		btnEnviar.setBounds(639, 31, 89, 23);
		contentPane.add(btnEnviar);
		
		
	}
	
	public void startSerialThread()
    {
      
       
		Thread thread = new Thread(() ->
        {
            try
            {
                final AtomicBoolean interrupt = new AtomicBoolean(false);
                session.processMessages(interrupt, 5000, message ->
                {
                	
                	textArea_receiver.append(message + "\n");
                	
                	JScrollBar vertical = sp.getVerticalScrollBar();
            		vertical.setValue( vertical.getMaximum() );
    				 
                		/*try {
                			String s = System.lineSeparator() + message;
                		    Files.write(p, s.getBytes(), StandardOpenOption.APPEND);
                		} catch (IOException e) {
                		    System.err.println(e);
                		}*/
    				
                	//System.out.println(i+") "+message);
                	//i++;
                    if (message.contains("@STARTED"))
                    {
                        //running.set(true);
                        //interrupt.set(true);
                    }
                });
                
                /*session.processMessages(interrupt, 5000, new Callback<String>() {
					
					@Override
					public void run(String result) {
						// TODO Auto-generated method stub
						System.out.println("ae" + result);
					}
				});*/

                /*if (running.get())
                {
                    SwingUtilities.invokeLater(() -> System.out.println("OK"));
                }
                else
                {
                    System.err.println("Didn't receive initial startup message from Arduino.");
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            this,
                            "Didn't receive initial startup message from Arduino.",
                            "Arduino Error",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }*/

                /*while (running.get())
                {
                    Runnable cmd;
                    while ((cmd = outputQueue.poll()) != null)
                    {
                        cmd.run();
                    }
                }*/

                //session.terminate();
            }
            catch (Exception ex)
            {
                System.err.println("COM Port IOException:\n" + ex.getMessage());
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        this,
                        "COM Port IOException:\n" + ex.getMessage(),
                        "Arduino Error",
                        JOptionPane.ERROR_MESSAGE
                ));
            }

            //running.set(false);

            /*SwingUtilities.invokeLater(() ->
            {
            	System.out.println("Musashi");
                //setConnectControlsEnabled(true);
                //setBlinkControlsEnabled(false);
            });*/
        });
		thread.start();
		
    }
}

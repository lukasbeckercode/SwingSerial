package swingserial;

/* TODO
*Enable and disable Buttons to avoid opening the same Port twice: DONE
* Add a Read-function: DONE
* Add a stop reading method
* design it better */

//Imports Handled by IntelliJ

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import com.fazecast.jSerialComm.*; //Imported manually

public class myFrame extends JFrame {
    //Create all control items
    private JButton selectButton;
    private JComboBox comboBox1;
    private JButton readSerialButton;
    private JButton sendButton;
    private JTextField textField1;
    private JPanel rootPanel;
    private JButton disconnectButton;
    private JTextArea textArea1;
    private JButton readDataButton;

    SerialPort[] portNames; //all available SerialPorts
    static SerialPort chosenPort; // the SerialPort we will work with
    PrintWriter output; // an output Streamer

    public myFrame(){
        add(rootPanel); // get the Layout from the designer

        //ActionListener for the "Read Serial" Button
        readSerialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 portNames = SerialPort.getCommPorts(); //Look for all available COM ports
                for(int i = 0; i < portNames.length; ++i) {
                    comboBox1.addItem(portNames[i].getSystemPortName()); //add all of them to our combo box
                }
            }
        });

        //ActionListener for the "Select" Button
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenPort = SerialPort.getCommPort(comboBox1.getSelectedItem().toString()); //set the COM port we will work with
                chosenPort.setBaudRate(9600); //Set the Baud Rate
                chosenPort.setComPortTimeouts(65536,0,0); //set Timeouts
                chosenPort.openPort(); //open the Port
                output = new PrintWriter(chosenPort .getOutputStream() ); // Assign the output Steamer to the COM port
                output.flush(); //flush the Port so it´ll be ready when we need it

                //Handle button enabling
                disconnectButton.setEnabled(true);
                selectButton.setEnabled(false);
            }
        });

        //ActionListener for the "Send" Button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chosenPort.openPort()){  // check if the used port is open
                    Thread thread = new Thread(){ //create a new Thread


                        public void run() {
                            try {
                                Thread.sleep(100L); //wait a bit
                            } catch (Exception var4) {
                            }




                                output.println(textField1.getText()); //send whatever is written in the TextField to the port
                                output.flush(); // Flush the port

                                try {
                                    Thread.sleep(100L); //wait a bit
                                } catch (Exception var3) {
                                }
                            }

                    };
                    thread.start(); //run the thread

                }
            }
        });

        //Disconnect Button
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenPort.closePort(); //Close the Port

                //Handle button enabling
                selectButton.setEnabled(true);
                disconnectButton.setEnabled(false);
            }
        });

//Read Button Method
        readDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Timer timer = new Timer(); //create a Timer so we read constantly
                    InputStream in = chosenPort.getInputStream(); //use an input stream
                    timer.scheduleAtFixedRate(new TimerTask() { //set up the timer
                        @Override
                        public void run() {

                            try {
                                while (in.available() > 0) { //As long as there is data coming in, read it
                                    String data = Character.toString((char) in.read()); //Save the incoming data in a variable
                                    textArea1.append(data); //Add the Variable to the TextArea (which is in a ScrollPanel)
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }, 0, 10); // repeat every 10 ms


                }

        });


    }


}

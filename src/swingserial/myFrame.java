package swingserial;

/*This Software allows the user to communicate with any device that uses Serial Communication
It lets the user chose from 7 default baud rates or enter their own, it looks for available com ports.
You can also send data to the port or receive data from the port

By Lukas Becker, 2019
*/

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
    private JComboBox baudRateBox;
    private JButton stopReadButton;
    private JLabel statusLabel;
    private Timer timer;   //Global Var so we can start and stop it

    private SerialPort[] portNames; //all available SerialPorts
    private static SerialPort chosenPort; // the SerialPort we will work with
    private PrintWriter output; // an output Streamer
    private int[] baudRates = {9600,19200,38400,57600,74880,115200,230400}; // common baud rates in an int-Array

    myFrame(){
        add(rootPanel); // get the Layout from the designer
        statusLabel.setText("Disconnected"); //Initially set the text to "Disconnected"
        //ActionListener for the "Read Serial" Button
        readSerialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Common baud rates
               for(int i:baudRates )
               {
                   baudRateBox.addItem(i); //Add all baudRates
               }

                 portNames = SerialPort.getCommPorts(); //Look for all available COM ports
                comboBox1.removeAllItems();//clear list beforehand
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
                int chosenBaud = ((int) baudRateBox.getSelectedItem()); // get the baudRate from the ComboBox
                chosenPort.setBaudRate(chosenBaud); //Set the Baud Rate
                chosenPort.setComPortTimeouts(65536,0,0); //set Timeouts
                chosenPort.openPort(); //open the Port
                output = new PrintWriter(chosenPort .getOutputStream() ); // Assign the output Steamer to the COM port
                output.flush(); //flush the Port so it´ll be ready when we need it

                String msg = comboBox1.getSelectedItem().toString() + "@" + baudRateBox.getSelectedItem().toString(); //prepare the status message(PORT@BAUDRATE)
                statusLabel.setText(msg); //Set the text to the status message
                //Handle button enabling
                disconnectButton.setEnabled(!disconnectButton.isEnabled());
                selectButton.setEnabled(!selectButton.isEnabled());
            }
        });

        //ActionListener for the "Send" Button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chosenPort.openPort()){  // check if the used port is open
                    Thread thread = new Thread(()->{ //create a new Thread



                            try {
                                Thread.sleep(100L); //wait a bit
                            } catch (Exception var4) {
                                statusLabel.setText("Error");
                            }




                                output.println(textField1.getText()); //send whatever is written in the TextField to the port
                                output.flush(); // Flush the port

                                try {
                                    Thread.sleep(100L); //wait a bit
                                } catch (Exception var3) {
                                    statusLabel.setText("Error");
                                }
                            }

                    );
                    thread.start(); //run the thread

                }
            }
        });

        //Disconnect Button
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenPort.closePort(); //Close the Port
                statusLabel.setText("Disconnected"); //set the Status Label back to "Disconnected"
                //Handle button enabling
                selectButton.setEnabled(!selectButton.isEnabled());
                disconnectButton.setEnabled(!disconnectButton.isEnabled());
            }
        });

//Read Button Method
        readDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    timer = new Timer(); //create a new timer every time, so we can start and stop multiple times
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
                                statusLabel.setText("Error");
                            }
                        }
                    }, 0, 10); // repeat every 10 ms

                    //Handle button enabling
                    readDataButton.setEnabled(!readDataButton.isEnabled());
                    stopReadButton.setEnabled(!stopReadButton.isEnabled());
                }

        });

//Stop Read Button Method
        stopReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.cancel(); //Stop the Timer

                //Handle button enabling
                readDataButton.setEnabled(!readDataButton.isEnabled());
                stopReadButton.setEnabled(!stopReadButton.isEnabled());
            }
        });
    }


}

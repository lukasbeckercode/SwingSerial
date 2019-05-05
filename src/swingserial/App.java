package swingserial;

import javax.swing.*;

public class App {
    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() { //Use the Thread for Swing
            @Override
            public void run() {
                myFrame frame = new myFrame(); //Use the design we made
                frame.setVisible(true); //make the window visible
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //if someone closes the APP, actually close it
                frame.setSize(500,500); //Set the Size
                frame.setTitle("Serial Communicator"); //Set the Title
            }
        });


    }
}

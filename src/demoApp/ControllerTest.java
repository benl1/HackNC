package demoApp;

import javax.swing.JOptionPane;

import ch.aplu.xboxcontroller.XboxController;
import ch.aplu.xboxcontroller.XboxControllerAdapter;

public class ControllerTest {
  private XboxController xc;
  private int leftVibrate = 0;
  private int rightVibrate = 0;

  public ControllerTest() {
    xc = new XboxController();

    if (!xc.isConnected()) {
      JOptionPane.showMessageDialog(null, "Xbox controller not connected.", "Fatal error", JOptionPane.ERROR_MESSAGE);
      xc.release();
      return;
    }

    xc.addXboxControllerListener(new XboxControllerAdapter() {

      public void leftTrigger(double value) {
        leftVibrate = (int) (65535 * value * value);
        xc.vibrate(leftVibrate, rightVibrate);
      }

      public void buttonA(boolean x) {
        if (!x) System.out.println("hi:");
        rightVibrate = (int) (0);
        xc.vibrate(leftVibrate, rightVibrate);
      }
      
      

    });

    JOptionPane.showMessageDialog(null, "Xbox controller connected.\n" + "Press left or right trigger, Ok to quit.", "RumbleDemo V1.0 (www.aplu.ch)", JOptionPane.PLAIN_MESSAGE);

    xc.release();
    System.exit(0);
  }

  public static void main(String[] args) {
    new ControllerTest();

  }
}

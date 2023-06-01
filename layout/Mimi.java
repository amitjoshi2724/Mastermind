import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Mimi{
   public static void main(String[] args){
      JFrame frame = new JFrame("Unit4, Lab16: Mastermind");
      frame.setSize(300, 600);
      frame.setLocation(200, 100);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new MainPanel());
   
      frame.setVisible(true);
   }
  
}

class BPanel extends JPanel {
   public BPanel() {
      GridLayout layout = new GridLayout(3,2, 5, 5);
      setLayout(layout);
      for (int row = 0; row < 3; row++){
         for (int col = 0; col < 2; col++){
            JButton b = new JButton ("("+row+","+col+")");
            add(b).setLocation(row, col);
         }
      }
   }
}
class LeftPanel extends JPanel {
   public LeftPanel() {}
}

class MainPanel extends JPanel {
   public MainPanel() {
      JPanel p = new JPanel();
      p.add(new LeftPanel());
      p.add(new BPanel());
      add(p);
   }
}

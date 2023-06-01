import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class UpToYouM{
   public static void main(String[] args){
      JFrame frame = new JFrame("Unit4, Lab16: Mastermind");
      frame.setSize(300, 600);
      frame.setLocation(200, 100);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new Panel16M());
   
      frame.setVisible(true);
   }
  
}
class Panel16M extends JPanel{
   private ButtonBoard16M buttonBoard;
   private HoleBoard16M holeBoard;
   public Panel16M(){
      GridLayout outer_grid = new GridLayout(1,2);
      setLayout(outer_grid);
      buttonBoard = new ButtonBoard16M(new Callback());
      holeBoard = new HoleBoard16M();
      add(holeBoard);
      add(buttonBoard);
   } 
   private class Callback implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
      
      }
   }
}
class ButtonBoard16M extends JPanel{
   private JButton end;
   private JPanel buttonPanel;

   public ButtonBoard16M(ActionListener a){
      setLayout(new BorderLayout());
      setBackground(Color.GRAY);
      end = new JButton("End");
      buttonPanel = new JPanel();
   
      buttonPanel.setLayout(new GridLayout(4, 2, 5, 5));
      addButton(buttonPanel, Color.RED, a);
      addButton(buttonPanel, Color.BLUE, a);
      addButton(buttonPanel, Color.GREEN, a);
      addButton(buttonPanel, Color.YELLOW, a);
      addButton(buttonPanel, Color.ORANGE, a);
      addButton(buttonPanel, Color.MAGENTA, a);
      add(buttonPanel, BorderLayout.CENTER);
      add(end, BorderLayout.SOUTH);
   }
   private void addButton(JPanel panel, Color c, ActionListener a)
   {
      JButton button = new JButton();
      button.setBackground(c);
      button.addActionListener(a);
      panel.add(button);
   }
}
class HoleBoard16M extends JPanel{
   public HoleBoard16M(){
   
   }
   public void putBallDown(Color c){
   
   }
}
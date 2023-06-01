//Name__Amit Joshi____________________________ Date____4/19/2015/C.E._________
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;
import javax.swing.border.Border;
import java.util.ArrayList;
public class UpToYou{
   public static void main(String[] args){
      JFrame frame = new JFrame("Unit4, Lab16: Mastermind");
      frame.setSize(1220, 724);
      frame.setResizable(false);
      frame.setLocation(0,0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new Panel16());
      frame.setVisible(true);
   }
}
class ScrollableLabel extends JPanel {
    private JLabel label;

    public ScrollableLabel(String text) {
        setLayout(new BorderLayout());
        setBackground(new Color(210, 210, 220));

        label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setOpaque(true); // Enable label to have a background color
        Color poop = UIManager.getColor ( "Panel.background" );
        label.setBackground(poop); // Set the background color of the label


        JScrollPane scrollPane = new JScrollPane(label);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public Dimension getPreferredSize() {
        return label.getPreferredSize();
    }
}

class Panel16 extends JPanel {
    private Color green2 = new Color(10, 210, 10);
    private ButtonBoard16 buttonBoard;
    private HoleBoard16 holeBoard;
    private JPanel instructionPanel;
    private ScrollableLabel instructions;
    private Scoreboard16 scoreboard = new Scoreboard16();

    public Panel16() {
        setLayout(new BorderLayout());
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        JPanel center = new JPanel();
        GridLayout outer_grid = new GridLayout(1, 3);
        center.setLayout(outer_grid);

        buttonBoard = new ButtonBoard16(new Callback(), new UndoListener());
        holeBoard = new HoleBoard16(new CheckUndoListener(), new ResetListener());

        center.add(buttonBoard);
        center.add(holeBoard);

        instructionPanel = new JPanel();
        instructionPanel.setLayout(new GridBagLayout());

        String instructionsText = "<html>Instructions:<br>This is the game mastermind. A sequence of colors<br>"
                + "is determined when the game starts. Colors can be repeated.<br>You have to guess<br>"
                + "that sequence by clicking the colored circle buttons. Notice the undo<br>"
                + "button to be used if you make a mistake on a particular row.<br>"
                + "After you have guessed a row, ensure that you want to submit<br>"
                + "that row by clicking check. Once you have clicked check<br>"
                + "you can't undo that row. After check is clicked you will be<br>"
                + "informed of your results. A white peg means that one of your<br>"
                + "guesses was guessed correctly in the right location while a<br>"
                + "black peg means that one of your guesses was guessed correctly<br>"
                + "but in the wrong location. If a<br>color is repeated in the answer,<br>"
                + "and a player selects that color, that color is only counted once<br>"
                + "by the pegs, or feedback from the computer<br>"
                + "You have ten tries to guess the correct sequence<br>"
                + "Credits: Developed by Amit Joshi. Github: @amitjoshi24</html>";

        instructions = new ScrollableLabel(instructionsText);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        instructionPanel.add(instructions, gbc);

        scoreboard.setBorder(border);
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        instructionPanel.add(scoreboard, gbc);

        center.add(instructionPanel);
        add(center, BorderLayout.CENTER);
    }
   private class Callback implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if((holeBoard.getChecking())){
            return;
         }
         if(holeBoard.getColNum() == 3){
            buttonBoard.freeze();
         }
         if(buttonBoard.getRed())
            holeBoard.putBallDown(Color.RED, 0);
         else if(buttonBoard.getBlue())
            holeBoard.putBallDown(Color.BLUE, 1);
         else if(buttonBoard.getGreen())
            holeBoard.putBallDown(green2, 2);
         else if(buttonBoard.getYellow())
            holeBoard.putBallDown(Color.YELLOW, 3); 
         else if(buttonBoard.getOrange())
            holeBoard.putBallDown(Color.ORANGE, 4);
         else 
            holeBoard.putBallDown(Color.MAGENTA, 5);          
      }
   }
   private class UndoListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         holeBoard.undo();
         buttonBoard.unFreeze();
      }
   }
   private class CheckUndoListener implements ActionListener{
      public void actionPerformed(ActionEvent ex){
         
         if(holeBoard.getLoser()){
            scoreboard.updateScoreboard(false);
            buttonBoard.totalFreeze();
            holeBoard.loser();
         }
         else if(holeBoard.getWinner()){
            scoreboard.updateScoreboard(true);
            buttonBoard.totalFreeze();
            holeBoard.winner();
         }
         else{
            buttonBoard.unFreeze(); 
            buttonBoard.undoFreeze();
         }
      }
   }
   private class ResetListener implements ActionListener{
      public void actionPerformed(ActionEvent exe){
         buttonBoard.reset();
         holeBoard.reset();
      }
   }
}
class ButtonBoard16 extends JPanel{
   private Color green2 = new Color(10, 210, 10);
   private Button end;
   private int pressTimes;
   private JPanel buttonPanel;
   private boolean red = false, blue = false, green = false, yellow = false, orange = false, magenta = false;
   private AmitButton[] buttonArray = new AmitButton[6];
   private CompoundBorder border = new CompoundBorder(
   BorderFactory.createMatteBorder(1, 1, 1, 1,Color.BLACK),
   BorderFactory.createMatteBorder(28, 28, 28, 28, new Color(210, 210, 220)));
   private JButton undo = new JButton("<html>Undo<br></html>");//took out sets: 2
   private Color[] colorArray = {Color.RED, Color.BLUE, green2, Color.YELLOW, Color.ORANGE, Color.MAGENTA};
   public ButtonBoard16(ActionListener a, ActionListener b){
      setLayout(new BorderLayout());
      setBackground(Color.GRAY);
      undo.setFont(new Font("SansSerif", Font.PLAIN, 80));
      undo.addActionListener(b);
      undo.addActionListener(new pressTimesDecrementerListener());
      undo.setEnabled(false);
      add(undo, BorderLayout.NORTH);
      end = new Button("End");
      end.addActionListener(new Listener());
      buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(3, 2));
      for(int i = 0; i < buttonArray.length; i++){
         buttonArray[i] = new AmitButton(colorArray[i]);
      }   
      addButton(buttonPanel, a, 0, buttonArray[0]);
      addButton(buttonPanel, a, 1, buttonArray[1]);
      addButton(buttonPanel, a, 2, buttonArray[2]);
      addButton(buttonPanel, a, 3, buttonArray[3]);
      addButton(buttonPanel, a, 4, buttonArray[4]);
      addButton(buttonPanel, a, 5, buttonArray[5]);
      add(buttonPanel, BorderLayout.CENTER);
      add(end, BorderLayout.SOUTH);
   }
   private void addButton(JPanel panel, ActionListener a, int integer, JButton button)
   {  
      button.addActionListener(a);
      button.addActionListener(new ButtonListener(integer));
      panel.add(button);
   }
   void freeze(){
      for(int i = 0; i < buttonArray.length; i++){
         buttonArray[i].setEnabled(false);
         buttonArray[i].tempDisable();
         buttonArray[i].instaTempLookDisable();
      }
   }
   void unFreeze(){
      for(int i = 0; i < buttonArray.length; i++){
         buttonArray[i].setEnabled(true);
         buttonArray[i].tempEnable();
      }
      
   }
   void undoFreeze(){
      undo.setEnabled(false);
   }
   void totalFreeze(){
      for(int i = 0; i < buttonArray.length; i++){
         buttonArray[i].setEnabled(false);
      }
      undo.setEnabled(false);
   }
   public void reset(){
      for(int i = 0; i < buttonArray.length; i++){
         buttonArray[i].setEnabled(true);
         buttonArray[i].tempEnable();
      }
      undo.setEnabled(false);
   
   }
   public boolean getRed(){
      if(red)
         return true;
      else
         return false;
   }
   public boolean getBlue(){
      if(blue)
         return true;
      else
         return false;
   }
   public boolean getGreen(){
      if(green)
         return true;
      else
         return false;
   }
   public boolean getYellow(){
      if(yellow)
         return true;
      else
         return false;
   }
   public boolean getOrange(){
      if(orange)
         return true;
      else
         return false;
   }
   public boolean getMagenta(){
      if(magenta)
         return true;
      else
         return false;
   }
   private class ButtonListener implements ActionListener{
      private int myNum;
      public ButtonListener(int i){
         myNum = i;
      }
      public void actionPerformed(ActionEvent e){
         switch (myNum){
            case 0:
               red = true;
               blue = false;
               green = false;
               yellow = false;
               orange = false;
               magenta = false;
               break;
            case 1:
               red = false;
               blue = true;
               green = false;
               yellow = false;
               orange = false;
               magenta = false;
               break;
            case 2:
               red = false;
               blue = false;
               green = true;
               yellow = false;
               orange = false;
               magenta = false;
               break;
            case 3:
               red = false;
               blue = false;
               green = false;
               yellow = true;
               orange = false;
               magenta = false;
               break;
            case 4:
               red = false;
               blue = false;
               green = false;
               yellow = false;
               orange = true;
               magenta = false;
               break;
            case 5:
               red = false;
               blue = false;
               green = false;
               yellow = false;
               orange = false;
               magenta = true;
               break;
         }
         ++pressTimes;
         undo.setEnabled(true);
      }
   }
   private class pressTimesDecrementerListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         pressTimes--;
         if(!(pressTimes % 4 == 0)){
            undo.setEnabled(true);
         }
         else if(pressTimes % 4 == 0){
            undo.setEnabled(false);
         }   
      }
   }
   private class Listener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         System.exit(0);
      }
   }
}
class HoleBoard16 extends JPanel{
   private Color green2 = new Color(10, 210, 10);
   private Color lgr = new Color(204, 204, 204);
   private boolean checking = false;
   private int[][] array = new int[11][4];
   private AmitLabel[][] labelArray = new AmitLabel[11][4];
   private int rowNum = 10, colNum = 0;
   private int[] combination = new int[4];
   private boolean loser = false, winner = false;
   private final int BALLRADIUS = 45;
   private final int PEGRADIUS = 20;
   private JLabel label, grayLabel;
   private Color color;
   private JButton[] checkArray = new JButton[10];
   private AmitLabel[][][] pegArray;
   private JButton reset = new JButton("Reset"); 
   private JPanel labelPanel, pegPanel, superPegPanel;
   private Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

   public HoleBoard16(ActionListener c, ActionListener d){
      setLayout(new BorderLayout());
      reset.addActionListener(d);
      reset.setEnabled(false);
      add(reset, BorderLayout.NORTH);
      labelPanel = new JPanel(new GridLayout(11, 5));
      pegArray = new AmitLabel[10][2][2];
      pegPanel = new JPanel(new GridLayout(20, 2));
      pegPanel.setBorder(border);
      for(int i = 0; i < combination.length; i++){
         combination[i] = (int)(Math.random() * 6);
      }
      for(int i = 0; i < 4; i++){
         labelArray[0][i] = new AmitLabel(BALLRADIUS, true, false);
         labelArray[0][i].setText("?");
         labelPanel.add(labelArray[0][i]);
      }   
      JPanel fillerPanel = new JPanel();
      label = new JLabel("<html>Correct<br>Sequence</html>");
      fillerPanel.add(label);
      labelPanel.add(fillerPanel);
      superPegPanel = new JPanel();
      superPegPanel.setLayout(new BorderLayout());
      superPegPanel.setBackground(Color.BLUE);
      grayLabel = new JLabel();
      grayLabel.setPreferredSize(new Dimension(75, 58));
      grayLabel.setOpaque(true);
      grayLabel.setText("Mastermind");
      grayLabel.setHorizontalAlignment(SwingConstants.CENTER);
      grayLabel.setBackground(Color.LIGHT_GRAY);
      superPegPanel.add(grayLabel, BorderLayout.NORTH);
      for(int x = 1; x < labelArray.length; x++){
         for(int y = 0; y < labelArray[0].length; y++){
            labelArray[x][y] = new AmitLabel(BALLRADIUS, true, false);
            labelPanel.add(labelArray[x][y]);
         }
         checkArray[x - 1] = new JButton();
         checkArray[x - 1].setMargin(new Insets(0,0,0,0));
         checkArray[x - 1].setFont(new Font("Times New Roman", Font.BOLD, 14));
         checkArray[x - 1].setText("Check");
         checkArray[x - 1].addActionListener(c);
         checkArray[x - 1].addActionListener(new CheckListener(x - 1));
         labelPanel.add(checkArray[x - 1]);
         checkArray[x - 1].setEnabled(false);
         for(int x2 = 0; x2 < pegArray[0].length; x2++){
            for(int y2= 0; y2 < pegArray[0][0].length; y2++){
               if(x2 == 0){
                  pegArray[x - 1][x2][y2] = new AmitLabel(PEGRADIUS, true, true);
               }
               else{
                  pegArray[x - 1][x2][y2] = new AmitLabel(PEGRADIUS, false, true);
               }
               pegPanel.add(pegArray[x - 1][x2][y2]);
            }
         }
      } 
      add(labelPanel, BorderLayout.CENTER);
      superPegPanel.add(pegPanel, BorderLayout.CENTER);
      add(superPegPanel, BorderLayout.EAST);
   }
   private class CheckListener implements ActionListener{
      private int myNum;
      public CheckListener(int i){
         this.myNum = i;
      }
      public void actionPerformed(ActionEvent e){
         putPegsDown();
         checkArray[myNum].setEnabled(false);
         checking = false;
      }
   }
   public void reset(){
      loser = false;
      reset.setEnabled(false);
      label.setText("<html>Correct<br>Sequence");
      rowNum = 10;
      colNum = 0;
      setColor(Color.BLACK);
      for(int i = 0; i < combination.length; i++){
         combination[i] = (int)(Math.random() * 6);
      }
      for(int x = 0; x < labelArray.length; x++){
         for(int y = 0; y < labelArray[0].length; y++){
            labelArray[x][y].undoBall();
         }
      }
      for(int i = 0; i < checkArray.length; i++){
         checkArray[i].setEnabled(false);
      }
      for(int x = 0; x < pegArray.length; x++){
         for(int y = 0; y < pegArray[0].length; y++){
            for(int z = 0; z < pegArray[0][0].length; z++)
               pegArray[x][y][z].undoBall();
         }
      }
   }
   public int getColNum(){
      return colNum;
   }
   public boolean getLoser(){
      return loser;
   }
   public boolean getWinner(){
      return winner;
   }
   public boolean getChecking(){
      return checking;
   }
   public void setColor(Color c){
      color = c;
      labelArray[rowNum][colNum].setColor(c);
   }
   public void putBallDown(Color c, int i){ 
      setColor(c);
      labelArray[rowNum][colNum].paintBall();
      array[rowNum][colNum] = i;
      if(colNum == 3 && rowNum == 1){
         checkArray[rowNum - 1].setEnabled(true);
         for(int x = 0; x < array[0].length; x++){
            if(!(array[rowNum][x] == combination[x])){
               loser = true;
            }
            else{
               winner = true;
            }
         }
         rowNum--;
         colNum = 0;
      }
      else if((colNum == 3) && !(rowNum == 0)){
         for(int x = 0; x < array[0].length; x++){
            if(!(array[rowNum][x] == combination[x])){
               winner = false;
               checkArray[rowNum - 1].setEnabled(true);
               checking = true;
               rowNum--;
               colNum = 0;
               return;
            }
            winner = true;
         }
         checkArray[rowNum - 1].setEnabled(true);
         checking = true;
         rowNum--;
         colNum = 0;
      }
      else if(!(colNum == 3)){
         colNum++;
      }
   }
   public void putPegsDown(){
      int whitePegs = 0, blackPegs = -1;
      int xCounter = 0, yCounter = 0;
      whitePegs = 0;
      blackPegs = 0;
      ArrayList list = new ArrayList(0);
      ArrayList combinationListClone = new ArrayList(0);
      ArrayList whitePegIndexList = new ArrayList();
      for(int i = 0; i < 4; i++){
         int x = combination[i];
         combinationListClone.add(x);
      }
      for(int i = 0; i < 4; i++){
         if((array[rowNum + 1][i] == combination[i])){
            whitePegs++;
            whitePegIndexList.add(i);
            list.add(array[rowNum + 1][i]);
            combinationListClone.remove((Object)array[rowNum + 1][i]);   
         }
      }
      for(int i = 0; i < 4; i++){
         if((!(combinationListClone.contains(array[rowNum + 1][i]))) || (whitePegIndexList.contains(i))){
            continue;
         }
         if((combinationListClone.contains(array[rowNum + 1][i])) ){
            blackPegs++;
            combinationListClone.remove((Object)array[rowNum + 1][i]);   
            list.add(array[rowNum + 1][i]);
         }
      }
      for(int i = 1; i <= whitePegs; i++){
         pegArray[rowNum][xCounter][yCounter].setColor(Color.WHITE);
         pegArray[rowNum][xCounter][yCounter].paintBall();
         xCounter++;
         if(xCounter == 2){
            yCounter = 1;
            xCounter = 0;
         }
      } 
      for(int i = 1; i <= blackPegs; i++){
         pegArray[rowNum][xCounter][yCounter].setColor(Color.BLACK);
         pegArray[rowNum][xCounter][yCounter].paintBall();
         xCounter++;
         if(xCounter == 2){
            yCounter = 1;
            xCounter = 0;
         }
      }            
   }
   public void loser(){ 
      label.setText("<html>Correct<br>Sequence<br>Loser</html>");
      for(int i = 0; i < checkArray.length; i++){
         checkArray[i].setEnabled(false);
      }
      displayCorrectCombination();
   }
   public void winner(){
      label.setText("<html>Correct<br>Sequence<br>Winner</html>");
      for(int i = 0; i < checkArray.length; i++){
         checkArray[i].setEnabled(false);
      }
      rowNum = 0;
      displayCorrectCombination();
   }
   private void displayCorrectCombination(){
      reset.setEnabled(true);
      for(int i = 0; i < combination.length; i++){
         int num = 0;
         switch (combination[i]){
            case 0:
               putBallDown(Color.RED, 0);
               break;
            case 1:
               putBallDown(Color.BLUE, 1);
               break;
            case 2:
               putBallDown(green2, 2);
               break;
            case 3:
               putBallDown(Color.YELLOW, 3);
               break;
            case 4:
               putBallDown(Color.ORANGE, 4);
               break;
            case 5:
               putBallDown(Color.MAGENTA, 5);
               break;
         }
      }
   }
   public void undo(){
      array[rowNum][colNum] = 0;
      if(colNum > 0){
         colNum--;
         setColor(Color.BLACK);
         labelArray[rowNum][colNum].setColor(color);
         labelArray[rowNum][colNum].undoBall();
      }
      else if((colNum == 0)){
         checkArray[rowNum].setEnabled(false);
         checking = false;
         if(rowNum < 10){
            colNum = 3;
            rowNum++;
            setColor(Color.BLACK);
            labelArray[rowNum][colNum].setColor(color);
            labelArray[rowNum][colNum].undoBall();
         }
      }
   
   }
}
class AmitLabel extends JLabel{
   private int radius;
   private Color color = Color.BLACK;
   private BufferedImage myImage;
   private Graphics myBuffer;
   private ImageIcon icon;
   private boolean lines, pegs;
   public AmitLabel(int r, boolean line, boolean peg){
      super();
      this.setOpaque(true);
      this.radius = r;
      this.lines = line;
      this.pegs = peg;
      if((lines) && !(pegs)){
         myImage =  new BufferedImage(75, 75, BufferedImage.TYPE_INT_RGB);
         myBuffer = myImage.getGraphics();
         myBuffer.setColor(Color.LIGHT_GRAY);
         myBuffer.fillRect(0,0,75, 75);
         myBuffer.setColor(Color.BLACK);
         drawLine(myImage, 0, 0, 75, 0);
         myBuffer.drawLine(0, 0, 75, 0);
         myBuffer.drawOval(10,10, radius, radius);
      }
      else if((lines) && (pegs)){
         myImage =  new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
         myBuffer = myImage.getGraphics();
         myBuffer.setColor(Color.LIGHT_GRAY);
         myBuffer.fillRect(0,0, 40, 40);
         myBuffer.setColor(Color.BLACK);
         myBuffer.drawLine(0, 0, 40, 0);
         myBuffer.drawOval(10,10, radius, radius);
      }
      else{
         myImage =  new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
         myBuffer = myImage.getGraphics();
         myBuffer.setColor(Color.LIGHT_GRAY);
         myBuffer.fillRect(0,0, 40, 40);
         myBuffer.setColor(Color.BLACK);
         myBuffer.drawOval(10,10, radius, radius);
      }
      repaint();
   }

   public static void drawLine(BufferedImage img, int x1, int y1, int x2, int y2)
   {         
      int distance = (int)Math.sqrt(((x1 - x2) * (x1 - x2)) + (y1 - y2) * (y1 - y2));
      double x = x1, y = y1, x3 = x1, x4 = x2, y3 = y1, y4 = y2;
      final double X_INCREMENT = (x4 - x3) / distance;
      final double Y_INCREMENT = (y4 - y3) / distance;
      for(int i = 0; i < distance - 1; i++){
         x += X_INCREMENT;
         y += Y_INCREMENT;
         img.setRGB( (int)x, (int)y, 0);
      }
   }
   public void setColor(Color c){
      this.color = c;
   }
   public void paintBall(){
     
      myBuffer.setColor(color);
      myBuffer.fillOval(10,10, radius, radius);
      myBuffer.setColor(Color.BLACK);
      repaint();
      
   }
   public void undoBall()
   {  
      if((lines) && !(pegs)){
         myBuffer.setColor(Color.LIGHT_GRAY);
         myBuffer.fillRect(0,0,75, 75);
         myBuffer.setColor(Color.BLACK);
         drawLine(myImage, 0, 0, 75, 0);
         myBuffer.drawLine(0,0,75,0);
         myBuffer.drawOval(10,10, radius, radius);
      }
      else if((lines) && (pegs)){
         myBuffer.setColor(Color.LIGHT_GRAY);
         myBuffer.fillRect(0,0, 40, 40);
         myBuffer.setColor(Color.BLACK);
         myBuffer.drawLine(0, 0, 40, 0);
         myBuffer.drawOval(10,10, radius, radius);
      }
      else{
         myBuffer.setColor(Color.LIGHT_GRAY);
         myBuffer.fillRect(0,0, 40, 40);
         myBuffer.setColor(Color.BLACK);
         myBuffer.drawOval(10,10, radius, radius);
      }
      repaint();
   }
   public void paintComponent(Graphics g){
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
}
class AmitButton extends JButton{
   private Color color;
   private boolean enabled = true;
   private Color bg = new Color(210, 210, 220), pressBg = new Color(180, 180, 190);
   private int radius = 45;
   private BufferedImage myImage;
   private Graphics myBuffer;
   private CompoundBorder border = new CompoundBorder(
   BorderFactory.createMatteBorder(1, 1, 1, 1,Color.BLACK),
   BorderFactory.createMatteBorder(28, 28, 28, 28, bg));
   private CompoundBorder enterBorder = new CompoundBorder(BorderFactory.createMatteBorder(3, 3, 3, 3,Color.BLUE.brighter().brighter()),
   BorderFactory.createMatteBorder(28, 28, 28, 28, bg));
   public AmitButton(Color c){
      this.color = c;
      myImage =  new BufferedImage(200, 210, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
      setOpaque(true);
      setBorder(border);
      myBuffer.setColor(bg);
      myBuffer.fillRect(0,0, 200, 200);
      myBuffer.setColor(color);
      myBuffer.fillOval((200/2) - (radius / 2), (200/2) - (radius / 2), radius, radius);
      addMouseListener(new HoverAndPressManager());
   }
   private class HoverAndPressManager extends MouseAdapter{
      public void mouseExited(MouseEvent m){
         instaTempLookDisable();
      }
      public void mouseEntered(MouseEvent me){
         if(enabled){
            setBorder(enterBorder);
         }
      }
   }
   void instaTempLookDisable(){
      setBorder(border);
   }
   void tempDisable(){
      this.enabled = false;
   }
   void tempEnable(){
      this.enabled = true;
   }
   public void paintComponent(Graphics g){
      g.drawImage(myImage, 0, 0, getHeight(), getWidth(), null);
   }
}
class Scoreboard16 extends JPanel
{
   private JLabel fraction, currentStreak, longestStreak;
   private double wins, total;
   private int currentStreakNumber, longestStreakNumber;
   private DecimalFormat df;
   public Scoreboard16()
   {
      setLayout(new GridLayout(3, 1));
      df = new DecimalFormat("0.00");
      wins = total = currentStreakNumber = longestStreakNumber = 0;
      fraction = new JLabel("Wins: 0/0(-.--%) ");
      fraction.setFont(new Font("Times New Roman", Font.PLAIN, 24));
      fraction.setHorizontalAlignment(SwingConstants.CENTER);
      add(fraction);
      currentStreak = new JLabel("Current Streak: 0 ");
      currentStreak.setFont(new Font("Times New Roman", Font.PLAIN, 24));
      currentStreak.setHorizontalAlignment(SwingConstants.CENTER);
      add(currentStreak);
      longestStreak = new JLabel("Longest Streak: 0");
      longestStreak.setFont(new Font("Times New Roman", Font.PLAIN, 24));
      longestStreak.setHorizontalAlignment(SwingConstants.CENTER);
      add(longestStreak);
   }
   public void updateScoreboard(boolean won){ 
      ++total;
      if(won){
         ++currentStreakNumber;
         ++wins;
      }
      else{
         currentStreakNumber = 0;
      }
      if(currentStreakNumber > longestStreakNumber){
         longestStreakNumber = currentStreakNumber;
      }
      fraction.setText("Wins: " + wins + "/" + total + "(" + df.format(100 * wins / total) + "%)");  
      currentStreak.setText("Current Streak: " + currentStreakNumber);
      longestStreak.setText("Longest Streak: " + longestStreakNumber);
   }
}
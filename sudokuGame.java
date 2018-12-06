/*
Authors: @Rawan Alsagheer 
         @Mauricio Aramburu
         
         See the Readme.txt for instructions
*/


import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridLayout; 
import java.awt.*;        
import java.awt.event.*;  
import javax.swing.*;     
import java.io.*;
import java.util.Scanner;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.awt.Font;
import javax.swing.JDialog;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.Calendar;
import javax.swing.event.*;

public class sudokuGame extends JFrame {

   public static void main(String[] args){
      JFrame j = new sudokuGame();   
   }
   
// Declare variables 
   static JTextField field = new JTextField(20);
   static JLabel label = new JLabel("0");   
   static int input_counter = 0, ct=0, minutes=0, sec;
   
   JPanel mainWindow = new JPanel(); 
   private JTextField[][] cells = new JTextField[9][9];
   private final Font font = new Font("SansSerif", Font.BOLD, 20);
   int[][] number = new int[9][9]; 
   int row,col;
   boolean solved=true;
   static int[][] solution =
   {
   {3,8,1,4,7,9,6,2,5},
   {6,2,7,5,8,1,4,3,9},
   {5,4,9,3,6,2,7,8,1},
   {9,3,6,2,5,8,1,4,7},
   {4,7,2,9,1,6,8,5,3},
   {1,5,8,7,4,3,2,9,6},
   {2,6,4,1,9,5,3,7,8},
   {8,9,3,6,2,7,5,1,4},
   {7,1,5,8,3,4,9,6,2}
   };
// end declare variables section 

   public sudokuGame(){
      super("Sudoku");
      
      int i=0,j=0;
   
      JPanel menu = new JPanel(); //JPanel for the give up button to be separate
      menu.setLayout(new BorderLayout(2,2)); // layout for the "menu"
      JButton giveUp = 
         new JButton("Give up?"){
            {
               setSize(8, 8);
               setMaximumSize(getSize());
            }
         }; // adding the give up button
    
      //display solution when give up
      giveUp.addActionListener(
         new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
               for (int i=0; i<9;i++)
                  for (int j=0; j<9; j++)
                     if(number[i][j] == 0) 
                     {   
                        cells[i][j].setText(solution[i][j]+"");
                        cells[i][j].setForeground(Color.GREEN);
                        cells[i][j].setFont(font);
                        cells[i][j].setEditable(false);
                     }
            }  
         });
    //END display solution when give up 
      
      menu.add(giveUp, BorderLayout.PAGE_START); // adding the button at the start of the panel
   
      Container cont = getContentPane();
      cont.setLayout(new GridLayout(9, 9));
            
                  
      Set forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
      Set newForwardKeys = new HashSet(forwardKeys);
      newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
      setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
   
      Set backwardKeys = getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
      Set newBackwardKeys = new HashSet(backwardKeys);
      newBackwardKeys.add(KeyStroke.getKeyStroke((KeyEvent.VK_LEFT), 0));
      setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newBackwardKeys);
        
      try{
         Scanner scanner = new Scanner(new FileReader("samplesudoku1.txt"));
         while(scanner.hasNextInt()){
            for (row = 0; row <9; row++)
               for (col = 0; col<9; col++)
                  number[row][col] = scanner.nextInt();             
         }
         scanner.close();   
      } 
      catch(IOException ex){
         System.out.println (ex.toString());
         System.out.println("Could not find file");
      }
      
      for (int row = 0; row < 9; row++) {
         for (int col = 0; col < 9; col++) {
            cells[row][col] = new JTextField();     
         
            cont.add(cells[row][col]);
            int k = number[row][col]; 
            if (k != 0)  {      
               cells[row][col].setText(k+""); 
               cells[row][col].setFont(font);
               cells[row][col].setEditable(false);
               cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            } 
            else {
               cells[row][col].setText(""); 
               cells[row][col].requestFocus();
               cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            
               final int o = row;
               final int oo = col;
            
                
               cells[row][col].addKeyListener(
                  new KeyAdapter() {
                     public void keyReleased(KeyEvent e) {
                        JTextField textField = (JTextField) e.getSource();
                        String text = textField.getText();
                        if(text.contains("c")){
                           String str[]= text.split("c", -1);
                           textField.setText(str[0]);
                        
                           boolean foundInRow1, foundInCol1, foundInBox1;
                           int cellValue1=0;
                           try{       
                              int rowSelected = -1;
                              int colSelected = -1;
                              int value;  
                           
                              boolean found = false;
                              for (int row = 0; row < 9 && !found; row++) {
                                 for (int col = 0; col < 9 &&!found; col++) {
                                    if (cells[row][col] == textField) {
                                       rowSelected = row;
                                       colSelected = col;
                                       found = true;                 
                                    }
                                 }
                              }
                              if (cells[rowSelected][colSelected].getText()!= "")
                                 cellValue1 = Integer.parseInt(cells[rowSelected][colSelected].getText());
                           
                              foundInRow1 = isInRow(rowSelected,colSelected, cellValue1);
                              foundInCol1 = isInCol(rowSelected,colSelected, cellValue1);
                              foundInBox1 = isInBox(rowSelected,colSelected,cellValue1 );
                           
                              if (foundInRow1 || foundInCol1 || foundInBox1 || Integer.parseInt(cells[rowSelected][colSelected].getText())!=solution[rowSelected][colSelected]){
                                 cells[rowSelected][colSelected].setForeground(Color.BLUE);
                                 cells[rowSelected][colSelected].setFont(font);
                              }
                           
                              if (!foundInRow1 && !foundInCol1 && Integer.parseInt(cells[rowSelected][colSelected].getText())==solution[rowSelected][colSelected]){
                                 cells[rowSelected][colSelected].setForeground(Color.GREEN); 
                                 cells[rowSelected][colSelected].setFont(font);  
                                 cells[rowSelected][colSelected].setEditable(false);
                                 input_counter++;
                              
                                 if (input_counter >= ct){
                                    JOptionPane.showMessageDialog(null, "CONGRATS, YOU WON! \n  \n Time Spent: "+minutes+" min "+(Integer.parseInt(label.getText()))+" sec");
                                    if (JOptionPane.OK_OPTION == 0){
                                       System.exit(0);
                                    } 
                                 } 
                              }
                           } 
                           catch (NumberFormatException e1){
                           
                              System.out.println("cought a NumberFormatException. Your value must be a number");
                           }
                        
                        }
                                   
                     }
                          
                  
                     public void keyTyped(KeyEvent e) {
                      
                     }
                  
                     public void keyPressed(KeyEvent e) {
                               
                     }// end keyPressed()
                     public boolean isInRow(int row, int col,  int value){ 
                        for (int i = 0; i < 9; i++){
                           if (i != col && !cells[row][i].getText().equals("")){
                              if (Integer.parseInt(cells[row][i].getText())== value){
                                 return true;}
                           }
                        }            
                        return false;
                     } 
                  
                     public boolean isInCol(int row, int col, int value){
                        for (int i = 0; i < 9; i++)
                           if (i != row)
                              if(!cells[i][col].getText().equals(""))
                                 if (Integer.parseInt(cells[i][col].getText()) == value)
                                    return true;
                     
                        return false;
                     }  
                  
                     public boolean isInBox(int row, int col, int value){
                     
                        int r = row - row % 3;
                        int c = col - col % 3;
                     
                        for (int i = r; i < r + 3; i++)
                           for (int j = c; j < c + 3; j++){
                              if (i == row && j == col)
                                 break;
                              if (!cells[i][j].getText().equals(""))
                                 if (Integer.parseInt(cells[i][j].getText())== value)
                                    return true;       
                           }
                        return false;
                     } 
                  });
            
            
               cells[row][col].setEditable(true);
               ct++;
            } 
            cells[row][col].setHorizontalAlignment(JTextField.CENTER);
         
         }   
      }
      cont.setPreferredSize(new Dimension(500, 500));
      pack();      
      setResizable(false);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLayout(new BorderLayout(15,15)); //border layout for the main window
      
         
      add(menu,BorderLayout.PAGE_END); // adding the panel at the end of the grid layout of the main window
      add(new TestPane());
   
      setVisible(true);
   }// end SudokuGame constructor 
 

   public class TestPane extends JPanel {
   
      public TestPane() {
      
        
      
         dListener listener = new dListener(1000, 
            new ActionListener() {
               @Override
                public void actionPerformed(ActionEvent e) {
                  label.setText((Integer.parseInt(label.getText())+1) + "");
                  if (Integer.parseInt(label.getText())% 60 ==0)
                  {label.setText("0");
                     minutes = minutes+1;}
               
               }
            }, true);
         if (input_counter == 0)
            listener.start();
         if(input_counter >= ct)
            listener.stop();
      
         field.getDocument().addDocumentListener(listener);
               
         setLayout(new GridBagLayout());
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridwidth = GridBagConstraints.REMAINDER;
         field.setVisible(false);
         label.setVisible(false);
         add(field, gbc);
         add(label, gbc);
      }
   
   }

   public class dListener implements DocumentListener {
   
      private final Timer timer;
   
      public dListener(int timeOut, ActionListener e2, boolean repeat) {
         timer = new Timer(timeOut, e2);
         timer.setRepeats(repeat);
      }
   
      public void start() {
         timer.start();
      }
   
      public void stop() {
         timer.stop();
      }
   
      @Override
        public void insertUpdate(DocumentEvent e) {
         timer.restart();
      }
   
      @Override
        public void removeUpdate(DocumentEvent e) {
         timer.restart();
      }
   
      @Override
        public void changedUpdate(DocumentEvent e) {
         timer.restart();
      }
   
   }     
}
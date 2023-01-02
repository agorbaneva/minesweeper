import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Game extends JFrame implements KeyListener {

  private Date startTime = new Date();
  private int winTime;

  private static int blockSize = 40;
  private int boardWidth = 18;
  private int boardHeight = 14;
  private int width = boardWidth * blockSize + blockSize;
  private int height = boardHeight * blockSize + 2 * blockSize + blockSize / 2;
  private Color bar = new Color(91, 141, 160);

  private Cell[][] cells;
  private Cell[][] finalCells;

  private boolean finished = false;
  private boolean win = false;
  private boolean firstClick = true;

  private int mines = 40;
  private int flagCount = mines;

  public Game() {
    this.setTitle("Minesweeper");
    this.setVisible(true);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(width, height);
    this.setBackground(bar);
    cells = new Cell[boardHeight + 2][boardWidth + 2];
    finalCells = new Cell[boardHeight][boardWidth];

    Board board = new Board(this);
    board.setPreferredSize(new Dimension(width, height));
    setMines();
    setNumbers();
    Click click = new Click();
    board.addMouseListener(click);
    addKeyListener(this);
    this.setContentPane(board);
    this.pack();
    this.setLocationRelativeTo(null);
  }

  public void setMines() {
    int count = 0;
    for (int y = 0; y < boardHeight + 2; y++) {
      for (int x = 0; x < boardWidth + 2; x++) {
        cells[y][x] = new Cell();
      }
    }
    while (count < mines) {
      int xCord = (int) (Math.random() * boardWidth) + 1;
      int yCord = (int) (Math.random() * boardHeight) + 1;
      if (
        !(cells[yCord][xCord].getMine() || (xCord == 1 && yCord == boardHeight))
      ) {
        cells[yCord][xCord].setMine(true);
        count++;
      }
    }
  }

  public void setNumbers() {
    for (int y = 1; y <= boardHeight; y++) {
      for (int x = 1; x <= boardWidth; x++) {
        if (!(cells[y][x].getMine())) {
          int mineCount = 0;
          for (int xDif = -1; xDif <= 1; xDif++) {
            for (int yDif = -1; yDif <= 1; yDif++) {
              if (!(xDif == 0 && yDif == 0)) {
                if (cells[y + yDif][x + xDif].getMine()) {
                  mineCount++;
                }
              }
            }
          }
          cells[y][x].setNumber(mineCount);
        }
      }
    }
    for (int y = 1; y <= boardHeight; y++) {
      for (int x = 1; x <= boardWidth; x++) {
        finalCells[y - 1][x - 1] = cells[y][x];
      }
    }
  }

  public void printNumbers() {
    System.out.println(" ");
    for (int y = 0; y < boardHeight; y++) {
      String s = "";
      for (int x = 0; x < boardWidth; x++) {
        int temp;
        if (finalCells[y][x].getMine()) {
          temp = 9;
        } else {
          temp = finalCells[y][x].getNumber();
        }
        s += Integer.toString(temp);
        s += ", ";
      }
      System.out.println(s);
    }
  }

  public void reset() {
    flagCount = mines;
    startTime = new Date();
    setMines();
    setNumbers();
    firstClick = true;
    finished = false;
    win = false;
    repaint();
  }

  public void clicked(int x, int y) {
    if (finalCells[y][x].getMine() && firstClick) {
      moveBomb(x, y);
      firstClick = false;
    }
    if (finalCells[y][x].getFlag()) {
      return;
    } else if (finalCells[y][x].isRevealed()) {
      return;
    } else if (finalCells[y][x].getNumber() > 0) {
      finalCells[y][x].reveal();
      firstClick = false;
    } else {
      revealZeros(x, y);
      firstClick = false;
    }
    if (finalCells[y][x].getMine()) {
      gameOver();
    } else if (checkWin()) {
      victory();
    }
  }

  public void revealZeros(int x, int y) {
    finalCells[y][x].reveal();
    for (int xDif = -1; xDif <= 1; xDif++) {
      for (int yDif = -1; yDif <= 1; yDif++) {
        if (
          !(
            (x + xDif < 0) ||
            (y + yDif < 0) ||
            (x + xDif > boardWidth - 1) ||
            (y + yDif > boardHeight - 1)
          ) &&
          !finalCells[y + yDif][x + xDif].isRevealed()
        ) {
          if (finalCells[yDif + y][xDif + x].getFlag()) {
            finalCells[yDif + y][xDif + x].unFlag();
            flagCount++;
          }
          finalCells[y + yDif][x + xDif].reveal();
          if (finalCells[y + yDif][x + xDif].getNumber() == 0) {
            revealZeros(x + xDif, y + yDif);
          }
        }
      }
    }
  }

  public void moveBomb(int x, int y) {
    finalCells[y][x].setMine(false);
    finalCells[boardHeight - 1][0].setMine(true);
    setNumbers();
  }

  public void mark(int x, int y) {
    if (finalCells[y][x].getFlag()) {
      finalCells[y][x].unFlag();
      flagCount++;
    } else if (!finalCells[y][x].isRevealed() && (flagCount > 0)) {
      finalCells[y][x].flag();
      flagCount--;
    }
  }

  public void gameOver() {
    for (int y = 0; y < boardHeight; y++) {
      for (int x = 0; x < boardWidth; x++) {
        if (!finalCells[y][x].isRevealed()) {
          finalCells[y][x].unFlag();
          finalCells[y][x].reveal();
        }
      }
    }
    finished = true;
    JOptionPane.showMessageDialog(null, "Game Over");
    reset();
  }

  public boolean checkWin() {
    for (int y = 0; y < boardHeight; y++) {
      for (int x = 0; x < boardWidth; x++) {
        if (!finalCells[y][x].getMine() && !finalCells[y][x].isRevealed()) {
          return false;
        }
      }
    }
    return true;
  }

  public void victory() {
    win = true;
    for (int y = 0; y < boardHeight; y++) {
      for (int x = 0; x < boardWidth; x++) {
        if (finalCells[y][x].getFlag()) {
          finalCells[y][x].unFlag();
        }
        finalCells[y][x].reveal();
      }
    }
    finished = true;
    winTime = getTime();
    JOptionPane.showMessageDialog(null, "You won!");
    try {
      if (winTime < getBestTime()) {
        setBestTime(winTime);
        JOptionPane.showMessageDialog(null, "New best time!");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    reset();
  }

  public Cell[][] getCells() {
    return finalCells;
  }

  public boolean getFinished() {
    return finished;
  }

  public boolean getWin() {
    return win;
  }

  public int getFlagCount() {
    return flagCount;
  }

  public int getTime() {
    int sec = (int) ((new Date().getTime() - startTime.getTime()) / 1000);
    return sec;
  }

  public int getBestTime() throws Exception {
    Scanner fileScanner = new Scanner(new File("bestTime.txt"));
    return fileScanner.nextInt();
  }

  public void setBestTime(int time) throws Exception {
    PrintStream output = new PrintStream(new FileOutputStream("bestTime.txt"));
    output.println(time);
    output.close();
  }

  public class Click implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent arg0) {
      if (arg0.getButton() == 1) {
        int mouseX = (arg0.getX() - blockSize / 2) / blockSize;
        int mouseY = (arg0.getY() - 2 * blockSize) / blockSize;
        clicked(mouseX, mouseY);
      }

      if (arg0.getButton() == 3) {
        int mouseX = (arg0.getX() - blockSize / 2) / blockSize;
        int mouseY = (arg0.getY() - 2 * blockSize) / blockSize;
        mark(mouseX, mouseY);
      }
      repaint();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
      // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
      // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
      // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
      // TODO Auto-generated method stub

    }
  }

  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      reset();
    }
    if (e.getKeyCode() == KeyEvent.VK_P) {
      printNumbers();
    }
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub

  }
}

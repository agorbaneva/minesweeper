public class Cell {

  private boolean revealed;
  private boolean mine;
  private boolean flagged;
  private int number;

  public Cell() {
    revealed = false;
    mine = false;
    flagged = false;
    number = 0;
  }

  public void reveal() {
    revealed = true;
  }

  public void flag() {
    flagged = true;
  }

  public void unFlag() {
    flagged = false;
  }

  public void setMine(boolean x) {
    mine = x;
  }

  public void setNumber(int x) {
    number = x;
  }

  public boolean isRevealed() {
    return revealed;
  }

  public boolean getFlag() {
    return flagged;
  }

  public boolean getMine() {
    return mine;
  }

  public int getNumber() {
    return number;
  }
}

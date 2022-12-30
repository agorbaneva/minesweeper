public class Driver implements Runnable{
    Game minesweeper = new Game();
    public static void main(String[] args){
        new Thread(new Driver()).start();
    }

    public void run(){
        while(true){
            minesweeper.repaint();
        }
    }
}

import greenfoot.Color;

public class PlayerStats {

    private final int MAX_LIVES = 3;

    private int woodCount = 0;    
    private int livesCount = 1;  
    private StatusBar statusBar = new StatusBar();
    private Label statusBarLabel = new Label(woodCount, 12); 
    
    public PlayerStats() {

        statusBarLabel.setFillColor(Color.BLACK);
        statusBarLabel.setLineColor(Label.transparent);
    }

    public Label getStatusBarLabel() {
        return statusBarLabel;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public int getMAX_LIVES() {
        return MAX_LIVES;
    }

    public void addWood() {
        woodCount++;
        statusBarLabel.setValue(woodCount);
        System.out.println("Wood count: " + woodCount); 
    }

    public boolean canAddLife() {
        
        boolean canAddLife = false;

        if (livesCount < MAX_LIVES) {
            livesCount++;
            canAddLife = true;
        }

        return canAddLife;
    }

    public boolean decreaseLife() {
        livesCount--;

        if (livesCount <= 0) {
            livesCount = 0;
            return true;
        }

        return false;
    }
    
    public int getWoodCount() {
        return woodCount;
    }

    public int getLivesCount() {
        return livesCount;
    }
}

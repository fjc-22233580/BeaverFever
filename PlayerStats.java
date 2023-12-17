public class PlayerStats {

    private final int MAX_LIVES = 3;

    private int woodCount;    
    private int livesCount;   
    
    public PlayerStats() {

    }

    public int getMAX_LIVES() {
        return MAX_LIVES;
    }

    public void addWood() {
        woodCount++;
        System.out.println("Wood count: " + woodCount);
    }

    public void addLife() {
        livesCount++;
    }
    
    public int getWoodCount() {
        return woodCount;
    }

    public int getLivesCount() {
        return livesCount;
    }
}

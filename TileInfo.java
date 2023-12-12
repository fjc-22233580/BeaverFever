public class TileInfo {    

    private int x;    
    private int y;    
    private String tilePath;    
    
    public TileInfo(int x, int y, String tilePath) {
        this.x = x;
        this.y = y;
        this.tilePath = tilePath;
        
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public String getTilePath() {
        return tilePath;
    }
}

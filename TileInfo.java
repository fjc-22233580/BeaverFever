public class TileInfo {    

    private int x;    
    private int y;    
    private String tilePath;
    private int tileNumber;    
    
    
    public TileInfo(int tileNumber, int x, int y, String tilePath) {
        this.tileNumber = tileNumber;
        this.x = x;
        this.y = y;
        this.tilePath = tilePath;
        
    }
    
    public int getTileNumber() {
        return tileNumber;
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

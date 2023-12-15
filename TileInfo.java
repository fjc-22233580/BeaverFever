public class TileInfo {    

    private int x;    
    private int y;    
    private String tilePath;
    private int tileNumber;
    private ActorType actorType; 
    
    public TileInfo(int tileNumber, int x, int y, String tilePath, ActorType actorType) {
        this.tileNumber = tileNumber;
        this.x = x;
        this.y = y;
        this.tilePath = tilePath;
        this.actorType = actorType;        
    }

    public ActorType getActorType() {
        return actorType;
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

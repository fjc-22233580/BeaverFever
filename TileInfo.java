/**
 * Represents information about a tile in a game.
 */
public class TileInfo {    

    private int x;    
    private int y;    
    private String tilePath;
    private int tileNumber;
    private ActorType actorType; 
    
    /**
     * Constructs a TileInfo object with the specified parameters.
     * 
     * @param tileNumber the number of the tile
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @param tilePath the path to the tile image
     * @param actorType the type of actor associated with the tile
     */
    public TileInfo(int tileNumber, int x, int y, String tilePath, ActorType actorType) {
        this.tileNumber = tileNumber;
        this.x = x;
        this.y = y;
        this.tilePath = tilePath;
        this.actorType = actorType;        
    }

    /**
     * Gets the type of actor associated with the tile.
     * 
     * @return the actor type
     */
    public ActorType getActorType() {
        return actorType;
    }
    
    /**
     * Gets the number of the tile.
     * 
     * @return the tile number
     */
    public int getTileNumber() {
        return tileNumber;
    }
    
    /**
     * Gets the x-coordinate of the tile.
     * 
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the tile.
     * 
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Gets the path to the tile image.
     * 
     * @return the tile path
     */
    public String getTilePath() {
        return tilePath;
    }
}

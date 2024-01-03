public class TileFactory {
    
    public void createTile(String tilePath, ActorType type) {
        // TODO - Create a new ObjectTile and return it

        switch (type) {
            case BERRY:
                //eturn new BerryTile(tilePath, type);
            case WOOD:
                //return new WoodTile(tilePath, type);
            case WATER:
                //return new WaterTile(tilePath, type);
            case OTHER:
                //return new ObjectTile(tilePath, type);
            default:
                throw new IllegalArgumentException("Invalid tile type");
        }
    }
}

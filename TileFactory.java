/**
 * The TileFactory class is responsible for creating different types of tiles
 * based on the given parameters.
 */
public class TileFactory {

    /**
     * Creates a new tile based on the given actor type,
     * also passes in the tile image path so we can pull the correct image from the
     * asset pack.
     *
     * @param tilePath The path of the tile image.
     * @param type     The type of the actor associated with the tile.
     * @return The created BaseTile object.
     * @throws IllegalArgumentException if the tile type is invalid.
     */
    public BaseTile createTile(String tilePath, ActorType type) {

        switch (type) {
            case BERRY:
                return new BerryTile(tilePath, type);
            case WOOD:
                return new WoodTile(tilePath, type);
            case WATER:
                return new WaterTile(tilePath, type);
            case WALKWAY:
                return new WalkWay(tilePath, type);
            case FENCE:
                return new FenceTile(tilePath, type);
            case OTHER:
                return new ObjectTile(tilePath, type);
            default:
                throw new IllegalArgumentException("Invalid tile type");
        }
    }
}

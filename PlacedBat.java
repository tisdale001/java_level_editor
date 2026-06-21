import java.awt.Image;

public class PlacedBat {
    int x;
    int y;
    int id;
    Image image;
    private PlacedFlightPath flightPath;

    PlacedBat(int x, int y, int id, Image image) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.image = image;
    }

    public boolean isClicked(int clickX, int clickY) {
        if (image != null) {
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            if (clickX >= this.x && clickX <= this.x + width
                    && clickY >= this.y && clickY <= this.y + height) {
                return true;
            }
        }
        return flightPath != null && flightPath.isClicked(clickX, clickY);
    }

    public int getClickedFlightPathHandle(int clickX, int clickY) {
        if (flightPath == null) {
            return PlacedFlightPath.HANDLE_NONE;
        }
        return flightPath.getClickedHandle(clickX, clickY);
    }

    public void setFlightPath(PlacedFlightPath flightPath) {
        this.flightPath = flightPath;
    }

    public PlacedFlightPath getFlightPath() {
        return this.flightPath;
    }

    public boolean hasFlightPath() {
        return this.flightPath != null;
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
        if (flightPath != null) {
            flightPath.translate(dx, dy);
        }
    }

    public boolean isOutside(int maxX, int maxY) {
        boolean batOutside = x < 0 || y < 0 || x >= maxX || y >= maxY;
        return batOutside || (flightPath != null && flightPath.isOutside(maxX, maxY));
    }
}

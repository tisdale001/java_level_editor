import java.awt.Image;

public class PlacedFlightPath {
    static final int HANDLE_NONE = -1;
    static final int HANDLE_START = 0;
    static final int HANDLE_CONTROL = 1;
    static final int HANDLE_END = 2;

    int startX;
    int startY;
    int controlX;
    int controlY;
    int endX;
    int endY;
    int id;
    Image image;

    private static final int HANDLE_SIZE = 14;

    PlacedFlightPath(int startX, int startY, int controlX, int controlY, int endX, int endY, int id, Image image) {
        this.startX = startX;
        this.startY = startY;
        this.controlX = controlX;
        this.controlY = controlY;
        this.endX = endX;
        this.endY = endY;
        this.id = id;
        this.image = image;
    }

    public int getClickedHandle(int clickX, int clickY) {
        if (isHandleClicked(clickX, clickY, startX, startY)) {
            return HANDLE_START;
        }
        if (isHandleClicked(clickX, clickY, controlX, controlY)) {
            return HANDLE_CONTROL;
        }
        if (isHandleClicked(clickX, clickY, endX, endY)) {
            return HANDLE_END;
        }
        return HANDLE_NONE;
    }

    public boolean isClicked(int clickX, int clickY) {
        if (getClickedHandle(clickX, clickY) != HANDLE_NONE) {
            return true;
        }
        for (int i = 0; i <= 40; i++) {
            double t = i / 40.0;
            int x = getBezierX(t);
            int y = getBezierY(t);
            if (Math.abs(clickX - x) <= HANDLE_SIZE && Math.abs(clickY - y) <= HANDLE_SIZE) {
                return true;
            }
        }
        return false;
    }

    public void moveHandle(int handle, int x, int y) {
        if (handle == HANDLE_START) {
            startX = x;
            startY = y;
        } else if (handle == HANDLE_CONTROL) {
            controlX = x;
            controlY = y;
        } else if (handle == HANDLE_END) {
            endX = x;
            endY = y;
        }
    }

    public void translate(int dx, int dy) {
        startX += dx;
        startY += dy;
        controlX += dx;
        controlY += dy;
        endX += dx;
        endY += dy;
    }

    public boolean isOutside(int maxX, int maxY) {
        return pointOutside(startX, startY, maxX, maxY)
            || pointOutside(controlX, controlY, maxX, maxY)
            || pointOutside(endX, endY, maxX, maxY);
    }

    public int getBezierX(double t) {
        double invT = 1.0 - t;
        return (int)Math.round(invT * invT * startX + 2.0 * invT * t * controlX + t * t * endX);
    }

    public int getBezierY(double t) {
        double invT = 1.0 - t;
        return (int)Math.round(invT * invT * startY + 2.0 * invT * t * controlY + t * t * endY);
    }

    private boolean isHandleClicked(int clickX, int clickY, int handleX, int handleY) {
        int half = HANDLE_SIZE / 2;
        return clickX >= handleX - half && clickX <= handleX + half
            && clickY >= handleY - half && clickY <= handleY + half;
    }

    private boolean pointOutside(int x, int y, int maxX, int maxY) {
        return x < 0 || y < 0 || x >= maxX || y >= maxY;
    }
}

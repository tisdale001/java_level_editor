import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics;

class DragOverlayPane extends JComponent {
    private Image draggedImage;
    private Point mousePoint;

    public void setDraggedImage(Image img) {
        this.draggedImage = img;
    }

    public void setMousePoint(Point p) {
        this.mousePoint = p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (draggedImage != null && mousePoint != null) {
            int imgWidth = draggedImage.getWidth(null);
            int imgHeight = draggedImage.getHeight(null);
            g.drawImage(draggedImage, mousePoint.x - imgWidth / 2, mousePoint.y - imgHeight / 2, null);
        }
    }
}
package frontend.objectWindows;

import frontend.ClientManager;
import frontend.graphicsInterface.Collection;
import frontend.graphicsInterface.controllers.Controllers;
import library.clientCommands.commandType.RemoveIdCommand;
import library.clientCommands.commandType.UpdateIdCommand;
import library.сlassModel.Organization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Icon extends JComponent implements ActionListener {
    private final Long id;
    private Color color;
    private final Timer timer = new Timer(35, this);

    private OrganizationController controller;
    private ObjectsMapModel model;

    private int tmpX;
    private int tmpY;


    private int velocityX;
    private int velocityY;

    private int startX;
    private int startY;

    // параметы - цвет и нужно ли зарисовывать всю область

    public Icon(Long id, Color color, Dimension size, int startX, int startY, OrganizationController organizationController, ObjectsMapModel model) {
        this.id = id;
        this.model = model;
        this.controller = organizationController;
        this.color = color;
        setSize(size);
        this.startX = startX;
        this.startY = startY;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                InfoFrame infoFrame = new InfoFrame();
                Organization or = model.getNewOrg().stream().collect(Collectors.toMap(Organization::getId, o -> o)).get(id);
                infoFrame.getTextArea().setText(Collection.toLocaleString(or, Controllers.getLocale()));
                infoFrame.getUpdateButton().addActionListener(a -> {
                    controller.setIdForUpdate(id);
                    controller.runCreation(UpdateIdCommand.class);
                    infoFrame.setVisible(false);
                    infoFrame.dispose();
                });
                infoFrame.getRemoveButton().addActionListener(a -> {
                    ClientManager cl = controller.getClientManager();
                    cl.executeCommand(new RemoveIdCommand(id, cl.getUserData()));
                    infoFrame.setVisible(false);
                    infoFrame.dispose();
                });

            }
            });
    }




    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        int x = 0;
        int y = 0;
        //Polygon polygon = new Polygon(xs, ys, n);
//        g2d.fill(polygon);
        int a = (int) size.getHeight();
        int c = a / 8;
        int b = (int) size.getWidth();
        g2d.fillArc(x, y - c, b, 2 * c, 180, 180);
        g2d.fillRect(x + b / 8, y, b * 6 / 8, a / 2);
        g2d.fillArc(x, y + a / 2 - c, b, 2 * c, 0, 180);
        int d = b / 4;
        int e = a / 2;
        int[] xs = {x + b / 2 - d / 2, x + b / 2 - d / 2, x + b / 2, x + b / 2 + d / 2, x + b / 2 + d / 2};
        int[] ys = {y + e, y + a - e / 5, y + a, y + a - e / 5, y + e};
        int n = xs.length;
        g2d.fillPolygon(xs, ys, n);
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getLocation().x == tmpX && getLocation().y == tmpY) {
            timer.stop();
            if (tmpY + getSize().height == 0) {
                this.getParent().remove(this);
            }
            this.startX = tmpX;
            this.startY = tmpY;

        } else {
            setLocation(getLocation().x + velocityX, getLocation().y + velocityY);
            repaint();
        }
    }


    private int gcd(int a, int b) {
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }


    public void changeLocation(int x, int y) {
        Dimension size = getSize();
        this.tmpX = x - (int) size.getWidth() / 2;
        this.tmpY = y - (int) size.getHeight();
        int a = tmpX - getLocation().x;
        int b = tmpY - getLocation().y;
        int k = Math.abs(gcd(a, b));
        if(k == 0) {
            velocityX = 0;
            velocityY = 0;
        } else {
            int vX = a / k;
            int vY = b / k;
            boolean bool = (Math.abs(vX) == 1 || Math.abs(vX) == 0) && (Math.abs(vY) == 1 || Math.abs(vY) == 0);

            if (bool) {
                vX = vX * 20;

                vY = vY * 20;

                timer.setDelay(30);
            } else {
                timer.setDelay(58);
            }
            velocityX = vX;
            velocityY = vY;
            timer.start();
        }
    }


    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Icon icon = (Icon) o;
        return tmpX == icon.tmpX &&
                tmpY == icon.tmpY &&
                velocityX == icon.velocityX &&
                velocityY == icon.velocityY &&
                startX == icon.startX &&
                startY == icon.startY &&
                Objects.equals(id, icon.id) &&
                Objects.equals(color, icon.color) &&
                Objects.equals(timer, icon.timer);
    }

    public Icon clone() {
        return new Icon(id, color, getSize(), startX, startY, controller, model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, timer, tmpX, tmpY, velocityX, velocityY, startX, startY);
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Icon{" +
                "id=" + id +
                ", color=" + color +
                ", tmpX=" + tmpX +
                ", tmpY=" + tmpY +
                '}';
    }
}
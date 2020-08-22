package frontend.objectWindows;


import library.сlassModel.Coordinates;
import library.сlassModel.Organization;
import library.сlassModel.OrganizationType;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ObjectsMapView extends JPanel {
    private DrawPanel drawPanel;
    private JTextArea objectsInfo;
    private JPanel southPanel;
    private JScrollPane scrollPane;
    private JScrollPane scrollPaneForObjectsInfo;
    private JButton clearButton;
    private JPanel buttonPanel;

    private List<Icon> icons = new ArrayList<>();






    public ObjectsMapView(int cellSize, int cellCount) {

        init(cellSize, cellCount);
    }

    public void init(int cellSize, int cellCount) {
        drawPanel = new DrawPanel(cellSize, cellCount);
        scrollPane = new JScrollPane(drawPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(Color.black)));
        scrollPane.setWheelScrollingEnabled(true);

        Font myFont = new Font("lucida grande", Font.BOLD, 15);
        objectsInfo = new JTextArea();
        objectsInfo.setEditable(false);
        objectsInfo.setLineWrap(true);
        objectsInfo.setWrapStyleWord(true);
        objectsInfo.setFont(myFont);


        scrollPaneForObjectsInfo = new JScrollPane(objectsInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneForObjectsInfo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(Color.black)));
        scrollPaneForObjectsInfo.setPreferredSize(new Dimension(1000, 150));


        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(scrollPaneForObjectsInfo, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        clearButton = new JButton();
        clearButton.setSize(20, 20);
        clearButton.setFocusPainted(false);

        buttonPanel.add(clearButton);
        southPanel.add(buttonPanel, BorderLayout.WEST);

//        getContentPane().setLayout(new BorderLayout());
//        getContentPane().add(southPanel, BorderLayout.SOUTH);
//        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        //add(southPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(900, 900);
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

    }


    public void setIcons(Deque<Icon> icons) {
        for (Icon i: icons) {
            i.setLocation(i.getStartX() - i.getSize().width /2, i.getStartY() - i.getSize().height);
            drawPanel.add(i);
        }

    }

    public void changeIconsCoordinate(Deque<Icon> icons) {
        //if( )
    }

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }

    public JTextArea getObjectsInfo() {
        return objectsInfo;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JScrollPane getScrollPaneForObjectsInfo() {
        return scrollPaneForObjectsInfo;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }


    public void setDrawPanel(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
    }

//    public static void main(String[] args) {
//
//        Deque<Organization> organizations; //вот она
//        organizations = new ArrayDeque<>(Arrays.asList(new Organization("a", 1L, "adw", new Coordinates(25, 25), new Date(), 123, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("curry", 2L, "adw", new Coordinates(-1, -1), new Date(), 10, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("rose", 3L, "adw", new Coordinates(15, 15), new Date(), 123, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("iva", 4L, "adw", new Coordinates(56, 56), new Date(), 512, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("polina", 5L, "adw", new Coordinates(23, 23), new Date(), 10, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("polina", 6L, "adw", new Coordinates(2, 2), new Date(), 330, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("ivan123", 7L, "adw", new Coordinates(18, 18), new Date(), 1230, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("Zion", 8L, "adw", new Coordinates(11, 1), new Date(), 5, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("lebron", 9L, "adw", new Coordinates(30, 25), new Date(), 12, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("opara", 10L, "adw", new Coordinates(25, 30), new Date(), 10230, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("kobe", 11L, "adw", new Coordinates(25, 17), new Date(), 12, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("", 12L, "adw", new Coordinates(34, 43), new Date(), 123, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("sena", 13L, "adw", new Coordinates(14, 90), new Date(), 6230, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("sena", 14L, "adw", new Coordinates(66, 55), new Date(), 12, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("sena8980990", 15L, "adw", new Coordinates(0, 100), new Date(), 1230, OrganizationType.COMMERCIAL, 12313d, null),
//                new Organization("0990", 16L, "adw", new Coordinates(0, 0), new Date(), 1230, OrganizationType.COMMERCIAL, 12313d, null)
//        ));
//
//        Deque<Organization> organizations2;
//        organizations2 = new ArrayDeque<>(Arrays.asList(organizations.getFirst(),
//                organizations.getLast()
//        ));
//
//        Deque<Organization> organizations3;
//        organizations3 = new ArrayDeque<>(Arrays.asList(organizations.getFirst(),
//                new Organization("0990", 16L, "adw", new Coordinates(10, 10), new Date(), 1, OrganizationType.COMMERCIAL, 12313d, null)
//
//        ));
//
//        Deque<Organization> organizations4;
//        organizations4 = new ArrayDeque<>(Arrays.asList(organizations.getFirst(),
//                organizations3.getLast(),
//                new Organization("0990", 15L, "adw", new Coordinates(5, 5), new Date(), 1230, OrganizationType.COMMERCIAL, 12313d, null)
//
//        ));
//        final ObjectsMapController[] c = new ObjectsMapController[1];
//        final ObjectsMapView[] view = new ObjectsMapView[1];
//        SwingUtilities.invokeLater(() -> {
//
//            ObjectsMapModel model = new ObjectsMapModel(20);
//            ObjectsMapModel.Entity entity = model.generateIcons(organizations);
//            view[0] = new ObjectsMapView(model.getCellSize(), entity.getCellCount());
//            c[0] = new ObjectsMapController(view[0], model);
//            c[0].updateObjectsMapView(organizations); //загружаю полную коллекцию
////            icons.forEach(o ->System.out.println( "x: " + o.getStartX() + " y: " + o.getStartY()  + "\n"));
////
////            c.getView().setIcons(entity.getIcons());
////            icons.getLast().changeLocation(2360, 280);
//        });
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        SwingUtilities.invokeLater(() -> {
//            c[0].updateObjectsMapView(organizations2);//потом удаляю все, кроме двух
//        });
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        SwingUtilities.invokeLater(() -> {
//            c[0].updateObjectsMapView(organizations3); //отом заменяю у двух размер и координату на ноль
//        });
//
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        SwingUtilities.invokeLater(() -> {
//            c[0].updateObjectsMapView(organizations4);//потом добавляю к ним один
//        });
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            c[0].updateObjectsMapView(new ArrayDeque<>());//удаляю все
//        });
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        SwingUtilities.invokeLater(() -> {
//            c[0].updateObjectsMapView(organizations);//загружаю новую
//        });
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(Arrays.toString(c[0].getView().getDrawPanel().getComponents()));
//
//
//
//    }
}

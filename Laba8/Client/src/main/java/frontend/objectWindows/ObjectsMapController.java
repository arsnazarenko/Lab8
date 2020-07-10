package frontend.objectWindows;

import library.сlassModel.Organization;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ObjectsMapController {
    private ObjectsMapView view;
    private ObjectsMapModel model;

    public ObjectsMapController(ObjectsMapView view, ObjectsMapModel model) {
        this.view = view;
        this.model = model;
    }

    private void clearTextArea() {
        JTextArea objectsArea = view.getObjectsInfo();
        objectsArea.selectAll();
        objectsArea.replaceSelection("");
    }
    public synchronized void updateObjectsMapView(Deque<Organization> organization) {

        model.setNewOrganization(organization);
        Deque<Organization> old = model.getOldOrg();
        Deque<Organization> newOrg = model.getNewOrg();

        if(old.size() == newOrg.size()) {
            List<Organization> oldList = new ArrayList<>(old);
            List<Organization> newList = new ArrayList<>(newOrg);
            Collections.sort(oldList);
            Collections.sort(newList);
            if (!oldList.equals(newList)) {
                newList.removeAll(oldList);
                ObjectsMapModel.Entity updatedIcons = model.generateIcons(new ArrayDeque<>(newList));
                if (updatedIcons.getCellCount() > view.getDrawPanel().getCellCount()) { //если при обновлении надо увеличть поле
                    if (view.getDrawPanel() != null) {
                        ObjectsMapModel.Entity oldIConsWithBigBoard = model.generateIcons(updatedIcons.getOffset(), old);
                        JScrollPane sp = view.getScrollPane();
                        int cellCount = oldIConsWithBigBoard.getCellCount();
                        view.remove(sp); //удаляем с панели старый объект
                        view.setDrawPanel(new DrawPanel(model.getCellSize(), cellCount)); //установили новый эелмент для рисования
                        sp.setViewportView(view.getDrawPanel()); //обновляем этот рисунок в перемещаемом окошке
                        view.add(sp, BorderLayout.CENTER);
                        view.revalidate();
                        view.repaint();
                        view.setIcons(oldIConsWithBigBoard.getIcons());
                        Map<Long, Icon> mapUpdated = updatedIcons.getIcons().stream().collect(Collectors.toMap(Icon::getId, o -> o));
                        Map<Long, Icon> onDrawPanel = Arrays.stream(view.getDrawPanel().getComponents()).map(o -> (Icon) o).collect(Collectors.toMap(Icon::getId, o -> o));
                        mapUpdated.entrySet().forEach(en -> {
                            onDrawPanel.get(en.getKey()).changeLocation(en.getValue().getStartX(), en.getValue().getStartY());
                        });
                    }
                } else {
                    ObjectsMapModel.Entity updatedIconsForOld = model.generateIcons(view.getDrawPanel().getCellCount()/2, new ArrayDeque<>(newList));
                    System.out.println("осталось");
                    Map<Long, Icon> mapUpdated = updatedIconsForOld.getIcons().stream().collect(Collectors.toMap(Icon::getId, o -> o));
                    Map<Long, Icon> onDrawPanel = Arrays.stream(view.getDrawPanel().getComponents()).map(o -> (Icon) o).collect(Collectors.toMap(Icon::getId, o -> o));
                    System.out.println(onDrawPanel);
                    System.out.println(onDrawPanel.get(5L));

                    mapUpdated.forEach((key, value) -> {
                        onDrawPanel.get(key).setSize(value.getSize());
                        onDrawPanel.get(key).changeLocation(value.getStartX(), value.getStartY());});
//                    for (Map.Entry<Long, Icon> e : mapUpdated.entrySet()) {
//                        System.out.println(e);
//                        Icon icon = onDrawPanel.get(e.getKey());
//                        System.out.println(icon);
//                        icon.changeLocation(e.getValue().getStartX(), e.getValue().getStartY());
//
//                    }
                }
            }

        } else if (old.size() > newOrg.size()) {
            System.out.println("тут");
            Deque<Organization> tmp = new ArrayDeque<>(old);

            tmp.removeAll(newOrg);
            System.out.println(tmp);
            List<Long> ids = tmp.stream().map(Organization::getId).collect(Collectors.toList());
            Arrays.stream(view.getDrawPanel().getComponents()).map(o -> (Icon) o).filter(o -> ids.contains(o.getId())).forEach(o -> o.changeLocation(o.getStartX() + o.getSize().width/2, 0));
            //удаление элемента

        } else {
            ObjectsMapModel.Entity newIcons = model.generateIcons(model.getNewOrg());


            if (newIcons.getCellCount() > view.getDrawPanel().getCellCount()) {
                if (view.getDrawPanel() != null) {
                    ObjectsMapModel.Entity oldIconsWithBigBoard = model.generateIcons(newIcons.getOffset(), model.getOldOrg());
                    System.out.println(oldIconsWithBigBoard.getIcons());
                    JScrollPane sp = view.getScrollPane();
                    int cellCount = oldIconsWithBigBoard.getCellCount();
                    view.remove(sp); //удаляем с панели старый объект
                    view.setDrawPanel(new DrawPanel(model.getCellSize(), cellCount)); //установили новый эелмент для рисования
                    sp.setViewportView(view.getDrawPanel()); //обновляем этот рисунок в перемещаемом окошке
                    view.add(sp, BorderLayout.CENTER);
                    view.revalidate();
                    view.repaint();
                    view.setIcons(oldIconsWithBigBoard.getIcons());
                    // ставили на новой доске те, которые были
                    List<Organization> tmp = new ArrayList<>(newOrg);
                    tmp.removeAll(old);
                    List<Icon> added = new ArrayList<>(model.generateIcons(new ArrayDeque<>(tmp)).getIcons());
                    for (Icon icon : added) {
                        Icon iconForAdd = icon.clone();
                        iconForAdd.setStartY(0);
                        iconForAdd.setLocation(iconForAdd.getStartX() - iconForAdd.getSize().width / 2, iconForAdd.getStartY() - iconForAdd.getSize().height);
                        view.getDrawPanel().add(iconForAdd);
                        iconForAdd.changeLocation(icon.getStartX(), icon.getStartY());
                    }
                }

            } else {
                List<Organization> tmp = new ArrayList<>(newOrg);
                tmp.removeAll(old);
                List<Icon> added = new ArrayList<>(model.generateIcons(view.getDrawPanel().getCellCount()/2, new ArrayDeque<>(tmp)).getIcons());
                for (Icon icon : added) {
                    Icon iconForAdd = icon.clone();
                    iconForAdd.setStartY(0);
                    iconForAdd.setLocation(iconForAdd.getStartX() - iconForAdd.getSize().width / 2, iconForAdd.getStartY() - iconForAdd.getSize().height);
                    view.getDrawPanel().add(iconForAdd);
                    iconForAdd.changeLocation(icon.getStartX(), icon.getStartY());
                }
            }

        }
    }

    public ObjectsMapView getView() {
        return view;
    }
}

package frontend.objectWindows;

import library.сlassModel.Organization;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectsMapModel {
    private final int CELL_SIZE;
    private Deque<Organization> oldOrg = new ArrayDeque<>();
    private Deque<Organization> newOrg = new ArrayDeque<>();

    private OrganizationController controller;


    public void setNewOrganization(Deque<Organization> organization) {
        this.oldOrg = this.newOrg;
        this.newOrg = organization;
    }



    public Deque<Organization> getOldOrg() {
        return oldOrg;
    }

    public Deque<Organization> getNewOrg() {
        return newOrg;
    }

    public static class Entity {
        private int offset;
        private int cellCount;
        private Deque<Icon> icons;

        public Entity(int offset, int cellCount, Deque<Icon> icons) {
            this.offset = offset;
            this.cellCount = cellCount;
            this.icons = icons;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getCellCount() {
            return cellCount;
        }

        public void setCellCount(int cellCount) {
            this.cellCount = cellCount;
        }

        public Deque<Icon> getIcons() {
            return icons;
        }

        public void setIcons(Deque<Icon> icons) {
            this.icons = icons;
        }
    }


    public ObjectsMapModel(int CELL_SIZE, OrganizationController controller) {
        this.controller = controller;
        this.CELL_SIZE = CELL_SIZE;
    }


    public Entity generateIcons(Deque<Organization> organizations) {
        int offset = calcOffset(organizations);

        int cellCount = offset * 2;

        Deque<Icon> icons = calcCoordinate(organizations, offset, cellCount);

        return new Entity(offset, cellCount, icons);

    }

    public Entity generateIcons(int offset, Deque<Organization> organizations) {
        int cellCount = offset * 2;
        Deque<Icon> icons = calcCoordinate(organizations, offset, cellCount);
        return new Entity(offset, cellCount, icons);
    }






    private int calcOffset(Deque<Organization> organizations) {
        return organizations.stream().
                flatMap(o -> Stream.of(Math.round(o.getCoordinates().getX()), Math.round(o.getCoordinates().getY()))).
                mapToInt(Number::intValue).
                map(Math::abs).
                filter(o -> o > 56).
                max().
                orElse(56) + 4;
    }


    private Deque<Icon> calcCoordinate(Deque<Organization> organizations, int offset, int cellCount) {
        return organizations.stream().map(o -> new Icon(o.getId(), ColorGenerator.generate(o.getUserLogin()), Sizes.calcSize(o.getEmployeesCount()).getSizeValue(), (int) Math.round((o.getCoordinates().getX()) + offset) * CELL_SIZE,
                (cellCount  - (Math.round(o.getCoordinates().getY()) + offset)) * CELL_SIZE, controller, this)).collect(Collectors.toCollection(ArrayDeque::new));
    }





    public int getCellSize() {
        return CELL_SIZE;
    }

}
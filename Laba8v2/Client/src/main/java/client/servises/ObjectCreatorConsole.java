package client.servises;

import library.сlassModel.Organization;

import java.util.Scanner;

public class ObjectCreatorConsole implements IObjectCreator {






    private IValidator validator;

    public ObjectCreatorConsole(IValidator validator) {
        this.validator = validator;
    }
    @Override
    public Organization create(Scanner reader) {
        return null;
    }
}

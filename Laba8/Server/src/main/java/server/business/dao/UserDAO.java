package server.business.dao;



public interface UserDAO<Entity, Key> {
    void create(Entity user);
    UserInfo read(Key login);

}

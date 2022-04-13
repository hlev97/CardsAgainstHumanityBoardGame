package hu.bme.cah.business_logic;

import hu.bme.cah.business_logic.User;

public class Room {

    private List<User> allowedUsers;
    private List<User> connectedUsers;

    public void startGame(){

    }

    public void joinUser(User u)
    {
        if (allowedUsers.contains(u))
        {
            u.setRoom(this);        //ez fogja meghívni, vagy a controller?

            if (connectedUsers.contains(u) == false)
                connectedUsers.append(u);
        }
    }

    public void leaveUser(User u)
    {
        if (connectedUsers.contains(u) == true)
                connectedUsers.delete(u);   //nem biztos hogy ez a függvény jó
    }

}
package nl.hr.annelies.medialab_admin;

public class ListItemModel {

    String id;
    boolean found;
    boolean hasMessage;

    public ListItemModel(String name, boolean found, boolean hasMessage) {

        this.id = name;
        this.found = found;
        this.hasMessage = hasMessage;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getFound() { return found; }

    public void setFound(boolean found) { this.found = found; }

    public boolean getHasMessage() { return hasMessage; }

    public void setHasMessage(boolean hasMessage) { this.hasMessage = hasMessage; }

}


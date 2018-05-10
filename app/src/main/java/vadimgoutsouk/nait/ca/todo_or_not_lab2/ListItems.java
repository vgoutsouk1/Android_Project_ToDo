package vadimgoutsouk.nait.ca.todo_or_not_lab2;

/**
 * Created by laptop on 3/14/2018.
 */

public class ListItems {
    String item;
    int status;
    String created;
    int id;

    //constructor class
    public ListItems(String item, int status, String created, int id) {
        this.id = id;
        this.item = item;
        this.status = status;
        this.created = created;
    }

    public ListItems()
    {

    }
    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    //getters
    public int getId() {

        return id;
    }

    public String getItem() {
        return item;
    }

    public int getStatus() {
        return status;
    }

    public String getCreated() {
        return created;
    }




}

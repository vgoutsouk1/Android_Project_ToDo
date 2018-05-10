package vadimgoutsouk.nait.ca.todo_or_not_lab2;

/**
 * Created by laptop on 3/14/2018.
 */

public class ListTitles

{
    private int titleId;
    private String title;

    public ListTitles() {
        this.titleId = 0;
        this.title = "byob";

    }

    @Override
    public String toString() {
        return title;
    }

    public int getTitleId() {
        return titleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

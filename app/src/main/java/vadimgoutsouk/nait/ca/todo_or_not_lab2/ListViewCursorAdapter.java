package vadimgoutsouk.nait.ca.todo_or_not_lab2;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;

/**
 * Created by laptop on 3/21/2018.
 */

public class ListViewCursorAdapter extends SimpleCursorAdapter
{
    static final String[] FROM = {DBManager.C_ITEM, DBManager.ITEM_ID, DBManager.CREATED}; //fields in the cursor
    static final int[] TO = {R.id.cursor_item, R.id.cursor_id, R.id.cursor_created}; // fields in the listview

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
    }

    public ListViewCursorAdapter(Context context, Cursor cursor)
    {

        super(context, R.layout.cursor_row, cursor, FROM, TO);

    }
}

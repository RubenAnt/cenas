package task_list.pmd.di.ubi.pt.task_list;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    private SQLiteDatabase oSQLiteDB;
    private OpenBd table;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        table = new OpenBd(this);
        oSQLiteDB = table.getWritableDatabase();

        ShowTable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        oSQLiteDB = table.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        table.close();
    }

    public void onINSERTclick(View v) {
        ContentValues oCV = new ContentValues ();
        EditText oED2 = (EditText) findViewById(R.id.Task_Name);
        EditText oED3 = (EditText) findViewById(R.id.Task_Description);
        EditText oED4 = (EditText) findViewById(R.id.Task_Date);
        oCV.put(table.COL2, oED2.getText().toString());
        oCV.put(table.COL3, oED3.getText().toString());
        oCV.put(table.COL4, oED4.getText().toString());
        oSQLiteDB.insert(table.TABLE_NAME, null, oCV);
        ShowTable();
    } /* Inserçâo de dados na tabela */

    public void onDELclick(View v) {
        oSQLiteDB.delete(table.TABLE_NAME, "ROWID="+v.getId()/10, null);
        LinearLayout oLL1 = (LinearLayout) findViewById(v.getId()+5);
        ((LinearLayout) oLL1.getParent()).removeView(oLL1);
    } /* Eliminaçâo de dados na tabela */


    public void onEDITclick(View v) {
        ContentValues oCV = new ContentValues ();
        EditText oED2 = (EditText) findViewById(v.getId()+1);
        EditText oED3 = (EditText) findViewById(v.getId()+2);
        EditText oED4 = (EditText) findViewById(v.getId()+3);
        oCV.put(table.COL2, oED2.getText().toString());
        oCV.put(table.COL3, oED3.getText().toString());
        oCV.put(table.COL4, oED4.getText().toString());
        oSQLiteDB.update(table.TABLE_NAME, oCV, "ROWID="+v.getId()/10, null);
    } /* Ediçâo de dados na tabela */


    private void ShowTable() {
        LinearLayout oLL = (LinearLayout)findViewById(R.id.llsv);
        oLL.removeAllViews();
        Cursor oCursor = oSQLiteDB.query(table.TABLE_NAME ,new String[]{ "*" },null,null,null,null,null,null);
        boolean bCarryOn = oCursor.moveToFirst();
        while(bCarryOn) {
            LinearLayout oLL1 = (LinearLayout)getLayoutInflater().inflate(R.layout.inserts,null);
            oLL1.setId(oCursor.getInt(0)*10+5);

            EditText oED2 = (EditText) oLL1.findViewById(R.id.Task_Name);
            oED2.setId(oCursor.getInt(0)*10+2);
            oED2.setText(oCursor.getString(1));

            EditText oED3 = (EditText) oLL1.findViewById(R.id.Task_Description);
            oED3.setId(oCursor.getInt(0)*10+3);
            oED3.setText(oCursor.getString(2));

            EditText oED4 = (EditText) oLL1.findViewById(R.id.Task_Date);
            oED4.setId(oCursor.getInt(0)*10+4);
            oED4.setText(oCursor.getString(3));

            Button oB1 = (Button) oLL1.findViewById(R.id.EDIT);
            oB1.setId(oCursor.getInt(0)*10+1);

            Button oB2 = (Button) oLL1.findViewById(R.id.DELETE);
            oB2.setId(oCursor.getInt(0)*10);

            oLL.addView(oLL1);
            bCarryOn = oCursor.moveToNext();
        }
    } /* Responsavel pela demostraçao das tarefas existentes na tabela */

}

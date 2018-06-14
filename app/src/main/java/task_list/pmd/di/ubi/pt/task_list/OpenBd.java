package task_list.pmd.di.ubi.pt.task_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenBd extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Tasks"; /* Nome de base de dados */
    protected static final String TABLE_NAME = "Task"; /* Nome da tabela da base de dados */
    protected static final String COL1 = "id"; /* 1ª campo da tabela */
    protected static final String COL2 = "Task_Name"; /* 2ª campo da tabela */
    protected static final String COL3 = "Task_Description"; /* 3ª campo da tabela */
    protected static final String COL4 = "Task_Date"; /* 4ª campo da tabela */
    protected static final String COL5 = "Priority"; /* 5ª campo da tabela */
    private static final String CREATE_TASK = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL2 + " VARCHAR(50)," + COL3 + " VARCHAR(200)," + COL4 + " VARCHAR(50)," + COL5 + " VARCHAR(50))";
    /* Criaçâo da tabela */

    public OpenBd (Context context) {
        super(context ,DB_NAME ,null ,DB_VERSION);
    } /* Abrir base de dados*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK);
    } /* Criaçao da tabela na base de dados*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME + ";");
        db.execSQL(CREATE_TASK);
    }   /* Manipulaçâo da base de dados */
}

package task_list.pmd.di.ubi.pt.task_list;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
    private SQLiteDatabase oSQLiteDB;
    private OpenBd table;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state); // boa noite
        setContentView(R.layout.activity_main);
        final EditText oED2 = (EditText) findViewById(R.id.Task_Name); //  declaraçao de variavel de Armazenamento de string Task_name introduzida
        final EditText oED3 = (EditText) findViewById(R.id.Task_Description); // declaraçao de variavel de Armazenamento de string Task_description introduzida
        final EditText oED4 = (EditText) findViewById(R.id.Task_Date); //declaraçao de variavel de Armazenamento de string Task_date introduzida
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);// declaraçao de variavel de Armazenamento de string radiobutton introduzida
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){ // ouvir qual opcao selecionada no radiobutton

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) { // funcao escutar output do radiobutton
                RadioButton selectedRadioButton = (RadioButton) findViewById(checkedId); //variavel responsavel por armazenar o botao selecionado no radiobutton
                String id_radio = selectedRadioButton.getText().toString();
                if(id_radio.matches("High")){
                    oED2.setBackgroundColor(Color.RED);
                    oED3.setBackgroundColor(Color.RED);
                    oED4.setBackgroundColor(Color.RED);
                }else if(id_radio.matches("Medium")){
                    oED2.setBackgroundColor(Color.BLUE);
                    oED3.setBackgroundColor(Color.BLUE);
                    oED4.setBackgroundColor(Color.BLUE);
                }else if(id_radio.matches("Low")){
                    oED2.setBackgroundColor(Color.GREEN);
                    oED3.setBackgroundColor(Color.GREEN);
                    oED4.setBackgroundColor(Color.GREEN);
                }
            }
        });
        table = new OpenBd(this); //criar tabela
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
        RadioGroup radio = (RadioGroup) findViewById(R.id.radiogroup); // declaraçao de variavel radiobutton
        int selectedId = radio.getCheckedRadioButtonId(); // varialvel responsavel por armazenar a opccao selecionada do radiobotton
        if(selectedId!=-1){
            if(oED2.getText().toString().matches("") || oED3.getText().toString().matches("") || oED4.getText().toString().matches("") ){
                alerta("Campos Vazios",Toast.LENGTH_SHORT); // mensagem de erro caso os campos da tarefa nao estiverem preenchidos
            }else{
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                String id_radio = selectedRadioButton.getText().toString();
                oCV.put(table.COL2, oED2.getText().toString());
                oCV.put(table.COL3, oED3.getText().toString());
                oCV.put(table.COL4, oED4.getText().toString());
                oCV.put(table.COL5, id_radio); // Escolha de cor em string atraves do radiobutton usar mais tarde (...)funcao showTable().
                oSQLiteDB.insert(table.TABLE_NAME, null, oCV);
                ShowTable();
            }
        }else{
            alerta("Prioridade Obrigatoria!",Toast.LENGTH_SHORT); // mensagem de erro caso o campo priority nao estiver preenchido
        }
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

    public void alerta(CharSequence text,int duration){ // funcao mostrar mensagems introduzidas
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void ShowTable() {
        LinearLayout oLL = (LinearLayout)findViewById(R.id.llsv);
        oLL.removeAllViews();
        Cursor oCursor = oSQLiteDB.query(table.TABLE_NAME ,new String[]{ "*" },null,null,null,null,null,null);
        boolean bCarryOn = oCursor.moveToFirst();
        int color=0; // variavel responsavel por verificar a string introduzida no radiobutton
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

            /*...*/
            if(oCursor.getString(4).equals("High")){ // verificaçao de string introduzida do raddionbutton caso seja high
                color=Color.RED; // mostrar cor de fundo
            }else if(oCursor.getString(4).equals("Medium")){// verificaçao de string introduzida do raddionbutton caso seja medium
                color=Color.BLUE;
            }else if(oCursor.getString(4).equals("Low")){// verificaçao de string introduzida do raddionbutton caso seja low
                color=Color.GREEN;
            }

            Button oB1 = (Button) oLL1.findViewById(R.id.EDIT);
            oB1.setId(oCursor.getInt(0)*10+1);

            Button oB2 = (Button) oLL1.findViewById(R.id.DELETE);
            oB2.setId(oCursor.getInt(0)*10);

            oLL.addView(oLL1);
            oLL1.setBackgroundColor(color); // mostrar as tarefas com preencimento de fundo nas strings
            bCarryOn = oCursor.moveToNext();
        }
    } /* Responsavel pela demostraçao das tarefas existentes na tabela */
}

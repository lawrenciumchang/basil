package com.lawrencium.basil;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.lawrencium.basil.R.id.txt_week1;


public class Act_CategoryView extends Activity {

    TextView txt_week1;
    TextView txt_week2;
    TextView txt_week3;
    TextView txt_week4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        Bundle bundle = getIntent().getExtras();
        String catName = bundle.getString("CAT_NAME");
        String catTotal = bundle.getString("CAT_TOTAL");

        TextView monthlyExpense = (TextView) findViewById(R.id.monthlyExpense);
        monthlyExpense.setText(catName +" Monthly Expense");

        txt_week1 = (TextView) findViewById(R.id.txt_week1);
        // hide until its title is clicked
        txt_week1.setVisibility(View.GONE);

        txt_week2 = (TextView) findViewById(R.id.txt_week2);
        // hide until its title is clicked
        txt_week2.setVisibility(View.GONE);

        txt_week3 = (TextView) findViewById(R.id.txt_week3);
        // hide until its title is clicked
        txt_week3.setVisibility(View.GONE);

        txt_week4 = (TextView) findViewById(R.id.txt_week4);
        // hide until its title is clicked
        txt_week4.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_category_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggle_contents(View v){

        switch(v.getId()){
            case R.id.week1:
                txt_week1.setVisibility( txt_week1.isShown()
                        ? View.GONE
                        : View.VISIBLE );
                break;
            case R.id.week2:
                txt_week2.setVisibility( txt_week2.isShown()
                        ? View.GONE
                        : View.VISIBLE );
                break;
            case R.id.week3:
                txt_week3.setVisibility( txt_week3.isShown()
                        ? View.GONE
                        : View.VISIBLE );
                break;
            case R.id.week4:
                txt_week4.setVisibility( txt_week4.isShown()
                        ? View.GONE
                        : View.VISIBLE );


        }

//        switch(v.getId()){
//            case R.id.week1:
//                if(txt_week1.isShown()){
//                    Fx.slide_up(this, txt_week1);
//                    txt_week1.setVisibility(View.GONE);
//                }
//                else{
//                    txt_week1.setVisibility(View.VISIBLE);
//                    Fx.slide_down(this, txt_week1);
//                }
//                break;
//            case R.id.week2:
//                if(txt_week2.isShown()){
//                    Fx.slide_up(this, txt_week2);
//                    txt_week2.setVisibility(View.GONE);
//                }
//                else{
//                    txt_week2.setVisibility(View.VISIBLE);
//                    Fx.slide_down(this, txt_week2);
//                }
//                break;
//            case R.id.week3:
//                if(txt_week3.isShown()){
//                    Fx.slide_up(this, txt_week3);
//                    txt_week3.setVisibility(View.GONE);
//                }
//                else{
//                    txt_week3.setVisibility(View.VISIBLE);
//                    Fx.slide_down(this, txt_week3);
//                }
//                break;
//            case R.id.week4:
//                if(txt_week4.isShown()){
//                    Fx.slide_up(this, txt_week4);
//                    txt_week4.setVisibility(View.GONE);
//                }
//                else{
//                    txt_week4.setVisibility(View.VISIBLE);
//                    Fx.slide_down(this, txt_week4);
//                }
//
//
//        }


    }


}

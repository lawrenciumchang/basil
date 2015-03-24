package com.lawrencium.basil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Act_PayPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__pay_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act__pay_page, menu);
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

    public void confirmPay(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your payment has been sent.");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                launchIntent();
            }});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void launchIntent(){
        Intent intent = new Intent(this, Act_TabsPage.class);
        startActivity(intent);
    }
}

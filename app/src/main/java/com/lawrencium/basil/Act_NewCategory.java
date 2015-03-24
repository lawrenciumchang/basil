package com.lawrencium.basil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Act_NewCategory extends Activity {
    protected EditText inputName;
    protected EditText inputBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        inputName = (EditText)findViewById(R.id.inputName);
        inputBudget = (EditText)findViewById(R.id.inputBudget);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_category, menu);
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

    public void createCategory(View view){
        String newName = inputName.getText().toString();
        double newBudget = Double.parseDouble(inputBudget.getText().toString());
        Category newCategory = new Category(newName, newBudget);
        Budget.getInstance().getCategories().add(newCategory);

        Intent intent = new Intent(this, Act_BudgetOverview.class);
        startActivity(intent);
    }
}

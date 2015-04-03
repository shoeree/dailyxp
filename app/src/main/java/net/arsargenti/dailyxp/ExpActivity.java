package net.arsargenti.dailyxp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.DialogPreference;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExpActivity extends ActionBarActivity {

    private String dateString;
    private int year;
    private int month;
    private int day;

    DayExpCursorAdapter dayExpCursorAdapter;

    ExpDatabaseOpenHelper expDatabaseOpenHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);
        Intent intent = getIntent();

        expDatabaseOpenHelper = new ExpDatabaseOpenHelper(getApplicationContext());
        db = expDatabaseOpenHelper.getWritableDatabase(); // TODO Should be async

        year = intent.getIntExtra(MainActivity.SELECTED_DATE_YEAR, -1);
        month = intent.getIntExtra(MainActivity.SELECTED_DATE_MONTH, -1);
        day = intent.getIntExtra(MainActivity.SELECTED_DATE_DAY, -1);
        dateString = year + "-" + month + "-" + day;

        if (year == -1 || month == -1 || day == -1) {
            throw new RuntimeException("Invalid year/month/day given to Activity from calendar: " +
                    dateString);
        }

        dayExpCursorAdapter = new DayExpCursorAdapter(getApplicationContext(), dateString, db, ExpActivity.this);

        TextView headerText = (TextView) findViewById(R.id.headerText);
        headerText.setText(dateString);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(dayExpCursorAdapter);
    }

    // User clicks the "Add" skill button.
    public void addSkill(View view) {
        String skillName = ((EditText)findViewById(R.id.enterSkillField)).getText().toString();
        if (skillName.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Cannot add an empty skill.", Toast.LENGTH_SHORT).show();
            return;
        }

        Long skillId = DatabaseComms.queryAddSkill(db, skillName);
        //Long expHistId = DatabaseComms.queryAddSkillForDate(db, skillId, dateString);
        Log.i(getClass().getName(), "INSERT INTO Skill returned " + skillId);
        if (skillId == -1) {
            Toast.makeText(getApplicationContext(),
                    "Skill already exists!", Toast.LENGTH_SHORT).show();
        } else {
            commitExpChanges();
            dayExpCursorAdapter.updateData();
        }
        ((EditText) findViewById(R.id.enterSkillField)).setText("");
    }

    // User stops the activity -- all exp values should be saved for each skill.
    // This can also happen if the user goes back to the main activity or otherwise leaves this date.
    @Override
    protected void onStop() {
        super.onStop();
        commitExpChanges();
    }

    /**
     * Commit all EXP changes to the database. This should be called whenever the activity
     * is closed, or if the data is reloaded from the db, etc. BEFORE those things happen.
     */
    protected void commitExpChanges() {
        ListView listView = (ListView) findViewById(R.id.listView);
        int numItems = listView.getCount();
        for (int i = 0; i < numItems; i++) {
            View v = listView.getChildAt(i);
            TextView rowView = (TextView)v.findViewById(R.id.rowSkillName);
            if (rowView == null) {
                continue;
            }
            String skillName = rowView.getText().toString();
            String skillExp = ((TextView) v.findViewById(R.id.rowSkillExp)).getText().toString();
            if (skillExp.isEmpty()
                    // TODO: For some reason, sqlite is reading NULLs as 0 from the DB...
                    /*|| skillExp.equals("0")*/ ) {
                Log.i(getClass().getName(), "Ignoring skill '" + skillName + "' with no EXP value.");
            } else {
                Log.i(getClass().getName(),
                        "Updating " + skillName + "'s EXP (" + skillExp + ") values: " +
                        DatabaseComms.queryAddSkillForDate(db, dateString, skillName, Integer.parseInt(skillExp)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return openSettings();
            case R.id.action_search:
                return openSearch();
            //case R.id.action_select_skill:
                //return openSelectSkill();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean openSettings() {
        // Do settingsy things here!
        Toast.makeText(getApplicationContext(),
                "Opened settings.", Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean openSearch() {
        // Do searchy things here!
        Toast.makeText(getApplicationContext(),
                "Opened search.", Toast.LENGTH_SHORT).show();
        return true;
    }

    /*public boolean openSelectSkill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_select_skill);

        // Set up spinner for selecting available skills.
        final Spinner spinner = new Spinner(getApplicationContext());
        builder.setView(spinner);

        // Populate the spinner.
        String[] from = {DatabaseContract.Skill.COL_NAME};
        int[] to = {android.R.id.text1};
        final SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),
                android.R.layout.simple_spinner_item, DatabaseComms.querySkills(db), from, to, 0);
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(simpleCursorAdapter);

        // Set up OK and Cancel buttons.
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = (Cursor)spinner.getSelectedItem();
                String value = cursor.getString(cursor.getColumnIndex(DatabaseContract.Skill.COL_NAME));
                ((EditText)findViewById(R.id.enterSkillField)).setText(value);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        return true;
    }*/
}

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExpActivity extends ActionBarActivity {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private Date date;
    private int year;
    private int month;
    private int day;

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
        String dateString = year + "-" + month + "-" + day;

        if (year == -1 || month == -1 || day == -1) {
            throw new RuntimeException("Invalid year/month/day given to Activity from calendar: " +
                    dateString);
        }

        Cursor cursor = DatabaseComms.querySkillsForDate(db, dateString);
        DayExpCursorAdapter cursorAdapter = new DayExpCursorAdapter(getApplicationContext(), cursor);

        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        try {
            this.date = dateFormatter.parse(dateString);
            TextView headerText = (TextView) findViewById(R.id.headerText);
            headerText.setText(this.date.toString());

            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(cursorAdapter);

        } catch (ParseException e) {
            System.err.println("Error parsing date string: " + dateString);
            e.printStackTrace();
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
            case R.id.action_add:
                return openAddSkill();
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

    public boolean openAddSkill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_add_skill);

        // Set up Text input field.
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up OK and Cancel buttons.
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newSkill = input.getText().toString();

                // Add the new skill name to the database.
                DatabaseComms.queryAddSkill(db, newSkill);

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
    }
}

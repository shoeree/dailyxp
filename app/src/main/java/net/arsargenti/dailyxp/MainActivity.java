package net.arsargenti.dailyxp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    public final static String SELECTED_DATE_YEAR = "net.arsargenti.dailyxp.SELECTED_DATE_YEAR";
    public final static String SELECTED_DATE_MONTH = "net.arsargenti.dailyxp.SELECTED_DATE_MONTH";
    public final static String SELECTED_DATE_DAY = "net.arsargenti.dailyxp.SELECTED_DATE_DAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(MainActivity.this, ExpActivity.class);
                intent.putExtra(SELECTED_DATE_YEAR, year);
                intent.putExtra(SELECTED_DATE_MONTH, month+1); // XXX: There's a bug in the calendar (I think) where month goes from 0->11.
                intent.putExtra(SELECTED_DATE_DAY, dayOfMonth);
                startActivity(intent);
            }
        });

    }

    public void gotoToday(View view) {
        Intent intent = new Intent(MainActivity.this, ExpActivity.class);
        final Calendar c = Calendar.getInstance();
        intent.putExtra(SELECTED_DATE_YEAR, c.get(Calendar.YEAR));
        intent.putExtra(SELECTED_DATE_MONTH, c.get(Calendar.MONTH)+1); // XXX: There's a bug in the calendar (I think) where month goes from 0->11.
        intent.putExtra(SELECTED_DATE_DAY, c.get(Calendar.DAY_OF_MONTH));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
}

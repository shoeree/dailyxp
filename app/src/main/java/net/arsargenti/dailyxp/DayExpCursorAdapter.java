package net.arsargenti.dailyxp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Sterling on 2015/03/14.
 */
public class DayExpCursorAdapter extends CursorAdapter {
    Activity parentActivity = null;
    SQLiteDatabase db = null;
    String date = null;

    public DayExpCursorAdapter(Context context, String date, SQLiteDatabase db, Activity parentActivity) {
        super(context, DatabaseComms.querySkillsForDate(db, date), 0);
        this.db = db;
        this.parentActivity = parentActivity;
        this.date = date;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_exp, parent, false);
    }

    public void updateData() {
        swapCursor(DatabaseComms.querySkillsForDate(db, date));
        DayExpCursorAdapter.this.notifyDataSetChanged();
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find the TextViews from the layout.
        final TextView rowSkillName = (TextView) view.findViewById(R.id.rowSkillName);
        final TextView rowSkillExp = (TextView) view.findViewById(R.id.rowSkillExp);

        ImageButton expUpButton = (ImageButton) view.findViewById(R.id.rowUpExp);
        expUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = rowSkillExp.getText().toString();
                Integer exp = Integer.parseInt(String.valueOf(text.isEmpty() ? 0 : text));
                exp += 1;
                rowSkillExp.setText(String.valueOf(exp));
            }
        });

        ImageButton expDownButton = (ImageButton) view.findViewById(R.id.rowDownExp);
        expDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = rowSkillExp.getText().toString();
                Integer exp = Integer.parseInt(String.valueOf(text.isEmpty() ? 0 : text));
                if (exp == 0) {
                    rowSkillExp.setText("");
                } else {
                    exp -= 1;
                    rowSkillExp.setText(String.valueOf(exp));
                }
            }
        });

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.rowRemoveSkill);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setTitle(R.string.dialog_title_delete_skill);
                final TextView textView = new TextView(parentActivity);
                textView.setText("Are you SURE you want to permanently delete this skill entirely?!");
                builder.setView(textView);

                // Set up OK and Cancel buttons.
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseComms.queryDeleteSkill(db, rowSkillName.getText().toString());
                        updateData();
                        //((ExpActivity)parentActivity).commitExpChanges();
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
            }
        });

        // Get the fields from the cursor.
        String skillName = "???";
        int idx = cursor.getColumnIndexOrThrow(DatabaseContract.Skill.COL_NAME);
        if (!cursor.isNull(idx)) {
            skillName = cursor.getString(idx);
        }

        String skillExp = "";
        idx = cursor.getColumnIndexOrThrow(DatabaseContract.ExpHist.COL_SKILL_EXP);
        if (!cursor.isNull(idx)) {
            Log.i(getClass().getName(), "skill_exp column is " + cursor.getInt(idx) + " for " + skillName);
            skillExp = String.valueOf(cursor.getInt(idx));
        } else {
            Log.i(getClass().getName(), "skill_exp column is NULL for " + skillName);
        }

        // Populate the TextViews with the data.
        rowSkillName.setText(skillName);
        rowSkillExp.setText(skillExp);
    }
}

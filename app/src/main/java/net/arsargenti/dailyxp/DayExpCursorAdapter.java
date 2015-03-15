package net.arsargenti.dailyxp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Sterling on 2015/03/14.
 */
public class DayExpCursorAdapter extends CursorAdapter {
    public DayExpCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_exp, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find the TextViews from the layout.
        TextView rowSkillName = (TextView) view.findViewById(R.id.rowSkillName);
        final TextView rowSkillExp = (TextView) view.findViewById(R.id.rowSkillExp);

        Button expUpButton = (Button) view.findViewById(R.id.rowUpExp);
        expUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = rowSkillExp.getText();
                Integer exp = Integer.parseInt(String.valueOf(text));
                exp = (exp+1 >= 0) ? exp+1 : 0;
                rowSkillExp.setText(String.valueOf(exp));
            }
        });

        Button expDownButton = (Button) view.findViewById(R.id.rowDownExp);
        expDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = rowSkillExp.getText();
                Integer exp = Integer.parseInt(String.valueOf(text));
                exp = (exp-1 >= 0) ? exp-1 : 0;
                rowSkillExp.setText(String.valueOf(exp));
            }
        });

        // Get the fields from the cursor.
        String skillName = cursor.getString(cursor.getColumnIndexOrThrow("skill_name"));
        Integer skillExp = cursor.getInt(cursor.getColumnIndexOrThrow("skill_exp"));

        // Populate the TextViews with the data.
        rowSkillName.setText(skillName);
        rowSkillExp.setText(String.valueOf(skillExp));
    }
}

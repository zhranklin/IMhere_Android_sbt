package cn.com.forthedream.imhere;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.HashSet;

import static cn.com.forthedream.imhere.R.styleable.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {
    private EditText content_note_location;
    private EditText content_note_maintain;
    private EditText content_note_type;
    private EditText content_note_time;
    private EditText content_note_title;
    private EditText content_note_update_time;
    private HashSet<EditText> editTexts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        content_note_location = (EditText) findViewById(R.id.content_note_location);
        content_note_maintain = (EditText) findViewById(R.id.content_note_maintain);
        content_note_type = (EditText) findViewById(R.id.content_note_type);
        content_note_time = (EditText) findViewById(R.id.content_note_time);
        content_note_title = (EditText) findViewById(R.id.content_note_title);
        content_note_update_time = (EditText) findViewById(R.id.content_note_update_time);
        editTexts.add(content_note_location);
        editTexts.add(content_note_maintain);
        editTexts.add(content_note_type);
        editTexts.add(content_note_time);
        editTexts.add(content_note_title);
        editTexts.add(content_note_update_time);
        for (EditText item :editTexts) {
            item.setCursorVisible(false);
            item.setFocusable(false);
            item.setFocusableInTouchMode(false);
        }
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!content_note_maintain.isFocusable()) {
                    for (EditText item : editTexts) {
                        item.setCursorVisible(true);
                        item.setFocusable(true);
                        item.setFocusableInTouchMode(true);
                    }
                    fab.setBackgroundResource(R.drawable.ic_menu_manage);
                } else {
                    for (EditText item : editTexts) {
                        item.setCursorVisible(false);
                        item.setFocusable(false);
                        item.setFocusableInTouchMode(false);
                    }
                    fab.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
                }
            }
        });
    }

}

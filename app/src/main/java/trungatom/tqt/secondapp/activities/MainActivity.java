package trungatom.tqt.secondapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.List;

import trungatom.tqt.secondapp.adapters.ToeicExpandableListView;
import trungatom.tqt.secondapp.database.DatabaseUtils;
import trungatom.tqt.secondapp.R;
import trungatom.tqt.secondapp.models.CategoryModel;
import trungatom.tqt.secondapp.models.TopicModel;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView lvTopics;
    private ToeicExpandableListView toeicExpandableListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTopics = findViewById(R.id.lv_topic);
        List<TopicModel> topicModels = DatabaseUtils.getInstance(this).getListTopic();
        List<CategoryModel> categoryModels = DatabaseUtils.getInstance(this).getListCategory(topicModels);
        HashMap<String, List<TopicModel>> hashMap = DatabaseUtils.getInstance(this).
                getHashMapTopic(topicModels, categoryModels);

        toeicExpandableListView = new ToeicExpandableListView(categoryModels, hashMap);
        lvTopics.setAdapter(toeicExpandableListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toeicExpandableListView.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}

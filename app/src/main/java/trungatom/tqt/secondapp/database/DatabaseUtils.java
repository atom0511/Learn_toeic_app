package trungatom.tqt.secondapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import trungatom.tqt.secondapp.models.CategoryModel;
import trungatom.tqt.secondapp.models.TopicModel;
import trungatom.tqt.secondapp.models.WordModel;

public class DatabaseUtils {

    private final String TABLE_TOPIC = "tbl_topic";
    private final String TABLE_WORD = "tbl_word";

    private SQLiteDatabase sqLiteDatabase;
    private MyDatabase myDatabase;

    //Singleton
    private static DatabaseUtils databaseUtils;

    private DatabaseUtils(Context context) {
        myDatabase = new MyDatabase(context);
    }

    public static DatabaseUtils getInstance(Context context) {
        if (databaseUtils == null) {
            databaseUtils = new DatabaseUtils(context);
        }
        return databaseUtils;
    }

    public List<TopicModel> getListTopic() {
        sqLiteDatabase = myDatabase.getReadableDatabase();
        List<TopicModel> topicModels = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_TOPIC, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            //read data
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String category = cursor.getString(4);
            String color = cursor.getString(5);
            String lastTime = cursor.getString(6);

            TopicModel topicModel = new TopicModel(id, name, category, color, lastTime);
            topicModels.add(topicModel);

            //next
            cursor.moveToNext();

        }
        Log.d("atom", "getListTopic: " + topicModels);

        return topicModels;
    }

    public List<CategoryModel> getListCategory(List<TopicModel> topicModels) {
        List<CategoryModel> categoryModelist = new ArrayList<>();
        for (int i = 0; i < topicModels.size(); i += 5) {
            CategoryModel categoryModel = new CategoryModel(
                    topicModels.get(i).getCategory(),
                    topicModels.get(i).getColor()
            );
            categoryModelist.add(categoryModel);
        }
        return categoryModelist;
    }

    public HashMap<String, List<TopicModel>> getHashMapTopic(List<TopicModel> topicModels, List<CategoryModel> categoryModels) {
        HashMap<String, List<TopicModel>> hashMap = new HashMap<>();

        for (int i = 0; i < categoryModels.size(); i++) {
            int positionTopic = i * 5;
            hashMap.put(categoryModels.get(i).getName(),
                    topicModels.subList(positionTopic, positionTopic + 5));
            Log.d("atom1", "getHashMapTopic: -------" + categoryModels.get(i).getName());
            Log.d("atom1", "getHashMapTopic: " + hashMap.get(categoryModels.get(i).getName()));
        }
        return hashMap;
    }

    public WordModel getRandomWord(int topicId, int preWordId) {
        sqLiteDatabase = myDatabase.getReadableDatabase();

        int level = 0;
        Cursor cursor;
        do {
            double random = Math.random() * 100;
            if (random <= 5) level = 4;
            else if (random <= 25) level = 3;
            else if (random <= 30) level = 2;
            else if (random <= 55) level = 1;
            else level = 0;

            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_WORD
                    + " WHERE topic_id = " + topicId
                    + " AND LEVEL = " + level
                    + " AND id<> " + preWordId +
                    " ORDER BY RANDOM() LIMIT 1", null);
        } while (cursor.getCount() == 0);

        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String origin = cursor.getString(1);
        String explanation = cursor.getString(2);
        String type = cursor.getString(3);
        String pronunciation = cursor.getString(4);
        String imageURL = cursor.getString(5);
        String example = cursor.getString(6);
        String example_trans = cursor.getString(7);

        return new WordModel(id, origin, explanation, type, pronunciation, imageURL,
                example, example_trans, id, level);
    }

    public void updateWordLevel(WordModel wordModel, boolean isKnown){
        sqLiteDatabase = myDatabase.getWritableDatabase();
        int level = wordModel.getLevel();
        if(isKnown && level < 4){
            level++;
        }else if (isKnown && level > 0){
            level--;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("level", level);
        sqLiteDatabase.update(TABLE_WORD, contentValues, "id = " + wordModel.getId(), null);
    }
    public int getNumberOfNewWordByTopicId(int topicId){
        sqLiteDatabase = myDatabase.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_WORD +
                " WHERE level  = 0 and topic_id = " + topicId, null);
        return cursor.getCount();
    }
    public int getNumberOfMasterByTopicId(int topicId){
        sqLiteDatabase = myDatabase.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_WORD +
                " WHERE level  = 4 and topic_id = " + topicId, null);
        return cursor.getCount();
    }

    public void updateLastTime(TopicModel topicModel, String lastTime){
        sqLiteDatabase = myDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_time", lastTime);
        sqLiteDatabase.update(TABLE_TOPIC, contentValues, "id = " + topicModel.getId(), null);
    }

    public String getLastTimeByTopicId(int topicId){
        sqLiteDatabase = myDatabase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT last_time FROM " + TABLE_TOPIC +
                " WHERE id = " + topicId, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
}

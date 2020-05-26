package trungatom.tqt.secondapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trungatom.tqt.secondapp.R;
import trungatom.tqt.secondapp.background.ReminderService;

public class SettingActivity extends AppCompatActivity {

    public static final String TIME_REMINDER_KEY = "time_reminder_key";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.sw_reminder)
    Switch swReminder;
    @BindView(R.id.tv_time_reminder)
    TextView tvTimeReminder;
    @BindView(R.id.iv_done)
    ImageView ivDone;

    String time;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        time = sharedPreferences.getString(TIME_REMINDER_KEY, null);
        if (time != null) {
            tvTimeReminder.setText(time);
            swReminder.setChecked(true);
        } else {
            tvTimeReminder.setVisibility(View.GONE);
        }

        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvTimeReminder.setVisibility(View.VISIBLE);
                } else {
                    tvTimeReminder.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_done, R.id.tv_time_reminder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_done:
                saveSetting();
                break;
            case R.id.tv_time_reminder:
                setTimeReminder();
                break;
        }
    }

    public void saveSetting() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(time == null) time = "00:00";

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3, 5)));
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, ReminderService.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (swReminder.isChecked()) {
            Log.d("debug2", "saveSetting: 22222");
            editor.putString(TIME_REMINDER_KEY, time);
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        } else {
            editor.putString(TIME_REMINDER_KEY, null);
            alarmManager.cancel(pendingIntent);
        }
        editor.commit();
        Toast.makeText(this, "Setting saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void setTimeReminder() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = String.format("%02d:%02d", hourOfDay, minute);
                tvTimeReminder.setText(time);
            }
        }, currentHour, currentMin, true);

        timePickerDialog.show();
    }
}

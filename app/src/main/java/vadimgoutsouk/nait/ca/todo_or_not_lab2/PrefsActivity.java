package vadimgoutsouk.nait.ca.todo_or_not_lab2;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PrefsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        String bgColor = settings.getString("main_bg_color_list", "#cccccc");
        this.getListView().setBackgroundColor(Color.parseColor(bgColor));
        this.finish();
    }
}

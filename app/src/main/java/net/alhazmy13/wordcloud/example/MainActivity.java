package net.alhazmy13.wordcloud.example;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import net.alhazmy13.example.R;
import net.alhazmy13.wordcloud.ColorTemplate;
import net.alhazmy13.wordcloud.WordCloud;
import net.alhazmy13.wordcloud.WordCloudView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    List<WordCloud> list ;
    String text = "الأندرويد  هو نظام تشغيل مجاني، طورته شركة جوجل  وهو نظام تشغيل للأجهزة التي تحتوي على شاشات اللمس كالهواتف الذكية  والأجهزة اللوحية   يقوم على تطوير الأندرويد  عدد كبير من المطورين والمبرمجين الذين يعتمدون على لغة الجافا   لتطوير برامجهم. يعد نظام الأندرويد  نظام التشغيل الاوسع انتشاراً، نظراً لما يتميز به من مميزات، جعلته مفضلاً عند العديد من النَّاس، منها";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateRandomText();
        final RelativeLayout relativeLayout = this.findViewById(R.id.activity_main);
        final WordCloudView wordCloud = findViewById(R.id.wordCloud);
        wordCloud.setDataSet(list);
        wordCloud.setColors(ColorTemplate.MATERIAL_COLORS);
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                wordCloud.notifyDataSetChanged();
            }
        });
    }

    private void generateRandomText() {
        String[] data = text.split(" ");
        list = new ArrayList<>();
        Random random = new Random();
        for (String s : data) {
            list.add(new WordCloud(s,random.nextInt(50)));
        }
    }
}



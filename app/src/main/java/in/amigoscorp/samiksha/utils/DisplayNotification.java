package in.amigoscorp.samiksha.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import org.apache.commons.lang3.StringUtils;

import in.amigoscorp.samiksha.R;
import in.amigoscorp.samiksha.activities.MainActivity;

/**
 * Created by sriny on 23/02/17.
 */

public class DisplayNotification extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final String imageUrl;
    private final String title;
    private final String text;
    private final String language;

    public DisplayNotification(Context context, String title, String text, String imageUrl, String language) {
        this.context = context;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.language = language;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int notificationCount = CommonUtils.readFromSharedPreferences(context, R.string.notification_count);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("language", language);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_movie_filter)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setSubText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        if (StringUtils.isNotBlank(imageUrl)) {
            Bitmap bitmap = CommonUtils.getBitmapFromImageUrl(imageUrl);
            if (bitmap != null) {
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
            }
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationCount, notificationBuilder.build());
        notificationCount++;
        CommonUtils.writeToSharedPreferences(context, R.string.notification_count, notificationCount);
        return null;
    }
}
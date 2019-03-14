package com.omninos.gwappprovider.FirebaseClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.activities.RequestActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String UserName, Status,UserServices, PaymentType, ProblemDetail, UserImage, LAtitude, Longitude, Message, title, id, address, phone;

    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 6578;
    NotificationChannel mChannel;
    Notification notification;
    Uri defaultSound;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Message = remoteMessage.getData().get("message");
        PaymentType = remoteMessage.getData().get("payment");
        UserImage = remoteMessage.getData().get("userImage");
        LAtitude = remoteMessage.getData().get("locationLatitude");
        Longitude = remoteMessage.getData().get("locationLongitude");
        title = remoteMessage.getData().get("title");
        UserName = remoteMessage.getData().get("userName");
        UserServices = remoteMessage.getData().get("service");
        ProblemDetail = remoteMessage.getData().get("comments");
        id = remoteMessage.getData().get("bookingServiceId");
        address = remoteMessage.getData().get("address");
        phone = remoteMessage.getData().get("userPhone");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setOreoNotification(title, Message, PaymentType, UserImage, LAtitude, Longitude, UserName, UserServices, ProblemDetail, id, address, phone);
            this.startForegroundService(new Intent(this, RequestActivity.class));
        } else {
            showNotification(title, Message, PaymentType, UserImage, LAtitude, Longitude, UserName, UserServices, ProblemDetail, id, address, phone);
            this.startService(new Intent(this, RequestActivity.class));
        }
    }

    private void showNotification(String title, String message, String paymentType, String userImage, String lAtitude, String longitude, String userName, String userServices, String problemDetail, String id, String address, String phone) {


        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("paymentType", paymentType);
        intent.putExtra("userImage", userImage);
        intent.putExtra("lAtitude", lAtitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("userName", userName);
        intent.putExtra("userServices", userServices);
        intent.putExtra("problemDetail", problemDetail);
        intent.putExtra("id", id);
        intent.putExtra("address", address);
        intent.putExtra("phone", phone);
        intent.putExtra("Status","Notification");
        //startActivity(intent);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentText(message)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void setOreoNotification(String title, String message, String paymentType, String userImage, String lAtitude, String longitude, String userName, String userServices, String problemDetail, String id, String address, String phone) {


        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("paymentType", paymentType);
        intent.putExtra("userImage", userImage);
        intent.putExtra("lAtitude", lAtitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("userName", userName);
        intent.putExtra("userServices", userServices);
        intent.putExtra("problemDetail", problemDetail);
        intent.putExtra("id", id);
        intent.putExtra("address", address);
        intent.putExtra("phone", phone);
        intent.putExtra("Status","Notification");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Sets an ID for the notification, so it can be updated.
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "ove";// The user-visible name of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

// Create a notification and set the notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setSound(defaultSound)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();
        }
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }

// Issue the notification.
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

}
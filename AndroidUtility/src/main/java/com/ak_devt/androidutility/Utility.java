package com.ak_devt.androidutility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by neha.saini on 5/31/2016.
 */
public class Utility {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenWidth(Context context) {
        int screenWidth = 0;

        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        int screenHeight = 0;

        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }


    public static int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }


    public static void hideKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }


    }

    public static void hideKeyboadOnView(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(
                view.getWindowToken(), 0);

       /* inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);*/
    }

    public static int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int convertPixelsToSp(float px, Context context) {

//        float sp = px / getResources().getDisplayMetrics().scaledDensity;

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / ((float) metrics.scaledDensity / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context
                .getResources().getDisplayMetrics());
        return px;
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }


    public static void setEditTextFocusWithEnd(EditText editText) {
        editText.requestFocus();
        editText.setSelection(editText.length());
    }

    //Time for Chat Onlu
    public static Date ChatconvertLocalToUTC(String date, String localTimein24) {

        long ts = System.currentTimeMillis();
        Date localTime = new Date(ts);

        String format = "dd MMM yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        try {
            localTime = simpleDateFormat.parse(date + " " + localTimein24);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        // Convert Local Time to UTC (Works Fine)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmtTime = new Date(sdf.format(localTime));
        System.out.println("Local:" + localTime.toString() + "," + localTime.getTime() + " --> " +
                "UTC time:"
                + gmtTime.toString() + "," + gmtTime.getTime());
        return gmtTime;
    }

    public static String ChatreturnLocaltoUTCDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");//dd-MM-yyyy

        String dateinYYYYMMDD = simpleDateFormat.format(date);
        return dateinYYYYMMDD;
    }

    public static String ChatreturnLocaltoUTCTime24(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        //K:mma
        //HH:mm:ss
        String timein24 = simpleDateFormat.format(date);
        return timein24;
    }


    public static File convertBitmaptoFile(Context context, String filename, Bitmap bitmapImage) {
        String filenameNew = UUID.randomUUID().toString();
        //create a file to write bitmap data
        File imageFile;
        imageFile = new File(context.getCacheDir(), filenameNew + ".png");
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//Convert bitmap to byte array
        Bitmap bitmap = bitmapImage;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    public static File setImageFromUri(Context mContext, Uri uri) {
        Bitmap bitmap = null;
        Uri imgUri = uri;
        File sourceFile = null;
        try {

            Matrix matrix = new Matrix();
            String path = Utility.getRealPathFromURI(mContext, imgUri);
            bitmap = Utility.getBitmap(path, 1024, 1024);
            matrix.postRotate(Utility.getImageOrientation(path + ""));
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            imageView.setImageBitmap(rotatedBitmap);

            Uri temp = Utility.getImageUri(mContext, rotatedBitmap);
            String temppath = Utility.getRealPathFromURI(mContext, temp);
            sourceFile = new File(temppath);


        } catch (OutOfMemoryError e) {

            e.printStackTrace();
        }

        return sourceFile;
    }

    public static File getImage(Context mContext, String path) {
        Bitmap bitmap = null;
        File sourceFile = null;
        try {

            Matrix matrix = new Matrix();
            matrix.postRotate(Utility.getImageOrientation(path + ""));

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(path, bmOptions);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
                    .getHeight(), matrix, true);

            Uri temp = Utility.getImageUri(mContext, rotatedBitmap);
            String temppath = Utility.getRealPathFromURI(mContext, temp);
            sourceFile = new File(temppath);


        } catch (OutOfMemoryError e) {

            e.printStackTrace();
        }

        return sourceFile;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = context.getContentResolver().query(contentUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            return cursor.getString(columnIndex);
        } catch (Exception e) {
            return contentUri.getPath();
//            return null;
        }

    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap getBitmap(String pathOfInputImage, int dstWidth, int dstHeight) {
        Bitmap resizedBitmap = null;
        try {
            int inWidth = 0;
            int inHeight = 0;

            InputStream in = new FileInputStream(pathOfInputImage);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(pathOfInputImage);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / dstWidth, inHeight / dstHeight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() *
                    values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
//
            // save image
            return resizedBitmap;
        } catch (IOException e) {

        }
        return resizedBitmap;
    }

    //    public static int getScreenWidth(Context context) {
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context
//                .WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
//        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
//        return outMetrics.widthPixels;
//    }
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    public static String toTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getMimeType(String url) {
        String extension = "";
        int i = url.lastIndexOf('.');
        if (i > 0) {
            extension = url.substring(i + 1).toLowerCase();
        }
        String type = null;
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (type != null && type.contains("text")) {
                type = "application/msword";
            }
        }
        return type;
    }

    public static String getFileNameFromPath(String path) {

        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String splitAndGetDomain(String email) {

        String[] domain = email.split("\\@");
        return domain[1];
    }

    public static String splitAndGetFirstName(String name) {
        String[] nameList = name.split(" ", 2);
        return (nameList.length > 1) ? nameList[0] : name;
    }

    public static String splitAndGetLastName(String name) {
        String[] nameList = name.split(" ", 2);
        return (nameList.length > 1) ? nameList[1] : " ";
    }

    public static String getFileDuration(String filePath) {
        Log.d("filePath", filePath);
        String out = "";

        if (new File(filePath).exists()) {
            // load data file
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(filePath);


            // get mp3 info

            // convert duration to minute:seconds
            String duration =
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Log.v("time", duration);
            long dur = Long.parseLong(duration);

            out = getDurationBreakdownHHMMSS(dur);

            // close object
            metaRetriever.release();
        } else {
            out = "File not Found";
        }

        return out;
    }

    public static String getTimeFormatFromSecond(int second) {


//        String out = "";
//        // get mp3 info
//
//        String seconds = String.valueOf((second % 60));
//        String minutes = String.valueOf(second / 60);
//        out = minutes + ":" + seconds;
//
//        Log.v("minutes", out);

        if (second < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.SECONDS.toDays(second);
        second -= TimeUnit.DAYS.toSeconds(days);
        long hours = TimeUnit.SECONDS.toHours(second);
        second -= TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(second);
        second -= TimeUnit.MINUTES.toSeconds(minutes);
        long seconds = TimeUnit.SECONDS.toSeconds(second);

        String time = String.format("%02d", hours) + ":" +
                String.format("%02d", minutes) + ":" + String.format
                ("%02d", seconds);

        // close object
        return time;
    }




    public static void clearAllNotification(Context mContext) {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    //Read more about HTML Flag here : https://developer.android.com/reference/android/text/Html
    // .html#FROM_HTML_MODE_COMPACT
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }


    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdownHHMMSS(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

//        long days = TimeUnit.MILLISECONDS.toDays(millis);
//        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
//
//        StringBuilder sb = new StringBuilder(64);
//        sb.append(days);
//        sb.append(" Days ");
//        sb.append(hours);
//        sb.append(" Hours ");
//        sb.append(minutes);
//        sb.append(" Minutes ");
//        sb.append(seconds);
//        sb.append(" Seconds");

//        btHighlightTimer.setText(String.format("%02d", hourHighlight) + ":" +
//                String.format("%02d", minutesHighlight) + ":" + String.format
//                ("%02d", secondsHighlight));


        String time = String.format("%02d", hours) + ":" +
                String.format("%02d", minutes) + ":" + String.format
                ("%02d", seconds);


//        return (sb.toString());
        return (time);
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdownMMSS(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

//        long days = TimeUnit.MILLISECONDS.toDays(millis);
//        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
//
//        StringBuilder sb = new StringBuilder(64);
//        sb.append(days);
//        sb.append(" Days ");
//        sb.append(hours);
//        sb.append(" Hours ");
//        sb.append(minutes);
//        sb.append(" Minutes ");
//        sb.append(seconds);
//        sb.append(" Seconds");

//        btHighlightTimer.setText(String.format("%02d", hourHighlight) + ":" +
//                String.format("%02d", minutesHighlight) + ":" + String.format
//                ("%02d", secondsHighlight));


        String time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" +
                String.format("%02d", seconds);

//        return (sb.toString());
        return (time);
    }

    public static String getDateFromTimeStamp(long timestamp, int flag) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp);
            //calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm aa");
            Date currenTimeZone = calendar.getTime();
            if (flag == 1) {
                return sdfTime.format(currenTimeZone);
            } else {
                return sdf.format(currenTimeZone);
            }

        } catch (Exception e) {
        }
        return "";
    }

    public static String getDateFromTimeStamp24(long timestamp, int flag) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp);
            //calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            Date currenTimeZone = calendar.getTime();
            if (flag == 1) {
                return sdfTime.format(currenTimeZone);
            } else {
                return sdf.format(currenTimeZone);
            }

        } catch (Exception e) {
        }
        return "";
    }

    public static String getProperCount(int likeCount) {
        if (likeCount == 0)
            return "";
        else
            return likeCount + "";

    }


    public static String generateRandomUUIDNumber() {
        return UUID.randomUUID().toString();
    }

    public static int getRandomId() {
        return (int) (Math.random() * 50 + 1);

    }

    public static void cancelNotification(Context mContext, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService
                (Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    public static long ChatconvertDateinTimestamp(String chatDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        if (chatDate == null) {
            return 0;
        }
        try {
            date = formatter.parse(chatDate);
        } catch (java.text.ParseException e) {
            date = null;
            e.printStackTrace();
        }
        long millis;
        if (date != null) {
            millis = date.getTime();
        } else {
            millis = 0;
        }
        return millis;
    }

    /*public static String getFormattedDate(String inputDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, YYYY");
        Date date = null;

        date = simpleDateFormat.format(inputDate);

    }*/

    public static String parseDateToMMMddyyyy(String time) {
        String inputPattern = "MM/dd/yyyy";
        String outputPattern = "MMM dd,yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean isDeviceNouget() {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 24)
            return true;
        else
            return false;
    }

//        public static boolean isMyServiceRunning(Context context,Class serviceClass) {
//        ActivityManager manager = (ActivityManager) context.getSystemService(serviceClass);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer
// .MAX_VALUE)) {
//            if ("com.example.MyService".equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }


//    public static boolean isMyServiceRunning(Context context, Class serviceClass) {
////        ActivityManager activityManager = (ActivityManager) ((Activity)context).getSystemService
////                (serviceClass);
////
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
// .ACCOUNT_SERVICE);
//
//        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices
//                (Integer.MAX_VALUE);
//
//        if (serviceList.size() <= 0) {
//            Log.d("serviceList", serviceList.size() + "");
//            return false;
//        }
//        for (int i = 0; i < serviceList.size(); i++) {
//            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
//            ComponentName serviceName = serviceInfo.service;
//            if (serviceName.getClassName().equals(WatsonToDBService.class.getName())) {
//                Log.d("isMyServiceRunning", "true");
//
//                return true;
//            }
//        }
//
//        return false;
//    }


    public static boolean isMyServiceRunning(Context mContext, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context
                .ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer
                .MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d("isMyServiceRunning", "true");
                return true;
            }
        }
        return false;
    }


    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    /**
     * @param timeStamp
     * @param format input date format like "yyyy-MM-dd"
     * @return
     */

    public static String convertTimestampToDate(long timeStamp,String format) {
        DateFormat objFormatter = new SimpleDateFormat(format);
        objFormatter.setTimeZone(TimeZone.getDefault());
        Calendar objCalendar = Calendar.getInstance(TimeZone.getDefault());
        objCalendar.setTimeInMillis(timeStamp * 1000);// edit
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;
    }

    /**
     * @param Date in string
     * @param format input date format lik "yyyy-MM-dd"
     * @return
     */
    public static long getDateToTimestamp(String Date,String format) {
        Calendar c = Calendar.getInstance();
        // System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(format);
        // String currentDate = df.format(c.getTime());
        Date currentDateObj = null;
        try {
            currentDateObj = df.parse(Date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        long currentDateTimeStamp = currentDateObj.getTime() / 1000L;
        return currentDateTimeStamp;

    }
}

package io.lutics.app.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.View;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import io.lutics.app.android.BuildConfig;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * @author Sunny-J
 * @description Utilities
 */
public class Utils {

    // Log Tag
    private static final String LOG_TAG = Utils.class.getName();

    /**
     * @author Sunny-J
     * @description Check Network is Available. If Available, return true
     */
    public static boolean isConnected(Context context) {
        // NetWork Connection Check
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean status3G = false;
        boolean statusWiFi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        boolean statusWiMax = false;

        try {
            status3G = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
        } catch (NullPointerException e) {
            status3G = false;
        }

        try {
            statusWiMax = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX).isConnected();
        } catch (NullPointerException e) {
            statusWiMax = false;
        }

        return status3G | statusWiFi | statusWiMax;
    }

    /**
     * @author Sunny-J
     * @description Check GPS or Network Location is Available. If Available, return true
     */
    public static boolean isLocationService(Context context) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * @author Sunny-J
     * @description Check Application is Installed with Package Name. If Installed in System, return true
     */
    public static boolean isApplicationExist(Context context, String packageName) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(packageName)) {
                Logger.w(LOG_TAG, packageName + " is Detected");
                return true;
            }
        }

        return false;
    }

    /**
     * @author Sunny-J
     * @description Check Application has granted Permission
     */
    public static boolean isApplicationPermissionGranted(Context context, String permission) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        return (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * @author Sunny-J
     * @description Get Translate DP size from PX
     */
    public static float getDPfromPX(Context context, int dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * @author Sunny-J
     * @description Get Translate PX size from DP
     */
    public static float getPXfromDP(Context context, int pxValue) {
        return pxValue / context.getResources().getDisplayMetrics().density;
    }

    /**
     * @author Sunny-J
     * @description Get Device's UUID
     */
    public static String getDeviceUUID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String deviceId = "" + telephonyManager.getDeviceId();
        String simSerialNumber = "" + telephonyManager.getSimSerialNumber();
        String androidId = "" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) deviceId.hashCode() << 32) | simSerialNumber.hashCode());

        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + deviceUuid.toString());

        return deviceUuid.toString();
    }

    /**
     * @author Sunny-J
     * @description Check Version. if True, Newer is Exist
     */
    public static boolean isVersionUpdated(String releaseVersion) {
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + releaseVersion);

        int current = Integer.parseInt(BuildConfig.VERSION_NAME.replaceAll("\\.", "").substring(0, 3));
        int release = Integer.parseInt(releaseVersion.replaceAll("\\.", "").substring(0, 3));

        return current < release;
    }

    /**
     * @author Sunny-J
     * @description Get Device's Mac-Address
     */
    public static String getMacAddress(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 17; i = i + 3) {
                sb.append(mWifiManager.getConnectionInfo().getMacAddress().substring(i, i + 2));
            }
            Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + sb.toString());
            return sb.toString();
        } catch (NullPointerException e) {
            Logger.w(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " - null");
            return "";
        }
    }

    /**
     * @author Sunny-J
     * @description Get Encoded URI Like 'Javascript' encodedURIComponent
     */
    public static String getStringEncodeURIComponent(String plainString) throws UnsupportedEncodingException {
        return URLEncoder.encode(plainString, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");
    }

    /**
     * @author Sunny-J
     * @description Get Escaped String
     */
    public static String getStringEscaped(String string) {
        int i;
        char j;
        StringBuilder tmp = new StringBuilder();
        tmp.ensureCapacity(string.length() * 6);

        for (i = 0; i < string.length(); i++) {
            j = string.charAt(i);

            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16) tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }

        return tmp.toString();
    }

    /**
     * @author Sunny-J
     * @description Get Unescaped String
     */
    public static String getStringUnescaped(String src) {
        StringBuilder tmp = new StringBuilder();
        tmp.ensureCapacity(src.length());

        int lastPos = 0, pos = 0;
        char ch;

        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }

        return tmp.toString();
    }

    /**
     * @author Sunny-J
     * @description Get Substitute String With Star
     */
    public static String getStringSubstituteWithStar(String id) {
        if (id.length() < 3) return "***";
        else return id.substring(0, id.length() - 3).concat("***");
    }

    /**
     * @author Sunny-J
     * @description Get Substitute String With Comma
     */
    public static String getStringSubstituteWithComma(String str) {
        String temp = getStringReversed(str);
        String result = "";

        for (int i = 0; i < temp.length(); i += 3) {
            if (i + 3 < temp.length()) {
                result += temp.substring(i, i + 3) + ",";
            } else {
                result += temp.substring(i);
            }
        }

        return getStringReversed(result);
    }

    /**
     * @author Sunny-J
     * @description Get Reversed String
     */
    private static String getStringReversed(String s) {
        return new StringBuffer(s).reverse().toString();
    }

    /**
     * @throws java.io.IOException
     * @author Sunny-J
     * @description Get Compressed String
     */
    public static byte[] getStringCompressed(String plaintext) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(plaintext.getBytes());
        gzip.close();

        return out.toByteArray();
    }

    /**
     * @throws java.io.IOException
     * @author Sunny-J
     * @description Get Decompressed String
     */
    public static String getStringDecompressed(byte[] bytes) throws IOException {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String outStr = "";
        String line;
        while ((line = bf.readLine()) != null) {
            outStr += line;
        }

        return outStr;
    }

    /**
     * @author Sunny-J
     * @description Get SHA-256 Encrypted String
     */
    public static String getStringSHA256Encrypted(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes());

            byte byteData[] = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte aByteData : byteData) {
                sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            }

            String result = sb.toString();

            Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + result);

            return result;
        } catch (NoSuchAlgorithmException e) {
            return plainText;
        }
    }

    /**
     * @author Sunny-J
     * @description Get View With Bitmap
     */
    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getWidth(), v.getHeight());
        v.draw(c);

        return b;
    }

    /**
     * @author Sunny-J
     * @description Set Bitmap to File
     */
    public static File getFileFromBitmap(Bitmap bitmap) {
        File file = null;
        FileOutputStream outputStream = null;

        try {
            file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".png");
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
            }
        }

        return file;
    }

    /**
     * @author Sunny-J
     * @description Get Byte From File
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;

        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        } else {
            is.close();
        }

        return bytes;
    }
}
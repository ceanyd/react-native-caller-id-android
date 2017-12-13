package com.callerid.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.text.Editable;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import java.util.Hashtable;
import java.util.Random;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import javax.crypto.CipherOutputStream;
import android.util.Base64;
import android.util.Log;
import android.security.KeyPairGeneratorSpec;
import 	java.security.KeyPair;
import 		java.security.KeyPairGenerator;
import javax.security.auth.x500.X500Principal;
import java.security.interfaces.RSAPrivateKey;
import 	javax.crypto.CipherInputStream;
import 	javax.crypto.KeyGenerator;
import 	android.security.keystore.KeyProperties;
import 	android.security.keystore.KeyGenParameterSpec;
import javax.crypto.SecretKey;
import 	javax.crypto.spec.GCMParameterSpec;
import 	android.content.SharedPreferences;
//import 	org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.util.Set;
import java.lang.Exception;
import java.security.KeyStore;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import android.util.Base64;
import java.math.BigInteger;

import android.widget.Toast;

@Database(entities = {User.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    private static DataBase INSTANCE;
    public abstract UserDAO userDao();
    private static final String keyStoreInstance = "AndroidKeyStore";
    private static final String cipherInstance = "AES/GCM/NoPadding";
    private static SharedPreferences prefs = null;

//    private final static String generateRandomChars(String candidateChars, int length) {
//        StringBuilder sb = new StringBuilder();
//        Random random = new Random();
//        for (int i = 0; i < length; i++) {
//            sb.append(candidateChars.charAt(random.nextInt(candidateChars
//                    .length())));
//        }
//
//        return sb.toString();
//    }

    private static void encryptString(Context ctx, String alias, String text) {
        try {
            final KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, keyStoreInstance);

            final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();

            keyGenerator.init(keyGenParameterSpec);
            final SecretKey secretKey = keyGenerator.generateKey();

            Cipher cipher = Cipher.getInstance(cipherInstance);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();
            byte[] encryption = cipher.doFinal(text.getBytes("UTF-8"));

            String ivStr = Base64.encodeToString(iv, Base64.DEFAULT);
            String encStr = Base64.encodeToString(encryption, Base64.DEFAULT);

            prefs.edit().putString("hash", encStr).commit();
            prefs.edit().putString("salt", ivStr).commit();
            prefs.edit().putBoolean("first", false).commit();
        } catch (Exception e) {
            Toast.makeText(ctx, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e("CREATE", Log.getStackTraceString(e));
        }
    }


    public static String decryptString(Context ctx, String alias) {
        String str = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreInstance);
            keyStore.load(null);

            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null);

            SecretKey secretKey = secretKeyEntry.getSecretKey();

            String strIV = prefs.getString("salt", null);
            String strEnc = prefs.getString("hash", null);

            byte[] ivB = Base64.decode(strIV, Base64.DEFAULT);
            byte[] encB = Base64.decode(strEnc, Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance(cipherInstance);
            GCMParameterSpec spec = new GCMParameterSpec(128, ivB);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] decodedData = cipher.doFinal(encB);

            str = new String(decodedData, "UTF-8");

        } catch (Exception e) {
            Toast.makeText(ctx, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e("Exception", Log.getStackTraceString(e));
        }
        return str;
    }

    public static DataBase getDatabase(Context context, String passPhrase) {
        try {
            PackageManager pm = context.getPackageManager();
            String pn = context.getPackageName().toString();
            ApplicationInfo appInfo = pm.getApplicationInfo(pn, 0);
            String appFile = appInfo.sourceDir;
            long installed = new File(appFile).lastModified();

            prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

            if (prefs.getBoolean("first", true) && null != passPhrase) {
                encryptString(context, pn + "_" + installed, passPhrase);
            }

            KeyStore keyst = KeyStore.getInstance(keyStoreInstance);
            keyst.load(null, null);

            List<String> keystlist = Collections.list(keyst.aliases());

            String str = decryptString(context, pn + "_" + installed);
            if (INSTANCE == null) {
                SafeHelperFactory factory=SafeHelperFactory.fromUser(Editable.Factory.getInstance().newEditable(str));
                INSTANCE = Room.databaseBuilder(context, DataBase.class, "users").openHelperFactory(factory).allowMainThreadQueries().build();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
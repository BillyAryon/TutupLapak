package com.p3lb.tutuplapak;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Config {
    public static final String BASE_URL = "https://tutuplapakpbp.000webhostapp.com/";
    // URL Image
    public static final String IMAGES_URL = "https://tutuplapakpbp.000webhostapp.com/assets/files/image/";
    public static final int REQUEST_PICK_PHOTO = 2;
    public static final int REQUEST_WRITE_PERMISSION = 786;
    public static final int ALERT_DIALOG_CLOSE = 10;
    public static final int ALERT_DIALOG_DELETE = 20;
    public static final String INSERT_FLAG = "INSERT";
    public static final String UPDATE_FLAG = "UPDATE";


}

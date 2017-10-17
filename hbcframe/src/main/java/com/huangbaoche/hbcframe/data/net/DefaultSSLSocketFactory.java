package com.huangbaoche.hbcframe.data.net;

import android.content.Context;
import android.net.SSLCertificateSocketFactory;

import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.util.Common;
import com.huangbaoche.hbcframe.util.MLog;
import com.leon.channel.helper.ChannelReaderUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by admin on 2015/10/3.
 */
public class DefaultSSLSocketFactory extends SSLCertificateSocketFactory {
    private  SSLContext sslContext = SSLContext.getInstance("TLS");
    private static KeyStore trustStore;
    private static DefaultSSLSocketFactory instance;

    private static String keystorepw = "";//32
    private static String keypw = "";//6
    static String channelNum = null;
    public static void resetSSLSocketFactory(Context context) {
        try {
            try {
                instance = new DefaultSSLSocketFactory();;
                keystorepw = Common.getKeyStorePsw(context);
                keypw = "123";//Common.getClientP12Key(context);
                long time = System.currentTimeMillis();
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                InputStream ins;
                if(channelNum.equals("10007")){
                    keystorepw = "6154acb614e80a42fc85509980ff3ea5";
                    ins = context.getResources().getAssets().open("clientgp.keystore");
                }else{
                    ins = context.getResources().getAssets().open("client.keystore");
                }
                trustStore.load(ins, keystorepw.toCharArray());
                MLog.e("trustStore load time = " + (System.currentTimeMillis() - time));
            } catch (Throwable var1) {
                MLog.e(var1.getMessage(), var1);
            }
        } catch (Throwable var1) {
            MLog.e(var1.getMessage(), var1);
        }
    }

    public static DefaultSSLSocketFactory getSocketFactory(Context context) {
        if(instance == null) {
            try {
                try {
                    keystorepw = Common.getKeyStorePsw(context);
                    keypw = "123";//Common.getClientP12Key(context);
                    long time = System.currentTimeMillis();
                    trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    InputStream ins;
                    if(channelNum.equals("10007")){
                        keystorepw = "6154acb614e80a42fc85509980ff3ea5";
                        ins = context.getResources().getAssets().open("clientgp.keystore");
                    }else{
                        ins = context.getResources().getAssets().open("client.keystore");
                    }
                    trustStore.load(ins, keystorepw.toCharArray());
                    MLog.e("trustStore load time = " + (System.currentTimeMillis() - time));
                } catch (Throwable var1) {
                    MLog.e(var1.getMessage(), var1);
                }

                instance = new DefaultSSLSocketFactory();
            } catch (Throwable var1) {
                MLog.e(var1.getMessage(), var1);
            }
        }

        return instance;
    }

    private DefaultSSLSocketFactory() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        super(10000);
//        super(trustStore);
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        sslContext.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        long time = System.currentTimeMillis();
        KeyManagerFactory keymanagerfactory = KeyManagerFactory.getInstance("X509");
        keymanagerfactory.init(trustStore, keypw.toCharArray());
        KeyManager[] akeymanager = keymanagerfactory.getKeyManagers();
        TrustManagerFactory trustmanagerfactory = TrustManagerFactory.getInstance("X509");
        trustmanagerfactory.init(trustStore);
        TrustManager[] atrustmanager = trustmanagerfactory.getTrustManagers();
        sslContext.init(akeymanager, atrustmanager, new java.security.SecureRandom());
        MLog.e("sslContext.init  time = " + (System.currentTimeMillis() - time));
//        this.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket() throws IOException {
        return this.sslContext.getSocketFactory().createSocket();
    }

    public SSLContext getSslContext(){
        return sslContext;
    }

    public static String getChannelNum(Context context){
        channelNum = ChannelReaderUtil.getChannel(context);
        MLog.e("defaultSslChannelNum=" + channelNum);
        return channelNum;
    }
}

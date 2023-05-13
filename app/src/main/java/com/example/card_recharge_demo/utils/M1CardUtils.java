package com.example.card_recharge_demo.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

import java.io.IOException;

/**
 * @author kuan
 * Created on 2019/2/26.
 * @description MifareClassic卡片读写工具类
 */
public class M1CardUtils {

    private static PendingIntent pendingIntent;
    public static PendingIntent getPendingIntent(){
        return pendingIntent;
    }

    public static void setPendingIntent(PendingIntent pendingIntent){
        M1CardUtils.pendingIntent = pendingIntent;
    }

    /**
     * 判断是否支持NFC
     * @return
     */
    public static NfcAdapter isNfcAble(Activity mContext){
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
        return mNfcAdapter;
    }

    /**
     * 监测是否支持MifareClassic
     * @param tag
     * @param activity
     * @return
     */
    public static boolean isMifareClassic(Tag tag, Activity activity){
        String[] techList = tag.getTechList();
        boolean haveMifareClassic = false;
        for (String tech : techList) {
            if (tech.contains("MifareClassic")) {
                haveMifareClassic = true;
                break;
            }
        }
        return haveMifareClassic;
    }

    /**
     * 读取卡片信息
     * @return
     */
    public static byte[][][] readCard(Tag tag, byte[] key)  throws IOException {
        MifareClassic mifareClassic = MifareClassic.get(tag);
        try {
            mifareClassic.connect();
            byte[][][] metaInfo = new byte[16][4][];
            // 获取TAG中包含的扇区数
            int sectorCount = mifareClassic.getSectorCount();
            for (int j = 0; j < sectorCount; j++) {
                int bCount;//当前扇区的块数
                int bIndex;//当前扇区第一块
                if (m1Auth(mifareClassic,j, key)) {
                    bCount = mifareClassic.getBlockCountInSector(j);
                    bIndex = mifareClassic.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mifareClassic.readBlock(bIndex);
//                        String dataString = bytesToHexString(data);
                        metaInfo[j][i] = data;
                        bIndex++;
                    }
                }// else {
////                    Log.e("readCard","密码校验失败");
//                    metaInfo[j]
//                }
            }
            return metaInfo;
        } catch (IOException e){
            throw new IOException(e);
        } finally {
            try {
                mifareClassic.close();
            }catch (IOException e){
                throw new IOException(e);
            }
        }
    }

    public static byte[] readBlock(Tag tag, int block, byte[] key) throws IOException {
        MifareClassic mifareClassic = MifareClassic.get(tag);
        byte[] ret = new byte[0];
        try {
            mifareClassic.connect();
            if(m1Auth(mifareClassic, block / 4, key)) {
                ret = mifareClassic.readBlock(block);
            }
        } catch (IOException e){
            throw new IOException(e);
        } finally {
            try {
                mifareClassic.close();
            }catch (IOException e){
                throw new IOException(e);
            }
        }
        return ret;
    }

    /**
     * 改写数据
     * @param block
     * @param blockbyte
     */
    public static boolean writeBlock(Tag tag, int block, byte[] key, byte[] blockbyte) throws IOException {
        MifareClassic mifareClassic = MifareClassic.get(tag);
        try {
            mifareClassic.connect();
            if (m1Auth(mifareClassic,block/4, key)) {
                mifareClassic.writeBlock(block, blockbyte);
            } else {
                return false;
            }
        } catch (IOException e){
            throw new IOException(e);
        } finally {
            try {
                mifareClassic.close();
            }catch (IOException e){
                throw new IOException(e);
            }
        }
        return true;
    }

    /**
     * 密码校验
     * @param mTag
     * @param position
     * @return
     * @throws IOException
     */
    public static boolean m1Auth(MifareClassic mTag,int position, byte[] key) throws IOException {
        if (mTag.authenticateSectorWithKeyA(position, key)) {
            return true;
        } else if (mTag.authenticateSectorWithKeyB(position, key)) {
            return true;
        }
        return false;
    }

//    private static String bytesToHexString(byte[] src) {
//        StringBuilder stringBuilder = new StringBuilder();
//        if (src == null || src.length <= 0) {
//            return null;
//        }
//        char[] buffer = new char[2];
//        for (int i = 0; i < src.length; i++) {
//            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
//            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
//            System.out.println(buffer);
//            stringBuilder.append(buffer);
//        }
//        return stringBuilder.toString();
//    }
}

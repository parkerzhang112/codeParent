package com.code.baseservice.util;

/** 越南银行收款二维码生成工具类
 * @author ZFYF-Hemant
 */
public class GeneratorVnQrUtil {

    private static final String MSG_ERR_CODE_VAL_SUCCESS = "00";

    private static final String MSG_ERR_CODE_VAL_TIMEOUT = "08";

    public static String vietQrGenerate(String bankNo, String bankAccount, String str3, String str4, String amount, String desc) {
        StringBuilder stringBuilder0 = new StringBuilder("000697044001");
        stringBuilder0.append(String.format("%02d", bankAccount.length()));
        stringBuilder0.append(bankAccount);
        String s = stringBuilder0.toString();
        String ss1 = ("0010A000000727");
        String str8;
        String ss2 = QRType.TERMINAL;
        String ss3 = (s.length()+"");
        String ss4_1 = MSG_ERR_CODE_VAL_SUCCESS;
        String ss4_2 = getLen(bankNo);
        String ss4_3 = bankNo;
        String ss4_4 = QRType.TERMINAL;
        String ss4_5 = getLen(bankAccount);
        String ss4 = ( ss4_1+ ss4_2 + ss4_3 +  ss4_4+ ss4_5 + bankAccount);
        String ss5 = QRType.BILLING;
        String ss6 = getLen(str3);
        String str9 = ss1+  ss2+ ss3 + ss4 + ss5 + ss6 + str3;
//        String str9 = ("0010A000000727") + QRType.TERMINAL + getLen(str7) + (MSG_ERR_CODE_VAL_SUCCESS + getLen(str) + str + QRType.TERMINAL + getLen(str2) + str2) + QRType.BILLING + getLen(str3) + str3;
        StringBuilder sb = new StringBuilder();
        sb.append("000201");
        sb.append("0102" + str4);
        sb.append("38");
        sb.append(getLen(str9));
        sb.append(str9);
        sb.append("5303");
        sb.append("704");
        String str10 = "";
        if (str4 == "11") {
            str8 = "";
        } else {
            str8 = "54" + getLen(amount) + amount;
        }
        sb.append(str8);
        sb.append("5802");
        sb.append(VNPAYTags.LANG_VN);
        if (str4 != "11") {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("62");
            sb2.append(getLen(MSG_ERR_CODE_VAL_TIMEOUT + getLen(desc) + desc));
            sb2.append(MSG_ERR_CODE_VAL_TIMEOUT);
            sb2.append(getLen(desc));
            sb2.append(desc);
            str10 = sb2.toString();
        }
        sb.append(str10);
        sb.append("6304");
        String sb3 = sb.toString();
        int crc16 = crc16(sb3.getBytes());
        String crc16_f = padLeftZeros(String.format("%04X", Integer.valueOf(crc16)), 4);
        return sb3 + crc16_f;
    }


    public static String getLen(String str) {
        return padLeftZeros(String.valueOf(str.length()), 2);
    }


    public static String padLeftZeros(String str, int i2) {
        if (str.length() >= i2) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < i2 - str.length()) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }


    public static int crc16(byte[] bArr) {
        int i2 = 65535;
        for (byte b2 : bArr) {
            for (int i3 = 0; i3 < 8; i3++) {
                boolean z2 = ((b2 >> (7 - i3)) & 1) == 1;
                boolean z3 = ((i2 >> 15) & 1) == 1;
                i2 <<= 1;
                if (z2 ^ z3) {
                    i2 ^= 4129;
                }
            }
        }
        return i2 & 65535;
    }



    public static  void main(String[] args){
        String desc = "hello_motor";
        String amount = "180000.000000";
        System.out.println("SHB vietQrGenerate: "+vietQrGenerate("970403", "050103601093", "QRIBFTTA", "12", amount, desc));
        System.out.println("SeA vietQrGenerate2: "+vietQrGenerate("970440", "0968414222", "QRIBFTTA", "12", amount, desc));
        System.out.println("MSB vietQrGenerate3: "+vietQrGenerate("970426", "80000240868", "QRIBFTTA", "12", amount, desc));
    }
}

package com.falconssoft.minipos;

public class ArabicEncode {

    public String ReturnArabic(String PAR)
    {
        String sresult = "";
        char x;
        int[] t = new int[PAR.length()];
        for (int i = 0; i < PAR.length(); i++) {
            x = PAR.charAt(i);
            int z = (int) x;
            t[i] = z;

            if ((x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z')) {
                sresult += x;
            } else {
                switch (t[i]) {
                    case 32: {
                        sresult += " ";
                        break;
                    }
                    case 33: {
                        sresult += "!";
                        break;
                    }
                    case 34: {
                        sresult += "\"";
                        break;
                    }
                    case 35: {
                        sresult += "#";
                        break;
                    }
                    case 36: {
                        sresult += "$";
                        break;
                    }
                    case 37: {
                        sresult += "%";
                        break;
                    }
                    case 38: {
                        sresult += "&";
                        break;
                    }
                    case 39: {
                        sresult += "'";
                        break;
                    }
                    case 40: {
                        sresult += "(";
                        break;
                    }
                    case 41: {
                        sresult += ")";
                        break;
                    }
                    case 42: {
                        sresult += "*";
                        break;
                    }
                    case 43: {
                        sresult += "+";
                        break;
                    }
                    case 44: {
                        sresult += ",";
                        break;
                    }
                    case 45: {
                        sresult += "-";
                        break;
                    }
                    case 46: {
                        sresult += ".";
                        break;
                    }
                    case 58: {
                        sresult += ":";
                        break;
                    }
                    case 59: {
                        sresult += ";";
                        break;
                    }
                    case 60: {
                        sresult += "<";
                        break;
                    }
                    case 61: {
                        sresult += "=";
                        break;
                    }
                    case 62: {
                        sresult += ">";
                        break;
                    }
                    case 63: {
                        sresult += "?";
                        break;
                    }
                    case 64: {
                        sresult += "@";
                        break;
                    }
                    case 91: {
                        sresult += "[";
                        break;
                    }
                    case 92: {
                        sresult += "\\";
                        break;
                    }
                    case 93: {
                        sresult += "]";
                        break;
                    }
                    case 94: {
                        sresult += "^";
                        break;
                    }
                    case 95: {
                        sresult += "_";
                        break;
                    }
                    case 96: {
                        sresult += "`";
                        break;
                    }
                    case 215: {
                        sresult += "×";
                        break;
                    }
                    case 247: {
                        sresult += "÷";
                        break;
                    }
                    case 236: {
                        sresult += "ى";
                        break;
                    }
                    case 240: {
                        sresult += "ً";
                        break;
                    }
                    case 241: {
                        sresult += "ٌ";
                        break;
                    }
                    case 242: {
                        sresult += "ٍ";
                        break;
                    }
                    case 243: {
                        sresult += "َ";
                        break;
                    }
                    case 245: {
                        sresult += "ُ";
                        break;
                    }
                    case 246: {
                        sresult += "ِ";
                        break;
                    }
                    case 248: {
                        sresult += "ّ";
                        break;
                    }
                    case 250: {
                        sresult += "ْ";
                        break;
                    }


                    //Number
                    case 48: {
                        sresult += "0";
                        break;
                    }
                    case 49: {
                        sresult += "1";
                        break;
                    }
                    case 50: {
                        sresult += "2";
                        break;
                    }
                    case 51: {
                        sresult += "3";
                        break;
                    }
                    case 52: {
                        sresult += "4";
                        break;
                    }
                    case 53: {
                        sresult += "5";
                        break;
                    }
                    case 54: {
                        sresult += "6";
                        break;
                    }
                    case 55: {
                        sresult += "7";
                        break;
                    }
                    case 56: {
                        sresult += "8";
                        break;
                    }
                    case 57: {
                        sresult += "9";
                        break;
                    }

                    // Arabic
                    case 199: {
                        sresult += "ا";
                        break;
                    }
                    case 200: {
                        sresult += "ب";
                        break;
                    }
                    case 201: {
                        sresult += "ة";
                        break;
                    }
                    case 202: {
                        sresult += "ت";
                        break;
                    }
                    case 203: {
                        sresult += "ث";
                        break;
                    }
                    case 204: {
                        sresult += "ج";
                        break;
                    }
                    case 205: {
                        sresult += "ح";
                        break;
                    }
                    case 206: {
                        sresult += "خ";
                        break;
                    }
                    case 207: {
                        sresult += "د";
                        break;
                    }
                    case 208: {
                        sresult += "ذ";
                        break;
                    }
                    case 209: {
                        sresult += "ر";
                        break;
                    }
                    case 210: {
                        sresult += "ز";
                        break;
                    }
                    case 211: {
                        sresult += "س";
                        break;
                    }
                    case 212: {
                        sresult += "ش";
                        break;
                    }
                    case 213: {
                        sresult += "ص";
                        break;
                    }
                    case 214: {
                        sresult += "ض";
                        break;
                    }
                    case 216: {
                        sresult += "ط";
                        break;
                    }
                    case 217: {
                        sresult += "ظ";
                        break;
                    }
                    case 218: {
                        sresult += "ع";
                        break;
                    }
                    case 219: {
                        sresult += "غ";
                        break;
                    }
                    case 221: {
                        sresult += "ف";
                        break;
                    }
                    case 222: {
                        sresult += "ق";
                        break;
                    }
                    case 223: {
                        sresult += "ك";
                        break;
                    }
                    case 225: {
                        sresult += "ل";
                        break;
                    }
                    case 227: {
                        sresult += "م";
                        break;
                    }
                    case 228: {
                        sresult += "ن";
                        break;
                    }
                    case 229: {
                        sresult += "ه";
                        break;
                    }
                    case 230: {
                        sresult += "و";
                        break;
                    }
                    case 237: {
                        sresult += "ي";
                        break;
                    }
                    case 193: {
                        sresult += "ء";
                        break;
                    }
                    case 194: {
                        sresult += "آ";
                        break;
                    }
                    case 195: {
                        sresult += "أ";
                        break;
                    }
                    case 196: {
                        sresult += "ؤ";
                        break;
                    }
                    case 197: {
                        sresult += "إ";
                        break;
                    }
                    case 198: {
                        sresult += "ئ";
                        break;
                    }
                }
            }
        }
        return sresult;
    }
}

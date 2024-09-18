package me.terramain.ozonhelperserver.api;

public class ErrorApi {
    public static String getErrorMassage(int id, String[] args){
        return "{\"code\":" + id + ",\"error\":\"" + switch (id){
            case 1 -> "articul not found";
            case 2 -> "illegal type(" + args[0] + ")";
            case 3 -> "File writing/reading/creating error, sorry.";
            case 4 -> "Connection Error"+(args==null?"": "("+args[0]+")" )+", please try again";
            case 5 -> "api-seller.ozon.ru connection Error! Please try again";
            case 6 -> "you didn't specify articul(s)";
            case 7 -> "read body json error";
            default -> "INVISIBLE ERROR";
        } + "\"}";
    }
    public static String getErrorMassage(int id){
        return getErrorMassage(id, null);
    }
}

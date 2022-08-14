package entity;

import java.io.Serializable;




public class Searchcode implements Serializable{
    private int code;
    private Counts data;
    private String message;
    public  Searchcode(){}

    public Searchcode(int code, Counts data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Counts getData() {
        return data;
    }

    public void setData(Counts data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Searchcode{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}

//private static String [] = ["rencai", "rencai", "rencai", "rencai"];
/*
Object rencai = json.getKey("rencai");
//        Object shebei = json.getKey("shebei");
if(shebei) {
        typeList.add("shebei");

        List<Shebei> shebeiList;
        Object shebeiitems = shebei.getArray;
        for(Object shebei in shebeiitems {
        Shebei shebei = Shebei.OjectFromJson(shebei);
        shebeiList.add(shebei);
        }
        dataList.add(shebeiList));
        }
        if(rencai) {
        typeList.add("rencai");
        dataList.add(rencai)；
        }
*/

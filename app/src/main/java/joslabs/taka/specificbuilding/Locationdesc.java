package joslabs.taka.specificbuilding;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OSCAR on 1/8/2018.
 */
@IgnoreExtraProperties
public class Locationdesc {
    public String title;
    String shortdesc;
    public String desc;
    String lockey;
   public String floorno;

    public Locationdesc() {
    }

    public Locationdesc(String title, String shortdesc, String desc, String lockey) {
        this.title = title;
        this.shortdesc = shortdesc;
        this.desc = desc;
        this.lockey = lockey;
    }

    public Locationdesc(String title, String shortdesc, String desc, String lockey, String floorno) {
        this.title = title;
        this.shortdesc = shortdesc;
        this.desc = desc;
        this.lockey = lockey;
        this.floorno = floorno;
    }

    public String getFloorno() {
        return floorno;
    }

    public void setFloorno(String floorno) {
        this.floorno = floorno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLockey() {
        return lockey;
    }

    public void setLockey(String lockey) {
        this.lockey = lockey;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result=new HashMap<>();
        result.put("title",title);
        result.put("shortdesc",shortdesc);
        result.put("desc",desc);
        result.put("lockey",lockey);
        result.put("floorno",floorno);

        return result;
    }
}

package tpson.utils;

import com.jfinal.kit.StrKit;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuandong on 2017/6/20.
 */
public class LuceneQueryBuilder {

    public static LuceneQueryBuilder getInstance(){
        return new LuceneQueryBuilder();
    }

    private ArrayList<String> list = new ArrayList<String>();

    public LuceneQueryBuilder term(String key, String value){
        if (StrKit.notBlank(value)){
            list.add(key + ":" + "\""+transformSolrMetacharactor(value)+"\"");
        }
        return this;
    }

    public LuceneQueryBuilder range(String key, Integer start, Integer end){
        if (start != null && end !=null)
        list.add(key + ":" + "["+start+" TO "+ end +"]");
        return this;
    }

    /**
     * 转义Solr/Lucene的保留运算字符
     * 保留字符有+ - && || ! ( ) { } [ ] ^ ” ~ * ? : \
     * @param input
     * @return 转义后的字符串
     */
    public static String transformSolrMetacharactor(String input){
        StringBuffer sb = new StringBuffer();
        String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            matcher.appendReplacement(sb, "\\\\"+matcher.group());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String build(){
        return StringUtils.join(list," , ");
    }

}

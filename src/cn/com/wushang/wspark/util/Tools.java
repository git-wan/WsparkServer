package cn.com.wushang.wspark.util;

import net.sf.json.JSONObject;

/**
 * Created by Maskjjx-pc on 2017-9-27.
 */
public class Tools {
    /**
     * ����Ҫ���ص�payEntityMK ��������ת��Ϊjson��ʽ
     * @param obj
     * @return json��ʽ���ַ���
     */
    public static String transObjToJsonStr(Object obj){
        JSONObject jsonObject = JSONObject.fromObject(obj);
        return jsonObject.toString();
    }

    public static void main (String[] args) {

    }
}

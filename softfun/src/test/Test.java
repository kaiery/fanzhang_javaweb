package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaiery.bean.TurnBean;

/**
 * Created by 范张 on 2016-10-15 6:50 6:57 6:58.
 */
public class Test {
    public static void main(String[] args){
        try {
            TurnBean tb = new TurnBean();
            tb.setPassword("123");
            tb.setTurn("456");
            tb.setUsername("789");

            ObjectMapper mapper = new ObjectMapper();
            //User类转JSON
            String json = mapper.writeValueAsString(tb);
            System.out.println("json:"+json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

package com.kaiery.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaiery.bean.TurnBean;
import com.kaiery.common.Des;
import com.kaiery.factory.Factory;
import com.kaiery.manager.ManagerInter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Controller
public class Action {

    private static ManagerInter manager = (ManagerInter) Factory.getInstance().getBean("managerBean");


    /**
     * 获取Coturn的信息
     */
    @RequestMapping(value = "/queryCoturn.do")
    public void queryCoturn(HttpServletRequest request, HttpServletResponse response) {
        String username;
        String password;
        String turn;
        String publicKey = request.getParameter("publicKey");
        PrintWriter out = null;
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            username = Des.encryptDES("Hellokaiery", publicKey);
            password = Des.encryptDES("Hellokaiery", publicKey);
            turn = Des.encryptDES("120.76.209.28:3478", publicKey);
            TurnBean tb = new TurnBean();
            tb.setPassword(password);
            tb.setTurn(turn);
            tb.setUsername(username);

            /**
             * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
             * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。
             * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。
             * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
             * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
             * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。
             */
            ObjectMapper mapper = new ObjectMapper();
            //User类转JSON
            String json = mapper.writeValueAsString(tb);

            out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}

package com.tensquare.sms.consumer;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.tensquare.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "tensquare")
public class SmsListener {
    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;

    @Value("${aliyun.sms.sign_name}")
    private String signName;

    /**
     * 发送短信
     */
    @RabbitHandler
    public void sms(Map msg){
        System.out.println("用户手机号码:"+ msg.get("mobile") + "====随机验证码:"+ msg.get("vcode"));

        // 新建参数:{"code":"1234"}  注意不能有空格
        String params = "{\"code\":\""+msg.get("vcode") +"\"}";
        //调用阿里云的短信发送
        try {
            SendSmsResponse sendSmsStatus = smsUtil.sendSms((String) msg.get("mobile"), templateCode, signName, params);
            // 打印发送状态.
            System.out.println("发送状态:"+ sendSmsStatus.getMessage());
        } catch (Exception e) {
            System.out.println("发送失败："+e.getMessage());
            e.printStackTrace();
        }


    }
}

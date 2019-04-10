package com.juzi.activemq;

import com.juzi.redis.RedisService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @author: juzi
 * @date: 2019/4/9
 * @time: 23:55
 */
@Service
public class MqProduct {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;


    /**
     * 秒杀入队
     */
    public void sengSpikeMsg(SpikeMessage spikeMessage){
        Destination destination = new ActiveMQQueue(MqConfig.SPIKE_QUEUE);
        String msg = RedisService.beanToString(spikeMessage);
        jmsMessagingTemplate.convertAndSend(destination,msg);
    }
    public void sendMsg(String name,String msg){
        Destination destination = new ActiveMQQueue(name);
        jmsMessagingTemplate.convertAndSend(destination,msg);
    }

}

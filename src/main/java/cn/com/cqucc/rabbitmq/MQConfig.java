package cn.com.cqucc.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutchange";


    /**
     * direct模式交换机Exchange
     *
     * @return
     */
    // 配置消息队列
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true); // 第一个参数就是队列的名称 第二个参数就是是否要做持久化
    }


    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1, true);
    }


    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1"); //
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#"); //
    }


    /**
     * fanout 模式 广播模式交换机 Exchange
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding FanoutBinding1 () {
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }


    @Bean
    public Binding FanoutBinding2 () {
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

}

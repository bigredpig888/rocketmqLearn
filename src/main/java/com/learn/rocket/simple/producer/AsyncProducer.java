package com.learn.rocket.simple.producer;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;

public class AsyncProducer {
	public static void main(String[] args) throws Exception {
		// Instantiate with a producer group name.
		DefaultMQProducer producer = new DefaultMQProducer("producerGroupTest");
		producer.setNamesrvAddr("127.0.0.1:9876");
		// Launch the instance.
		producer.start();
		producer.setRetryTimesWhenSendAsyncFailed(0);
		for (int i = 0; i < 100; i++) {
			final int index = i;
			// Create a message instance, specifying topic, tag and message body.
			Message msg = new Message("TopicTest", "TagA", "OrderID188",
					"Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
			producer.send(msg, new SendCallback() {
				public void onSuccess(SendResult sendResult) {
					System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
				}

				public void onException(Throwable e) {
					System.out.printf("%-10d Exception %s %n", index, e);
					e.printStackTrace();
				}
			});
		}
		// Shut down once the producer instance is not longer in use.
		producer.shutdown();
	}
}

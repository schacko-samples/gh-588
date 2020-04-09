package com.example.gh588;

import java.util.Map;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = {"server.port=0"})
@ExtendWith(OutputCaptureExtension.class)
@EmbeddedKafka(topics = "gh588-input", bootstrapServersProperty = "spring.cloud.stream.kafka.streams.binder.brokers")
@DirtiesContext

		// If the old test binder is in the classpath (spring-cloud-stream-test-support), then uncomment the following line
		// to avoid trying to bind Kafka Streams components to that binder which results in an exception thrown in the logs.
//@EnableAutoConfiguration(exclude = TestSupportBinderAutoConfiguration.class)

		//Or the following.
//@TestPropertySource(properties =
//		"spring.autoconfigure.exclude=org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration")
class Gh588ApplicationTests {

	@Autowired
	EmbeddedKafkaBroker broker;

	@Test
	public void testDefault(CapturedOutput output) throws InterruptedException {
		Map<String, Object> senderProps = KafkaTestUtils.producerProps(this.broker);
		DefaultKafkaProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
		try {
			KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf, true);
			template.setDefaultTopic("gh588-input");
			template.sendDefault(1, "hello");
			Awaitility.await().until(output::getOut, value -> value.contains("Key: " + 1 + " Value: " + "hello"));
		} finally {
			pf.destroy();
		}
	}

}

package com.poc.wsjava;

import com.poc.wsjava.controller.kafka.PayloadMessagesKafkaController;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.streams.state.internals.Murmur3;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.kafka.common.utils.Utils.murmur2;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class WsJavaPocApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(WsJavaPocApplicationTests.class);
	@Test
	void contextLoads() {
	}

	@Test
	public void testMurmur2() {
		/**
		 @Kafka default value
		 hash the keyBytes to choose a partition
		 - toPositive() = number & 0x7fffffff -> Numero mas grande del 32 bits.
		 - Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
		 */
		int partitionNum = 4;
		Map<byte[], Integer> cases = new java.util.HashMap<>();
		cases.put("user_id_uno".getBytes(), 1306005418);
		cases.put("user_id_dos".getBytes(), 385101357);
		cases.put("user_id_tres".getBytes(), 1939650777);
		cases.put("user_id_cueatro".getBytes(), 1663367173);
		cases.put("user_id_cinco".getBytes(), 1088956188);

		String[] list = {"user_id_uno","user_id_dos","user_id_tres","user_id_cueatro","user_id_cinco"};

		Arrays.stream(list).toList().forEach( item -> {
			int positive = Utils.toPositive(Utils.murmur2(item.getBytes())) % partitionNum;
			logger.info("Key by murmur: [ "+ Utils.toPositive(Utils.murmur2(item.getBytes())) +" ]");
			logger.info("Partition for this user: [ "+ positive +" ]");
		});

		for (Map.Entry<byte[], Integer> c : cases.entrySet()) {
			String keyBytes = String.format("Redondeo de bytes a Int: %d ", c.getValue().intValue());
			String murmurResult = String.format("Murmur calc: %d ", Utils.toPositive(Utils.murmur2(c.getKey())));
			logger.info("Case value: " + c.getValue() + " - KeyBytes [ " + keyBytes + " ] vs Murmur Result [ " + murmurResult + " ]");
			assertEquals(c.getValue().intValue(), Utils.toPositive(Utils.murmur2(c.getKey())));
		}
	}


	@Test
	public void testMurmur3_32() {

		/**
		 @Kafka default value
		 hash the keyBytes to choose a partition
		 - toPositive() = number & 0x7fffffff -> Numero mas grande del 32 bits.
		 - Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
		 */

		int partitionNum = 4;
		Map<byte[], Integer> cases = new java.util.HashMap<>();
		cases.put("21".getBytes(), 896581614);
		cases.put("foobar".getBytes(), -328928243);
		cases.put("a-little-bit-long-string".getBytes(), -1479816207);
		cases.put("a-little-bit-longer-string".getBytes(), -153232333);
		cases.put("lkjh234lh9fiuh90y23oiuhsafujhadof229phr9h19h89h8".getBytes(), 13417721);
		cases.put(new byte[]{'a', 'b', 'c'}, 461137560);

		int seed = 123;

		for (Map.Entry c : cases.entrySet()) {
			byte[] b = (byte[]) c.getKey();

			int partitionSelected = Utils.toPositive(Utils.murmur2(b)) % partitionNum;
			String keyBytes = String.format("Redondeo de bytes a Int: %s ", c.getValue());
			String murmurResult = String.format("Murmur calc: %d ", Murmur3.hash32(b, b.length, seed));
			logger.info("Case value: " + c.getValue() + " - KeyBytes [ " + keyBytes + " ] vs Murmur Result [ " + murmurResult + " ]");
			logger.info("Partition for this user: [ "+ partitionSelected +" ]");

			assertEquals(c.getValue(), Murmur3.hash32(b, b.length, seed));
		}
	}

}

package com.example.bot.spring;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.example.bot.spring.database.BookingDBEngine;
import com.example.bot.spring.textsender.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { KitchenSinkTester.class, SQTextSender.class, BookingTextSender.class,
		BookingDBEngine.class})
public class KitchenSinkTester {
	@Autowired
	private SQTextSender sqsender;
	private String testerId = "123456";
	
	@Test
	public void simpleReply() throws Exception {
	}
	
	@Test
	public void bookingTest() throws Exception {
		BookingTextSender bookingTS = new BookingTextSender();
		String reply = null;
		reply = bookingTS.process(testerId, "I would like to book tour 2D001");
		reply = bookingTS.process(testerId, "Yes.");
		assertThat(reply).isEqualTo("On which date you are going? (in DD/MM format)");
		reply = bookingTS.process(testerId, "21/11");
		assertThat(reply).isEqualTo("Invalid date. Please enter a valid date.");
		reply = bookingTS.process(testerId, "18/11");
		assertThat(reply).isEqualTo("Your name please (Firstname LASTNAME)");
		reply = bookingTS.process(testerId, "Abc DEF");
		assertThat(reply).isEqualTo("How many adults?");
		reply = bookingTS.process(testerId, "2");
		assertThat(reply).isEqualTo("How many children (Age 4 to 11)?");
		reply = bookingTS.process(testerId, "3");
		assertThat(reply).isEqualTo("How many children (Age 0 to 3)?");
		reply = bookingTS.process(testerId, "0");
		assertThat(reply).isEqualTo("Your phone number please.");
		reply = bookingTS.process(testerId, "12345678");
		reply = bookingTS.process(testerId, "Yes.");
		assertThat(reply).isEqualTo("Thank you. Please pay the tour fee by ATM to "
							+ "123-345-432-211 of ABC Bank or by cash in our store.\n"
							+ "When you complete the ATM payment, please send the bank "
							+ "in slip to us. Our staff will validate it.");
	}
}

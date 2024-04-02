package com.ren.rl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author ren
 * @date 2024/03/06
 */
@SpringBootApplication(scanBasePackages = { "com.ren.rl" }, exclude = {DataSourceAutoConfiguration.class})
public class PaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

}

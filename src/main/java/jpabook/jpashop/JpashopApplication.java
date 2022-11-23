package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);



	}

	@Bean
	// 엔티티를 외부노출 x -> 이런방법이 있다라고 설명을 해준다
	Hibernate5Module hibernate5Module(){

		Hibernate5Module hibernate5Module = new Hibernate5Module();

		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING,true);
		return hibernate5Module;
	}
}

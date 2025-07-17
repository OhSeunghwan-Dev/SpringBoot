package org.mbc.board.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // 환경설정용 클레스임을 스프링에 알리는 어노테이션
public class RootConfig {

    @Bean   // 환경설정용 객체로 지정
    public ModelMapper getMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE).setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
        // 엔티티를 DTO로 변환하게 환경설정
    }
}

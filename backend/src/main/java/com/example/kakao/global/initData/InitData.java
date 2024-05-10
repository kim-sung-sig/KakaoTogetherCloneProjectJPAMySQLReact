package com.example.kakao.global.initdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.kakao.domain.dreamboard.entity.DreamBoardCategoryEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardCategoryRepository;
import com.example.kakao.domain.dreamboard.service.DreamBoardService;
import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.repository.UserRepository;

@Configuration
public class InitData {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    CommandLineRunner init(DreamBoardCategoryRepository categoryRepository, UserRepository userRepository, DreamBoardService service) {
        return args -> {
            // 유저
            UserEntity user = UserEntity.builder()
                    .type("test")
                    .username("test1")
                    .password(bCryptPasswordEncoder.encode("1234"))
                    .email("test1")
                    .role("ROLE_USER")
                    .name("test1")
                    .build();
            
            userRepository.save(user);
            System.out.println(userRepository.findById(1L).get());
            // 카테고리 
            String[] cate = {
                "자원봉사",
                "어려운이웃",
                "교육지원",
                "지구촌",
                "우리사회",
                "청년",
                "아동|청소년",
                "여성",
                "이주민|다문화",
                "실버세대",
                "독거노인",
                "장애인",
                "저소득가정"
            };
            for(String ca : cate){
                DreamBoardCategoryEntity categoryEntity = new DreamBoardCategoryEntity();
                categoryEntity.setCategoryName(ca);
                categoryRepository.save(categoryEntity);
            }

            // 게시글
            // service.saveDreamBoard(null, null);

        };
    }

}

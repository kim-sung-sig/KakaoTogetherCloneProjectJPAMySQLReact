package com.example.kakao.global.initdata;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.kakao.domain.dreamboard.entity.DreamBoardCategoryEntity;
import com.example.kakao.domain.dreamboard.repository.DreamBoardCategoryRepository;
import com.example.kakao.domain.dreamboard.service.DreamBoardService;
import com.example.kakao.global.user.entity.UserEntity;
import com.example.kakao.global.user.repository.UserRepository;

@Configuration
public class InitData {

    @Bean
    CommandLineRunner init(DreamBoardCategoryRepository categoryRepository, UserRepository userRepository, DreamBoardService service) {
        return args -> {
            // 유저
            UserEntity user = UserEntity.builder()
                                .username("test1").name("test1").email("test1").role("ROLE_USER").type("test")
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

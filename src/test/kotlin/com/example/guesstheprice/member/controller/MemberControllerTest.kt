package com.example.guesstheprice.member.controller

import com.example.guesstheprice.config.JwtTokenProvider
import com.example.guesstheprice.member.service.MemberService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.MockBeans
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@ExtendWith(SpringExtension::class)
@ContextConfiguration
//@SpringBootTest
@WebMvcTest(controllers = [MemberController::class])
@MockBeans(value = [
    MockBean(MemberService::class),
    MockBean(PasswordEncoder::class),
    MockBean(JwtTokenProvider::class),
    MockBean(RedisTemplate::class)
])
class MemberControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Any>

    @Test
    fun 스웨거_접근_테스트() {
        mvc.get("/swagger-ui/index.html")
            .andExpect { status { is3xxRedirection() } }
    }

    @Test
    fun 기본_접근_테스트() {
        mvc.get("/")
            .andExpect { status { is3xxRedirection() } }
    }

    @Test
    fun redisTemplate_test() {
        assertNotNull(redisTemplate)
        assertNotNull(redisTemplate.opsForValue())
    }

    @Test
    @WithMockCustomOAuth2User(roles = ["ROLE_USER"], registrationId = "google")
    fun 기본_접근_테스트_with_custom_user() {
        mvc.get("/token")
            .andExpect { status { isOk() } }
    }

    @Test
    @WithMockCustomOAuth2User(roles = ["ROLE_USER"], registrationId = "google")
    fun 롤_접근_테스트_with_custom_user() {
        mvc.get("/main")
            .andExpect { status { isOk() } }
    }

    @Test
    @WithMockCustomOAuth2User(roles = ["ROLE_USER", "ROLE_ADMIN"], registrationId = "google")
    fun 접근_테스트_with_custom_admin() {
        mvc.get("/swagger-ui/index.html")
    }
}
package io.sixhours.memcached.demo

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.test.context.junit4.SpringRunner

/**
 * Application tests.
 *
 * @author Igor Bolic
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var cacheManager: CacheManager

    @Test
    fun thatSearchReturnsAllBooks() {
        val expected = "[{\"id\":1,\"title\":\"Kotlin in Action\",\"year\":2017},{\"id\":2,\"title\":\"Spring Boot in Action\",\"year\":2016},{\"id\":3,\"title\":\"Programming Kotlin\",\"year\":2017},{\"id\":4,\"title\":\"Kotlin\",\"year\":2017}]"

        val response = restTemplate.getForEntity("/books", String::class.java)

        assertThat(response.body, `is`(expected))
    }

    @Test(timeout = 4000)
    fun thatSearchByTitleUsesCachedData() {
        val expected = "[{\"id\":4,\"title\":\"Kotlin\",\"year\":2017}]"

        val firstResponse = restTemplate.getForEntity("/books/Kotlin", String::class.java)
        val secondResponse = restTemplate.getForEntity("/books/Kotlin", String::class.java)

        assertThat(firstResponse.body, `is`(expected))
        assertThat(secondResponse.body, `is`(expected))
    }

    @Test
    fun thatCacheManagerIsLoaded() {
        assertThat(cacheManager, instanceOf(ConcurrentMapCacheManager::class.java))
    }
}

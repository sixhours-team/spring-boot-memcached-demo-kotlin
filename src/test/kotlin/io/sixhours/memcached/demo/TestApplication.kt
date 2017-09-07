package io.sixhours.memcached.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean

/**
 * Test application configuration.
 *
 * @author Igor Bolic
 */
@SpringBootApplication
@EnableCaching
class TestApplication {

    fun main(args: Array<String>) {
        SpringApplication.run(TestApplication::class.java, *args)
    }

    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager()
    }
}
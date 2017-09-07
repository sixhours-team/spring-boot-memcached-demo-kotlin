package io.sixhours.memcached.demo

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable

data class Book(val id: Int, val title: String, val year: Int) : Serializable

interface BookRepository {

    fun findAll(): List<Book>

    fun findByTitle(title: String): List<Book>
}

@Component
class SimpleBookRepository : BookRepository {
    private val books = listOf(
            Book(1, "Kotlin in Action", 2017),
            Book(2, "Spring Boot in Action", 2016),
            Book(3, "Programming Kotlin", 2017),
            Book(4, "Kotlin", 2017))

    override fun findAll(): List<Book> = books

    @Cacheable("books")
    override fun findByTitle(title: String): List<Book> {
        simulateSlowService()
        return books.filter { it.title == title }
    }

    private fun simulateSlowService() {
        Thread.sleep(3000L)
    }

}

@RestController
class BookController(val repository: BookRepository) {

    @GetMapping("/books")
    fun books() = repository.findAll()

    @GetMapping("/books/{title}")
    fun booksByTitle(@PathVariable title: String) = repository.findByTitle(title)
}

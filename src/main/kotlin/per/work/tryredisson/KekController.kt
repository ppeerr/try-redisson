package per.work.tryredisson

import org.redisson.api.RedissonClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kek")
class KekController(
    private val redissonClient: RedissonClient,
    private val kekService: KekService
) {
    @GetMapping("/keys")
    fun run(): List<String> {
        return redissonClient.keys.keys.toList()
    }

    @PostMapping("/one")
    fun addOne() {
        kekService.addOne()
    }

    @PostMapping("/two")
    fun addTwo() {
        kekService.addTwo()
    }
}
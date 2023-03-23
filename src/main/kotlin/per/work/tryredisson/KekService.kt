package per.work.tryredisson

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.redisson.api.RedissonClient
import org.redisson.api.map.event.EntryEvent
import org.redisson.api.map.event.EntryExpiredListener
import org.redisson.codec.JsonJacksonCodec
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

private val log = KotlinLogging.logger {}

@Service
class KekService(
    private val redissonClient: RedissonClient,
    private val objectMapper: ObjectMapper
) {

    private val mapka = redissonClient.getMapCache<KekData, Boolean>(
        "mapka",
        JsonJacksonCodec(objectMapper)
    )

    init {
        mapka.addListener(object: EntryExpiredListener<KekData, Boolean> {
            override fun onExpired(event: EntryEvent<KekData, Boolean>) {
                val key = event.key
                val value = event.value

                log.info { "The value $key ----- $value expired" }
            }
        })
    }

    fun addOne() {
        mapka.put(KekData("one", LocalDateTime.now().toString()), true, 15, TimeUnit.SECONDS)
    }

    fun addTwo() {
        mapka.put(KekData("two", LocalDateTime.now().toString()), true, 150, TimeUnit.SECONDS)
    }
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class")
data class KekData(
    val name: String,
    val addedDateTime: String
)
package vote.california.registrationservice.controllers

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import vote.california.registrationservice.RegistrationServiceApplication
import vote.california.registrationservice.data.Voter

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(
    classes = [RegistrationServiceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/*@ContextConfiguration(
    initializers = [MonoDBIInitializer::class]
)*/
class VoterRegistrationControllerTest(
) {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @LocalServerPort
    protected var port: Int = 0

    private fun getRootUrl(): String? = "http://localhost:$port/voters"

    @Test
    fun `register voter`() {
        val voter = Voter(name = "janitha", description = "Black American")
        val response = getRootUrl()?.let { restTemplate.postForEntity<Voter>(it, voter) }

    }

    @Test
    fun `get all registered users`() {
        val allUsers = getRootUrl()?.let { restTemplate.getForEntity(it, Voter::class.java) }
        println()
    }

}
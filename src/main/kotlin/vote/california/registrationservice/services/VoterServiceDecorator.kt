package vote.california.registrationservice.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import vote.california.registrationservice.data.Voter

@Service
class VoterServiceDecorator(
    private val voterService: VoterService,
    @Value("\${summary.host}") private val host: String,
    @Value("\${summary.port}") private val port: String,
    val restTemplate: RestTemplate
) : VoterService by voterService {

    override fun registerVoter(voter: Voter): Voter {
        val created = voterService.registerVoter(voter)
        val path  = "http://$host:$port/voter/create/${created.id}"
        restTemplate.getForEntity(path, String.javaClass)
        return created
    }

    override fun updateVoter(voter: Voter): Voter {
        val updated = voterService.updateVoter(voter)
        val path  = "http://$host:$port/voter/update/${updated.id}"
        restTemplate.getForEntity(path, String.javaClass)
        return updated
    }

    override fun deleteUser(voterId: String) {
        restTemplate.getForEntity("http://$host:$port/voter/remove/$voterId", String.javaClass)
        return voterService.deleteUser(voterId)
    }
}
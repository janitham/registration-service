package vote.california.registrationservice.services

import org.springframework.stereotype.Component
import vote.california.registrationservice.data.Voter
import vote.california.registrationservice.repositories.VoterRepository
import java.util.*

@Component
class VoterServiceImpl(
    val voterRepository: VoterRepository
) : VoterService {
    override fun registerVoter(voter: Voter): Voter {
        return voterRepository.save(voter.apply {
            id = UUID.randomUUID().toString()
        })
    }

    override fun getAllRegisteredUsers(): List<Voter> {
        return voterRepository.findAll()
    }
}
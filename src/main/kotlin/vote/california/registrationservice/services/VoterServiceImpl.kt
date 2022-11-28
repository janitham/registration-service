package vote.california.registrationservice.services

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import vote.california.registrationservice.data.Voter
import vote.california.registrationservice.repositories.VoterRepository
import java.util.*

@Service
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

    override fun updateVoter(voter: Voter): Voter {
        return voterRepository.save(voter)
    }

    override fun deleteUser(voterId: String) {
        return voterRepository.delete(voterRepository.findById(voterId).get())
    }

    override fun findVoter(voterId: String): Voter {
        return voterRepository.findById(voterId).get()
    }
}
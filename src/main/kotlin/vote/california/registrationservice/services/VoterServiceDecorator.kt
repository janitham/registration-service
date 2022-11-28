package vote.california.registrationservice.services

import org.springframework.stereotype.Service
import vote.california.registrationservice.data.Voter

@Service
class VoterServiceDecorator(private val voterService: VoterService) : VoterService by voterService {

    override fun registerVoter(voter: Voter): Voter {
        return voterService.registerVoter(voter)
    }

    override fun updateVoter(voter: Voter): Voter {
        return voterService.updateVoter(voter)
    }

    override fun deleteUser(voterId: String) {
        return voterService.deleteUser(voterId)
    }
}
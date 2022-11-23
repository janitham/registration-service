package vote.california.registrationservice.services

import vote.california.registrationservice.data.Voter

interface VoterService {
    fun registerVoter(voter: Voter): Voter
    abstract fun getAllRegisteredUsers(): List<Voter>
}
package vote.california.registrationservice.services

import vote.california.registrationservice.data.Voter

interface VoterService {
    fun registerVoter(voter: Voter): Voter
    fun getAllRegisteredUsers(): List<Voter>
    fun updateVoter(voter: Voter): Voter
    fun deleteUser(voterId: String)
    fun findVoter(voterId: String): Voter
}
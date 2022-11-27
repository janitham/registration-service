package vote.california.registrationservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vote.california.registrationservice.data.Voter
import vote.california.registrationservice.services.VoterService

@RestController
@RequestMapping("/voters")
class VoterController(
    val voterService: VoterService
) {

    @PostMapping
    fun register(
        @RequestBody voter: Voter
    ): ResponseEntity<Voter> {
        return ResponseEntity.ok(voterService.registerVoter(voter))
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<Voter>> {
        return ResponseEntity.ok(voterService.getAllRegisteredUsers())
    }
}
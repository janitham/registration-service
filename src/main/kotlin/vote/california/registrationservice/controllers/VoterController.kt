package vote.california.registrationservice.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vote.california.registrationservice.data.Voter
import vote.california.registrationservice.services.VoterServiceDecorator

@RestController
@RequestMapping("/voters")
class VoterController(
    val voterService: VoterServiceDecorator
) {

    @PostMapping
    fun register(
        @RequestBody voter: Voter
    ): ResponseEntity<Voter> {
        return ResponseEntity(voterService.registerVoter(voter), HttpStatus.CREATED)
    }

    @GetMapping()
    fun getAll(): ResponseEntity<List<Voter>> {
        return ResponseEntity.ok(voterService.getAllRegisteredUsers())
    }

    @PutMapping
    fun update(
        @RequestBody voter: Voter
    ): ResponseEntity<Voter> {
        return ResponseEntity(voterService.updateVoter(voter), HttpStatus.OK)
    }

    @DeleteMapping("/{voterId}")
    fun delete(@PathVariable voterId: String) {
        voterService.deleteUser(voterId)
    }

    @GetMapping(value = ["/{voterId}"])
    fun findById(@PathVariable voterId: String) {
        voterService.findVoter(voterId)
    }
}
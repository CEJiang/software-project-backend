package software.project.project.controller;

import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import software.project.project.component.job.Job;
import software.project.project.component.job.JobService;

@RestController
public class JobController {
    @Autowired
    private JobService JobService;
    @GetMapping("/auth/Jobs/{user}/{order}")
    public ResponseEntity<Job> getJob(@PathVariable("user") String user, @PathVariable("order") int order) {
        Job Job = JobService.getJob(user, order);
        System.out.println(Job);
        return ResponseEntity.ok(Job);
    }
    @GetMapping("/auth/Jobs/{user}")
    public ResponseEntity<List<Job>> getUserJob(@PathVariable("user") String user) {
        List<Job> Jobs = JobService.getJob(user);

        return ResponseEntity.ok(Jobs);
    }
    @GetMapping("/auth/Jobs")
    public ResponseEntity<List<Job>> getJobs() {
        List<Job> Jobs = JobService.getAllJob();

        return ResponseEntity.ok(Jobs);
    }
    
    @PostMapping("/auth/Jobs")
    public ResponseEntity<Job> createJob(@RequestBody Job request) {
        System.out.println("POST");
        Job Job = JobService.createJob(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{user}")
                .buildAndExpand(Job.getUser())
                .toUri();

        return ResponseEntity.created(location).body(Job);
    }

    @PutMapping("/auth/Jobs/{user}/{order}")
    public ResponseEntity<Job> replaceJob(
            @PathVariable("user") String user, @PathVariable("order") int order, @RequestBody Job request) {
            Job Job = JobService.replaceJob(user, order, request);

        return ResponseEntity.ok(Job);
    }

    @DeleteMapping("/auth/Jobs/{user}/{order}")
    public ResponseEntity<Job> deleteJob(@PathVariable("user") String user, @PathVariable("order") int order) {
        System.out.println("Delete user = " + user + " order = " + order);
        JobService.deleteJob(user, order);

        return ResponseEntity.noContent().build();
    }
}

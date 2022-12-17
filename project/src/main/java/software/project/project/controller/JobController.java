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

import software.project.project.component.Condition;
import software.project.project.component.job.Job;
import software.project.project.component.job.JobService;
import software.project.project.component.resume.Resume;
import software.project.project.component.resume.ResumeService;

@RestController
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private ResumeService resumeService;
    @GetMapping("/auth/Jobs/{userID}/{createTime}")
    public ResponseEntity<Job> getJob(@PathVariable("userID") String userID, @PathVariable("createTime") String createTime) {
        Job Job = jobService.getJob(userID, createTime);
        return ResponseEntity.ok(Job);
    }

    @GetMapping("/auth/Jobs/getUserJobs/{userID}")
    public ResponseEntity<List<Job>> getUserJobs(@PathVariable("userID") String userID) {
        List<Job> Jobs = jobService.getJobs(userID);

        return ResponseEntity.ok(Jobs);
    }

    @GetMapping("/auth/Jobs/getAllJobs/{userID}")
    public ResponseEntity<List<Job>> getJobs(@PathVariable("userID") String userID) {
        List<Job> Jobs = jobService.getAllJobs(userID);

        return ResponseEntity.ok(Jobs);
    }
    
    @PostMapping("/auth/Jobs")
    public ResponseEntity<Job> createJob(@RequestBody Job request) {
        System.out.println("POST");
        Job Job = jobService.createJob(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userID}")
                .buildAndExpand(Job.getUserID())
                .toUri();

        return ResponseEntity.created(location).body(Job);
    }

    @PutMapping("/auth/Jobs/{userID}/{createTime}")
    public ResponseEntity<Job> replaceJob(
            @PathVariable("userID") String userID, @PathVariable("createTime") String createTime, @RequestBody Job request) {
            Job Job = jobService.replaceJob(userID, createTime, request);

        return ResponseEntity.ok(Job);
    }

    @DeleteMapping("/auth/Jobs/{userID}/{createTime}")
    public ResponseEntity<Job> deleteJob(@PathVariable("userID") String userID, @PathVariable("createTime") String createTime) {
        System.out.println("Delete user = " + userID + " createTime = " + createTime);
        jobService.deleteJob(userID, createTime);

        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/auth/Jobs/changeShelvesStatus/{userID}/{createTime}")
    public void changeShelvesStatus(@PathVariable("userID") String userID, @PathVariable("createTime") String createTime){
        jobService.changeShelvesStatus(userID, createTime);
    }

    @PostMapping("/auth/Jobs/search/{userID}")
    public ResponseEntity<List<Job>> search(@PathVariable("userID") String userID, @RequestBody Condition searchCondition){
        List<Job> response = jobService.search(userID, searchCondition);
        

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/Jobs/match/{userID}")
    public ResponseEntity<List<Job>> match(@PathVariable("userID") String userID){
        List<Resume> myResumes = resumeService.getResumes(userID);
        List<Job> matchJobs = jobService.match(userID, myResumes);

        return ResponseEntity.ok(matchJobs);
    }
}

package software.project.project.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import software.project.project.component.resume.Resume;
import software.project.project.component.resume.ResumeService;

@RestController
public class ResumeController {
    @Autowired
    private ResumeService ResumeService;

    @GetMapping("/auth/Resumes/{user}/{createTime}")
    public ResponseEntity<Resume> getResume(@PathVariable("user") String user, @PathVariable("createTime") String createTime) {
        Resume Resume = ResumeService.getResume(user, createTime);
        System.out.println(Resume);
        return ResponseEntity.ok(Resume);
    }
    @GetMapping("/auth/Resumes/{user}")
    public ResponseEntity<List<Resume>> getUserResume(@PathVariable("user") String user) {
        List<Resume> Resumes = ResumeService.getResume(user);

        return ResponseEntity.ok(Resumes);
    }
    @GetMapping("/auth/Resumes")
    public ResponseEntity<List<Resume>> getResumes() {
        List<Resume> Resumes = ResumeService.getAllResume();

        return ResponseEntity.ok(Resumes);
    }
    
    @PostMapping("/auth/Resumes")
    public ResponseEntity<Resume> createResume(@RequestBody Resume request) {
        System.out.println("POST");
        Resume Resume = ResumeService.createResume(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{user}")
                .buildAndExpand(Resume.getUser())
                .toUri();

        return ResponseEntity.created(location).body(Resume);
    }

    @PutMapping("/auth/Resumes/{user}/{createTime}")
    public ResponseEntity<Resume> replaceResume(
            @PathVariable("user") String user, @PathVariable("createTime") String createTime, @RequestBody Resume request) {
            Resume Resume = ResumeService.replaceResume(user, createTime, request);

        return ResponseEntity.ok(Resume);
    }

    @DeleteMapping("/auth/Resumes/{user}/{createTime}")
    public ResponseEntity<Resume> deleteResume(@PathVariable("user") String user, @PathVariable("createTime") String createTime) {
        System.out.println("Delete user = " + user + " createTime = " + createTime);
        ResumeService.deleteResume(user, createTime);

        return ResponseEntity.noContent().build();
    }
}

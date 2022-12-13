package software.project.project.component.job;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.project.project.component.resume.Resume;

@Service
public class JobService {

    @Autowired
    private JobRepository repository;
    
    public Job getJob(String user, String createTime) {
        return repository.findByUserAndCreateTime(user, createTime);
    }

    public List<Job> getJob(String user) {
        return repository.findByUser(user);
    }

    public List<Job> getAllJob(){
        return repository.findAll();
    }
    
    public Job createJob(Job request) {
        String time = getLocalTime();
        Job Job = new Job(request.getTitle(), 
                          request.getName(), 
                          request.getSex(), 
                          request.getPhoneNumber(), 
                          request.getEmail(), 
                          request.getNature(), 
                          request.getType(), 
                          request.getContent(), 
                          request.getDate(), 
                          request.getTime(), 
                          request.getSalary(), 
                          request.getRegion(), 
                          request.getSalaryMethod(), 
                          request.getSalaryDate(), 
                          request.getId(), 
                          request.getUser(), 
                          time, 
                          time);

        return repository.insert(Job);
    }
    
    public Job replaceJob(String user, String createTime, Job request) {
        Job oldJob = getJob(user, createTime);

        Job Job = new Job(request.getTitle(), 
                          request.getName(), 
                          request.getSex(), 
                          request.getPhoneNumber(), 
                          request.getEmail(), 
                          request.getNature(), 
                          request.getType(), 
                          request.getContent(), 
                          request.getDate(), 
                          request.getTime(), 
                          request.getSalary(), 
                          request.getRegion(), 
                          request.getSalaryMethod(), 
                          request.getSalaryDate(), 
                          oldJob.getId(), 
                          oldJob.getUser(), 
                          request.getCreateTime(), 
                          getLocalTime());

        return repository.save(Job);
    }
    
    public void deleteJob(String user, String createTime) {
        repository.deleteByUserAndCreateTime(user, createTime);
    }

    public List<Job> search(Object searchCondition){
        
        List<Job> currentList = getAllJob();

        // 地區查詢
        currentList = currentList.stream().filter((Job job) -> job.getRegion().equals("台北市信義區")).collect(Collectors.toList());

        // 工作種類查詢
        currentList = currentList.stream().filter((Job job) -> job.getNature().equals("陪讀")).collect(Collectors.toList());
        

        // 關鍵字查詢
        currentList = currentList.stream().filter((Job job) -> job.getTitle().indexOf("家教") > 1 || job.getContent().indexOf("家教") > 1).collect(Collectors.toList());


        return currentList;
    }

    public List<Job> match(List<Resume> myResumes){
        List<Job> currentList = getAllJob();
        

        for(Resume resume : myResumes){
            // 地區過濾
            currentList = currentList.stream().filter((Job job) -> job.getRegion().equals(resume.getRegion())).collect(Collectors.toList());

            // 工作種類過濾
            currentList = currentList.stream().filter((Job job) -> job.getNature().equals(resume.getNature())).collect(Collectors.toList());
        }

        return currentList;
    }

    private String getLocalTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        Date currentDate = new Date();
        Instant now = currentDate.toInstant();
        ZoneId currentZone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, currentZone);
        System.out.println("Local date: " + localDateTime.format(format));
        String time = localDateTime.format(format);

        return time;
    }
}
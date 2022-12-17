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
    private JobRepository jobRepository;
    
    public Job getJob(String userID, String createTime) {
        return jobRepository.findByUserAndCreateTime(userID, createTime);
    }

    public List<Job> getJobs(String userID) {
        return jobRepository.findByUser(userID);
    }

    public List<Job> getAllJobs(String userID){
        List<Job> jobsList = jobRepository.findAll();
        jobsList = jobsList.stream().filter((Job job) -> !(job.getUserID().equals(userID))).collect(Collectors.toList());

        return jobsList;
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
                          request.getUserID(), 
                          time, 
                          time,
                          true);

        return jobRepository.insert(Job);
    }
    
    public Job replaceJob(String userID, String createTime, Job request) {
        Job oldJob = getJob(userID, createTime);

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
                          oldJob.getUserID(), 
                          request.getCreateTime(), 
                          getLocalTime(),
                          request.getShelvesStatus());

        return jobRepository.save(Job);
    }
    
    public void deleteJob(String userID, String createTime) {
        jobRepository.deleteByUserAndCreateTime(userID, createTime);
    }

    public List<Job> search(String userID, Object searchCondition){
        
        List<Job> currentList = getAllJobs(userID);

        // 地區查詢
        currentList = currentList.stream().filter((Job job) -> job.getRegion().equals("台北市信義區")).collect(Collectors.toList());

        // 工作種類查詢
        currentList = currentList.stream().filter((Job job) -> job.getNature().equals("陪讀")).collect(Collectors.toList());
        

        // 關鍵字查詢
        currentList = currentList.stream().filter((Job job) -> job.getTitle().indexOf("家教") > 1 || job.getContent().indexOf("家教") > 1).collect(Collectors.toList());


        return currentList;
    }

    public List<Job> match(String userID, List<Resume> myResumes){
        List<Job> currentList = getAllJobs(userID);
        

        for(Resume resume : myResumes){
            // 地區、工作種類過濾
            currentList = currentList.stream().filter((Job job) -> job.getRegion().equals(resume.getRegion()) && job.getNature().equals(resume.getNature())).collect(Collectors.toList());
        }

        return currentList;
    }

    public void changeShelvesStatus(String userID, String createTime) {
        Job job = getJob(userID, createTime);
        job.setShelvesStatus(!job.getShelvesStatus());
        jobRepository.save(job);
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
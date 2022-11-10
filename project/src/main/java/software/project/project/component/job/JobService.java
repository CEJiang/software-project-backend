package software.project.project.component.job;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    @Autowired
    private JobRepository repository;
    
    public Job getJob(String user, int order) {
        return repository.findByUserAndOrder(user, order);
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
                          time, 
                          0);

        return repository.insert(Job);
    }
    
    public Job replaceJob(String user, int order, Job request) {
        Job oldJob = getJob(user, order);

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
                          getLocalTime(), 
                          oldJob.getOrder());

        return repository.save(Job);
    }
    
    public void deleteJob(String user, int order) {
        repository.deleteByUserAndOrder(user, order);
        for(int i = 1; i < 5; ++i){
            Job oldJob = getJob(user, order + i);
            if(oldJob == null) break;
            oldJob.setOrder(oldJob.getOrder() - 1);
            repository.save(oldJob);
        }
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
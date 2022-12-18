package software.project.project.component.job;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.project.project.component.Condition;
import software.project.project.component.resume.Resume;

@Service
public class JobService {
    private final Map<String, Integer> SEARCH_INDEX = new HashMap<String, Integer>(){{
        put("地區", 1);
        put("工作種類", 2);
        put("評星", 3);
        put("關鍵字", 4);
    }};
    
    @Autowired
    private JobRepository jobRepository;
    
    public Job getJob(String userID, String createTime) {
        return jobRepository.findByUserIDAndCreateTime(userID, createTime);
    }

    public List<Job> getJobs(String userID) {
        return jobRepository.findByUserID(userID);
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
        jobRepository.deleteByUserIDAndCreateTime(userID, createTime);
    }

    public List<Job> search(String userID, Condition searchCondition){
        List<Job> originCurrentList = getAllJobs(userID);
        List<String> searchConditions = searchCondition.getSearchCondition();
        Collections.sort(searchConditions, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return SEARCH_INDEX.get(o1.split("-")[0]) - SEARCH_INDEX.get(o2.split("-")[0]);
            }
            
        });
        
        List<Job> currentList = new ArrayList<>();
        
        String pastString = searchConditions.get(0).split("-")[0];

        for(String searchString : searchConditions){
            String[] searchStrings = searchString.split("-");
            String type = searchStrings[0];

            if(!type.equals(pastString)){
                originCurrentList.clear();
                originCurrentList.addAll(currentList);
            }
            // 地區查詢
            if(type.equals("地區")){
                currentList.addAll(
                    originCurrentList.stream().filter((Job job) -> job.getRegion().equals(searchStrings[1])).collect(Collectors.toList())
                );
            }

            // 工作種類查詢
            else if(type.equals("工作種類")){
                currentList.addAll(
                    originCurrentList.stream().filter((Job job) -> job.getNature().equals(searchStrings[1])).collect(Collectors.toList())
                );
            }

            // 評星篩選
            else if(type.equals("評星")){
                // currentList.addAll(
                //     originCurrentList.stream().filter((Resume resume) -> resume.getNature().equals(searchStrings[1])).collect(Collectors.toList())
                // );
            }

            // 關鍵字查詢
            else if(type.equals("關鍵字")){
                currentList.addAll(
                    originCurrentList.stream().filter((Job job) -> job.getTitle().indexOf(searchStrings[1]) > 1 || job.getContent().indexOf(searchStrings[1]) > 1).collect(Collectors.toList())
                );
            }

            pastString = searchStrings[0];
        }
        


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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss", Locale.US);
        Date currentDate = new Date();
        Instant now = currentDate.toInstant();
        ZoneId currentZone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, currentZone);
        System.out.println("Local date: " + localDateTime.format(format));
        String time = localDateTime.format(format);

        return time;
    }

    
}
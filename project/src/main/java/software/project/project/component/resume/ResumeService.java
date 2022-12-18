package software.project.project.component.resume;

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
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.project.project.component.Condition;
import software.project.project.component.job.Job;

@Service
public class ResumeService {
    private final Map<String, Integer> SEARCH_INDEX = new HashMap<String, Integer>(){{
        put("地區", 1);
        put("工作種類", 2);
        put("評星", 3);
        put("關鍵字", 4);
    }};
    @Autowired
    private ResumeRepository resumeRepository;
    
    public Resume getResume(String userID, String createTime) {
        return resumeRepository.findByUserIDAndCreateTime(userID, createTime);
    }

    public List<Resume> getResumes(String userID) {
        return resumeRepository.findByUserID(userID);
    }

    public List<Resume> getAllResumes(String userID){
        List<Resume> resumesList = resumeRepository.findAll();
        resumesList = resumesList.stream().filter((Resume resume) -> !(resume.getUserID().equals(userID))).collect(Collectors.toList());

        return resumesList;
    }
    public Resume createResume(Resume request) {
        String time = getLocalTime();
        Resume Resume = new Resume(request.getTitle(), 
                                   request.getName(), 
                                   request.getSex(), 
                                   request.getBirth(), 
                                   request.getPhoneNumber(), 
                                   request.getEmail(), 
                                   request.getSchool(), 
                                   request.getDepartment(), 
                                   request.getStatus(), 
                                   request.getYear(), 
                                   request.getNature(), 
                                   request.getType(), 
                                   request.getTime(), 
                                   request.getSalary(), 
                                   request.getRegion(), 
                                   request.getIntroduction(), 
                                   request.getId(), 
                                   request.getUserID(), 
                                   time,
                                   time,
                                   true);


        return resumeRepository.insert(Resume);
    }
    
    public Resume replaceResume(String userID, String createTime, Resume request) {
        Resume oldResume = getResume(userID, createTime);
        
        Resume Resume = new Resume(request.getTitle(), 
                                   request.getName(), 
                                   request.getSex(), 
                                   request.getBirth(), 
                                   request.getPhoneNumber(), 
                                   request.getEmail(), 
                                   request.getSchool(), 
                                   request.getDepartment(), 
                                   request.getStatus(), 
                                   request.getYear(), 
                                   request.getNature(), 
                                   request.getType(), 
                                   request.getTime(), 
                                   request.getSalary(), 
                                   request.getRegion(), 
                                   request.getIntroduction(), 
                                   oldResume.getId(), 
                                   oldResume.getUserID(), 
                                   request.getCreateTime(),
                                   getLocalTime(),
                                   request.getShelvesStatus());
        
        return resumeRepository.save(Resume);
    }
    
    public void deleteResume(String userID, String createTime) {
        resumeRepository.deleteByUserIDAndCreateTime(userID, createTime);
    }

    public List<Resume> search(String userID, Condition searchCondition){
        List<Resume> originCurrentList = getAllResumes(userID);
        List<String> searchConditions = searchCondition.getSearchCondition();
        Collections.sort(searchConditions, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return SEARCH_INDEX.get(o1.split("-")[0]) - SEARCH_INDEX.get(o2.split("-")[0]);
            }
            
        });
        List<Resume> currentList = new ArrayList<>();
        
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
                    originCurrentList.stream().filter((Resume resume) -> resume.getRegion().equals(searchStrings[1])).collect(Collectors.toList())
                );
            }

            // 工作種類查詢
            else if(type.equals("工作種類")){
                currentList.addAll(
                    originCurrentList.stream().filter((Resume resume) -> resume.getNature().equals(searchStrings[1])).collect(Collectors.toList())
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
                    originCurrentList.stream().filter((Resume resume) -> resume.getTitle().indexOf(searchStrings[1]) > 1 || resume.getIntroduction().indexOf(searchStrings[1]) > 1).collect(Collectors.toList())
                );
            }

            pastString = searchStrings[0];
        }
        


        return currentList;
    }

    public List<Resume> match(String userID, List<Job> myJobs){
        List<Resume> currentList = getAllResumes(userID);
        

        for(Job job : myJobs){
            // 地區、工作種類過濾
            currentList = currentList.stream().filter((Resume resume) -> resume.getRegion().equals(job.getRegion()) && resume.getNature().equals(job.getNature())).collect(Collectors.toList());
        }

        return currentList;
    }

    public void changeShelvesStatus(String userID, String createTime) {
       Resume resume = getResume(userID, createTime);
       resume.setShelvesStatus(!resume.getShelvesStatus());
       resumeRepository.save(resume);
    }

    private String getLocalTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Instant now = currentDate.toInstant();
        ZoneId currentZone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, currentZone);
        System.out.println("Local date: " + format.format(localDateTime));
        String time = format.format(localDateTime);

        return time;
    }

    
}
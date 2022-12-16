package software.project.project.component.resume;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;
    
    public Resume getResume(String userID, String createTime) {
        return resumeRepository.findByUserAndCreateTime(userID, createTime);
    }

    public List<Resume> getResumes(String userID) {
        return resumeRepository.findByUser(userID);
    }

    public List<Resume> getAllResumes(String userID){
        List<Resume> resumesList = resumeRepository.findAll();
        resumesList = resumesList.stream().filter((Resume resume) -> !(resume.getUser().equals(userID))).collect(Collectors.toList());

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
                                   request.getUser(), 
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
                                   oldResume.getUser(), 
                                   request.getCreateTime(),
                                   getLocalTime(),
                                   request.getShelvesStatus());
        
        return resumeRepository.save(Resume);
    }
    
    public void deleteResume(String userID, String createTime) {
        resumeRepository.deleteByUserAndCreateTime(userID, createTime);
    }

    public void changeShelvesStatus(String userID, String createTime) {
       Resume resume = getResume(userID, createTime);
       resume.setShelvesStatus(!resume.getShelvesStatus());
       resumeRepository.save(resume);
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
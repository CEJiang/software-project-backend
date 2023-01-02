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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.project.project.component.member.Condition;
import software.project.project.component.member.MemberAccount;
import software.project.project.component.member.MemberRepository;
import software.project.project.component.member.Pair;
import software.project.project.component.resume.Resume;

@Service
public class JobService {
    private final Map<String, Integer> SEARCH_INDEX = new HashMap<String, Integer>() {
        {
            put("地區", 1);
            put("工作種類", 2);
            put("評星", 3);
            put("關鍵字", 4);
        }
    };

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Job getJob(String userID, String createTime) {
        return jobRepository.findByUserIDAndCreateTime(userID, createTime);
    }

    public List<Job> getJobs(String userID) {
        return jobRepository.findByUserID(userID);
    }

    public List<Job> getAllJobs(String userID) {
        List<Job> jobsList = jobRepository.findAll();
        jobsList = jobsList.stream().filter((Job job) -> !(job.getUserID().equals(userID)))
                .filter((Job job) -> (job.getShelvesStatus()))
                .collect(Collectors.toList());
        MemberAccount memberAccount = memberRepository.findByUserID(userID);
        List<Pair> jobCollect = memberAccount.getJobCollectList();
        for (Job job : jobsList) {
            System.out.println(job.getUserID() + " " + job.getCreateTime());
            if (jobCollectExist(jobCollect, job.getUserID(), job.getCreateTime())) {
                job.setCollectStatus(true);
            }
        }
        return jobsList;
    }

    private Boolean jobCollectExist(List<Pair> jobCollect, String userID, String createTime) {
        return jobCollect.stream().anyMatch((Pair a) -> a.getUserID().equals(userID) && a.getCreateTime().equals(createTime));
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
                true,
                false);

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
                request.getShelvesStatus(),
                request.getCollectStatus());

        return jobRepository.save(Job);
    }

    public void deleteJob(String userID, String createTime) {
        jobRepository.deleteByUserIDAndCreateTime(userID, createTime);
    }

    public List<Job> search(String userID, Condition searchCondition) {
        List<Job> originCurrentList = getAllJobs(userID);
        List<String> searchConditions = searchCondition.getSearchCondition();
        Collections.sort(searchConditions, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return SEARCH_INDEX.get(o1.split("-")[0]) - SEARCH_INDEX.get(o2.split("-")[0]);
            }

        });
        Iterator<String> iterator = searchConditions.iterator();
        while (iterator.hasNext()) {
            String string = iterator.next();
            try {
                String temp = string.split("-")[1];

            } catch (ArrayIndexOutOfBoundsException e) {
                iterator.remove();
            }
        }
        List<Job> currentList = new ArrayList<>();

        String pastString = searchConditions.get(0).split("-")[0];
        for (String searchString : searchConditions) {
            String[] searchStrings = searchString.split("-");
            String type = searchStrings[0];
            if (!type.equals(pastString)) {
                originCurrentList.clear();
                originCurrentList.addAll(currentList);
                currentList.clear();

            }
            // 地區查詢
            if (type.equals("地區")) {
                currentList.addAll(
                        originCurrentList.stream().filter((Job job) -> job.getRegion().equals(searchStrings[1]))
                                .collect(Collectors.toList()));

            }

            // 工作種類查詢
            else if (type.equals("工作種類")) {
                String[] natureStrings = searchStrings[1].split(",");

                for (String nature : natureStrings) {
                    currentList.addAll(
                            originCurrentList.stream().filter((Job job) -> job.getNature().equals(nature))
                                    .collect(Collectors.toList()));
                }

            }

            // 評星篩選
            else if (type.equals("評星")) {
                // currentList.addAll(
                // originCurrentList.stream().filter((Resume resume) ->
                // resume.getNature().equals(searchStrings[1])).collect(Collectors.toList())
                // );
                
            }

            // 關鍵字查詢
            else if (type.equals("關鍵字")) {
                currentList.addAll(
                        originCurrentList.stream()
                                .filter((Job job) -> job.getTitle().indexOf(searchStrings[1]) > -1
                                        || job.getContent().indexOf(searchStrings[1]) > -1
                                        || job.getRegion().indexOf(searchStrings[1]) > -1
                                        || job.getType().indexOf(searchStrings[1]) > -1
                                        || job.getNature().indexOf(searchStrings[1]) > -1)
                                .collect(Collectors.toList()));
            }

            pastString = searchStrings[0];
        }
        return currentList;
    }

    public List<Job> match(String userID, List<Resume> myResumes) {
        List<Job> currentList = getAllJobs(userID);

        for (Resume resume : myResumes) {
            // 地區、工作種類過濾
            currentList = currentList.stream().filter((Job job) -> job.getRegion().equals(resume.getRegion())
                    && job.getNature().equals(resume.getNature())).collect(Collectors.toList());
        }

        return currentList;
    }

    public void changeShelvesStatus(String userID, String createTime) {
        Job job = getJob(userID, createTime);
        job.setShelvesStatus(!job.getShelvesStatus());
        jobRepository.save(job);
    }

    private String getLocalTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Instant now = currentDate.toInstant();
        ZoneId currentZone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, currentZone);
        String time = format.format(localDateTime);

        return time;
    }

}
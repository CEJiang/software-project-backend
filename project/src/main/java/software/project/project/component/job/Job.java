package software.project.project.component.job;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("jobs")
public class Job {
    // 基本資料
    private String title; // 工作主旨
    private String name; // 雇主姓名
    private String sex; // 性別
    private String phoneNumber; // 電話號碼
    private String email; // 信箱

    // 工作條件
    private String nature;   // 工作性質
    private String type; // 工作需求種類
    private String content; // 工作內容
    private List<String> date; // 工作日期時間
    private List<String> time; // 工作時段
    private String salary; // 薪資待遇
    private String region; // 工作地點

    private String salaryMethod; // 支薪方式
    private String salaryDate; // 支薪日

    // 系統要求
    private String id; // MongoDB ID
    private String userID; // 用戶名
    private String createTime; // 創立時間
    private String refreshTime; // 更新時間
    private Boolean shelvesStatus; // 上下架狀態

    // public Job(
    //     String title,
    //     String name,
    //     String sex,    
    //     String phoneNumber,
    //     String email,
    //     String nature,
    //     String type,
    //     String content,
    //     List<String> date,
    //     List<String> time,
    //     String salary,
    //     String region,
    //     String salaryMethod,
    //     String salaryDate,
    //     String id,
    //     String user,
    //     String createTime,
    //     String refreshTime,
    //     int order){
    //         this.title = title;
    //         this.name = name;
    //         this.sex = sex;    
    //         this.phoneNumber = phoneNumber;
    //         this.email = email;
    //         this.nature = nature;
    //         this.type = type;
    //         this.content = content;
    //         this.date = date;
    //         this.time = time;
    //         this.salary = salary;
    //         this.region = region;
    //         this.salaryMethod = salaryMethod;
    //         this.salaryDate = salaryDate;
    //         this.id = id;
    //         this.user = user;
    //         this.createTime = createTime;
    //         this.refreshTime = refreshTime;
    //         this.order = order;
    //     }
}


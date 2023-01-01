package software.project.project.component.resume;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("resumes")
public class Resume {
    
    // 基本資料
    @NotNull
    private String title; // 主旨
    @NotNull
    private String name; // 姓名
    @NotNull
    private String sex; // 性別
    @NotNull
    private String birth; // 出生年
    @NotNull
    private String phoneNumber; // 電話號碼
    @NotNull
    @Email
    private String email; // 信箱

    // 學經歷
    @NotNull
    private String school; // 學校名稱
    @NotNull
    private String department; // 科系名稱
    @NotNull
    private String status;  // 就學狀態
    @NotNull
    private String year; // 年級

    // 求職條件
    @NotNull
    private String nature; // 工作性質
    @NotNull
    private String type; // 工作種類
    @NotNull
    private String time; // 工作時段
    @NotNull
    private String salary; // 希望待遇
    @NotNull
    private String region; // 地區
    @NotNull
    private String introduction; // 自我介紹

    // 系統要求
    private String id; // MongoDB ID
    @NotNull
    private String userID; // 用戶名
    private String createTime; // 創立時間
    private String refreshTime; // 更新時間
    private Boolean shelvesStatus; // 上下架狀態
    private Boolean collectStatus; // 收藏狀態

    // public Resume(
    //     String title,
    //     String name,
    //     String sex,
    //     String birth,
    //     String phoneNumber,
    //     String email,
    //     String school,
    //     String department,
    //     String status,
    //     String year,
    //     String nature,
    //     String type,
    //     String time,
    //     String salary,
    //     String region,
    //     String introduction,
    //     String id,
    //     String user
    // ){
    //     this.title = title;
    //     this.name = name;
    //     this.sex = sex;
    //     this.birth = birth;
    //     this.phoneNumber = phoneNumber;
    //     this.email = email;
    //     this.school = school;
    //     this.department = department;
    //     this.status = status;
    //     this.year = year;
    //     this.nature = nature;
    //     this.type = type;
    //     this.time = time;
    //     this.salary = salary;
    //     this.region = region;
    //     this.introduction = introduction;
    //     this.id = id;
    //     this.user = user;
    //     this.createTime = createTime;
    //     this.refreshTime = refreshTime;
    //     this.order = order;
    // }
}




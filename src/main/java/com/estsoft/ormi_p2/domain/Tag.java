package com.estsoft.ormi_p2.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag", nullable = false, length = 10)
    private String tag;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<PostKeyword> postKeywords;

    public Tag(String tag) {
        if (tag != null && tag.length() > 10) {
            this.tag = tag.substring(0, 10);  // 최대 길이 10으로 제한
        } else {
            this.tag = tag;
        }
    }

    @Override
    public String toString() {
        return this.tag;  // Tag 객체에서 반환되는 값은 tag 필드로 설정
    }

//    public void setTag(String tag) {
//        if (tag.length() > 10) {
//            System.out.println("태그 길이가 깁니다.");
//            tag = tag.substring(0, 10); // 자동 보정
//        }
//        this.tag = tag;
//    }
//
//    public Tag(String tag) {
//        this.tag = tag;
//    }
//
//    @Override
//    public String toString() {
//        return this.name;  // 이름만 반환
//    }
//
//    public String getName() {
//        return this.tag;
//    }

}

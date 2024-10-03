package com.houssem.book.book;

import com.houssem.book.common.BaseCommon;
import com.houssem.book.feedback.FeedBack;
import com.houssem.book.history.BookTransactionHistory;
import com.houssem.book.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book extends BaseCommon {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<FeedBack> feedBacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate(){
        if(feedBacks.isEmpty() | feedBacks==null){
            return 0.0;
        }
        var rate= this.feedBacks.stream()
                .mapToDouble(FeedBack::getNote)
                .average()
                .orElse(0.0);
        return Math.round(rate*10.0)/10.0;
    }
}

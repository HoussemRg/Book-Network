package com.houssem.book.feedback;

import com.houssem.book.book.Book;
import com.houssem.book.common.BaseCommon;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeedBack extends BaseCommon {


    private Double note;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}

package com.houssem.book.feedback;

import com.houssem.book.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedBackMapper {
    public FeedBack toFeedBack(FeedBackRequest feedBackRequest) {
            return FeedBack.builder()
                    .note(feedBackRequest.note())
                    .comment(feedBackRequest.comment())
                    .book(Book.builder().id(feedBackRequest.bookId()).build())
                    .build();
    }

    public FeedbackResponse toFeedbackResponse(FeedBack feedBack, Integer id) {
        return FeedbackResponse.builder()
                .note(feedBack.getNote())
                .comment(feedBack.getComment())
                .ownFeedback(Objects.equals(feedBack.getCreatedBy(),id))
                .build();
    }
}

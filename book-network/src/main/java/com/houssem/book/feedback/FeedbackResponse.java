package com.houssem.book.feedback;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    Double note;
    String comment;
    private boolean ownFeedback;
}

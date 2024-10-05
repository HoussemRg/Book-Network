package com.houssem.book.feedback;


import com.houssem.book.book.Book;
import com.houssem.book.book.BookRepository;
import com.houssem.book.common.PageResponse;
import com.houssem.book.exception.OperationNotPermitted;
import com.houssem.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final BookRepository bookRepository;
    private final FeedBackMapper feedBackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer save(FeedBackRequest feedBackRequest, Authentication connectedUser) {
        Book book=bookRepository.findById(feedBackRequest.bookId()).orElseThrow(()-> new EntityNotFoundException("No book found with ID::"+feedBackRequest.bookId()));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermitted("You can not give a feedback for an archived or not shareable book");
        }
        User user=((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getId(),user.getId()))
            throw new OperationNotPermitted("you cannot give a feedback to your own book");
        FeedBack feedBack=feedBackMapper.toFeedBack(feedBackRequest);
        return feedbackRepository.save(feedBack).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable= PageRequest.of(page,size);
        User user=((User) connectedUser.getPrincipal());
        Page<FeedBack> feedBacks=feedbackRepository.findAllByBookId(bookId,pageable);
        List<FeedbackResponse> feedbackResponses=feedBacks.stream().map(f-> feedBackMapper.toFeedbackResponse(f,user.getId())).toList();

        return new PageResponse<>(
                feedbackResponses,
                feedBacks.getNumber(),
                feedBacks.getSize(),
                feedBacks.getTotalElements(),
                feedBacks.getTotalPages(),
                feedBacks.isFirst(),
                feedBacks.isLast()
        );
    }
}

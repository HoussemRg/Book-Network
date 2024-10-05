package com.houssem.book.feedback;


import com.houssem.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedBackService feedBackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedBackRequest feedBackRequest,
            Authentication connectedUser
    ){
        return  ResponseEntity.ok(feedBackService.save(feedBackRequest,connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacksByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name = "page",defaultValue = "0",required = false) int page,
            @RequestParam(name = "size",defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(feedBackService.findAllFeedbacksByBook(bookId,page,size,connectedUser));
    }
}

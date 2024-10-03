package com.houssem.book.book;

import com.houssem.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {
    private final BookService  bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @Valid @RequestBody BookRequest bookRequest,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.save(bookRequest,connectedUser));
    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(
            @PathVariable("book-id") Integer id
    ){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "Page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "Size",defaultValue = "10",required = false) int size,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.findAll(page,size,userConnected));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "Page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "Size",defaultValue = "10",required = false) int size,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page,size,userConnected));
    }
}

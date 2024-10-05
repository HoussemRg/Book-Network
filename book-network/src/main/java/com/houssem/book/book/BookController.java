package com.houssem.book.book;

import com.houssem.book.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findBorrowedBooks(
            @RequestParam(name = "Page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "Size",defaultValue = "10",required = false) int size,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.findBorrowedBooks(page,size,userConnected));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findReturnedBooks(
            @RequestParam(name = "Page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "Size",defaultValue = "10",required = false) int size,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.findReturnedBooks(page,size,userConnected));
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable ("book-id") Integer bookId,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId,userConnected));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable ("book-id") Integer bookId,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.updateArchivedStatus(bookId,userConnected));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable ("book-id") Integer bookId,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.borrowBook(bookId,userConnected));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBorrowedBook(
            @PathVariable ("book-id") Integer bookId,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId,userConnected));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnBorrowedBook(
            @PathVariable ("book-id") Integer bookId,
            Authentication userConnected
    ){
        return ResponseEntity.ok(bookService.approveReturnBorrowedBook(bookId,userConnected));
    }

    @PostMapping(value = "/cover/{book-id}" ,consumes = "multipart/form-data")
    public ResponseEntity<?> uploadCoverPicture(
            @PathVariable ("book-id") Integer bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication userConnected
    ){
        bookService.uploadCoverPicture(file,bookId,userConnected);
        return  ResponseEntity.accepted().build();
    }
}

package com.houssem.book.book;

import com.houssem.book.common.PageResponse;
import com.houssem.book.exception.OperationNotPermitted;
import com.houssem.book.history.BookTransactionHistory;
import com.houssem.book.history.BookTransactionHistoryRepository;
import com.houssem.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.houssem.book.book.BookSpecification.*;


@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;

    public Integer save(BookRequest bookRequest, Authentication connectedUser) {
        User user=((User) connectedUser.getPrincipal());
        Book book=bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(()-> new EntityNotFoundException("book not found with the ID::"+bookId));
    }

    public PageResponse<BookResponse> findAll(int page, int size, Authentication userConnected) {
        User user=((User) userConnected.getPrincipal());
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Book> books=bookRepository.findAllDisplayedBooks(pageable,user.getId());
        List<BookResponse> bookResponse=books.stream().map(bookMapper::toBookResponse).toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication userConnected) {
        User user=((User) userConnected.getPrincipal());
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Book> books=bookRepository.findAll(withOwnerId(user.getId()),pageable);
        List<BookResponse> bookResponse=books.stream().map(bookMapper::toBookResponse).toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findBorrowedBooks(int page, int size, Authentication userConnected) {
        User user=((User) userConnected.getPrincipal());
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> allBorrowedBooks=bookTransactionHistoryRepository.findAllBorrowedBooks(pageable,user.getId());
        List<BorrowedBookResponse> borrowedBookResponse=allBorrowedBooks.stream().map(bookMapper::toBorrowedBookResponse).toList();

        return new PageResponse<>(
                borrowedBookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findReturnedBooks(int page, int size, Authentication userConnected) {
        User user=((User) userConnected.getPrincipal());
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> allBorrowedBooks=bookTransactionHistoryRepository.findAllReturnedBooks(pageable,user.getId());
        List<BorrowedBookResponse> borrowedBookResponse=allBorrowedBooks.stream().map(bookMapper::toBorrowedBookResponse).toList();

        return new PageResponse<>(
                borrowedBookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication userConnected) {
        Book book=bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with ID::"+bookId));
        User user=((User) userConnected.getPrincipal());
        if(Objects.equals(book.getOwner().getId(),user.getId()))
            throw new OperationNotPermitted("you cannot update books shareable status ");
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return book.getId();
    }
}

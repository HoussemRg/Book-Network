package com.houssem.book.book;

import com.houssem.book.common.PageResponse;
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

import static com.houssem.book.book.BookSpecification.*;


@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
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
}

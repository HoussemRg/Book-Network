package com.houssem.book.book;

import com.houssem.book.common.PageResponse;
import com.houssem.book.exception.OperationNotPermitted;
import com.houssem.book.file.FileStorageService;
import com.houssem.book.history.BookTransactionHistory;
import com.houssem.book.history.BookTransactionHistoryRepository;
import com.houssem.book.user.User;
import jakarta.mail.Multipart;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.houssem.book.book.BookSpecification.*;


@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;
    private final FileStorageService fileStorageService;

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
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
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
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
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
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
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
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
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
        if(!Objects.equals(book.getOwner().getId(),user.getId()))
            throw new OperationNotPermitted("you cannot update books shareable status ");
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication userConnected) {
        Book book=bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with ID::"+bookId));
        User user=((User) userConnected.getPrincipal());
        if(!Objects.equals(book.getOwner().getId(),user.getId()))
            throw new OperationNotPermitted("you cannot update books archived status ");
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer borrowBook(Integer bookId, Authentication userConnected) {
        Book book=bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with ID::"+bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermitted("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user=((User) userConnected.getPrincipal());
        if(Objects.equals(book.getOwner().getId(),user.getId()))
            throw new OperationNotPermitted("you cannot borrow your own book");

        final boolean isAlreadyBorrowed=bookTransactionHistoryRepository.isAlreadyBorrowedById(bookId,user.getId());
        if(isAlreadyBorrowed)
            throw new OperationNotPermitted("Book already borrowed");
        BookTransactionHistory bookTransactionHistory=BookTransactionHistory.builder()
                .book(book)
                .user(user)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication userConnected) {
        Book book=bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with ID::"+bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermitted("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user=((User) userConnected.getPrincipal());
        if(!Objects.equals(book.getOwner().getId(),user.getId()))
            throw new OperationNotPermitted("you cannot return your own book");
        BookTransactionHistory bookTransactionHistory=bookTransactionHistoryRepository.findByBookIdAndUserId(bookId,user.getId())
                .orElseThrow(()->  new OperationNotPermitted("you did not borrow this book"+ bookId + " " + user.getId()));
        bookTransactionHistory.setReturned(true);

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication userConnected) {
        Book book=bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with ID::"+bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermitted("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user=((User) userConnected.getPrincipal());
        if(!Objects.equals(book.getOwner().getId(),user.getId()))
            throw new OperationNotPermitted("you cannot return your own book");
        BookTransactionHistory bookTransactionHistory=bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId,user.getId())
                .orElseThrow(()->  new OperationNotPermitted("this book is not returned yet, you cannot approve this return"));
        bookTransactionHistory.setReturnApproved(true);

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public void uploadCoverPicture(MultipartFile file, Integer bookId, Authentication userConnected) {
        Book book=bookRepository.findById(bookId).orElseThrow(()-> new EntityNotFoundException("No book found with ID::"+bookId));
        User user=((User) userConnected.getPrincipal());
        var bookCover=fileStorageService.saveFile(file,user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}

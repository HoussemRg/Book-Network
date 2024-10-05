package com.houssem.book.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer> {

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userI);


    @Query("""
            SELECT (COUNT(*) > 0) as isBorrwoed
            From BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.user.id := userId
            AND bookTransactionHistory.book.id := bookId
            AND bookTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowedById(Integer bookId, Integer userId);


    @Query("""
            SELECT history
            From BookTransactionHistory history
            WHERE history.user.id := userId
            AND history.book.id := bookId
            AND history.returned = false
            AND history.returnApproved = false
            """)
    Optional<BookTransactionHistory> findBookByIdAndUserId(Integer bookId, Integer userId);

    @Query("""
            SELECT transaction
            From BookTransactionHistory transaction
            WHERE transaction.book.owner.id := ownerId
            AND transaction.book.id := bookId
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findBookByIdAndOwnerId(Integer bookId, Integer ownerId);
}

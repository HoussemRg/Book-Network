package com.houssem.book.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book,Integer>, JpaSpecificationExecutor {

    @Query("""
            SELECT book
            FROM Book book
            WHERE book.archived=false
            AND book.shareable=true
            AND book.owner!= :userId
            """)
    Page<Book> findAllDisplayedBooks(Pageable pageable, Integer userId);
}

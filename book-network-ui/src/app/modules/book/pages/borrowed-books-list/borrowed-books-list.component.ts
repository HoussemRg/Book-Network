import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BookResponse, BorrowedBookResponse, FeedBackRequest, PageResponseBorrowedBookResponse } from '../../../../services/models';
import { BookService, FeedbackService } from '../../../../services/services';
import { RatingComponent } from '../../componenets/rating/rating.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-borrowed-books-list',
  standalone: true,
  imports: [NgIf,NgFor,RouterModule,RatingComponent,FormsModule],
  templateUrl: './borrowed-books-list.component.html',
  styleUrl: './borrowed-books-list.component.scss'
})
export class BorrowedBooksListComponent implements OnInit {

  page = 0;
  size = 5;
  pages: any = [];
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  selectedBook: BookResponse | undefined = undefined;
  feedbackRequest: FeedBackRequest = {bookId: 0, comment: '', note: 0};
  constructor(
    private bookService: BookService,
    private feedbackService: FeedbackService
  ) {
  }
  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }

  private findAllBorrowedBooks() {
    this.bookService.findBorrowedBooks({
      Page: this.page,
      Size: this.size
    }).subscribe({
      next: (resp) => {
        this.borrowedBooks = resp;
        this.pages = Array(this.borrowedBooks.totalPages)
          .fill(0)
          .map((x, i) => i);
      }
    });
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllBorrowedBooks();
  }

  goToLastPage() {
    this.page = this.borrowedBooks.totalPages as number - 1;
    this.findAllBorrowedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  get isLastPage() {
    return this.page === this.borrowedBooks.totalPages as number - 1;
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
  }

  returnBook(withFeedback: boolean) {
    this.bookService.returnBorrowedBook({
      'book-id': this.selectedBook?.id as number
    }).subscribe({
      next: () => {
        if (withFeedback) {
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      }
    });
  }

  private giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: () => {
      }
    });
  }

}

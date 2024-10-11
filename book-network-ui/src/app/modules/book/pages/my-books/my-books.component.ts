import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { BookCardComponent } from '../../componenets/book-card/book-card.component';
import { BookResponse, PageResponseBookResponse } from '../../../../services/models';
import { BookService } from '../../../../services/services';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-my-books',
  standalone: true,
  imports: [NgFor, BookCardComponent,NgIf,RouterModule],
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit{


  
  bookResponse:PageResponseBookResponse={};

  page:number=0;
  size:number=4;



  constructor(
    private booksService:BookService,
    private router:Router
  ){}

  ngOnInit(): void {
    this.findAllBooks();
  }

  findAllBooks() :void {
    this.booksService.findAllBooksByOwner({Page:this.page,Size:this.size})
      .subscribe((books:PageResponseBookResponse):void=>{
          this.bookResponse=books;
      } )
  }

  goToFirstPage():void{
    this.page=0;
    this.findAllBooks();

  }

  goToPreviousPage():void{
    this.page--;
    this.findAllBooks();

  }
  goToPage(page:number){
    this.page=page;
    this.findAllBooks();

  }

  goToNextPage():void{
    this.page++;
    this.findAllBooks();

  }

  goToLastPage():void{
    this.page=this.bookResponse.totalPages as number -1;
    this.findAllBooks();

  }

  get isLastPage():boolean{
    return this.page=== this.bookResponse.totalPages as number -1;
  }

  shareBook(book: BookResponse) {
    this.booksService.updateShareableStatus({
      'book-id':book.id as number
    }).subscribe({
      next:():void=>{
        book.shareable=!book.shareable;
      }
    })
  }
  onArchiveBook(book: BookResponse) {
    this.booksService.updateArchivedStatus({
      'book-id':book.id as number
    }).subscribe({
      next:():void=>{
        book.archived=!book.archived;
      }
    })
  }
  editBook(book: BookResponse) {
    this.router.navigate([`/books/manage-book/${book.id}`])
  }


}

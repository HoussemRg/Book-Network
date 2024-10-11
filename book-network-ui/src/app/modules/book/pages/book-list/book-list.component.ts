import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../../services/services';
import { Router } from '@angular/router';
import { BookResponse, PageResponseBookResponse } from '../../../../services/models';
import { NgFor, NgIf } from '@angular/common';
import { BookCardComponent } from "../../componenets/book-card/book-card.component";

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [NgFor, BookCardComponent,NgIf],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
  bookResponse:PageResponseBookResponse={};

  page:number=0;
  size:number=4;

  message:string='';
  level :string='success'

  constructor(
    private booksService:BookService,
    private router:Router
  ){}

  ngOnInit(): void {
    this.findAllBooks();
  }

  findAllBooks() :void {
    this.booksService.findAllBooks({Page:this.page,Size:this.size})
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

  borrowBook(book:BookResponse):void{
    this.message='';
    this.booksService.borrowBook({'book-id':book.id as number})
    .subscribe({
      next:():void=>{
        this.message='Book successfully added to your list'
        this.level='success'
      },
      error:(err):void=>{
        console.log(err)
        this.level='error';
        this.message=err.error.error;
      }

    })
  }
}

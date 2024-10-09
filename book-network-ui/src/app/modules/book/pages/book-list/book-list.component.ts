import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../../services/services';
import { Router } from '@angular/router';
import { PageResponseBookResponse } from '../../../../services/models';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [NgFor],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
  bookResponse:PageResponseBookResponse={};

  page:number=0;
  size:number=5;

  constructor(
    private booksService:BookService,
    router:Router
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
}

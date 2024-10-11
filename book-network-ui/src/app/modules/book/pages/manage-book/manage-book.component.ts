import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BookRequest, BookResponse } from '../../../../services/models';
import { BookService } from '../../../../services/services';

@Component({
  selector: 'app-manage-book',
  standalone: true,
  imports: [NgIf,NgFor,RouterModule,FormsModule],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit {

  constructor(
     private bookService:BookService,
     private router:Router,
     private activatedRoute:ActivatedRoute
  ){}
  ngOnInit(): void {
    const bookId:number=this.activatedRoute.snapshot.params['bookId'];
    if(bookId){
      this.bookService.findBookById({'book-id':bookId})
        .subscribe({
          next:(book:BookResponse):void=>{
            this.bookRequest={
              title:book.title as string,
              authorName:book.authorName as string,
              isbn:book.isbn as string,
              synopsis:book.synopsis as string,
              shareable:book.shareable
            }
            if(book.cover){
              const isJpeg = book.cover[0].startsWith('/9j/');
  
              const mimeType = isJpeg ? 'image/jpeg' : 'image/png'; 

              this.pictureSelected=`data:${mimeType};base64,${book.cover}`;
            }
          }
        })
    }
  }

  errorMsg: string[] = [];
  pictureSelected: string | undefined;
  selectedBookCover:any;
  
  bookRequest:BookRequest={
    authorName: '',
    isbn: '',
    synopsis: '',
    title: ''
  }

  onSelectedPicture(event: any):void {
    this.selectedBookCover=event.target.files[0];
    console.log(this.selectedBookCover);
    if(this.selectedBookCover){
      const reader:FileReader=new FileReader();
      reader.onload=():void=>{
        this.pictureSelected=reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }
  saveBook(event:Event):void {
    console.log(this.errorMsg)
    event.preventDefault();
    this.bookService.saveBook({
      body:this.bookRequest
    }).subscribe({
      next:(bookId:number):void=>{
        this.bookService.uploadCoverPicture({
          'book-id':bookId,
          body:{
            file:this.selectedBookCover
          }
        }).subscribe({
          next:():void=>{
            this.router.navigate(['/books/my-books']);
          }
        })
      },
      error:(err):void=>{
        this.errorMsg = err.error.validationErrors ;

      }
    })
  }
  
}

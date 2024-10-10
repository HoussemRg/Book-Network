import { NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { BookRequest } from '../../../../services/models';
import { BookService } from '../../../../services/services';

@Component({
  selector: 'app-manage-book',
  standalone: true,
  imports: [NgIf,NgFor,RouterModule,FormsModule],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent {

  constructor(
     private bookService:BookService,
     private router:Router
  ){}

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

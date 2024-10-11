import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './pages/main/main.component';
import { BookListComponent } from './pages/book-list/book-list.component';
import { MyBooksComponent } from './pages/my-books/my-books.component';
import { ManageBookComponent } from './pages/manage-book/manage-book.component';
import { BorrowedBooksListComponent } from './pages/borrowed-books-list/borrowed-books-list.component';
import { ReturnedBooksComponent } from './pages/returned-books/returned-books.component';

const routes: Routes = [
  {
    path:'',
    component:MainComponent,
    children:[
      {
        path:'',
        component:BookListComponent
      },
      {
        path:'my-books',
        component:MyBooksComponent
      },
      {
        path:'manage-book',
        component:ManageBookComponent
      },
      {
        path:'manage-book/:bookId',
        component:ManageBookComponent
      },
      {
        path:'my-borrowed-books',
        component:BorrowedBooksListComponent
      },
      {
        path:'my-returned-books',
        component:ReturnedBooksComponent
      },
      {path:'my-waiting-list',component:MyBooksComponent}
    ]
  },
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }

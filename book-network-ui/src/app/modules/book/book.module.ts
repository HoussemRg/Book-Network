import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BookRoutingModule } from './book-routing.module';
import { RouterOutlet } from '@angular/router';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    BookRoutingModule,
    RouterOutlet
  ]
})
export class BookModule { }

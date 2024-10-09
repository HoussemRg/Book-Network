import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from '../../../../services/models';
import { NgIf } from '@angular/common';
import { RatingComponent } from '../rating/rating.component';

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [NgIf,RatingComponent],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss',
})
export class BookCardComponent {
  private _book: BookResponse = {};
  private _bookCover: string | undefined;

  private _manage: boolean = false;

  public get book(): BookResponse {
    return this._book;
  }

  @Input()
  public set book(value: BookResponse) {
    this._book = value;
  }

  public get bookCover(): string | undefined {
    if (this._book.cover) {
      return 'data:image/jpg;base64' + this._book.cover;
    }
    return 'https://picsum.photos/1900/800';
  }

  public get manage(): boolean {
    return this._manage;
  }

  @Input()
  public set manage(value: boolean) {
    this._manage = value;
  }

  @Output() private share:EventEmitter<BookResponse>=new EventEmitter<BookResponse>(); 
  @Output() private archive:EventEmitter<BookResponse>=new EventEmitter<BookResponse>(); 
  @Output() private addWaitingList:EventEmitter<BookResponse>=new EventEmitter<BookResponse>(); 
  @Output() private borrow:EventEmitter<BookResponse>=new EventEmitter<BookResponse>(); 
  @Output() private edit:EventEmitter<BookResponse>=new EventEmitter<BookResponse>(); 
  @Output() private details:EventEmitter<BookResponse>=new EventEmitter<BookResponse>(); 

  onSchowDetails() {
    this.details.emit(this._book)
  }
  onArchive() {
    this.archive.emit(this._book)
  }
  onShare() {
    this.share.emit(this._book)
  }
  onEdit() {
    this.edit.emit(this._book)
  }
  onAddToWaitingList() {
    this.addWaitingList.emit(this._book)
  }
  onBorrow() {
    this.borrow.emit(this._book)
  }
}

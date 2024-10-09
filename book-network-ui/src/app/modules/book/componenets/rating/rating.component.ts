import { NgFor, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-rating',
  standalone: true,
  imports: [NgFor,NgIf],
  templateUrl: './rating.component.html',
  styleUrl: './rating.component.scss'
})
export class RatingComponent {

  @Input()
  rating :number=0;
  
  maxRating:number=5;

  get fullStars():number{
    return Math.round(this.rating);
  }

  get halfStar():boolean{
    return this.rating % 1 !==0;
  }

  get emptyStars():number{
    return this.maxRating- Math.ceil(this.rating);
  }
}

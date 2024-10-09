import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit{

  ngOnInit(): void {
    if (typeof window !== 'undefined' && typeof document !== 'undefined') {
      const linkColor = document.querySelectorAll('.nav-link');
      linkColor.forEach((link :Element)=> {
        if (window.location.href.endsWith(link.getAttribute('routerLink') || '')) {
          link.classList.add('active');
        }
        link.addEventListener('click', () => {
          linkColor.forEach((l :Element) => l.classList.remove('active'));
          link.classList.add('active');
        });
      });
    }
  }

  logout():void{

  }
}

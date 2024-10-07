import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthentcicationService } from '../../services/services';
import { NgIf } from '@angular/common';
import { CodeInputModule } from 'angular-code-input';

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [NgIf,CodeInputModule],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {

  message:string="";
  isOk:boolean=true;
  submitted:boolean=false;

  constructor(
    private router:Router,
    private authService:AuthentcicationService
  ){}

  onCodeComplete(token:string):void{
    this.confirmAccount(token);
  }
  confirmAccount(token: string):void {
    this.authService.activateAccount({token}).subscribe({
      next:():void=>{
        this.message="Your account has been successfully activated. \nNow you can procced to login.";
        this.submitted=true;
        this.isOk=true;
      },
      error:(err):void=>{
        this.message="Token has been expired or invalid";
        this.submitted=true;
        this.isOk=false;
      }
    })
  }

  redirectToLoginPage():void{
    this.router.navigate(["login"]);
  }
}

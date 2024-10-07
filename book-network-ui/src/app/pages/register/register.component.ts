import { Component } from '@angular/core';
import { RegistrationRequest } from '../../services/models';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthentcicationService } from '../../services/services';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [NgIf,NgFor,FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  registrationRequest:RegistrationRequest={email:'',firstname:'',lastname:'',password:''}
  errorMsg:Array<string> =[];

  constructor(
    private router:Router,
    private authService:AuthentcicationService
  ){}
  register() :void {
    this.errorMsg=[];
    this.authService.register({
      body:this.registrationRequest
    }).subscribe({
      next:():void=>{
        this.router.navigate(['activate-account']);
      },
      error:(err) : void=>{
        console.log(err);
        if(err.error.validationErrors){
          this.errorMsg=err.error.validationErrors;
        }else{
          this.errorMsg.push(err.error.error);
        }
      }
    })
  }

  login() :void {
    this.router.navigate(['login']);
  }

}

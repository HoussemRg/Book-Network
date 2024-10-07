import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../services/models';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthentcicationService } from '../../services/services';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [NgIf,NgFor,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {


  constructor(
    private router:Router,
    private authService:AuthentcicationService,
    private tokenService:TokenService
  ){

  }

  authRequest:AuthenticationRequest={email:'',password:''};
  errorMsg:Array<string>=[];
  
  
  login() :void {
    this.errorMsg=[];
    this.authService.authenticate({
      body:this.authRequest
    }).subscribe({
      next:(res):void=>{
        // save the token
        this.tokenService.token=res.token as string;
        this.router.navigate(['books']);
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

  register() :void {
    this.router.navigate(['register']);
  }

}

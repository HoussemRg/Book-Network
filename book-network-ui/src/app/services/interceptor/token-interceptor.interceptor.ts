import { HttpHeaders, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../token/token.service';

export const tokenInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService=inject(TokenService);
  const token:string | null=tokenService.token;
  if(token){
    const authReq=req.clone({
      setHeaders:{
        Authorization: 'Bearer '+token
      }
    });
    return next(authReq);
  }

  return next(req);
};

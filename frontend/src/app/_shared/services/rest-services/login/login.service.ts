import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserLoginInterface } from 'src/app/_shared/models/user/user-login.interface';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { tap, shareReplay } from 'rxjs/operators';

enum ENDPOINTS {
  LOGIN = 'login'
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  constructor(private http: HttpClient) {}

  public login(userLogin: UserLoginInterface): Observable<UserLoginInterface> {
    return this.http
      .post<UserLoginInterface>(
        environment.myapi.url + ENDPOINTS.LOGIN,
        userLogin
      )
      .pipe(
        tap(res => this.setSession(res)),
        shareReplay()
      );
  }

  public logout(): void {
    localStorage.removeItem('accessToken');
  }

  public isLoggedIn(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  private setSession(authResult): void {
    localStorage.setItem('accessToken', authResult.accessToken);
  }
}

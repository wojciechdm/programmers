import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { UserInterface } from '../../../models/user/user.interface';

enum ENDPOINTS {
  REGISTER = 'register'
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  public saveUser(user: UserInterface): Observable<UserInterface> {
    return this.http.post<UserInterface>(
      environment.myapi.url + ENDPOINTS.REGISTER,
      user
    );
  }
}

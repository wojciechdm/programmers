import { Component, OnInit } from '@angular/core';
import { DialogService, MessageService } from 'primeng/api';
import { LoginComponent } from '../login/login.component';
import { Message } from 'src/app/_shared/message.enum';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  providers: [DialogService, MessageService]
})
export class HeaderComponent implements OnInit {
  constructor(
    private dialogService: DialogService,
    private loginService: LoginService,
    private messageService: MessageService
  ) {}

  ngOnInit() {}

  public onLogin(): void {
    const ref = this.dialogService.open(LoginComponent, {
      header: Message.LOGIN,
      contentStyle: {
        'max-width': '45rem',
        'max-height': '65rem',
        overflow: 'auto'
      }
    });

    ref.onClose.subscribe(() => {});
  }

  public onLogout(): void {
    this.loginService.logout();
    this.showLogoutMessage();
  }

  public isLoggedIn(): boolean {
    return this.loginService.isLoggedIn();
  }

  public onConfirmMessage(): void {
    this.messageService.clear('success');
  }

  private showLogoutMessage(): void {
    this.messageService.add({
      key: 'success',
      severity: 'success',
      detail: Message.LOGOUT_SUCCESS,
      closable: false
    });
  }
}

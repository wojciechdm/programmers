import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoaderService } from '../../_shared/services/loader/loader.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit, OnDestroy {
  private _show = false;
  private subscription: Subscription;

  constructor(private loaderService: LoaderService) {}

  ngOnInit() {
    this.subscription = this.loaderService.loaderState.subscribe(isLoading => {
      this._show = isLoading;
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get show(): boolean {
    return this._show;
  }

  public set show(value: boolean) {
    this._show = value;
  }
}

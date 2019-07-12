import { Component, forwardRef, Input, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CountryService } from '../../services/rest-services/country/country.service';
import { catchError, debounceTime, filter, switchMap } from 'rxjs/operators';
import { of, Subject, Subscription } from 'rxjs';

@Component({
  selector: 'app-autocomplete',
  templateUrl: './autocomplete.component.html',
  styleUrls: ['./autocomplete.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => AutocompleteComponent),
      multi: true
    }
  ]
})
export class AutocompleteComponent
  implements OnInit, OnDestroy, ControlValueAccessor {
  private _value: string;
  private _countries = [];
  private value$: Subject<string> = new Subject();
  private apiError: string;
  private subscription: Subscription;
  @Input() public inputClass: string;

  constructor(private countryService: CountryService) {}

  public onChange: any = () => {};

  public get value(): string {
    return this._value;
  }

  public set value(value: string) {
    this._value = value;
  }

  public get countries() {
    return this._countries;
  }

  public ngOnInit(): void {
    this.subscription = this.value$
      .pipe(
        debounceTime(500),
        filter(o => !!o),
        switchMap(value => {
          return this.countryService.getCountries(value).pipe(
            catchError(error => {
              if (error.status === 404) {
                this.apiError = 'No countries found.';
              } else {
                this.apiError = 'Unknown error.';
              }
              return of([]);
            })
          );
        })
      )
      .subscribe(countries => {
        this._countries = countries;
      });
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public pushChange(value: string): void {
    this._value = value;
    this.onChange(value);
    this.value$.next(value);
  }

  public chooseCountry(country: string) {
    this._value = country;
    this.onChange(country);
    this._countries = [];
  }

  public registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  public registerOnTouched(fn: any): void {}

  public setDisabledState(isDisabled: boolean): void {}

  public writeValue(value: string): void {
    this.value = value;
  }
}

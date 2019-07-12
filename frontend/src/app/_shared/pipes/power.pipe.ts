import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'power'
})
export class PowerPipe implements PipeTransform {
  transform(value: number, power?: number): number {
    if (!power) {
      power = 2;
    }
    return Math.pow(value, power);
  }
}

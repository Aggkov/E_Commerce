import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'encodeURIComponentPipe',
  standalone: true
})
export class EncodeURIComponentPipePipe implements PipeTransform {

  transform(value: string): string {
    return encodeURIComponent(value);
  }

}

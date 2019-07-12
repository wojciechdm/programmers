import { Pipe, PipeTransform } from '@angular/core';
import { ProjectStatus } from '../models/project/project-status.enum';

@Pipe({
  name: 'projectStatus'
})
export class ProjectStatusPipe implements PipeTransform {
  transform(value: string, args?: any): string {
    return ProjectStatus[value];
  }
}

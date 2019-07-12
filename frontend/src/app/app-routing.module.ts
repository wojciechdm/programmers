import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {ClientComponent} from './components/clients/client/client.component';
import {ClientShowComponent} from './components/clients/client-show/client-show.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {ClientSaveComponent} from './components/clients/client-save/client-save.component';
import {EmployeeComponent} from './components/employees/employee/employee.component';
import {EmployeeShowComponent} from './components/employees/employee-show/employee-show.component';
import {EmployeeSaveComponent} from './components/employees/employee-save/employee-save.component';
import {HomeComponent} from './components/home/home.component';
import {EmployeeUpdateComponent} from './components/employees/employee-update/employee-update.component';
import {ClientUpdateComponent} from './components/clients/client-update/client-update.component';
import {ProjectComponent} from './components/projects/project/project.component';
import {ProjectShowComponent} from './components/projects/project-show/project-show.component';
import {ProjectSaveComponent} from './components/projects/project-save/project-save.component';
import {ProjectUpdateComponent} from './components/projects/project-update/project-update.component';
import {ProjectallocationComponent} from './components/projectallocations/projectallocation/projectallocation.component';
import {ProjectallocationShowComponent} from './components/projectallocations/projectallocation-show/projectallocation-show.component';
import {ProjectallocationSaveComponent} from './components/projectallocations/projectallocation-save/projectallocation-save.component';
import {ProjectallocationUpdateComponent}
  from './components/projectallocations/projectallocation-update/projectallocation-update.component';
import { LoginComponent } from './components/login/login.component';

const routes: Routes = [
  {
    path: '', component: HomeComponent
  },
  {
    path: 'registration', component: RegistrationComponent
  },
  {
    path: 'login', component: LoginComponent
  },
  {
    path: 'clients', component: ClientComponent
  },
  {
    path: 'clients/client-show', component: ClientShowComponent
  },
  {
    path: 'clients/client-save', component: ClientSaveComponent
  },
  {
    path: 'clients/client-update', component: ClientUpdateComponent
  },
  {
    path: 'employees', component: EmployeeComponent
  },
  {
    path: 'employees/employee-show', component: EmployeeShowComponent
  },
  {
    path: 'employees/employee-save', component: EmployeeSaveComponent
  },
  {
    path: 'employees/employee-update', component: EmployeeUpdateComponent
  },
  {
    path: 'projects', component: ProjectComponent
  },
  {
    path: 'projects/project-show', component: ProjectShowComponent
  },
  {
    path: 'projects/project-save', component: ProjectSaveComponent
  },
  {
    path: 'projects/project-update', component: ProjectUpdateComponent
  },
  {
    path: 'projectallocations', component: ProjectallocationComponent
  },
  {
    path: 'projectallocations/projectallocation-show', component: ProjectallocationShowComponent
  },
  {
    path: 'projectallocations/projectallocation-save', component: ProjectallocationSaveComponent
  },
  {
    path: 'projectallocations/projectallocation-update', component: ProjectallocationUpdateComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

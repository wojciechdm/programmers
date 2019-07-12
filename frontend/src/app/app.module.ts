import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ClientListComponent } from './components/clients/client-list/client-list.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClientShowComponent } from './components/clients/client-show/client-show.component';
import { ClientSaveComponent } from './components/clients/client-save/client-save.component';
import { ClientUpdateComponent } from './components/clients/client-update/client-update.component';
import { ClientComponent } from './components/clients/client/client.component';
import { LoaderComponent } from './components/loader/loader.component';
import { LoaderInterceptor } from './_shared/services/loader/loader.interceptor';
import { RegistrationComponent } from './components/registration/registration.component';
import { AutocompleteComponent } from './_shared/components/autocomplete/autocomplete.component';
import { PowerPipe } from './_shared/pipes/power.pipe';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ButtonModule } from 'primeng/button';
import {
  CheckboxModule,
  DropdownModule,
  PaginatorModule,
  RadioButtonModule,
  TooltipModule
} from 'primeng/primeng';
import { EmployeeComponent } from './components/employees/employee/employee.component';
import { EmployeeListComponent } from './components/employees/employee-list/employee-list.component';
import { EmployeeSaveComponent } from './components/employees/employee-save/employee-save.component';
import { EmployeeShowComponent } from './components/employees/employee-show/employee-show.component';
import { EmployeeUpdateComponent } from './components/employees/employee-update/employee-update.component';
import { DynamicDialogModule } from 'primeng/dynamicdialog';
import { EmployeeKeyAccountComponent } from './components/employees/employee-key-account/employee-key-account.component';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { HomeComponent } from './components/home/home.component';
import { EmployeeStatusPipe } from './_shared/pipes/employee-status.pipe';
import { EmployeeRolePipe } from './_shared/pipes/employee-role.pipe';
import { EmployeeLevelPipe } from './_shared/pipes/employee-level.pipe';
import { HeaderComponent } from './components/header/header.component';
import { ProjectStatusPipe } from './_shared/pipes/project-status.pipe';
import { ProjectComponent } from './components/projects/project/project.component';
import { ProjectListComponent } from './components/projects/project-list/project-list.component';
import { ProjectSaveComponent } from './components/projects/project-save/project-save.component';
import { ProjectShowComponent } from './components/projects/project-show/project-show.component';
import { ProjectUpdateComponent } from './components/projects/project-update/project-update.component';
import { ClientForProjectSaveComponent } from './components/clients/client-for-project-save/client-for-project-save.component';
import { ProjectallocationComponent } from './components/projectallocations/projectallocation/projectallocation.component';
import { ProjectallocationListComponent } from './components/projectallocations/projectallocation-list/projectallocation-list.component';
import { ProjectallocationSaveComponent } from './components/projectallocations/projectallocation-save/projectallocation-save.component';
import { ProjectallocationShowComponent } from './components/projectallocations/projectallocation-show/projectallocation-show.component';
import { ProjectallocationUpdateComponent } from './components/projectallocations/projectallocation-update/projectallocation-update.component';
import { ProjectForAllocationSaveComponent } from './components/projects/project-for-allocation-save/project-for-allocation-save.component';
import { LoginComponent } from './components/login/login.component';
import { JwtInterceptor } from './_shared/services/jwt/jwt.interceptor';
import { EmployeeRoleLevelPipe } from './_shared/pipes/employee-role-level.pipe';

@NgModule({
  declarations: [
    AppComponent,
    ClientListComponent,
    ClientShowComponent,
    ClientSaveComponent,
    ClientUpdateComponent,
    ClientComponent,
    LoaderComponent,
    RegistrationComponent,
    AutocompleteComponent,
    PowerPipe,
    EmployeeComponent,
    EmployeeListComponent,
    EmployeeSaveComponent,
    EmployeeShowComponent,
    EmployeeUpdateComponent,
    EmployeeKeyAccountComponent,
    HomeComponent,
    EmployeeStatusPipe,
    EmployeeRolePipe,
    EmployeeLevelPipe,
    HeaderComponent,
    ProjectStatusPipe,
    ProjectComponent,
    ProjectListComponent,
    ProjectSaveComponent,
    ProjectShowComponent,
    ProjectUpdateComponent,
    ClientForProjectSaveComponent,
    ProjectallocationComponent,
    ProjectallocationListComponent,
    ProjectallocationSaveComponent,
    ProjectallocationShowComponent,
    ProjectallocationUpdateComponent,
    ProjectForAllocationSaveComponent,
    LoginComponent,
    EmployeeRoleLevelPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    ButtonModule,
    PaginatorModule,
    CheckboxModule,
    RadioButtonModule,
    DropdownModule,
    DynamicDialogModule,
    CommonModule,
    ToastModule,
    TooltipModule
  ],
  entryComponents: [
    EmployeeKeyAccountComponent,
    ClientForProjectSaveComponent,
    ProjectForAllocationSaveComponent,
    LoginComponent
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}

import { UserLoginInterface } from './user-login.interface';

export interface UserInterface {
  userLogin: UserLoginInterface;
  firstName: string;
  lastName: string;
  age: number;
  dateOfBirth: string;
  address: {
    zipcode: string;
    number: string;
    city: string;
    street: string;
    country: string;
  };
  title: string;
}

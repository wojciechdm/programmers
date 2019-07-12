import { EmployeeStatusPipe } from './employee-status.pipe';

describe('EmployeeStatusPipe', () => {
  it('create an instance', () => {
    const pipe = new EmployeeStatusPipe();
    expect(pipe).toBeTruthy();
  });
});

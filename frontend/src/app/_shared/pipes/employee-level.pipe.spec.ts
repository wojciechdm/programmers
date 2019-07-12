import { EmployeeLevelPipe } from './employee-level.pipe';

describe('EmployeeLevelPipe', () => {
  it('create an instance', () => {
    const pipe = new EmployeeLevelPipe();
    expect(pipe).toBeTruthy();
  });
});

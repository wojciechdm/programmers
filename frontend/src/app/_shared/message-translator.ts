export class MessageTranslator {
  public static translateProjectAllocationRulesViolations(
    message: string
  ): string {
    return message
      .replace(
        'Start date of project allocation cannot be before start date of project and ' +
          'end date of project allocation cannot be after end date of project;',
        'Data rozpoczęcia przypisania nie może być ' +
          'przed datą rozpoczęcia projektu, a data zakończenia przypisania nie może być po dacie zakończenia projektu.'
      )
      .replace(
        'There cannot be both of percentile or hourly workload;',
        'Należy podać tylko jedno obciążenie ' + 'procentowe lub godzinowe.'
      )
      .replace(
        'Cannot allocate an employee to the project, in a role that is not allocated to it and ' +
          'a level higher than that currently specified in the given role;',
        'Nie można przypisać pracownika w roli, ' +
          'która nie jest do niego przypisania i na poziomie wyższym niż ma aktualnie przypisany dla danej roli.'
      )
      .replace(
        'Cannot allocate project with completed or sale status;',
        'Nie można przypisać projektu, ' +
          'który jest zakończony lub w fazie sprzedaży.'
      )
      .replace(
        'Cannot allocate developer to more than two projects and tester to more than three projects;',
        'Nie można przypisać programisty do więcej niż dwóch, a testera do więcej niż trzech projektów.'
      )
      .replace(
        'Cannot allocate developer to more roles for one project;',
        'Nie można przypisać programisty ' + 'do więcej ról w danym projekcie.'
      )
      .replace(
        'Workload for one project cannot be more than 120 percent;',
        'Obciążenie dla jednego projektu ' + 'nie może przekroczyć 120 procent.'
      );
  }
}

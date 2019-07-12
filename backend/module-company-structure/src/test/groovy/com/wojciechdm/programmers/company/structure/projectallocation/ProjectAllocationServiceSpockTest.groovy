package com.wojciechdm.programmers.company.structure.projectallocation

import com.wojciechdm.programmers.company.structure.project.ProjectDisplayDto
import com.wojciechdm.programmers.company.structure.project.ProjectServiceFacade
import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationDisplayDto
import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationNotFoundException
import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationSaveDto
import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationValidationException

import static ProjectAllocationRuleViolation.*

import com.wojciechdm.programmers.company.structure.employee.EmployeeServiceFacade
import com.wojciechdm.programmers.company.structure.employee.EmployeeDisplayDto
import spock.lang.Specification

class ProjectAllocationServiceSpockTest extends Specification {

    private def projectAllocationDaoStub = Stub(ProjectAllocationDao)
    private def projectAllocationMapperStub = Stub(ProjectAllocationMapper)
    private def projectAllocationValidatorStub = Stub(ProjectAllocationValidator)
    private def employeeServiceStub = Stub(EmployeeServiceFacade)
    private def projectServiceStub = Stub(ProjectServiceFacade)
    private def projectAllocationService =
            new ProjectAllocationService(
                    projectAllocationDaoStub,
                    projectAllocationMapperStub,
                    projectAllocationValidatorStub,
                    employeeServiceStub,
                    projectServiceStub)

    def "should throw ProjectAllocationValidationException with correct message when save invalid projectAllocation"() {

        given:

        projectAllocationDaoStub.fetchByEmployeeId(_) >>
                [new ProjectAllocation(null, null, null, null, null, null, null, null, null, null)]
        projectAllocationMapperStub.toDisplayDto(_) >>
                new ProjectAllocationDisplayDto(null, null, null, null, null, null, null, null, null, null)
        projectAllocationValidatorStub.validate(_, _, _, _) >>
                ProjectAllocationValidationResult.getInstance(new EnumMap<>(Map.of(INVALID_TIME_FRAME, true)))
        employeeServiceStub.fetch(_) >>
                new EmployeeDisplayDto(null, null, null, null, null, null, null)
        projectServiceStub.fetch(_) >>
                new ProjectDisplayDto(null, null, null, null, null, null, null, null, null)
        def expected = "Start date of project allocation cannot be before start date of project and " +
                "end date of project allocation cannot be after end date of project; "

        when:

        projectAllocationService.save(new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null))

        then:

        def actual = thrown(ProjectAllocationValidationException)
        actual.message == expected
    }

    def "should throw ProjectAllocationValidationException with correct message when update invalid projectAllocation"() {

        given:

        projectAllocationDaoStub.exists(_) >> true
        projectAllocationDaoStub.fetchByEmployeeId(_) >>
                [new ProjectAllocation(null, null, null, null, null, null, null, null, null, null)]
        projectAllocationMapperStub.toDisplayDto(_) >>
                new ProjectAllocationDisplayDto(null, null, null, null, null, null, null, null, null, null)
        projectAllocationValidatorStub.validate(_, _, _, _, _) >>
                ProjectAllocationValidationResult.getInstance(new EnumMap<>(Map.of(INVALID_TIME_FRAME, true)))
        employeeServiceStub.fetch(_) >>
                new EmployeeDisplayDto(null, null, null, null, null, null, null)
        projectServiceStub.fetch(_) >>
                new ProjectDisplayDto(null, null, null, null, null, null, null, null, null)
        def expected = "Start date of project allocation cannot be before start date of project and " +
                "end date of project allocation cannot be after end date of project; "

        when:

        projectAllocationService.update(1L, new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null))

        then:

        def actual = thrown(ProjectAllocationValidationException)
        actual.message == expected
    }

    def "should throw ProjectAllocationNotFoundException with correct message when update not existing projectAllocation"() {

        given:

        projectAllocationDaoStub.exists(_) >> false
        def expected = "Project allocation id: 1 not found"

        when:

        projectAllocationService.update(1L, new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null))

        then:

        def actual= thrown(ProjectAllocationNotFoundException)
        actual.message == expected
    }

    def "should throw ProjectAllocationNotFoundException with correct message when fetch not existing projectAllocation"() {

        given:

        projectAllocationDaoStub.fetch(_) >> Optional.empty()
        def expected = "Project allocation id: 1 not found"

        when:

        projectAllocationService.fetch(1L)

        then:

        def actual = thrown(ProjectAllocationNotFoundException)
        actual.message == expected
    }
}

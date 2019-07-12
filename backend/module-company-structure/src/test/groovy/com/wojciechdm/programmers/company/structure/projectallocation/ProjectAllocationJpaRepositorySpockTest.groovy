package com.wojciechdm.programmers.company.structure.projectallocation

import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationJpaRepository
import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationSortProperty

import static com.wojciechdm.programmers.company.structure.employee.EmployeeLevel.*
import static com.wojciechdm.programmers.company.structure.employee.EmployeeRole.*

import org.testcontainers.containers.MySQLContainer
import spock.lang.*

import javax.persistence.Persistence
import java.time.LocalDate

class ProjectAllocationJpaRepositorySpockTest extends Specification {

    @Shared
    private def projectAllocationJpaRepository
    @AutoCleanup
    @Shared
    private def container
    @AutoCleanup
    @Shared
    private def entityManagerFactory

    def setupSpec() {
        container = new MySQLContainer()
        container.withInitScript("database_model.sql")
        container.start()
        def properties = new HashMap<>()
        properties.put(
                "javax.persistence.jdbc.url",
                container.getJdbcUrl() + "?serverTimezone=Europe/Warsaw&useSSL=false")
        properties.put("javax.persistence.jdbc.user", container.getUsername())
        properties.put("javax.persistence.jdbc.password", container.getPassword())
        entityManagerFactory = Persistence.createEntityManagerFactory("persistence", properties)
        projectAllocationJpaRepository = new ProjectAllocationJpaRepository(entityManagerFactory)
    }

    def "should save, check if exists, fetch, fetch by employee, update and delete projectAllocation"() {

        given:

        def expectedSavedFirst =
                new ProjectAllocation(
                        null, 1L, 1L, LocalDate.now(), null, 100, null, DEVELOPER, JUNIOR, 1500)
        projectAllocationJpaRepository.save(expectedSavedFirst)
        def expectedSavedSecond =
                new ProjectAllocation(
                        null, 1L, 1L, LocalDate.now(), LocalDate.now(), 10, null, SCRUM_MASTER, JUNIOR, 2500)
        def expectedUpdated =
                new ProjectAllocation(1L, 1L, 1L, LocalDate.now(), null, 90, null, DEVELOPER, JUNIOR, 1800)
        def expectedCount = 2L
        when:

        def actualSaved = projectAllocationJpaRepository.save(expectedSavedSecond)
        def actualExists = projectAllocationJpaRepository.exists(1L)
        def actualCount = projectAllocationJpaRepository.count()
        def actualFetch = projectAllocationJpaRepository.fetch(1L).get()
        def actualFetchByEmployee = projectAllocationJpaRepository.fetchByEmployeeId(1L)
        def actualFetchByProject = projectAllocationJpaRepository.fetchByProjectId(1L)
        def actualFetchAll =
                projectAllocationJpaRepository.fetchAll(1, 1, ProjectAllocationSortProperty.ID, false).get(0)
        def actualUpdated = projectAllocationJpaRepository.update(expectedUpdated)
        def actualDeleted = projectAllocationJpaRepository.delete(1L)

        then:

        equalsProjectAllocations(expectedSavedSecond, actualSaved)
        actualExists
        actualCount == expectedCount
        equalsProjectAllocations(expectedSavedFirst, actualFetch)
        equalsProjectAllocations(expectedSavedFirst, actualFetchByEmployee.get(0))
        equalsProjectAllocations(expectedSavedSecond, actualFetchByEmployee.get(1))
        equalsProjectAllocations(expectedSavedFirst, actualFetchByProject.get(0))
        equalsProjectAllocations(expectedSavedSecond, actualFetchByProject.get(1))
        equalsProjectAllocations(expectedSavedFirst, actualFetchAll)
        equalsProjectAllocations(expectedUpdated, actualUpdated)
        actualDeleted
    }

    void equalsProjectAllocations(def expected, def actual) {
        assert expected.getProjectId() == actual.getProjectId()
        assert expected.getEmployeeId() == actual.getEmployeeId()
        assert expected.getStartDate() == actual.getStartDate()
        assert expected.getEndDate() == actual.getEndDate()
        assert expected.getPercentileWorkload() == actual.getPercentileWorkload()
        assert expected.getHoursPerMonthWorkload() == actual.getHoursPerMonthWorkload()
        assert expected.getRole() == actual.getRole()
        assert expected.getLevel() == actual.getLevel()
        assert expected.getRateMonthly() == actual.getRateMonthly()
    }

}

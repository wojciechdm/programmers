package com.wojciechdm.programmers.controllers.rest.client;

import static org.springframework.http.HttpStatus.*;

import com.wojciechdm.programmers.company.structure.client.*;
import com.wojciechdm.programmers.company.structure.project.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/clients")
@Slf4j
public class ClientController {

  private final ClientServiceFacade clientServiceFacade;
  private final ProjectServiceFacade projectServiceFacade;

  @ResponseStatus(CREATED)
  @PostMapping
  public ClientDisplayDto save(@RequestBody ClientSaveDto clientSaveDto) {

    ClientDisplayDto clientSaved = clientServiceFacade.save(clientSaveDto);
    log.info(
        "Client id: "
            + clientSaved.getId()
            + ", name: "
            + clientSaved.getName()
            + ", codeName: "
            + clientSaved.getCodeName()
            + " saved");

    return clientSaved;
  }

  @PutMapping(path = "/{id}")
  public ClientDisplayDto update(
      @PathVariable("id") long id, @RequestBody ClientSaveDto clientSaveDto) {

    ClientDisplayDto clientUpdated = clientServiceFacade.update(id, clientSaveDto);
    log.info(
        "Client id: "
            + clientUpdated.getId()
            + ", name: "
            + clientUpdated.getName()
            + ", codeName: "
            + clientUpdated.getCodeName()
            + " updated");

    return clientUpdated;
  }

  @DeleteMapping(path = "/{id}")
  public boolean delete(@PathVariable("id") long id) {

    boolean clientDeleted = clientServiceFacade.delete(id);
    log.info("Client id:" + id + " deleted");

    return clientDeleted;
  }

  @GetMapping(path = "/{id}")
  public ClientDisplayDto fetch(@PathVariable("id") long id) {

    return clientServiceFacade.fetch(id);
  }

  @GetMapping(path = "/{id}/projects")
  public List<ProjectDisplayDto> fetchClientProjects(@PathVariable("id") long id) {

    return projectServiceFacade.fetchByClientId(id);
  }

  @GetMapping
  public ClientFetchAllResponse fetchAll(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(defaultValue = "ID") ClientSortProperty sortProperty,
      @RequestParam(defaultValue = "false") boolean sortDesc) {

    return clientServiceFacade.fetchAll(page, limit, sortProperty, sortDesc);
  }
}

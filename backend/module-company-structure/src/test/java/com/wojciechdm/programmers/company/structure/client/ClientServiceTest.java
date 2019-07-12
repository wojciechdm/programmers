package com.wojciechdm.programmers.company.structure.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

class ClientServiceTest {

  private ClientService clientService;
  private ClientDao clientDaoMock = mock(ClientDao.class);
  private ClientMapper clientMapperMock = mock(ClientMapper.class);

  @BeforeEach
  void setUp() {
    clientService = new ClientService(clientDaoMock, clientMapperMock);
  }

  @AfterEach
  void tearDown() {
    reset(clientDaoMock, clientMapperMock);
  }

  @Test
  void shouldThrowClientNotFoundExceptionWhenUpdateNotExistsClient() {
    // given
    ClientSaveDto clientSaveDto = new ClientSaveDto("", "", 1L);
    Client client = new Client(1L, "", "", 1L, LocalDate.now(), LocalDate.now());
    when(clientDaoMock.exists(1L)).thenReturn(false);
    when(clientMapperMock.toEntity(1L, clientSaveDto)).thenReturn(client);
    Supplier<ClientDisplayDto> execute = () -> clientService.update(1L, clientSaveDto);
    Class expected = ClientNotFoundException.class;
    // when
    Class actual = assertThrows(ClientNotFoundException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowClientNotFoundExceptionWithCorrectMessageWhenUpdateNotExistsClient() {
    // given
    ClientSaveDto clientSaveDto = new ClientSaveDto("", "", 1L);
    Client client = new Client(1L, "", "", 1L, LocalDate.now(), LocalDate.now());
    when(clientDaoMock.exists(1L)).thenReturn(false);
    when(clientMapperMock.toEntity(1L, clientSaveDto)).thenReturn(client);
    Supplier<ClientDisplayDto> execute = () -> clientService.update(1L, clientSaveDto);
    String expected = "Client id: 1 not found";
    // when
    String actual = assertThrows(ClientNotFoundException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowClientNotFoundExceptionWhenFetchNotExistsClient() {
    // given
    when(clientDaoMock.fetch(anyLong())).thenReturn(Optional.empty());
    when(clientMapperMock.toDisplayDto(any())).thenReturn(any());
    Supplier<ClientDisplayDto> execute = () -> clientService.fetch(1L);
    Class expected = ClientNotFoundException.class;
    // when
    Class actual = assertThrows(ClientNotFoundException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowClientNotFoundExceptionWithCorrectMessageWhenFetchNotExistsClient() {
    // given
    when(clientDaoMock.fetch(anyLong())).thenReturn(Optional.empty());
    when(clientMapperMock.toDisplayDto(any())).thenReturn(any());
    Supplier<ClientDisplayDto> execute = () -> clientService.fetch(1L);
    String expected = "Client id: 1 not found";
    // when
    String actual = assertThrows(ClientNotFoundException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }
}

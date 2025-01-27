package cx.catapult.animals.web;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import cx.catapult.animals.domain.Cat;
import cx.catapult.animals.service.CatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(MockitoExtension.class)
class CatsControllerTest {

  private static final String ID = "ff8080817b6fd27e017b6fd282bb0000";
  private static final String CATS_URL = "/api/1/cats";
  private static final String FORMATTED_URL = format("%s/%s", CATS_URL, ID);
  private PodamFactory podamFactory;
  private ObjectMapper mapper;
  private MockMvc mockMvc;
  @InjectMocks
  private CatsController classUnderTest;

  @Mock
  private CatsService mockCatsService;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    podamFactory = new PodamFactoryImpl();
    mockMvc = standaloneSetup(classUnderTest).build();
  }

  @Test
  void all_happyPath_ableToFetchAllTheRecord() throws Exception {
    mockMvc.perform(get(CATS_URL)
        .contentType(APPLICATION_JSON))
           .andDo(print())
           .andExpect(status().isOk());
    verify(mockCatsService).all();
  }

  @Test
  void get_happyPath_ableToFetchRecord() throws Exception {

    mockMvc.perform(get(FORMATTED_URL)
        .contentType(APPLICATION_JSON))
           .andDo(print())
           .andExpect(status().isOk());
    verify(mockCatsService).get(ID);
  }

  @Test
  void create_happyPath_manageToCreateRecord() throws Exception {
    final Cat cat = podamFactory.manufacturePojo(Cat.class);

    mockMvc.perform(post(CATS_URL).content(mapper.writeValueAsString(cat))
                                  .contentType(APPLICATION_JSON))
           .andDo(print())
           .andExpect(status().isCreated());
    verify(mockCatsService).create(any(Cat.class));
  }

  @Test
  void update_happyPath_manageToUpdateRecord() throws Exception {
    final Cat cat = podamFactory.manufacturePojo(Cat.class);
    mockMvc.perform(put(FORMATTED_URL).content(mapper.writeValueAsString(cat))
                                      .contentType(APPLICATION_JSON))
           .andDo(print())
           .andExpect(status().isOk());
    verify(mockCatsService).update(anyString(), any(Cat.class));
  }

  @Test
  void delete_happyPath_manageToDeleteRecord() throws Exception {
    mockMvc.perform(delete(FORMATTED_URL).contentType(APPLICATION_JSON))
           .andDo(print())
           .andExpect(status().isOk());
    verify(mockCatsService).delete(ID);
  }
}
package cx.catapult.animals.web;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import cx.catapult.animals.domain.Cat;
import java.net.URL;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class CatsControllerIT {

  private static final String CAT_NAME = "Test 1";

  @LocalServerPort
  private int port;

  private URL base;

  private final Cat cat = new Cat("Tom", "Bob cat");

  @Autowired
  private TestRestTemplate template;

  @BeforeEach
  void setUp() throws Exception {
    this.base = new URL(format("http://localhost:%s%s", port, "/api/1/cats"));
  }

  @Test
  void createShouldWork() {
    final ResponseEntity<Cat> response = template.postForEntity(base.toString(), cat, Cat.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()
                       .getId()).isNotEmpty();
    assertThat(response.getBody()
                       .getName()).isEqualTo(cat.getName());
    assertThat(response.getBody()
                       .getDescription()).isEqualTo(cat.getDescription());
    assertThat(response.getBody()
                       .getGroup()).isEqualTo(cat.getGroup());
  }

  @Test
  void allShouldWork() {
    final Collection items = template.getForObject(base.toString(), Collection.class);
    assertThat(items).hasSizeGreaterThanOrEqualTo(7);
  }

  @Test
  void getShouldWork() {
    final Cat created = create(CAT_NAME);
    final ResponseEntity<String> response = template.getForEntity(getFormattedUrl(created), String.class);
    assertThat(response.getBody()).isNotEmpty();
  }

  @Test
  void deleteShouldWork() {
    final Cat created = create(CAT_NAME);
    template.delete(getFormattedUrl(created), String.class);
  }

  private String getFormattedUrl(final Cat created) {
    return format("%s/%s", base.toString(), created.getId());
  }

  private Cat create(final String name) {
    final Cat created = template.postForObject(base.toString(), new Cat(name, name), Cat.class);
    assertThat(created.getId()).isNotEmpty();
    assertThat(created.getName()).isEqualTo(name);
    return created;
  }
}

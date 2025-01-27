package cx.catapult.animals.web;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import cx.catapult.animals.domain.Cat;
import cx.catapult.animals.service.CatsService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/1/cats", produces = APPLICATION_JSON_VALUE)
public class CatsController {

  private final CatsService catsService;

  @GetMapping(value = EMPTY, produces = APPLICATION_JSON_VALUE)
  public @ResponseBody
  Collection<Cat> all() {
    return catsService.all();
  }

  @GetMapping(value = "/{id}")
  public @ResponseBody
  Cat get(@PathVariable final String id) {
    return catsService.get(id);
  }

  @PostMapping(value = EMPTY, consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(CREATED)
  public @ResponseBody
  Cat
  create(@RequestBody final Cat cat) {
    return catsService.create(cat);
  }

  @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(OK)
  public @ResponseBody
  Cat update(@PathVariable final String id, @RequestBody final Cat cat) {
    return catsService.update(id, cat);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(OK)
  public @ResponseBody
  void delete(@PathVariable final String id) {
    catsService.delete(id);
  }
}

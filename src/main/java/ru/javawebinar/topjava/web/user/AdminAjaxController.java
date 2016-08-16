package ru.javawebinar.topjava.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.View;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Sergey on 27.07.16.
 */

@RestController
@RequestMapping("/ajax/admin/users")
public class AdminAjaxController extends AbstractUserController{

    @Autowired
    private MessageSource messageSource;


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.UI.class)
    public List<User> getAll() {
        return super.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.UI.class)
    public User get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
   public void createOrUpdate(@Valid UserTo userTo){
       try {
           if (userTo.isNew()) {
               super.create(UserUtil.createNewFromTo(userTo));
           } else {
               super.update(userTo);
           }
       }catch (DataIntegrityViolationException e){
           throw new DataIntegrityViolationException(messageSource.getMessage("exception.duplicate_email",null, LocaleContextHolder.getLocale()));
       }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public void enabled(@PathVariable("id") int id, @RequestParam("enabled") boolean enabled) {
        super.enable(id, enabled);
    }
}

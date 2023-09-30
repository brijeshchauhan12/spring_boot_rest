package com.pluralsight.conferencedemo.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.pluralsight.conferencedemo.model.Session;
import com.pluralsight.conferencedemo.repository.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/sessions")
public class SessionController {
    @Autowired
    private SessionRepository sessionRepository;

//    @GetMapping
//    public List<Session> list() {
//        return sessionRepository.findAll();
//    }

    @GetMapping
    CollectionModel<EntityModel<Session>> list() {

        List<EntityModel<Session>> sessions = sessionRepository.findAll().stream()
                .map(session -> EntityModel.of(session,
                        linkTo(methodOn(SessionController.class).one(session.getSession_id())).withSelfRel(),
                        linkTo(methodOn(SessionController.class).list()).withRel("session")))
        .collect(Collectors.toList());

        return CollectionModel.of(sessions, linkTo(methodOn(SessionController.class).list()).withSelfRel());
    }
    @GetMapping("{id}")
    EntityModel<Session> one(@PathVariable Long id) {

        Session session; //
        session = sessionRepository.getReferenceById(id);


        return EntityModel.of(session, //
                linkTo(methodOn(SessionController.class).one(id)).withSelfRel(),
                linkTo(methodOn(SessionController.class).list()).withRel("session"));
    }

//    @GetMapping
//    @RequestMapping("{id}")
//    public Session get(@PathVariable Long id) {
//        return sessionRepository.getReferenceById(id);
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Session create(@RequestBody final Session session) {
        return sessionRepository.saveAndFlush(session);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        sessionRepository.deleteById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Session update (@PathVariable Long id, @RequestBody Session session){
        Session existingSession =sessionRepository.getReferenceById(id);
        BeanUtils.copyProperties(session,existingSession,"session_id");
        return sessionRepository.saveAndFlush(existingSession);
    }
}

